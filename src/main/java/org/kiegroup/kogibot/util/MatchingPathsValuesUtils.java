package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.kiegroup.kogibot.config.AbstractMatchingPathsValuesWithDefaults;
import org.kiegroup.kogibot.config.MatchingPathsValues;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;

public class MatchingPathsValuesUtils {

    public static boolean matchPathsToFilename(String filename, MatchingPathsValues matchingPathsValues) {
        return matchingPathsValues.getPaths().stream().map(Pattern::compile).anyMatch(p -> p.matcher(filename).find());
    }

    public static List<String> retrieveMatchingPathsValuesFromPullRequest(GHPullRequest pullRequest,
            AbstractMatchingPathsValuesWithDefaults matchingPathsValuesWithDefaults) throws IOException {
        Set<GHPullRequestFileDetail> ghUpdatedFiles = pullRequest.listFiles().toSet();
        List<String> values = matchingPathsValuesWithDefaults.getMatchingPathsValues()
                .stream()
                .filter(matchingPathsValues -> ghUpdatedFiles.stream()
                        .map(GHPullRequestFileDetail::getFilename)
                        .anyMatch(filename -> {
                            return MatchingPathsValuesUtils.matchPathsToFilename(filename,
                                    matchingPathsValues);
                        }))
                .flatMap(matchingPathsValues -> {
                    return matchingPathsValues.getValues().stream();
                })
                .collect(Collectors.toList());
        if (values.isEmpty()) {
            values.addAll(matchingPathsValuesWithDefaults.getDefaults());
        }
        return values;
    }
}
