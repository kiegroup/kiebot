package org.kiegroup.kogibot.cli.commands;

import java.io.IOException;
import java.util.Arrays;

import org.kiegroup.kogibot.util.ErrorUtils;
import org.kiegroup.kogibot.util.LabelsUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;

@Cli(name = "/backport", defaultCommand = BackportCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
@Command(name = "backport", description = "{BRANCH} or `/backport {BRANCH}` or `/cherry-pick {BRANCH}`. Add the cherry-pick/backport label so it can be backported to another once the PR is merged.")
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class BackportCommand implements PullRequestScopedCommands {

    @Arguments(description = "Backport branch")
    String branch;

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
        if (branch != null) {
            LabelsUtils.addLabelsToPullRequest(Arrays.asList("backport-" + branch), pullRequest);   
        } else {
            ErrorUtils.logErrorAsPRComment(pullRequest, "No branch specified to backport\n Run `/bot help` for more information");
        }
    }

}
