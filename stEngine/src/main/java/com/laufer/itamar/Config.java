package com.laufer.itamar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Config {
    public static boolean DEBUG;

    public static void config(){
        
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("configuration.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            DEBUG = (boolean) jsonObject.get("debug");

        } catch (IOException | ParseException e) {
            System.out.println("configuration failed!");
            e.printStackTrace();

        }
    }
}
