package org.kiegroup.kogibot.cli.commands.legacy;

import java.io.IOException;

import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;

import com.github.rvesse.airline.annotations.Command;

import static org.kiegroup.kogibot.util.Workflows.executeGHWorkflows;

// @Cli(name = "/retest",
//         commands = {RetestCommand.RetestPullRequestCommand.class},
//         defaultCommand = RetestCommand.RetestPullRequestCommand.class)
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class RetestCommand {

    @Command(name = "retest", description = "Retest Pull Request", hidden = true)
    static class RetestPullRequestCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            executeGHWorkflows(issueCommentPayload);
        }
    }
}
