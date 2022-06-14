package org.kiegroup.kogibot.commands;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

@Cli(name = "/lgtm",
        commands = {
                LgtmCommand.AddLgtmLabelCommand.class,
                LgtmCommand.RemoveLgtmLabelCommand.class},
        defaultCommand = LgtmCommand.AddLgtmLabelCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"gatekeepers"})
public class LgtmCommand {

    // define LgtmLabel on Label class

    @Command(name = "add", description = "Adds lgtm label")
    static class AddLgtmLabelCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("lgtm label will be added - WIP");
                // add label and move the flow forward
            }
        }
    }

    // find a way to create a alias, maybe create sub methods for one command?
    @Command(name = "cancel", description = "Removes lgtm label")
    static class RemoveLgtmLabelCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("lgtm label will be removed - WIP");
                // remove label and move the flow forward
            }
        }
    }

    // add custom help message

    // block merge - how can we do this?
}
