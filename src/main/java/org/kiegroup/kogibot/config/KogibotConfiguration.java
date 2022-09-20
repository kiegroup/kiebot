package org.kiegroup.kogibot.config;

import java.util.HashMap;
import java.util.Map;

public class KogibotConfiguration {

    private Reviewers reviewers;

    private Labels labels;

    private Map<String, String> comments = new HashMap<>();

    public Reviewers getReviewers() {
        return reviewers;
    }

    public void setReviewers(Reviewers reviewers) {
        this.reviewers = reviewers;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

}