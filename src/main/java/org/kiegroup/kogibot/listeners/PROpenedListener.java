package org.kiegroup.kogibot.listeners;

import java.io.IOException;
import java.util.Optional;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.util.Constants;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;

public interface PROpenedListener {
  void onOpenedPullRequest(@PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad, @ConfigFile(Constants.KOGIBOT_CONFIGURATION_FILE) Optional<KogibotConfiguration> kogibotConfig, GitHub github) throws IOException;
}
