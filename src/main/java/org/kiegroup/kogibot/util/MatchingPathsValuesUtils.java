package org.kiegroup.kogibot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.kiegroup.kogibot.config.MatchingPathsValues;

public class MatchingPathsValuesUtils {
  
  public static List<String> findPath(String value, List<MatchingPathsValues> list) {
    
    for (int i = 0; i < list.size(); i++) {
        for (String path : list.get(i).getPaths()) {
            Pattern p = Pattern.compile(path);
            if (p.matcher(value).find()) {
                return list.get(i).getValues();
            }
        }
    }
    return new ArrayList<>();
}
}
