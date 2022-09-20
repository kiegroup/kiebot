package org.kiegroup.kogibot.config;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientConfigParserTest {

    @Test
    public void testConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream rawYaml = this.getClass().getResourceAsStream("/.kogibot-config.yml");
        KogibotConfiguration config = mapper.readValue(rawYaml, KogibotConfiguration.class);
        Assertions.assertNotNull(config.getReviewers());
        Assertions.assertNotNull(config.getLabels());
        Assertions.assertTrue(config.getComments().size() > 0);
    }
}
