package org.kiegroup.kogibot.config;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kiegroup.kogibot.config.pojo.ClientConfiguration;

public class ClientConfigParserTest {

    @Test
    public void testConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream rawYaml = this.getClass().getResourceAsStream("/kogibot-config.yml");
        ClientConfiguration config = mapper.readValue(rawYaml, ClientConfiguration.class);
        Assertions.assertNotNull(config.getReview());
    }
}
