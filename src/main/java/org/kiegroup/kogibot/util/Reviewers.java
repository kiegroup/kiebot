package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.pojo.ClientConfiguration;
import org.kiegroup.kogibot.config.pojo.Review;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.ReactionContent;

public class Reviewers {

    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static void addReviewers(ClientConfiguration config, GHPullRequest pullRequest, GitHub github) throws IOException {

        Set<GHPullRequestFileDetail> ghUpdatedFiles = pullRequest.listFiles().toSet();
        LinkedHashSet<String> reviewers = new LinkedHashSet<>();

        ghUpdatedFiles.stream().forEach(rFile -> {
            List<String> found = Reviewers.findPath(rFile.getFilename(), config.getReview());
            if (!found.isEmpty()) {
                reviewers.addAll(found);
            }
        });
        if (reviewers.isEmpty()) {
            reviewers.addAll(config.getDefaultReviewers());
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

    public static List<String> findPath(String value, List<Review> list) {
        for (int i = 0; i < list.size(); i++) {
            for (String path : list.get(i).getPaths()) {
                Pattern p = Pattern.compile(path);
                if (p.matcher(value).find()) {
                    return list.get(i).getReviewers();
                }
            }
        }
        return new ArrayList<>();
    }
}

