package com.khanfar.clientside.Converter;

import com.khanfar.clientside.META_NAME;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class MainTest {

    public static void main(String[] args) throws Exception {


        long startTime = System.nanoTime();
        System.out.println("START");
       String connectionURL = "http://91.107.221.252:8080/jdbc";
       // String connectionURL = "http://localhost:8080/jdbc";

        Class.forName("com.khanfar.clientside.Converter.XDriver");
        System.out.println("Started:" + new java.util.Date() + "  ->" + connectionURL);
    //    Connection con = DriverManager.getConnection(connectionURL, "root", "1234" );

        Properties properties = new Properties() ;
        properties.setProperty("user" , "root");
        properties.setProperty("password" , "1234");
        Connection con = DriverManager.getConnection(connectionURL , properties);

        System.out.println("_________________________________________________________");

        System.out.println("DB_META_DATA");
        System.out.println("_________________________________________________________");

        DatabaseMetaData metaDataD = con.getMetaData() ;
        System.out.println(metaDataD.getURL());
        System.out.println(metaDataD.getDriverName());
        System.out.println(metaDataD.getUserName());
        System.out.println(metaDataD.getDatabaseProductName());
        System.out.println(metaDataD.getDriverName());
        System.out.println(metaDataD.getDriverVersion());

        System.out.println("_________________________________________________________");



           Statement statement = con.createStatement();

           System.out.println("start");
          ResultSet rs = statement.executeQuery("SELECT *  FROM test");
           //ResultSet rs = statement.executeQuery("select * from patient ");

           ResultSetMetaData metaData = rs.getMetaData();
           int columnCount = metaData.getColumnCount();
           System.out.println(META_NAME.COLUMN_NAME + ": " + metaData.getColumnName(1));


           for (int i = 1; i <= columnCount; i++) {
               System.out.println("Column " + i + " details:");
               System.out.println("________________________________________________________");
               System.out.println(META_NAME.COLUMN_NAME + ": " + metaData.getColumnName(i));
               System.out.println(META_NAME.COLUMN_TYPE + ": " + metaData.getColumnType(i));
               System.out.println(META_NAME.COLUMN_TYPE_NAME + ": " + metaData.getColumnTypeName(i));
               System.out.println(META_NAME.COLUMN_CLASS_NAME + ": " + metaData.getColumnClassName(i));
               System.out.println(META_NAME.COLUMN_LABEL + ": " + metaData.getColumnLabel(i));
               System.out.println(META_NAME.COLUMN_DISPLAY_SIZE + ": " + metaData.getColumnDisplaySize(i));
               System.out.println(META_NAME.TABLE_NAME + ": " + metaData.getTableName(i));
               System.out.println(META_NAME.PRECISION + ": " + metaData.getPrecision(i));
               System.out.println(META_NAME.SCALE + ": " + metaData.getScale(i));
               System.out.println(META_NAME.IS_NULLABLE + ": " + metaData.isNullable(i));
               System.out.println(META_NAME.IS_AUTO_INCREMENT + ": " + metaData.isAutoIncrement(i));
               System.out.println(META_NAME.IS_CASE_SENSITIVE + ": " + metaData.isCaseSensitive(i));
               System.out.println(META_NAME.IS_CURRENCY + ": " + metaData.isCurrency(i));
               System.out.println(META_NAME.IS_DEFINITELY_WRITABLE + ": " + metaData.isDefinitelyWritable(i));
               System.out.println(META_NAME.IS_READ_ONLY + ": " + metaData.isReadOnly(i));
               System.out.println(META_NAME.IS_SEARCHABLE + ": " + metaData.isSearchable(i));
               System.out.println(META_NAME.IS_SIGNED + ": " + metaData.isSigned(i));
               System.out.println(META_NAME.IS_WRITABLE + ": " + metaData.isWritable(i));
           }

           System.out.println("end");

     while (rs.next()) {
         System.out.println(rs.getTimestamp(3));
     }


           System.out.println("end");


           System.out.println("___________________________________________________________________");




        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;

        System.out.println("TIME EXECUTION :  " + duration);
        System.exit(0);
    }
}