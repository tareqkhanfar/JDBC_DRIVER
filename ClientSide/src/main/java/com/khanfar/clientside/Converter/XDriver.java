package com.khanfar.clientside.Converter;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.Logger;

public class XDriver implements Driver {

    private XConnection connection ;


    static {
        try {

            DriverManager.registerDriver(new XDriver());


        } catch (SQLException e) {

            throw new RuntimeException("not register ");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        String username = info.getProperty("user");
        String password = info.getProperty("password") ;

        info.put("URL" , url);

        if (info.getProperty("Paging") ==null){
            info.put("Paging" , "DISABLE");
        }

        if (info.getProperty("URL")==null) {
            info.put("URL" , url);
        }

       LinkedHashMap<String , Object> DataBaseMetaData =  sendRequestToAuthunticate(url , username , password) ;
        this.connection =new XConnection(url , info , DataBaseMetaData );


        return this.connection ;
    }

    private LinkedHashMap<String , Object > sendRequestToAuthunticate(String url, String username, String password) {
        JsonToList jsonToList = new JsonToList() ;
        try {
            String  response = jsonToList.sendRequestAndGetResponse(url, username , password );
             LinkedHashMap<String , Object > DataBaseMetaData = jsonToList.convertJsonToHashMap(response) ;
            return DataBaseMetaData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
