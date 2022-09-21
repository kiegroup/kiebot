package org.kiegroup.kogibot;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;

public class KogitoBot {

    @Inject
    Logger log;

    void init(@Observes StartupEvent startupEvent) {
        log.debug("››› Kogibot is starting...");
    }
}
