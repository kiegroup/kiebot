package org.kiegroup.kiebot.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.kiegroup.kiebot.util.Constants.DefaultLabels;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

public class LabelsUtils {
    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static final void addLabelsToPullRequest(GHPullRequest pullRequest, String... labels) throws IOException {
        addLabelsToPullRequest(pullRequest, Arrays.asList(labels));
    }

    public static final void addLabelsToPullRequest(GHPullRequest pullRequest, List<String> labels) throws IOException {
        GHRepository ghRepository = pullRequest.getRepository();
        List<GHLabel> ghLabels = ghRepository.listLabels().toList();
        for (String labelName : labels) {
            if (!ghLabels.stream().map(GHLabel::getName).anyMatch(l -> l.equals(labelName))) {
                LOG.debugf("Label [%s] not found, creating...", labelName);

                String color = null;
                String description = null;
                if (DefaultLabels.hasLabel(labelName)) {
                    color = DefaultLabels.getLabel(labelName).getColor();
                    description = DefaultLabels.getLabel(labelName).getDescription();
                }
                LOG.infof("Create label %s with color %s and description %s", labelName, color, description);
                ghRepository.createLabel(labelName, color, description);
            }

            // Add label to PR
            LOG.debugf("Applying label [%s].", labelName);
            pullRequest.addLabels(labelName);
        }
    }

    public static final void removeLabelsFromPullRequest(GHPullRequest pullRequest, String... labels) throws IOException {
        removeLabelsFromPullRequest(pullRequest, Arrays.asList(labels));
    }

    public static final void removeLabelsFromPullRequest(GHPullRequest pullRequest, List<String> labels) throws IOException {
        GHRepository ghRepository = pullRequest.getRepository();
        for (String labelName : labels) {
            LOG.debugf("Removing label [%s].", labelName);
            pullRequest.removeLabels(labelName);
        }
    }
}
