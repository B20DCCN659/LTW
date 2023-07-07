package com.example.btl_web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;


public class HttpUtils{
    private String value;
    public HttpUtils(String value)
    {
        this.value = value;
    }
    public <T> T toModel(Class<T> tClass)
    {
        try {
            return new ObjectMapper().readValue(value, tClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static HttpUtils of(BufferedReader reader)
    {
        StringBuffer json = new StringBuffer();
        String line;

        try {
            while ((line = reader.readLine()) != null)
            {
                json.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HttpUtils(json.toString());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

