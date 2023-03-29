package org.kiegroup.kiebot.listeners.pr.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.kiegroup.kiebot.config.KiebotConfiguration;
import org.kiegroup.kiebot.config.Labels;
import org.kiegroup.kiebot.listeners.pr.PRConfigListener;
import org.kiegroup.kiebot.util.Constants;
import org.kiegroup.kiebot.util.LabelsUtils;
import org.kiegroup.kiebot.util.MatchingPathsValuesUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;

public class PRLabelsListener implements PRConfigListener {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KIEBOT_CONFIG_FILE) Optional<KiebotConfiguration> kiebotConfiguration,
            GitHub github) throws IOException {
        applyForConfig(prPayLoad, kiebotConfiguration, github);
    }

    @Override
    public String getListenerName() {
        return "Labels";
    }

    @Override
    public boolean hasConfigEnabled(KiebotConfiguration kiebotConfiguration) {
        return kiebotConfiguration.getLabels() != null;
    }

    @Override
    public void apply(GHEventPayload.PullRequest prPayLoad, KiebotConfiguration kiebotConfiguration, GitHub github)
            throws IOException {
        GHPullRequest pullRequest = prPayLoad.getPullRequest();
        List<String> labels = retrieveLabels(pullRequest, kiebotConfiguration.getLabels());
        LabelsUtils.addLabelsToPullRequest(labels, pullRequest);
    }

    public List<String> retrieveLabels(GHPullRequest pullRequest, Labels labelsCfg) throws IOException {
        return MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(pullRequest, labelsCfg);
    }
}
