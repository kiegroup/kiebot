package org.kiegroup.kogibot.util;

import java.io.IOException;

import org.kohsuke.github.GHRepository;

public class Labels {

    public static final String TEST = "needs-review";
    public static final String NEEDS_REVIEW = "cherry-pick :cherries:";

    public static void createMissingLabels(GHRepository ghRepository) throws IOException {
        ghRepository.createLabel(NEEDS_REVIEW, "e57ec4", "This PR is a cherry-picked");
    }
}
