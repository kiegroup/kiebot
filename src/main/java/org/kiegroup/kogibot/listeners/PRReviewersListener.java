package org.kiegroup.kogibot.listeners;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;
import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.config.Reviewers;
import org.kiegroup.kogibot.util.Constants;
import org.kiegroup.kogibot.util.MatchingPathsValuesUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.ReactionContent;

public class PRReviewersListener implements PROpenedListener {

    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KOGIBOT_CONFIGURATION_FILE) Optional<KogibotConfiguration> kogibotConfig,
            GitHub github) throws IOException {
        // check for existence of configuration file.
        if (kogibotConfig.isPresent()) {
            // if there is a configuration file, check for the reviewers, if found set.
            addReviewers(kogibotConfig.get(), prPayLoad.getPullRequest(), github);
        }
    }

    public void addReviewers(KogibotConfiguration config, GHPullRequest pullRequest, GitHub github) throws IOException {
        Set<GHPullRequestFileDetail> ghUpdatedFiles = pullRequest.listFiles().toSet();
        LinkedHashSet<String> reviewers = new LinkedHashSet<>();

        Reviewers reviewersCfg = config.getReviewers();

        ghUpdatedFiles.stream().forEach(rFile -> {
            List<String> found = MatchingPathsValuesUtils.findPath(rFile.getFilename(), reviewersCfg.getMatchingPathsValues());
            if (!found.isEmpty()) {
                reviewers.addAll(found);
            }
        });
        if (reviewers.isEmpty()) {
            reviewers.addAll(reviewersCfg.getDefaults());
        }

        // does it make sense to do nonblocking here?
        // fetch each from GitHub and request review.
        List<GHUser> ghUsers = new ArrayList<>();
        for (String r : reviewers) {
            // pr.user is the author, if author is also a reviewer don't request.
            if (!r.equals(pullRequest.getUser().getLogin())) {
                log.debugv("Trying to fetch GHUser {0} to add as reviewer...", r);
                ghUsers.add(github.getUser(r));
            }
        }
        try {
            pullRequest.requestReviewers(ghUsers);
        } catch (final Exception e) {
            // TODO need to format this message on GH
            StringBuilder msg = new StringBuilder("Error while requesting reviewers `")
                    .append(ghUsers.stream().map(u -> u.getLogin()).collect(Collectors.toList())).append("`\n")
                    .append("\n```JSON\n").append(e.getMessage()).append("\n```");
            pullRequest.comment(msg.toString()).createReaction(ReactionContent.EYES);
        }
    }
}
