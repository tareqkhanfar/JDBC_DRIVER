package com.khanfar.clientside.Converter;

import java.util.LinkedHashMap;

public class HashMapObject {

    private String[] keys ;
    private LinkedHashMap<String , Object> map;

    public HashMapObject(String[] keys, LinkedHashMap<String, Object> map) {
        this.keys = keys;
        this.map = map;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public LinkedHashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, Object> map) {
        this.map = map;
    }
}
