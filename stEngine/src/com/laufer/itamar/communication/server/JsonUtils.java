package com.laufer.itamar.communication.server;

import com.laufer.itamar.JsonParsable;
import com.laufer.itamar.org.json.simple.JSONArray;

import java.util.List;

public class JsonUtils {
    public static <T extends JsonParsable> JSONArray listToJsonArray(List<T>list, String[]args){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: list)
            res.add(obj.parseJson(args));
        return res;
    }
    public static <T extends JsonParsable> JSONArray arrayToJsonArray(T[] arr, String[]args){
        JSONArray res = new JSONArray();
        for(JsonParsable obj: arr)
            res.add(obj.parseJson(args));
        return res;
    }
}
