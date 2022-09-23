package org.kiegroup.kogibot.cli.commands;

import java.io.IOException;

import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

public interface PullRequestScopedCommands {
    void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException;

    default GHPullRequest getPullRequest(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        return issueCommentPayload.getRepository().getPullRequest(issueCommentPayload.getIssue().getNumber());
    }
}
