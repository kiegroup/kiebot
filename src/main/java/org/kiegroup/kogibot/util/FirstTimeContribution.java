package org.kiegroup.kogibot.util;

import java.io.IOException;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;

public class FirstTimeContribution {

    // needs better test on a real repo
    public static void firstTimeContributionChecker(GHRepository ghRepository) throws IOException {
        PagedIterable<GHRepository.Contributor> contributors = ghRepository.listContributors();
    }
}
