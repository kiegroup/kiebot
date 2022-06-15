package org.kiegroup.kogibot;

import java.io.IOException;

import javax.inject.Inject;

import io.quarkiverse.githubapp.event.IssueComment;
import io.quarkiverse.githubapp.event.PullRequest;
import io.quarkiverse.githubapp.event.PullRequestReviewComment;
import org.jboss.logging.Logger;
import org.kiegroup.kogibot.util.Labels;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;

public class PullRequestListener {

    @Inject
    Logger log;

    public void onOpenedPullRequest(@PullRequest.Opened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad, GitHub gitHub) throws IOException {

        log.infov("test PR author: {0}", prPayLoad.getPullRequest().getUser());
        log.info("adding labels");
        // what happens if the labels already exists?
        Labels.createMissingLabels(prPayLoad.getRepository());
        // Add initial labels, needs review, what else?

        // First time contributor? add a cool message; and link to a some starting guide to the project.
        // can be set on the clientConfig as well on the target repo
        // Use the util.FirstTimeContribution class to implement it
        prPayLoad.getPullRequest().comment("Hi from kogibot.");

        // check the PR patterns, look for a configuration file on the target repository
        // e.g. .kogibot.yaml
        // it can contain the desire pattern for:
        // 1 - Tittle pattern
        // 2 - Description patter
        // 3 - PR commit message
        // Bot will automatically not approve this PR until this condition is not satisfied.
        // Use the util.PullRequestPatternAnalyzer class to implement it.
        // Client config is held on the config.ClientConfig class.
    }

    public void onRequestReview(@PullRequestReviewComment.Created @PullRequestReviewComment.Edited GHEventPayload.PullRequestReviewComment comment) throws IOException {

        // do something
    }


    // list for comment
    public void onPullRequestComments(@IssueComment.Created GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = null;

        log.infov("aaaaa {0}", issueCommentPayload.getComment().getUser().getType());
        log.infov("CVCCCC {0}", issueCommentPayload.getIssue().isPullRequest() && !issueCommentPayload.getComment().getUser().getType().equals("Bot"));
        if (issueCommentPayload.getIssue().isPullRequest() && !issueCommentPayload.getComment().getUser().getType().equals("Bot")) {
            pullRequest = issueCommentPayload.getRepository()
                    .getPullRequest(issueCommentPayload.getIssue().getNumber());
            log.infov("test PR author: {0}", pullRequest.getUser());
            pullRequest.comment("Hi from kogibot - comments");

            log.info("adding labels");
            // create the missing labels on the same function to add it?
            // Labels.craeteMissingLabels(pullRequest.getRepository());
        }
    }
}
