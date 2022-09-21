package org.kiegroup.kogibot.cli.commands.legacy;

import java.io.IOException;

import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;

import com.github.rvesse.airline.annotations.Command;

import static org.kiegroup.kogibot.util.Workflows.executeGHWorkflows;

// @Cli(name = "/test",
//         commands = {TestCommand.TestPullRequestCommand.class},
//         defaultCommand = TestCommand.TestPullRequestCommand.class)
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class TestCommand {

    @Command(name = "test", description = "Test Pull Request", hidden = true)
    static class TestPullRequestCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            executeGHWorkflows(issueCommentPayload);
        }
    }

}
