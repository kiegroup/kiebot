package org.kiegroup.kogibot.cli.commands.wip;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import com.github.rvesse.airline.annotations.Command;

// @Cli(name = "/approve",
//         commands = {
//                 ApproveCommand.ApprovePullRequestCommand.class,
//                 ApproveCommand.PullRequestNotApprovedCommand.class
//         },
//         defaultCommand = ApproveCommand.ApprovePullRequestCommand.class
// )
// @CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS))
//@Team ({"AUTHORS", "CONTRIBUTORS"})
public class ApproveCommand {

    // define ApprovedLabel on Label class
    // define NotApprovedLabel on Label class

    @Command(name = "add", description = "Approve Pull Request", hidden = false)
    static class ApprovePullRequestCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            // create annotation/predicate/default interface method @isPullRequest
            if (issueCommentPayload.getIssue().isPullRequest()) {
                GHPullRequest pullRequest = issueCommentPayload.getRepository()
                        .getPullRequest(issueCommentPayload.getIssue().getNumber());

                pullRequest.comment("approved label will be added - WIP");
                // pullRequest.addLabels(Constants.DefaultLabels.APPROVED.getName());
            }
        }
    }

    // find a way to create an alias, maybe create sub methods for one command?
    @Command(name = "cancel", description = "Pull Request not Approved")
    static class PullRequestNotApprovedCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) {
            try {
                // create annotation/predicate/default interface method @isPullRequest
                if (issueCommentPayload.getIssue().isPullRequest()) {
                    GHPullRequest pullRequest = issueCommentPayload.getRepository()
                            .getPullRequest(issueCommentPayload.getIssue().getNumber());
                    pullRequest.comment("approve label will be removed and added the not approved - WIP");

                    // pullRequest.removeLabel(Constants.DefaultLabels.APPROVED.getName());

                    // pullRequest.addLabels(Constants.DefaultLabels.ON_HOLD_NOT_APPROVED.getName());

                    // remove label and move the flow forward
                    // add label not approved
                    // block merge - how can we do this?
                    System.out.println("auto merge - " + pullRequest.getAutoMerge());
                    System.out.println("mergeable? - " + pullRequest.getMergeable());
                    System.out.println("mergeable State - " + pullRequest.getMergeableState());
                }
            } catch (FileNotFoundException e) {
                // ignore - label not found
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    // add custom help message
    // when add DefaultCommand it
}
