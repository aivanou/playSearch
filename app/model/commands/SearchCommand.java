package model.commands;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The search command, used by
 * <p/>
 * Currently used by @see services.search.provider.impl.ElasticSearchProvider
 */
public class SearchCommand extends Command {
    public SearchCommand(String name, JSONObject command) {
        super(name, command);
    }

    @Override
    public String fill(Map<String, String> parameter, List<String> searchFields) {
        JSONArray fields = toArray(searchFields);
        try {
            JSONObject obj = traverse(command, parameter, fields);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject traverse(JSONObject obj, Map<String, String> parameters, JSONArray fields) throws JSONException {
        Iterator<String> keys = obj.keys();
        JSONObject newObj = new JSONObject();
        while (keys.hasNext()) {
            String key = keys.next();
            if (parameters.containsKey(key)) {
                if (obj.get(key) instanceof String && obj.getString(key).equals("%s")) {
                    newObj.put(key, parameters.get(key));
                    continue;
                }
            }
            if (key.equals("fields")) {
                newObj.put(key, fields);
                continue;
            }
            if (obj.get(key) instanceof JSONObject) {
                newObj.put(key, traverse((JSONObject) obj.get(key), parameters, fields));
            } else if (obj.get(key) instanceof JSONArray) {
                newObj.put(key, traverse((JSONArray) obj.get(key), parameters, fields));
            } else {
                newObj.put(key, obj.get(key));
            }
        }
        return newObj;
    }

    private JSONArray traverse(JSONArray array, Map<String, String> parameters, JSONArray fields) throws JSONException {
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < array.length(); ++i) {
            if (array.get(i) instanceof JSONObject) {
                newArray.put(traverse(array.getJSONObject(i), parameters, fields));
            } else if (array.get(i) instanceof JSONArray) {
                newArray.put(traverse(array.getJSONArray(i), parameters, fields));
            } else newArray.put(array.get(i));
        }
        return newArray;
    }

    private JSONArray toArray(List<String> fields) {
        JSONArray arr = new JSONArray();
        for (String field : fields)
            arr.put(field);
        return arr;
    }
}
