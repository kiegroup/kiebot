package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.util.Constants.DefaultLabels;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

public class LabelsUtils {
  private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  public static final void addLabelsToPullRequest(List<String> labels, GHPullRequest pullRequest) throws IOException{
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
}
