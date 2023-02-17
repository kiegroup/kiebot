package org.kiegroup.kogibot.listeners.pr;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;

public interface PRConfigListener {
    Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    default void applyForConfig(GHEventPayload.PullRequest prPayLoad,
            Optional<KogibotConfiguration> kogibotConfigurationOpt,
            GitHub github) throws IOException {
        LOG.debugf("Process %s on %s PR number %d", getListenerName(), prPayLoad.getRepository().getFullName(),
                prPayLoad.getNumber());
        if (kogibotConfigurationOpt.isPresent()) {
            KogibotConfiguration kogibotConfiguration = kogibotConfigurationOpt.get();
            if (hasConfigEnabled(kogibotConfiguration)) {
                apply(prPayLoad, kogibotConfiguration, github);
            } else {
                LOG.infof("No config found in file for processing %s on %s repository",
                        getListenerName(), prPayLoad.getRepository().getFullName());
            }
        } else {
            LOG.debugf("No config file found on %s", prPayLoad.getRepository().getFullName());
        }
    }

    boolean hasConfigEnabled(KogibotConfiguration kogibotConfiguration);

    String getListenerName();

    void apply(GHEventPayload.PullRequest prPayLoad, KogibotConfiguration kogibotConfiguration,
            GitHub github) throws IOException;
}
