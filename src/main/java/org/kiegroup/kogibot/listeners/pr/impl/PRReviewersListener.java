package org.kiegroup.kogibot.listeners.pr.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.config.Reviewers;
import org.kiegroup.kogibot.listeners.pr.PRConfigListener;
import org.kiegroup.kogibot.util.Constants;
import org.kiegroup.kogibot.util.ErrorUtils;
import org.kiegroup.kogibot.util.MatchingPathsValuesUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;

public class PRReviewersListener implements PRConfigListener {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KOGIBOT_CONFIG_FILE) Optional<KogibotConfiguration> kogibotConfiguration,
            GitHub github) throws IOException {
        applyForConfig(prPayLoad, kogibotConfiguration, github);
    }

    @Override
    public boolean hasConfigEnabled(KogibotConfiguration kogibotConfiguration) {
        return kogibotConfiguration.getReviewers() != null;
    }

    @Override
    public String getListenerName() {
        return "Reviewers";
    }

    @Override
    public void apply(GHEventPayload.PullRequest prPayLoad, KogibotConfiguration kogibotConfiguration, GitHub github)
            throws IOException {
        GHPullRequest pullRequest = prPayLoad.getPullRequest();
        List<String> reviewers = retrieveReviewers(pullRequest, kogibotConfiguration.getReviewers());
        List<GHUser> ghUsers = retrieveGHUsers(github, reviewers);

        try {
            pullRequest.requestReviewers(ghUsers);
        } catch (final IOException e) {
            StringBuilder msg = new StringBuilder("Error while requesting those reviewers:\n");
            for (GHUser user : ghUsers) {
                msg.append("- `" + user.getLogin() + "`\n");
            }
            ErrorUtils.logErrorAsPRComment(pullRequest, msg.toString(), e);
            throw e;
        }
    }

    public List<String> retrieveReviewers(GHPullRequest pullRequest, Reviewers reviewersCfg) throws IOException {
        List<String> reviewers = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(pullRequest,
                reviewersCfg);
        final String currentAuthor = pullRequest.getUser().getLogin();
        // Filter out current author
        return reviewers.stream().filter(reviewer -> !reviewer.equals(currentAuthor)).collect(Collectors.toList());
    }

    // does it make sense to do nonblocking here?
    // fetch each from GitHub and request review.
    public List<GHUser> retrieveGHUsers(GitHub github, Collection<String> users) throws IOException {
        List<GHUser> ghUsers = new ArrayList<>();
        for (String r : users) {
            LOG.debugf("Trying to fetch GHUser %s...", r);
            ghUsers.add(github.getUser(r));
        }
        return ghUsers;
    }
}
