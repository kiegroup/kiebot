package org.kiegroup.kogibot.listeners;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHEvent;
import org.mockito.Mockito;

import io.quarkiverse.githubapp.testing.GitHubAppTest;
import io.quarkiverse.githubapp.testing.GitHubAppTesting;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@GitHubAppTest
public class PRNoConfigFileListenerTest {

    static final Integer PULL_REQUEST_ID = 1062891801;
    static final Integer GH_INSTALLATION_ID = 29424899;

    public PRNoConfigFileListenerTest() {
    }

    @Test
    void testPROpenedNoConfigFile() throws IOException {
        GitHubAppTesting.when()
                .payloadFromClasspath("/pr/opened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    Mockito.verifyNoInteractions(mocks.pullRequest(PULL_REQUEST_ID));
                });
    }

    @Test
    void testPRReopenedNoConfigFile() throws IOException {
        GitHubAppTesting.when()
                .payloadFromClasspath("/pr/reopened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    Mockito.verifyNoInteractions(mocks.pullRequest(PULL_REQUEST_ID));
                });
    }

    @Test
    void testPRSynchronizedNoConfigFile() throws IOException {
        GitHubAppTesting.when()
                .payloadFromClasspath("/pr/reopened.json")
                .event(GHEvent.PULL_REQUEST)
                .then()
                .github(mocks -> {
                    Mockito.verifyNoInteractions(mocks.pullRequest(PULL_REQUEST_ID));
                });
    }
}
