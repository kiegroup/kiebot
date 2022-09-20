package org.kiegroup.kogibot.listeners;

import java.io.IOException;
import java.util.Optional;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.util.Constants;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class PRFirstTimeContributorListener implements PROpenedListener {

    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KOGIBOT_CONFIGURATION_FILE) Optional<KogibotConfiguration> kogibotConfig,
            GitHub github) throws IOException {

        // // First time contributor? add a cool message; and link to a some starting
        // guide to the project.
        // // can be set on the clientConfig as well on the target repo
        // // Use the util.FirstTimeContribution class to implement it
        firstTimeContributionChecker(prPayLoad.getRepository());
        prPayLoad.getPullRequest().comment("Hi from " + prPayLoad.getPullRequest().getUser().getLogin());
    }

    // needs better test on a real repo
    private void firstTimeContributionChecker(GHRepository ghRepository) throws IOException {
        PagedIterable<GHRepository.Contributor> contributors = ghRepository.listContributors();
        // TODO
    }
}
