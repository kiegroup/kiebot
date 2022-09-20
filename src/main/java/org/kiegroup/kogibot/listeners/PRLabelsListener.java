package org.kiegroup.kogibot.listeners;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;
import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.util.Constants;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class PRLabelsListener implements PROpenedListener  {

    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public void onOpenedPullRequest(@PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad, @ConfigFile(Constants.KOGIBOT_CONFIGURATION_FILE) Optional<KogibotConfiguration> kogibotConfig ,GitHub github) throws IOException {
        log.info("Process labels");
        // processLabels(prPayLoad.getNumber(), prPayLoad.getRepository());
    }

    private void processLabels(int id, GHRepository ghRepository) throws IOException {
        log.info("Process labels");
        // // can this algorithm be improved?
        // for (DefaultLabels lbl : DefaultLabels.allLabels()) {
        //     if (!ghRepository.listLabels().toList().stream().anyMatch(l -> l.getName().equals(lbl.getName()))) {
        //         ghRepository.createLabel(lbl.getName(), lbl.getColor(), lbl.getDescription());
        //         log.debugv("Label [{0}] not found, creating...", lbl.getName());
        //     }

        //     // if the label is flagged as required, add it on the target Pull Request
        //     if (lbl.isRequired()) {
        //         ghRepository.getPullRequest(id).addLabels(lbl.getName());
        //         log.debugv("Setting default label [{0}].", lbl.getName());
        //     }
        // }
    }

    private void addLabel() {
        // TODO
    }
}
