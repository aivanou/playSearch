package util;

import model.SearchEngineType;
import model.SearchType;
import model.commands.Command;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The initial configuration of search system
 * Currently supports: engines, query types, content types
 */

public class SearchConfiguration {

    private Set<SearchType> searchTypes;
    private Set<SearchEngineType> searchEngines;
    private Map<SearchEngineType, List<Command>> commands;

    public static SearchConfiguration instance;

    public static SearchConfiguration getInstance() {
        return instance;
    }

    public static SearchConfiguration fromFile(String path) throws IOException, JSONException {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        String json = new String(encoded, Charset.defaultCharset());
        return SearchConfiguration.build(json);
    }

    public static SearchConfiguration build(String json) throws JSONException {
        instance = new SearchConfiguration(json);
        return instance;
    }

    public List<Command> getCommandsByEngine(SearchEngineType type) {
        return commands.get(type);
    }

    public SearchConfiguration(String json) throws JSONException {
        parse(json);
    }

    public SearchType getSearchTypeByName(String name) {
        for (SearchType type : searchTypes) {
            if (type.getName().equals(name))
                return type;
        }
        return null;
    }

    public SearchEngineType getEngineByName(String engineName) {
        for (SearchEngineType engine : searchEngines) {
            if (engine.getName().equals(engineName))
                return engine;
        }
        return null;
    }

    private SearchEngineType getEngineByName(Set<SearchEngineType> engines, String engineName) {
        for (SearchEngineType engine : engines) {
            if (engine.getName().equals(engineName))
                return engine;
        }
        return null;
    }

    public void parse(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        this.searchEngines = parseEngines(root.getJSONArray("searchEngines"));
        this.commands = parseCommands(root.getJSONArray("commands"), searchEngines);
        this.searchTypes = parseSearchTypes(root.getJSONArray("content"), searchEngines);
    }

    private Set<SearchType> parseSearchTypes(JSONArray array, Set<SearchEngineType> engines) throws JSONException {
        Set<SearchType> searchTypes = new HashSet<>();
        for (int i = 0; i < array.length(); ++i) {
            searchTypes.add(parseSearchType(array.getJSONObject(i), engines));
        }
        return searchTypes;
    }

    private SearchType parseSearchType(JSONObject obj, Set<SearchEngineType> engines) throws JSONException {
        String name = obj.getString("name");
        JSONArray fieldsArr = obj.getJSONArray("searchFields");
        List<String> searchFields = toString(fieldsArr);
        Map<SearchEngineType, List<String>> hosts = parseHosts(obj.getJSONArray("hosts"), engines);
        return new SearchType(name, searchFields, hosts);
    }

    private Map<SearchEngineType, List<String>> parseHosts(JSONArray array, Set<SearchEngineType> engines) throws JSONException {
        Map<SearchEngineType, List<String>> hosts = new HashMap<>();
        for (int i = 0; i < array.length(); ++i) {
            JSONObject obj = array.getJSONObject(i);
            String engine = obj.getString("engine");
            List<String> engineHosts = toString(obj.getJSONArray("hosts"));
            SearchEngineType engineType = getEngineByName(engines, engine);
            if (engineType == null)
                throw new JSONException("unsupported engine type during parsing the content array: " + engine);
            hosts.put(engineType, engineHosts);
        }
        return hosts;
    }

    private List<String> toString(JSONArray array) throws JSONException {
        List<String> stringFields = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); ++i) {
            stringFields.add(array.getString(i));
        }
        return stringFields;
    }

    private Map<SearchEngineType, List<Command>> parseCommands(JSONArray array, Set<SearchEngineType> engines) throws JSONException {
        Map<SearchEngineType, List<Command>> commands = new HashMap<>();
        for (int i = 0; i < array.length(); ++i) {
            JSONObject obj = array.getJSONObject(i);
            SearchEngineType engine = getEngineByName(engines, obj.getString("engine").trim().toLowerCase());
            if (engine == null)
                throw new JSONException("unsupported engine: " + obj.getString("engine").trim().toLowerCase());
            commands.put(engine, parseEngineCommands(obj.getJSONArray("values")));
        }
        return commands;
    }


    private List<Command> parseEngineCommands(JSONArray array) throws JSONException {
        List<Command> commands = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            commands.add(parseCommand(array.getJSONObject(i)));
        }
        return commands;
    }

    private Command parseCommand(JSONObject obj) throws JSONException {
        String name = obj.getString("command").trim().toLowerCase();
        JSONObject query = obj.getJSONObject("value");
        return Command.parseCommand(name, query);
    }

    private Set<SearchEngineType> parseEngines(JSONArray array) throws JSONException {
        Set<SearchEngineType> engines = new HashSet<>();
        for (int i = 0; i < array.length(); ++i) {
            engines.add(new SearchEngineType(array.getString(i).trim().toLowerCase()));
        }
        return engines;
    }

    public static void main(String[] args) throws IOException, JSONException {
        String file = "/home/aliaksandr/study/code/search_ok/guppy-play/conf/query.json";
        byte[] encoded = Files.readAllBytes(Paths.get(file));
        String json = new String(encoded, Charset.defaultCharset());
        SearchConfiguration.build(json);
        Map<SearchEngineType, List<Command>> c = SearchConfiguration.getInstance().commands;
        List<Command> elastic = SearchConfiguration.getInstance().getCommandsByEngine(new SearchEngineType("elastic"));
        Command exc = null;
        for (Command ec : elastic)
            if (ec.getName().equals("search"))
                exc = ec;
        List<String> sf = SearchConfiguration.getInstance().getSearchTypeByName("document").getSearchFields();
        Map<String, String> map = new HashMap<>();
        map.put("from", 10 + "");
        map.put("size", 10 + "");
        map.put("query", "RASAR");
        System.out.println(exc.fill(map, sf));
//        for (SearchEngineType key : c.keySet()) {
//            System.out.println(key);
//            for (Command cm : c.get(key)) {
//                System.out.println(cm.getName());
//            }
//        }
//        for (SearchType t : SearchConfiguration.getInstance().searchTypes) {
//            System.out.println(t);
//        }


    }
}
