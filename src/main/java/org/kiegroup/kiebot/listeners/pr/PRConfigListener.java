package org.kiegroup.kiebot.listeners.pr;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.kiegroup.kiebot.config.KiebotConfiguration;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;

public interface PRConfigListener {
    Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    default void applyForConfig(GHEventPayload.PullRequest prPayLoad,
            Optional<KiebotConfiguration> kiebotConfigurationOpt,
            GitHub github) throws IOException {
        LOG.debugf("Process %s on %s PR number %d", getListenerName(), prPayLoad.getRepository().getFullName(),
                prPayLoad.getNumber());
        if (kiebotConfigurationOpt.isPresent()) {
            KiebotConfiguration kiebotConfiguration = kiebotConfigurationOpt.get();
            if (hasConfigEnabled(kiebotConfiguration)) {
                apply(prPayLoad, kiebotConfiguration, github);
            } else {
                LOG.infof("No config found in file for processing %s on %s repository",
                        getListenerName(), prPayLoad.getRepository().getFullName());
            }
        } else {
            LOG.debugf("No config file found on %s", prPayLoad.getRepository().getFullName());
        }
    }

    boolean hasConfigEnabled(KiebotConfiguration kiebotConfiguration);

    String getListenerName();

    void apply(GHEventPayload.PullRequest prPayLoad, KiebotConfiguration kiebotConfiguration,
            GitHub github) throws IOException;
}
