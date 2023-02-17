package org.kiegroup.kogibot.listeners.pr;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;

public interface PRCommentConfigListener extends PRConfigListener {
    Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    default void apply(GHEventPayload.PullRequest prPayLoad, KogibotConfiguration kogibotConfiguration,
            GitHub github) throws IOException {
        if (kogibotConfiguration.getComments().containsKey(getCommentKey())) {
            applyWithComment(
                    kogibotConfiguration.getComments().get(getCommentKey()), prPayLoad, kogibotConfiguration, github);
        } else {
            LOG.warnf("No %s comment found in config file on %s repository",
                    getCommentKey(), prPayLoad.getRepository().getFullName());
        }
    }

    default boolean hasConfigEnabled(KogibotConfiguration kogibotConfiguration) {
        return !kogibotConfiguration.getComments().isEmpty();
    }

    String getCommentKey();

    void applyWithComment(String comment, GHEventPayload.PullRequest prPayLoad, KogibotConfiguration kogibotConfiguration,
            GitHub github) throws IOException;
}
