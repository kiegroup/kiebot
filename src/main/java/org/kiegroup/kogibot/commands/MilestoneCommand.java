package org.kiegroup.kogibot.commands;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

@Cli(name = "/milestone",
        commands = {
                MilestoneCommand.MilestoneAddCommand.class},
        defaultCommand = MilestoneCommand.MilestoneAddCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
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
}
