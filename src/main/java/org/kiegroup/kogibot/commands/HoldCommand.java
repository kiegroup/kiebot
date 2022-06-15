package org.kiegroup.kogibot.commands;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

@Cli(name = "/hold",
        commands = {
                HoldCommand.HoldAddCommand.class,
                HoldCommand.HoldAddCommand.class},
        defaultCommand = HoldCommand.HoldAddCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"GATEKEEPERS", "AUTHORS"})
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
