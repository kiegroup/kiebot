package org.kiegroup.kiebot;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepository.Contributor;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.mockito.Mockito;

public class GHTestUtils {
    private GHTestUtils() {
    }

    @SuppressWarnings("unchecked")
    public static void mockGHLabels(GHRepository ghRepository, String... labelNames) throws IOException {
        PagedIterable<GHLabel> labelsIterableMock = (PagedIterable<GHLabel>) Mockito.mock(PagedIterable.class);
        Mockito.when(ghRepository.listLabels()).thenReturn(labelsIterableMock);
        List<GHLabel> labelsMock = Arrays.asList(labelNames).stream()
                .map(GHTestUtils::mockGHLabel)
                .collect(Collectors.toList());
        Mockito.when(labelsIterableMock.toList()).thenReturn(labelsMock);
    }

    public static GHLabel mockGHLabel(String labelName) {
        GHLabel labelMock = Mockito.mock(GHLabel.class);
        Mockito.when(labelMock.getName()).thenReturn(labelName);
        return labelMock;
    }

    public static void mockGHUsers(GitHub githubMock, String... logins) throws IOException {
        for (String login : logins) {
            GHUser user = mockGHUser(login);
            Mockito.when(githubMock.getUser(login)).thenReturn(user);
        }
    }

    public static GHUser mockGHUser(String login) {
        GHUser userMock = Mockito.mock(GHUser.class);
        Mockito.when(userMock.getLogin()).thenReturn(login);
        return userMock;
    }

    @SuppressWarnings("unchecked")
    public static void mockGHPullRequestFileDetail(GHPullRequest ghPullRequestMock, String... filenames) throws IOException {
        PagedIterable<GHPullRequestFileDetail> fileDetailsIterableMock = (PagedIterable<GHPullRequestFileDetail>) Mockito
                .mock(PagedIterable.class);
        Mockito.when(ghPullRequestMock.listFiles()).thenReturn(fileDetailsIterableMock);
        Set<GHPullRequestFileDetail> fileDetailsMock = Arrays.asList(filenames).stream()
                .map(GHTestUtils::mockGHPullRequestFileDetail)
                .collect(Collectors.toSet());
        Mockito.when(fileDetailsIterableMock.toSet()).thenReturn(fileDetailsMock);
    }

    public static GHPullRequestFileDetail mockGHPullRequestFileDetail(String filename) {
        GHPullRequestFileDetail fileDetailsMock = Mockito.mock(GHPullRequestFileDetail.class);
        Mockito.when(fileDetailsMock.getFilename()).thenReturn(filename);
        return fileDetailsMock;
    }

    @SuppressWarnings("unchecked")
    public static void mockContributors(GHRepository ghRepository, String... contributors) throws IOException {
        PagedIterable<Contributor> contributorsIterableMock = (PagedIterable<Contributor>) Mockito
                .mock(PagedIterable.class);
        Mockito.when(ghRepository.listContributors()).thenReturn(contributorsIterableMock);
        Set<Contributor> contributorsMock = Arrays.asList(contributors).stream()
                .map(GHTestUtils::mockContributor)
                .collect(Collectors.toSet());
        Mockito.when(contributorsIterableMock.toSet()).thenReturn(contributorsMock);
    }

    public static Contributor mockContributor(String contributorLogin) {
        Contributor contributorMock = Mockito.mock(Contributor.class);
        Mockito.when(contributorMock.getLogin()).thenReturn(contributorLogin);
        return contributorMock;
    }
}
