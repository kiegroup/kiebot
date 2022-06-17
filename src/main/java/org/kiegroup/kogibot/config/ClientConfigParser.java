package org.kiegroup.kogibot.config;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.pojo.ClientConfiguration;
import org.kohsuke.github.GHRepository;

@ApplicationScoped
public class ClientConfigParser {

    private final String CONFIGURATION_FILE = ".kogibot-config.yml";

    @Inject
    Logger log;

    @Inject
    ClientConfiguration client;

    public ClientConfiguration parseConfigurationFile(GHRepository ghRepository) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            client = mapper.readValue(ghRepository.getFileContent(CONFIGURATION_FILE).read(),
                                      ClientConfiguration.class);
        } catch (IOException e) {
            log.tracev("Configuration file {0} not found, skipping.", CONFIGURATION_FILE);
        }
        return client;
    }
}
