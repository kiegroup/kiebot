package org.kiegroup.kiebot.listeners.pr.impl;

import java.io.IOException;
import java.util.Optional;

import org.kiegroup.kiebot.config.KiebotConfiguration;
import org.kiegroup.kiebot.listeners.pr.PRCommentConfigListener;
import org.kiegroup.kiebot.util.Constants;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepository.Contributor;
import org.kohsuke.github.GitHub;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;

public class PRFirstTimeContributorListener implements PRCommentConfigListener {

    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KIEBOT_CONFIG_FILE) Optional<KiebotConfiguration> kiebotConfiguration,
            GitHub github) throws IOException {
        applyForConfig(prPayLoad, kiebotConfiguration, github);
    }

    @Override
    public String getCommentKey() {
        return Constants.KIEBOT_CONFIG_KEY_FIRST_TIME_CONTRIBUTOR;
    }

    @Override
    public String getListenerName() {
        return "First-time Contributor";
    }

    @Override
    public void applyWithComment(String comment, GHEventPayload.PullRequest prPayLoad,
            KiebotConfiguration kiebotConfiguration,
            GitHub github) throws IOException {
        GHPullRequest pullRequest = prPayLoad.getPullRequest();
        String author = pullRequest.getUser().getLogin();
        if (isFirstTimeContributor(pullRequest.getRepository(), author)) {
            pullRequest.comment(String.format("Hello @%s\n%s", author, comment));
        }

    }

    private boolean isFirstTimeContributor(GHRepository ghRepository, String login) throws IOException {
        return !ghRepository.listContributors().toSet().stream().map(Contributor::getLogin).anyMatch(login::equals);
    }
}
