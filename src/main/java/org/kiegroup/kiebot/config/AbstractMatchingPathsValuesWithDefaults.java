package org.kiegroup.kiebot.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "matching_paths"
})
public abstract class AbstractMatchingPathsValuesWithDefaults {

    private List<String> defaults = new ArrayList<>();

    @JsonProperty("matching_paths")
    private List<MatchingPathsValues> matchingPathsValues = new ArrayList<>();

    public List<String> getDefaults() {
        return defaults;
    }

    public void setDefaults(List<String> defaults) {
        this.defaults = defaults;
    }

    public List<MatchingPathsValues> getMatchingPathsValues() {
        return matchingPathsValues;
    }

    public void setMatchingPathsValues(List<MatchingPathsValues> matchingPathsValues) {
        this.matchingPathsValues = matchingPathsValues;
    }

}
