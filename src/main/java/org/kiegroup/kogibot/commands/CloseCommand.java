package org.kiegroup.kogibot.commands;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

@Cli(name = "/close",
        commands = {CloseCommand.ClosePullRequestCommand.class},
        defaultCommand = CloseCommand.ClosePullRequestCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class CloseCommand {


    @Command(name = "close", description = "Close Pull Request", hidden = true)
    static class ClosePullRequestCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                // add a message about the PR closure.
                pullRequest.close();

            }
        }
    }


    // add custom help message

    // block merge - how can we do this?
}
