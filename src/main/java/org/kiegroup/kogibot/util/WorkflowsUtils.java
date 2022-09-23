package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHWorkflow;
import org.kohsuke.github.GHWorkflowRun;

import io.quarkiverse.githubapp.event.WorkflowRun;

public class WorkflowsUtils {

        private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

        public static void executeGHWorkflows(final GHEventPayload.IssueComment issueCommentPayload) throws IOException {
                if (issueCommentPayload.getIssue().isPullRequest()) {
                        GHPullRequest pullRequest = issueCommentPayload.getRepository()
                                        .getPullRequest(issueCommentPayload.getIssue().getNumber());
                        List<GHWorkflowRun> ghWorkflowRuns = pullRequest.getRepository().queryWorkflowRuns()
                                        .branch(pullRequest.getHead().getRef())
                                        .list()
                                        .toList()
                                        .stream()
                                        .filter(workflowRun -> workflowRun.getHeadRepository().getOwnerName()
                                                        .equals(pullRequest.getHead().getRepository().getOwnerName()))
                                        .filter(workflowRun -> workflowRun.getStatus() != GHWorkflowRun.Status.QUEUED
                                                        || workflowRun.getStatus() != GHWorkflowRun.Status.IN_PROGRESS)
                                        .collect(Collectors.toList());

                        for (GHWorkflowRun workflowRun : ghWorkflowRuns) {
                                workflowRun.rerun();
                        }
                } else {
                        log.warn("Issue is not a PR");
                }
        }

        /**
         * Get all workflow runs
         * 
         * @param pullRequest
         * @return
         * @throws IOException
         */
        public static List<GHWorkflowRun> getAllWorkflowRuns(GHPullRequest pullRequest) throws IOException {
                return pullRequest.getRepository().queryWorkflowRuns()
                                .branch(pullRequest.getHead().getRef())
                                .list()
                                .toSet()
                                .stream()
                                .filter(workflowRun -> workflowRun.getHeadRepository().getOwnerName()
                                                .equals(pullRequest.getHead().getRepository().getOwnerName()))
                                .collect(Collectors.toList());
        }

        /**
         * Return all workflow runs which
         * 
         * @param pullRequest
         * @return
         * @throws IOException
         */
        public static List<GHWorkflowRun> getLatestWorkflowRuns(GHPullRequest pullRequest) throws IOException {
                Map<String, GHWorkflowRun> latestWorkflowRuns = new HashMap<>();
                for (GHWorkflowRun wr : getAllWorkflowRuns(pullRequest)) {
                        if (latestWorkflowRuns.containsKey(wr.getName())) {
                                if (wr.getRunAttempt() > latestWorkflowRuns.get(wr.getName()).getRunAttempt()) {
                                        latestWorkflowRuns.put(wr.getName(), wr);
                                }
                        } else {
                                latestWorkflowRuns.put(wr.getName(), wr);
                        }
                }
                return latestWorkflowRuns.values().stream().collect(Collectors.toList());
        }

        /**
         * Return all workflow runs which
         * 
         * @param pullRequest
         * @return
         * @throws IOException
         */
        public static List<GHWorkflowRun> getNonRunningWorkflows(GHPullRequest pullRequest) throws IOException {
                return getLatestWorkflowRuns(pullRequest).stream()
                                .filter(workflowRun -> workflowRun.getStatus() != GHWorkflowRun.Status.QUEUED
                                                || workflowRun.getStatus() != GHWorkflowRun.Status.IN_PROGRESS)
                                .collect(Collectors.toList());
        }
}
