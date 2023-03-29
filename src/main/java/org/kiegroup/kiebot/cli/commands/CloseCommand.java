package org.kiegroup.kiebot.cli.commands;

import java.io.IOException;

import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;

@Cli(name = "/close", defaultCommand = CloseCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
@Command(name = "close", description = "or `/close`. Close current Pull Request")
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class CloseCommand implements PullRequestScopedCommands {

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);

        // add a message about the PR closure.
        pullRequest.close();

        // TODO delete branch ?
    }

}
