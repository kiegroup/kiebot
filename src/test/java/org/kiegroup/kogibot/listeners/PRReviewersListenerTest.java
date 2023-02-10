package org.kiegroup.kogibot.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kiegroup.kogibot.GHTestUtils;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.quarkiverse.githubapp.testing.GitHubAppTest;
import io.quarkiverse.githubapp.testing.GitHubAppTesting;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@GitHubAppTest
public class PRReviewersListenerTest {

    static final Integer PULL_REQUEST_ID = 1062891801;
    static final Integer GH_INSTALLATION_ID = 29424899;
    static final String CONFIG_FILE_PATH = "/pr/.kogibot-config-reviewers.yml";

    @Test
    void testPROpenedWithConfigFileDefaultValues() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "anypath");
                    GHTestUtils.mockGHUsers(mocks.installationClient(GH_INSTALLATION_ID), "defaultUser1");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    @SuppressWarnings("unchecked")
                    ArgumentCaptor<List<GHUser>> reviewersListCaptor = ArgumentCaptor.forClass(List.class);
                    Mockito.verify(prMock).requestReviewers(reviewersListCaptor.capture());
                    List<String> ghUsers = new ArrayList<>();
                    for (GHUser user : reviewersListCaptor.getValue()) {
                        ghUsers.add(user.getLogin());
                    }
                    Assertions.assertThat(ghUsers).containsOnly("defaultUser1");
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPath() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "path1/anypath");
                    GHTestUtils.mockGHUsers(mocks.installationClient(GH_INSTALLATION_ID), "defaultUser1", "user1");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    @SuppressWarnings("unchecked")
                    ArgumentCaptor<List<GHUser>> reviewersListCaptor = ArgumentCaptor.forClass(List.class);
                    Mockito.verify(prMock).requestReviewers(reviewersListCaptor.capture());
                    List<String> ghUsers = new ArrayList<>();
                    for (GHUser user : reviewersListCaptor.getValue()) {
                        ghUsers.add(user.getLogin());
                    }
                    Assertions.assertThat(ghUsers).containsOnly("user1");
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPathAuthorIsReviewer() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "Jenkinsfile3anything");
                    GHTestUtils.mockGHUsers(mocks.installationClient(GH_INSTALLATION_ID), "defaultUser1", "user3");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    @SuppressWarnings("unchecked")
                    ArgumentCaptor<List<GHUser>> reviewersListCaptor = ArgumentCaptor.forClass(List.class);
                    Mockito.verify(prMock).requestReviewers(reviewersListCaptor.capture());
                    List<String> ghUsers = new ArrayList<>();
                    for (GHUser user : reviewersListCaptor.getValue()) {
                        ghUsers.add(user.getLogin());
                    }
                    Assertions.assertThat(ghUsers).containsOnly("user3");
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPathUserNotExistingInRepo() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "path2/anypath");
                    GHTestUtils.mockGHUsers(mocks.installationClient(GH_INSTALLATION_ID), "defaultUser1", "user21", "user22");

                    Mockito.doThrow(new IOException("Cannot request reviewers"))
                            .when(prMock)
                            .requestReviewers(Mockito.any());
                    GHIssueComment issueCommentMock = Mockito.mock(GHIssueComment.class);
                    Mockito.when(prMock.comment(Mockito.any())).thenReturn(issueCommentMock);
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);

                    Mockito.verify(prMock).comment("Error while requesting those reviewers:\n" +
                            "- `user21`\n" +
                            "- `user22`\n" +
                            "\nMessage:" +
                            "\n```JSON\nCannot request reviewers\n```");
                });
    }
}
