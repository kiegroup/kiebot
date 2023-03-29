package org.kiegroup.kiebot.cli.commands;

import java.io.IOException;

import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import io.quarkiverse.githubapp.command.airline.Team;

@Cli(name = "/open", defaultCommand = CloseCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS), aliases = { "/reopen" })
@Command(name = "open", description = "`or /open` or `/reopen`. Open current Pull Request")
@Team("developers")
public class OpenCommand implements PullRequestScopedCommands {

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
        pullRequest.reopen();
    }
}
