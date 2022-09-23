package org.kiegroup.kogibot.util;

import java.io.IOException;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.ReactionContent;

public class ErrorUtils {

  public static final void logErrorAsPRComment(GHPullRequest pullRequest, String message, Throwable t) throws IOException {
    StringBuilder sb = new StringBuilder(message);
    sb.append("\nMessage:")
        .append("\n```JSON\n").append(t.getMessage()).append("\n```");
    logErrorAsPRComment(pullRequest, sb.toString());
  }

  public static final void logErrorAsPRComment(GHPullRequest pullRequest, String message) throws IOException {
    pullRequest.comment(message).createReaction(ReactionContent.EYES);
  }
}
