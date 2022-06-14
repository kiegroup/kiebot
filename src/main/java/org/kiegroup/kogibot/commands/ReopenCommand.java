package org.kiegroup.kogibot.commands;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

@Cli(name = "/reopen",
        commands = {ReopenCommand.ReopenPullRequestCommand.class},
        defaultCommand = ReopenCommand.ReopenPullRequestCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class ReopenCommand {


    @Command(name = "reopen", description = "Reopen Pull Request", hidden = true)
    static class ReopenPullRequestCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                // add a message about the PR closure.
                pullRequest.reopen();

            }
        }
    }


    // add custom help message

    // block merge - how can we do this?
}
