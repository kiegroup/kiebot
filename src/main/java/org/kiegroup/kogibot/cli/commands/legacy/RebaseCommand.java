package org.kiegroup.kogibot.cli.commands.legacy;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Command;
import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

// @Cli(name = "/rebase",
//         commands = {RebaseCommand.RebasePullRequestCommand.class},
//         defaultCommand = RebaseCommand.RebasePullRequestCommand.class)
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class RebaseCommand {


    @Command(name = "rebase", description = "Reopen Pull Request", hidden = true)
    static class RebasePullRequestCommand implements PullRequestScopedCommands {

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
