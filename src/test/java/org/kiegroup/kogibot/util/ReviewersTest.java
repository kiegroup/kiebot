package org.kiegroup.kogibot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kiegroup.kogibot.config.pojo.ClientConfiguration;

public class ReviewersTest {

    @Test
    public void testContainsPath() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream rawYaml = this.getClass().getResourceAsStream("/kogibot-config.yml");
        ClientConfiguration config = mapper.readValue(rawYaml, ClientConfiguration.class);

        List<String> files1 = Arrays.asList("file1.txt", "test/test.bats", "controllers/main.go");
        List<String> files2 = Arrays.asList("internal/main.go", "super.java");
        List<String> files3 = Arrays.asList("main.java", "super.java");
        List<String> files4 = Arrays.asList("README.md", "OWNERS", ".ci");
        List<String> files5 = Arrays.asList("README.md", "api/operator/file2.go");

        LinkedHashSet<String> files1reviewers = new LinkedHashSet<>();
        LinkedHashSet<String> files2reviewers = new LinkedHashSet<>();
        LinkedHashSet<String> files3reviewers = new LinkedHashSet<>();
        LinkedHashSet<String> files4reviewers = new LinkedHashSet<>();
        LinkedHashSet<String> files5reviewers = new LinkedHashSet<>();

        files1.stream().forEach(f1 -> {
            List<String> found = Reviewers.findPath(f1, config.getReview());
            if (!found.isEmpty()) {
                files1reviewers.addAll(found);
            }
        });
        if (files1reviewers.isEmpty()) {
            files1reviewers.addAll(config.getDefaultReviewers());
        }

        files2.stream().forEach(f2 -> {
            List<String> found = Reviewers.findPath(f2, config.getReview());
            if (!found.isEmpty()) {
                files2reviewers.addAll(found);
            }
        });
        if (files2reviewers.isEmpty()) {
            files2reviewers.addAll(config.getDefaultReviewers());
        }

        files3.stream().forEach(f3 -> {
            List<String> found = Reviewers.findPath(f3, config.getReview());
            if (!found.isEmpty()) {
                files3reviewers.addAll(found);
            }
        });
        if (files3reviewers.isEmpty()) {
            files3reviewers.addAll(config.getDefaultReviewers());
        }

        files4.stream().forEach(f4 -> {
            List<String> found = Reviewers.findPath(f4, config.getReview());
            if (!found.isEmpty()) {
                files4reviewers.addAll(found);
            }
        });
        if (files4reviewers.isEmpty()) {
            files4reviewers.addAll(config.getDefaultReviewers());
        }

        files5.stream().forEach(f5 -> {
            List<String> found = Reviewers.findPath(f5, config.getReview());
            if (!found.isEmpty()) {
                files5reviewers.addAll(found);
            }
        });
        if (files5reviewers.isEmpty()) {
            files5reviewers.addAll(config.getDefaultReviewers());
        }

        Assertions.assertEquals(Arrays.asList("user5", "user6", "user1", "user2", "user3").toString(), files1reviewers.toString(), "1");
        Assertions.assertEquals(Arrays.asList("user1", "user2", "user3").toString(), files2reviewers.toString(), "2");
        Assertions.assertEquals(Arrays.asList("user1", "user2", "user3", "user10").toString(), files3reviewers.toString(), "3");
        Assertions.assertEquals(Arrays.asList("user4").toString(), files4reviewers.toString(), "4");
        Assertions.assertEquals(Arrays.asList("user1", "user2", "user3").toString(), files5reviewers.toString(), "5");
    }
}
