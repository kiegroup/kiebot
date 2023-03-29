package org.kiegroup.kiebot.cli.commands;

import java.io.IOException;

import org.kiegroup.kiebot.util.ErrorUtils;
import org.kiegroup.kiebot.util.LabelsUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import io.quarkiverse.githubapp.command.airline.Team;

@Cli(name = "/backport", defaultCommand = BackportCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS), aliases = { "/cherry-pick" })
@Command(name = "backport",
        description = "{BRANCH} or `/backport {BRANCH}` or `/cherry-pick {BRANCH}`. Add the cherry-pick/backport label so it can be backported to the given branch once the PR is merged.")
@Team("gatekeepers")
public class BackportCommand implements PullRequestScopedCommands {

    @Arguments(description = "Backport branch")
    String branch;

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
        if (branch != null) {
            LabelsUtils.addLabelsToPullRequest(pullRequest, "backport-" + branch);
        } else {
            ErrorUtils.logErrorAsPRComment(pullRequest, "No branch specified to backport\n Run `/bot help` for more information");
        }
    }

}
