package util;

import model.SearchType;

import java.util.Map;

public class QueryConfiguration {

    private Map<String, SearchType> searchTypes;
    private Map<String, String> commands;

    public static QueryConfiguration build(String json) {

        return new QueryConfiguration();
    }
}
