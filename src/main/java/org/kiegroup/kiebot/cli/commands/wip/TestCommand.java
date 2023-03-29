package org.kiegroup.kiebot.cli.commands.wip;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.kiegroup.kiebot.cli.commands.PullRequestScopedCommands;
import org.kiegroup.kiebot.util.WorkflowsUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.ReactionContent;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;

@Cli(name = "/test", defaultCommand = TestCommand.class)
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS), aliases = { "/retest" })
@Command(name = "test",
        description = "(not yet fully implemented) or `/test` or `/retest`. Launch GHA Workflows. You can specific the workflow name to (re)start (Workflow names with whitespaces should be entered in quotes). Add `failed` if you want to retest all failed workflows.")
// @Team ({"AUTHORS", "CONTRIBUTORS"})
public class TestCommand implements PullRequestScopedCommands {
    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Arguments(description = "Job name to execute")
    String jobName;

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        GHPullRequest pullRequest = getPullRequest(issueCommentPayload);
        GHIssueComment comment = issueCommentPayload.getComment();
        if (jobName != null) {
            switch (jobName) {
                case "failed":
                    // TODO
                    executeWorkflowsAndComment(Arrays.asList(), comment, pullRequest);
                    break;
                default:
                    executeWorkflowsAndComment(Arrays.asList(), comment, pullRequest);
                    break;
            }
        } else {
            executeWorkflowsAndComment(WorkflowsUtils.getLatestWorkflowRuns(pullRequest), comment, pullRequest);
        }
    }

    void executeWorkflowsAndComment(List<GHWorkflowRun> workflowRuns, GHIssueComment comment, GHPullRequest pullRequest) throws IOException {
        try {
            for (GHWorkflowRun wr : workflowRuns) {
                LOG.debugf("Rerun workflow %s", wr.getName());
                wr.rerun();
            }
        } catch (IOException e) {
            LOG.error("Error while rerunning workflows ...", e);
            comment.createReaction(ReactionContent.CONFUSED);
        }
    }

    // private String listWorkflowsString(List<GHWorkflowRun> workflowRuns) {
    //     StringBuilder sb = new StringBuilder("(Re)Started) those workflows:\n");
    //     for(GHWorkflowRun wr : workflowRuns) {
    //         sb.append("- Workflow `" + wr.getName() +"`. Attempt = " + wr.getRunAttempt() + "\n");
    //     }
    //     return sb.toString();
    // }
}
