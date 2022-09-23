package org.kiegroup.kogibot.cli.commands.wip;

import java.io.IOException;

import org.kiegroup.kogibot.cli.commands.CloseCommand;
import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;

@Cli(name = "/reopen", defaultCommand = CloseCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS), aliases = { "/open" })
@Command(name = "open", description = "(not yet working ...) or `/open` or `/reopen`. Open current Pull Request")
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class OpenCommand implements PullRequestScopedCommands {

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
        pullRequest.reopen();
    }
}
