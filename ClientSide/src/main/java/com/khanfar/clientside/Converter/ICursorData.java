package com.khanfar.clientside.Converter;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

public interface ICursorData  {

    int findColumn(String str) throws SQLException ;
    int getInt(int index) throws SQLException;
    int getInt(String str) throws SQLException;
    String getString(int index) throws SQLException;
    String getString (String str) throws SQLException;
    XResultSetMetaData getMetaData() ;
    boolean next() ;
    java.sql.Date getDate(int index) throws SQLException;
    java.sql.Date getDate(String str) throws SQLException;


    double getDouble(int index) throws SQLException;
    double getDouble(String str) throws SQLException;

    float getFloat(int index) throws SQLException;
    float getFloat(String str) throws SQLException;

    byte getByte(int index) throws SQLException;
    byte getByte(String str) throws SQLException;

    long getLong(int index) throws SQLException;
    long getLong(String str) throws SQLException;

    short getShort(int index) throws SQLException;
    short getShort(String str) throws SQLException;

    Boolean getBoolean(int index) throws SQLException;
    Boolean getBoolean(String str) throws SQLException;

    Timestamp getTimeStamp(int index) throws SQLException;
    Timestamp getTimeStamp(String str) throws SQLException;


    Object getObject(int index) throws  SQLException ;
    Object getObject(String str) throws  SQLException ;



}
