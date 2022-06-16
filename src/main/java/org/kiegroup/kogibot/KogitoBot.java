package org.kiegroup.kogibot;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

public class KogitoBot {

    @Inject
    Logger log;

    void init(@Observes StartupEvent startupEvent) {
        log.debug("››› Kogibot is starting...");
    }
}
