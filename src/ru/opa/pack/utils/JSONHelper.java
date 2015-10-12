package ru.opa.pack.utils;

import org.apache.jena.query.QuerySolution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public final class JSONHelper {
    public static JSONArray generateJSONResponse(Map<String[], List<QuerySolution>> map) {
        Map.Entry<String[], List<QuerySolution>> item = map.entrySet().iterator().next();
        String[] vars = item.getKey();
        List<QuerySolution> values = item.getValue();

        JSONArray result = values.stream().map(qs -> generateJSONObject(vars, qs)).collect(Collectors.toCollection(() -> new JSONArray()));

        return result;
    }

    public static JSONObject generateJSONObject(String[] vars, QuerySolution qs) {
        JSONObject result = new JSONObject();
        for (int i = 0; i < vars.length; i++) {
            result.put(vars[i], qs.get(vars[i]).toString());
        }

        return result;
    }
}
