package org.kiegroup.kiebot.listeners;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.kiegroup.kiebot.GHTestUtils;
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
public class PRFirtimeContributorListenerTest {

    static final Integer PULL_REQUEST_ID = 1062891801;
    static final Integer GH_INSTALLATION_ID = 29424899;
    static final String CONFIG_FILE_PATH = "/pr/kiebot-config-first-time-contributor.yml";
    static final String FIRST_TIME_CONTRIBUTOR_COMMENT = "Any first time contributor comment";
    static final String PR_USER_LOGIN = "radtriste";

    @Test
    void testPROpenedFirstTimeContributor() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile("kiebot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockContributors(repoMock, "anyuser", "anotheruser");
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    Mockito.verify(prMock).comment("Hello @" + PR_USER_LOGIN + "\n" + FIRST_TIME_CONTRIBUTOR_COMMENT);
                });
    }

    @Test
    void testPROpenedNOTFirstTimeContributor() throws IOException {
        GitHubAppTesting
                .given()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    GHRepository repoMock = Mockito.mock(GHRepository.class);
                    Mockito.when(prMock.getRepository()).thenReturn(repoMock);

                    mocks.configFile("kiebot-config.yml").fromClasspath(CONFIG_FILE_PATH);
                    GHTestUtils.mockContributors(repoMock, "anyuser", "anotheruser", PR_USER_LOGIN);
                })
                .when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    GHPullRequest prMock = mocks.pullRequest(PULL_REQUEST_ID);
                    Mockito.verify(prMock, times(0)).comment("Hello @" + PR_USER_LOGIN + "\n" + FIRST_TIME_CONTRIBUTOR_COMMENT);
                });
    }
}
