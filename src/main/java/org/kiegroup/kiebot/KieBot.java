package org.kiegroup.kiebot;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;

public class KieBot {

    @Inject
    Logger log;

    void init(@Observes StartupEvent startupEvent) {
        log.debug("››› KieBot is starting...");
    }
}
