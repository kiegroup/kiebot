package org.kiegroup.kiebot.cli.commands.wip;

import java.io.IOException;

import org.kiegroup.kiebot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Command;

// @Cli(name = "/milestone",
//         commands = {
//                 MilestoneCommand.MilestoneAddCommand.class,
//                 MilestoneCommand.MilestoneRemoveCommand.class},
//         defaultCommand = MilestoneCommand.MilestoneAddCommand.class)
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
// @Team ({"@kiegroup/gatekeepers"})
public class MilestoneCommand {

    @Command(name = "add", description = "Adds milestone")
    static class MilestoneAddCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("add milestone XXXX - WIP");
                // add milestone

            }
        }
    }

    @Command(name = "remove", description = "Removes milestone")
    static class MilestoneRemoveCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("remove  milestone - WIP");
                // remove milestone
                pullRequest.getMilestone().delete();
            }
        }
    }
}
