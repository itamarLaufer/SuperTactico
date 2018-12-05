package com.laufer.itamar.communication.server;

import com.laufer.itamar.JsonParsable;
import org.json.simple.JSONArray;

import java.util.List;

public class JsonUtils {
    public static <T extends JsonParsable> JSONArray listToJsonArray(List<T>list){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: list)
            res.add(obj.parseJson());
        return res;
    }
    public static <T extends JsonParsable> JSONArray arrayToJsonArray(T[] arr){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: arr)
            res.add(obj.parseJson());
        return res;
    }
}
