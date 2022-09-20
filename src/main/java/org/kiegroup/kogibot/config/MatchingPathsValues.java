package org.kiegroup.kogibot.config;

import java.util.ArrayList;
import java.util.List;

public class MatchingPathsValues {

  private List<String> paths = new ArrayList<>();

  private List<String> values = new ArrayList<>();

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

}
