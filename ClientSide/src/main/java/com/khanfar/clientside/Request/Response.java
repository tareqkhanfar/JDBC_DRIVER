package com.khanfar.clientside.Request;

import java.util.LinkedHashMap;
import java.util.List;

public class Response {

    private List<LinkedHashMap<String , Object>> data ;
    private List<LinkedHashMap<String , Object>> metaData ;


    public Response(List<LinkedHashMap<String, Object>> data, List<LinkedHashMap<String, Object>> metaData) {
        this.data = data;
        this.metaData = metaData;
    }

    public List<LinkedHashMap<String, Object>> getData() {
        return data;
    }

    public void setData(List<LinkedHashMap<String, Object>> data) {
        this.data = data;
    }

    public List<LinkedHashMap<String, Object>> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<LinkedHashMap<String, Object>> metaData) {
        this.metaData = metaData;
    }
}
