package org.kiegroup.kiebot.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kiegroup.kiebot.config.AbstractMatchingPathsValuesWithDefaults;
import org.kiegroup.kiebot.config.MatchingPathsValues;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.PagedIterable;
import org.mockito.Mockito;

public class MatchingPathsValuesUtilsTest {

    GHPullRequest ghPullRequestMock;
    PagedIterable<GHPullRequestFileDetail> fileDetailsIterableMock;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        ghPullRequestMock = Mockito.mock(GHPullRequest.class);
        fileDetailsIterableMock = (PagedIterable<GHPullRequestFileDetail>) Mockito.mock(PagedIterable.class);
        Mockito.when(ghPullRequestMock.listFiles()).thenReturn(fileDetailsIterableMock);
    }

    @Test
    public void testMatchPathsToFilenameMatchFile() {
        MatchingPathsValues matchingPathsValues = new MatchingPathsValues();
        matchingPathsValues.setPaths(Arrays.asList("path1/*", "file1*", "file2"));
        matchingPathsValues.setValues(Arrays.asList("user"));

        Assertions.assertThat(MatchingPathsValuesUtils.matchPathsToFilename("file2", matchingPathsValues))
                .as("Should be matching exact file")
                .isTrue();
    }

    @Test
    public void testMatchPathsToFilenameMatchFileInSubfolder() {
        MatchingPathsValues matchingPathsValues = new MatchingPathsValues();
        matchingPathsValues.setPaths(Arrays.asList("path1/*", "file1*", "file2"));
        matchingPathsValues.setValues(Arrays.asList("user"));

        Assertions.assertThat(MatchingPathsValuesUtils.matchPathsToFilename("path1/anyfile", matchingPathsValues))
                .as("Should be matching file in subfolder")
                .isTrue();
    }

    @Test
    public void testMatchPathsToFilenameMatchFileRegex() {
        MatchingPathsValues matchingPathsValues = new MatchingPathsValues();
        matchingPathsValues.setPaths(Arrays.asList("path1/*", "file1*", "file2"));
        matchingPathsValues.setValues(Arrays.asList("user"));

        Assertions.assertThat(MatchingPathsValuesUtils.matchPathsToFilename("file1anything", matchingPathsValues))
                .as("Should be matching file with regex")
                .isTrue();
    }

    @Test
    public void testMatchPathsToFilenameNoMatch() {
        MatchingPathsValues matchingPathsValues = new MatchingPathsValues();
        matchingPathsValues.setPaths(Arrays.asList("path1/*", "file1*", "file2"));
        matchingPathsValues.setValues(Arrays.asList("user"));

        Assertions.assertThat(MatchingPathsValuesUtils.matchPathsToFilename("nomatch", matchingPathsValues))
                .as("Should NOT be matching any path")
                .isFalse();
    }

    @Test
    public void testRetrieveMatchingPathsValuesFromPullRequestNoPathMatching() throws IOException {
        AbstractMatchingPathsValuesWithDefaults abstractMatchingPathsValuesWithDefaults = createMatchingPathsValuesWithDefaultsTest();
        createGHPullRequestFileDetailMocks("no/path/matching");

        List<String> values = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(ghPullRequestMock, abstractMatchingPathsValuesWithDefaults);
        Assertions.assertThat(values)
                .as("No path matching, we should have only default users")
                .contains("defaultUser1", "defaultUser2");
    }

    @Test
    public void testRetrieveMatchingPathsValuesFromPullRequestOneUserOnePathMatching() throws IOException {
        AbstractMatchingPathsValuesWithDefaults abstractMatchingPathsValuesWithDefaults = createMatchingPathsValuesWithDefaultsTest();
        createGHPullRequestFileDetailMocks("oneUserPath1/anything");

        List<String> values = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(ghPullRequestMock, abstractMatchingPathsValuesWithDefaults);
        Assertions.assertThat(values)
                .as("One path matching, we should have only one user")
                .contains("oneUser");
    }

    @Test
    public void testRetrieveMatchingPathsValuesFromPullRequestManyUsersOnePathMatching() throws IOException {
        AbstractMatchingPathsValuesWithDefaults abstractMatchingPathsValuesWithDefaults = createMatchingPathsValuesWithDefaultsTest();
        createGHPullRequestFileDetailMocks("manyUserFile1anything");

        List<String> values = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(ghPullRequestMock, abstractMatchingPathsValuesWithDefaults);
        Assertions.assertThat(values)
                .as("One path matching, we should have many users")
                .contains("manyUser1", "manyUser2");
    }

    @Test
    public void testRetrieveMatchingPathsValuesFromPullRequestOneUserManyPathsMatching() throws IOException {
        AbstractMatchingPathsValuesWithDefaults abstractMatchingPathsValuesWithDefaults = createMatchingPathsValuesWithDefaultsTest();
        createGHPullRequestFileDetailMocks("oneUserFile2", "anotherFile");

        List<String> values = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(ghPullRequestMock, abstractMatchingPathsValuesWithDefaults);
        Assertions.assertThat(values)
                .as("Many paths matching, we should have only one user")
                .contains("oneUser");
    }

    @Test
    public void testRetrieveMatchingPathsValuesFromPullRequestManyUsersManyPathsMatching() throws IOException {
        AbstractMatchingPathsValuesWithDefaults abstractMatchingPathsValuesWithDefaults = createMatchingPathsValuesWithDefaultsTest();
        createGHPullRequestFileDetailMocks("oneUserFile2", "anotherFile", "manyUserFile2");

        List<String> values = MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(ghPullRequestMock, abstractMatchingPathsValuesWithDefaults);
        Assertions.assertThat(values)
                .as("Many paths matching, we should have only one user")
                .contains("oneUser", "manyUser1", "manyUser2");
    }

    private void createGHPullRequestFileDetailMocks(String... filenames) throws IOException {
        Set<GHPullRequestFileDetail> fileDetailsMock = Arrays.asList(filenames).stream()
                .map(this::createGHPullRequestFileDetailMock)
                .collect(Collectors.toSet());
        Mockito.when(fileDetailsIterableMock.toSet()).thenReturn(fileDetailsMock);
    }

    private GHPullRequestFileDetail createGHPullRequestFileDetailMock(String filename) {
        GHPullRequestFileDetail fileDetailsMock = Mockito.mock(GHPullRequestFileDetail.class);
        Mockito.when(fileDetailsMock.getFilename()).thenReturn(filename);
        return fileDetailsMock;
    }

    private AbstractMatchingPathsValuesWithDefaults createMatchingPathsValuesWithDefaultsTest() {
        MatchingPathsValuesWithDefaultsTest matchingPathsValuesWithDefaultsTest = new MatchingPathsValuesWithDefaultsTest();
        matchingPathsValuesWithDefaultsTest.setDefaults(Arrays.asList("defaultUser1", "defaultUser2"));

        MatchingPathsValues oneUserMatchingPathsValues = new MatchingPathsValues();
        oneUserMatchingPathsValues.setPaths(Arrays.asList("oneUserPath1/*", "oneUserFile1*", "oneUserFile2"));
        oneUserMatchingPathsValues.setValues(Arrays.asList("oneUser"));

        MatchingPathsValues manyUsersMatchingPathsValues = new MatchingPathsValues();
        manyUsersMatchingPathsValues.setPaths(Arrays.asList("manyUserPath1/*", "manyUserFile1*", "manyUserFile2"));
        manyUsersMatchingPathsValues.setValues(Arrays.asList("manyUser1", "manyUser2"));

        MatchingPathsValues anotherPathSameOneUserMatchingPathsValues = new MatchingPathsValues();
        anotherPathSameOneUserMatchingPathsValues.setPaths(Arrays.asList("anotherFile"));
        anotherPathSameOneUserMatchingPathsValues.setValues(Arrays.asList("oneUser"));

        matchingPathsValuesWithDefaultsTest.setMatchingPathsValues(Arrays.asList(
                oneUserMatchingPathsValues,
                manyUsersMatchingPathsValues,
                anotherPathSameOneUserMatchingPathsValues));
        return matchingPathsValuesWithDefaultsTest;
    }

    static class MatchingPathsValuesWithDefaultsTest extends AbstractMatchingPathsValuesWithDefaults {
    }

    // @Test
    // public void testContainsPath() throws IOException {
    //     List<String> files1 = Arrays.asList("file1.txt", "test/test.bats", "controllers/main.go");
    //     List<String> files2 = Arrays.asList("internal/main.go", "super.java");
    //     List<String> files3 = Arrays.asList("main.java", "super.java");
    //     List<String> files4 = Arrays.asList("README.md", "OWNERS", ".ci");
    //     List<String> files5 = Arrays.asList("README.md", "api/operator/file2.go");

    //     LinkedHashSet<String> files1reviewers = new LinkedHashSet<>();
    //     LinkedHashSet<String> files2reviewers = new LinkedHashSet<>();
    //     LinkedHashSet<String> files3reviewers = new LinkedHashSet<>();
    //     LinkedHashSet<String> files4reviewers = new LinkedHashSet<>();
    //     LinkedHashSet<String> files5reviewers = new LinkedHashSet<>();

    //     files1.stream().forEach(f1 -> {
    //         List<String> found = MatchingPathsValuesUtils.findPath(f1, config.getReviewers().getMatchingPathsValues());
    //         if (!found.isEmpty()) {
    //             files1reviewers.addAll(found);
    //         }
    //     });
    //     if (files1reviewers.isEmpty()) {
    //         files1reviewers.addAll(config.getReviewers().getDefaults());
    //     }

    //     files2.stream().forEach(f2 -> {
    //         List<String> found = MatchingPathsValuesUtils.findPath(f2, config.getReviewers().getMatchingPathsValues());
    //         if (!found.isEmpty()) {
    //             files2reviewers.addAll(found);
    //         }
    //     });
    //     if (files2reviewers.isEmpty()) {
    //         files2reviewers.addAll(config.getReviewers().getDefaults());
    //     }

    //     files3.stream().forEach(f3 -> {
    //         List<String> found = MatchingPathsValuesUtils.findPath(f3, config.getReviewers().getMatchingPathsValues());
    //         if (!found.isEmpty()) {
    //             files3reviewers.addAll(found);
    //         }
    //     });
    //     if (files3reviewers.isEmpty()) {
    //         files3reviewers.addAll(config.getReviewers().getDefaults());
    //     }

    //     files4.stream().forEach(f4 -> {
    //         List<String> found = MatchingPathsValuesUtils.findPath(f4, config.getReviewers().getMatchingPathsValues());
    //         if (!found.isEmpty()) {
    //             files4reviewers.addAll(found);
    //         }
    //     });
    //     if (files4reviewers.isEmpty()) {
    //         files4reviewers.addAll(config.getReviewers().getDefaults());
    //     }

    //     files5.stream().forEach(f5 -> {
    //         List<String> found = MatchingPathsValuesUtils.findPath(f5, config.getReviewers().getMatchingPathsValues());
    //         if (!found.isEmpty()) {
    //             files5reviewers.addAll(found);
    //         }
    //     });
    //     if (files5reviewers.isEmpty()) {
    //         files5reviewers.addAll(config.getReviewers().getDefaults());
    //     }

    //     Assertions.assertEquals(Arrays.asList("user5", "user6", "user1", "user2", "user3").toString(),
    //             files1reviewers.toString(), "1");
    //     Assertions.assertEquals(Arrays.asList("user1", "user2", "user3").toString(), files2reviewers.toString(), "2");
    //     Assertions.assertEquals(Arrays.asList("user1", "user2", "user3", "user10").toString(),
    //             files3reviewers.toString(), "3");
    //     Assertions.assertEquals(Arrays.asList("user4").toString(), files4reviewers.toString(), "4");
    //     Assertions.assertEquals(Arrays.asList("user1", "user2", "user3").toString(), files5reviewers.toString(), "5");
    // }
}
