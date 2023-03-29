package org.kiegroup.kiebot.listeners.pr;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.jboss.logging.Logger;
import org.kiegroup.kiebot.config.KiebotConfiguration;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;

public interface PRCommentConfigListener extends PRConfigListener {
    Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    default void apply(GHEventPayload.PullRequest prPayLoad, KiebotConfiguration kiebotConfiguration,
            GitHub github) throws IOException {
        if (kiebotConfiguration.getComments().containsKey(getCommentKey())) {
            applyWithComment(
                    kiebotConfiguration.getComments().get(getCommentKey()), prPayLoad, kiebotConfiguration, github);
        } else {
            LOG.warnf("No %s comment found in config file on %s repository",
                    getCommentKey(), prPayLoad.getRepository().getFullName());
        }
    }

    default boolean hasConfigEnabled(KiebotConfiguration kiebotConfiguration) {
        return !kiebotConfiguration.getComments().isEmpty();
    }

    String getCommentKey();

    void applyWithComment(String comment, GHEventPayload.PullRequest prPayLoad, KiebotConfiguration kiebotConfiguration,
            GitHub github) throws IOException;
}
