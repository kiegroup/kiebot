package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.kohsuke.github.GHRepository;

public class Labels {

    private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static void processLabels(int id, GHRepository ghRepository) throws IOException {
        /*
            TODO: read conf from user - see ClientConfig
            createDefaultLabels?
            customLabels
            overrideDefultLabels
            deleteDefaultGHLabels?
         */
        // can this algorithm be improved?
        for (DefaultLabels lbl : DefaultLabels.allLabels()) {
            if (!ghRepository.listLabels().toList().stream().anyMatch(l -> l.getName().equals(lbl.name))) {
                ghRepository.createLabel(lbl.name, lbl.color, lbl.description);
                log.debugv("Label [{0}] not found, creating...", lbl.name);
            }

            // if the label is flagged as required, add it on the target Pull Request
            if (lbl.required) {
                ghRepository.getPullRequest(id).addLabels(lbl.name);
                log.debugv("Setting default label [{0}].", lbl.name);
            }
        }
    }

    public static void addLabel() {

    }


    public enum DefaultLabels {
        // keep in alphabetical order
        CHERRY_PICK(false, "cherry-pick :cherries:", "8f81e2", "Pull Request is a cherry-pick"),
        NEEDS_CHERRY_PICK(false, "needs cherry-pick :cherries:", "e57ec4", "This PR needs to be cherry-picked"),
        ON_HOLD_NOT_APPROVED(false, "on-hold/not-approved :hand:", "5319E7", "Pull request is on Hold, not approved"),
        APPROVED(false, "approved/ready :rocket:", "C2E0C6", "The pull request is approved"),
        LGTM(false, "lgtm :eyes:", "BFD4F2", "Pull Request looks good"),
        NEEDS_REVIEW(true, "needs review :mag:", "c12645", "Pull Request that needs reviewers"),
        WIP(false, "WIP :construction_worker_man:", "f9f584", "Pull Request is on work in progress"),
        OPERATOR(false, "operator :cloud:", "71afe2", "Pull Request related to kogito-operator code base"),
        BDD_TESTS(false, "bdd-tests \uD83E\uDDEA", "bfdadc", "PR is related to the BDD test framework");

        // Required labels will be added when a new Pull Request is created.
        private final boolean required;
        private final String name;
        private final String color;
        private final String description;

        DefaultLabels(boolean required, String name, String color, String description) {
            this.required = required;
            this.name = name;
            this.color = color;
            this.description = description;
        }

        public static List<DefaultLabels> allLabels() {
            return Arrays.asList(DefaultLabels.values());
        }

        public String getName() {
            return this.name;
        }
    }
}
