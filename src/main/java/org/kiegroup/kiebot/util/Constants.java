package org.kiegroup.kiebot.util;

import java.util.Arrays;
import java.util.Optional;

public class Constants {

    public static final String KIEBOT_CONFIG_FILE = "kiebot-config.yml";
    public static final String KIEBOT_CONFIG_KEY_FIRST_TIME_CONTRIBUTOR = "first_time_contribute";

    public enum DefaultLabels {

        // keep in alphabetical order
        CHERRY_PICK("cherry-pick :cherries:", "8f81e2", "Pull Request is a cherry-pick"),
        ON_HOLD_NOT_APPROVED("on-hold/not-approved :hand:", "5319E7", "Pull request is on Hold, not approved"),
        APPROVED("approved/ready :rocket:", "C2E0C6", "The pull request is approved"),
        LGTM("lgtm :eyes:", "BFD4F2", "Pull Request looks good"),
        NEEDS_REVIEW("needs review :mag:", "c12645", "Pull Request that needs reviewers"),
        WIP("WIP :construction_worker_man:", "f9f584", "Pull Request is on work in progress");

        // Required labels will be added when a new Pull Request is created.
        private final String name;
        private final String color;
        private final String description;

        DefaultLabels(String name, String color, String description) {
            this.name = name;
            this.color = color;
            this.description = description;
        }

        public String getName() {
            return this.name;
        }

        public String getColor() {
            return color;
        }

        public String getDescription() {
            return description;
        }

        public static boolean hasLabel(String name) {
            return getOptionalLabel(name).isPresent();
        }

        public static DefaultLabels getLabel(String name) {
            return getOptionalLabel(name).get();
        }

        private static Optional<DefaultLabels> getOptionalLabel(String name) {
            return Arrays.asList(DefaultLabels.values()).stream().filter(l -> l.getName().equals(name)).findFirst();
        }

    }

    private Constants() {
    }
}
