package org.kiegroup.kogibot.listeners;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.kiegroup.kogibot.GHTestUtils;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.mockito.Mockito;

import io.quarkiverse.githubapp.testing.GitHubAppTest;
import io.quarkiverse.githubapp.testing.GitHubAppTesting;
import io.quarkus.test.junit.QuarkusTest;

import static org.mockito.Mockito.times;

@QuarkusTest
@GitHubAppTest
public class PRLabelsListenerTest {

    static final Integer PULL_REQUEST_ID = 1062891801;
    static final Integer GH_INSTALLATION_ID = 29424899;
    static final String CONFIG_FILE_PATH = "/pr/.kogibot-config-labels.yml";

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
                    GHTestUtils.mockGHLabels(repoMock, "defaultLabel");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = mocks.repository("kogitobot-playground");
                    Mockito.verify(prMock).addLabels("defaultLabel");
                    Mockito.verify(repoMock, times(0)).createLabel("defaultLabel", null, null);
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPathLabelNOTExisting() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "path1/anypath");
                    GHTestUtils.mockGHLabels(repoMock, "defaultLabel");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = prMock.getRepository();
                    Mockito.verify(repoMock).createLabel("path1", null, null);
                    Mockito.verify(prMock).addLabels("path1");
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPathLabelExisting() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "path1/anypath");
                    GHTestUtils.mockGHLabels(repoMock, "defaultLabel", "path1");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = mocks.repository("kogitobot-playground");
                    Mockito.verify(repoMock, times(0)).createLabel("path1", null, null);
                    Mockito.verify(prMock).addLabels("path1");
                });
    }

    @Test
    void testPROpenedWithConfigFileWithMatchingPathLabelIsDefaultOneButNotExisting() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile(".kogibot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockGHPullRequestFileDetail(prMock, "existing-default-label");
                    GHTestUtils.mockGHLabels(repoMock, "defaultLabel");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = prMock.getRepository();
                    Mockito.verify(repoMock).createLabel("needs review :mag:", "c12645", "Pull Request that needs reviewers");
                    Mockito.verify(prMock).addLabels("needs review :mag:");
                });
    }
}
