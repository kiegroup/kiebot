package org.kiegroup.kiebot.cli.commands.wip;

import java.io.IOException;

import org.kiegroup.kiebot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Command;

// @Cli(name = "/hold",
//         commands = {
//                 HoldCommand.HoldAddCommand.class,
//                 HoldCommand.HoldAddCommand.class},
//         defaultCommand = HoldCommand.HoldAddCommand.class)
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
// @Team({"developers","gatekeepers"})
public class HoldCommand {

    @Command(name = "add", description = "Put the Pull Request on hold status")
    static class HoldAddCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("add hold and not approved labels and block merge - WIP");
                // add milestone

            }
        }
    }

    @Command(name = "cancel", description = "Removes milestone")
    static class HoldCancelCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("remove hold cancel label and unblock merge - WIP");
            }
        }
    }
}
