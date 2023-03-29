package org.kiegroup.kiebot.cli.commands;

import java.io.IOException;

import org.kiegroup.kiebot.util.Constants;
import org.kiegroup.kiebot.util.LabelsUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import io.quarkiverse.githubapp.command.airline.Team;

@Cli(name = "/approve",
        commands = {
                ApproveCommand.ApprovePullRequestCommand.class,
                ApproveCommand.ApproveCancelPullRequestCommand.class
        },
        defaultCommand = ApproveCommand.ApprovePullRequestCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
@Team({ "developers", "gatekeepers" })
public class ApproveCommand {

    @Command(name = "add", description = "Approve Pull Request", hidden = false)
    public static class ApprovePullRequestCommand implements PullRequestScopedCommands {
        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
            LabelsUtils.addLabelsToPullRequest(pullRequest, Constants.DefaultLabels.APPROVED.getName());
            pullRequest.comment(String.format("Approved by %s", pullRequest.getUser().getName()));

            // Disabled for now as I did not find a way to make a review approval on behalf of a user ...
            // GHPullRequestReview review = pullRequest.createReview().event(GHPullRequestReviewEvent.APPROVE).create();
        }
    }

    @Command(name = "cancel", description = "Pull Request not Approved")
    public static class ApproveCancelPullRequestCommand implements PullRequestScopedCommands {
        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
            LabelsUtils.removeLabelsFromPullRequest(pullRequest, Constants.DefaultLabels.APPROVED.getName());

            // To activate once review approval can be done on behalf of a user (see above)
            // for(GHPullRequestReview review : pullRequest.listReviews().toSet()) {
            //     if(review.getUser().getLogin().equals(pullRequest.getUser().getLogin()) 
            //         && review.getState() == GHPullRequestReviewState.APPROVED) {
            //         review.dismiss(String.format("Dismissed by %s", pullRequest.getUser().getName()));
            //     }
            // }
        }
    }
}
