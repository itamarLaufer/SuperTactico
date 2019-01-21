package com.laufer.itamar;


import com.laufer.itamar.org.json.simple.JSONObject;

public interface JsonParsable {
    JSONObject parseJson(String[]args);
}
