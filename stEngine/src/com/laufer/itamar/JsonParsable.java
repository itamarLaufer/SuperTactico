package com.laufer.itamar;


import org.json.simple.JSONObject;

public interface JsonParsable {
    JSONObject parseJson(String[]args);
}
