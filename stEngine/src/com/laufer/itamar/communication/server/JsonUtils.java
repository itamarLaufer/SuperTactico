package com.laufer.itamar.communication.server;

import com.laufer.itamar.JsonParsable;
import org.json.simple.JSONArray;

import java.util.List;

public class JsonUtils {
    public static JSONArray listToJsonArray(List<JsonParsable>list){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: list)
            res.add(obj.parseJson());
        return res;
    }
    public static JSONArray arrayToJsonArray(JsonParsable[] arr){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: arr)
            res.add(obj.parseJson());
        return res;
    }
}
