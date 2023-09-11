package com.khanfar.clientside.Converter;


import com.khanfar.clientside.Exception.NoMorePagesToFetchException;
import com.khanfar.clientside.Request.Response;
import com.khanfar.clientside.Transaction.Transaction;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ResultSetAdapter implements ICursorData {
    private List<LinkedHashMap<String, Object>> hashMapList;
    private List<LinkedHashMap<String , Object>> metaData ;
    private Properties properties;
    private JsonToList jsonToList = new JsonToList();
    private int index = 0;
    private Transaction transaction ;



    public ResultSetAdapter(Transaction transaction , Response response, Properties properties) {
        this.properties = properties;
        this.hashMapList = response.getData();
        this.metaData = response.getMetaData();
        this.transaction = transaction ;
    }


    public boolean next() {

        if (this.hashMapList == null || this.hashMapList.isEmpty()) {
            return false;
        } else {
            if (this.index == this.hashMapList.size()) {
                if (!this.properties.getProperty("Paging").trim().equalsIgnoreCase("ENABLE")) {
                    this.index = 0;
                    return false;
                }

                try {
                    SharedPage.setPageNumber(SharedPage.getPageNumber() + 1);
                    String token ;
                    if (transaction.getToken() ==null) {
                        token = "N/A";
                    }
                    else {
                        token = transaction.getToken();
                    }
                    String response = this.jsonToList.retriveDataWithPaging(this.properties.getProperty("URL"), token , STATUS.SELECT_PAGING, this.properties.getProperty("query"));
                    this.hashMapList = this.jsonToList.convertJsonToList(response).getData();
                    this.index = 0;
                } catch (IOException var2) {
                    throw new RuntimeException(var2);
                } catch (NoMorePagesToFetchException var3) {
                    SharedPage.setPageNumber(1);
                    SharedPage.setPageSize(100);
                    return false;
                }
            }

            ++this.index;
            return true;
        }
    }

    public java.sql.Date getDate(int index) throws SQLException {
        index = index -1 ;
        HashMapObject hashMapObject = this.getMap();
        if (index >= hashMapObject.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        }
        else {
            String KEY = hashMapObject.getKeys()[index];
            String value = (String)hashMapObject.getMap().get(KEY);

            if (value == null) {
                return null;
            } else {
               // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
               // System.out.println(value);
           //     DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
               // LocalDate localDate = LocalDate.parse(value, formatter);
                Date date = extractSqlDate(getTimeStamp(index+1));

                return date;
            }
        }
    }

    public Date getDate(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            String value = (String)hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value == null) {
                return null;
            } else {
               // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = extractSqlDate(getTimeStamp(str));

                return date;
            }
        }
    }



    private synchronized HashMapObject getMap() {
        LinkedHashMap<String, Object> hashmap = (LinkedHashMap)this.hashMapList.get(this.index - 1);
        String[] KEYS = new String[hashmap.keySet().size()];
        Object[] objects = hashmap.keySet().toArray();

        for(int i = 0; i < KEYS.length; ++i) {
            KEYS[i] = (String)objects[i];
        }

        HashMapObject object = new HashMapObject(KEYS, hashmap);
        return object;
    }


    public XResultSetMetaData getMetaData() {
        /*
        HashMap<MetaName, Object> metaData = new HashMap();
        LinkedHashMap<String, Object> entry = (LinkedHashMap)this.hashMapList.get(0);
        metaData.put(MetaName.CLOULMNS_NAME, entry.keySet());
        metaData.put(MetaName.NUMBER_OF_COULMNS, entry.keySet().size());
        LinkedList<String> coulmn_type = new LinkedList();
        Collection<Object> stringSet = entry.values();
        Iterator var5 = stringSet.iterator();

        while(var5.hasNext()) {
            Object s = var5.next();
            if (s == null) {
                coulmn_type.add(null);
            } else {
                coulmn_type.add(s.getClass().getName());
            }
        }

        metaData.put(MetaName.COLUMN_TYPE, coulmn_type);

         */
        return new XResultSetMetaData(this.metaData);
    }


    @Override
    public int findColumn(String str) throws SQLException {
        HashMapObject map = this.getMap();
        String[]KEYS = map.getKeys() ;

        for (int i = 0 ; i< KEYS.length ; i++) {
            if (KEYS[i].equalsIgnoreCase(str.trim())){
                return i+1 ;
            }
        }
        throw new SQLException("Error : the  Column not found : " + str) ;

    }

    public int getInt(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Object value = map.getMap().get(KEY);
            if (value instanceof Integer) {
                return (Integer)value;
            }
            else if (value instanceof String)  {
                return Integer.parseInt(value.toString().trim()) ;
            }
            return (Integer) value;
        }
    }

    public int getInt(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value instanceof Integer) {
                return (Integer)value;
            }
            else if (value instanceof String)  {
                return Integer.parseInt(value.toString().trim()) ;
            }

            return (Integer) value;
        }
    }

    public String getString(int index) throws SQLException {
        index = index -1 ;

        HashMapObject hashMapObject = this.getMap();
        if (index >= hashMapObject.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = hashMapObject.getKeys()[index];
            Object value = hashMapObject.getMap().get(KEY);
            if (value instanceof  Integer) {
                Integer x = (Integer) value ;
                return x.toString();
            }
            else if (value instanceof Long) {
                Long x = (Long) value ;
                return x.toString();
            }
            else if (value instanceof Double){
                Double x = (Double) value ;
                return x.toString();
            }
            else if (value instanceof Float){
                Float x = (Float) value ;
                return x.toString();
            }
            else if (value instanceof String){
                return (String) value;
            }
            if (value == null) {
                return  null;
            }
            return (String) value;
        }
    }

    public String getString(String str) throws SQLException {
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            HashMapObject hashMapObject = this.getMap();
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value instanceof  Integer) {
                Integer x = (Integer) value ;
                return x.toString();
            }
            else if (value instanceof Long) {
                Long x = (Long) value ;
                return x.toString();
            }
            else if (value instanceof Double){
                Double x = (Double) value ;
                return x.toString();
            }
            else if (value instanceof Float){
                Float x = (Float) value ;
                return x.toString();
            }
            else if (value instanceof String){
                return (String) value;
            }
            if (value == null) {
                return  null;
            }

            return value.toString();
        }
    }

    private boolean isValidCoulmnName(String str) {
        String[] keys = this.getMap().getKeys();

        for(int i = 0; i < keys.length; ++i) {
            if (str.trim().equalsIgnoreCase(keys[i])) {
                return true;
            }
        }

        return false;
    }

    public double getDouble(int index) throws SQLException {
        index = index -1 ;

        HashMapObject hashMapObject = this.getMap();
        if (index >= hashMapObject.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = hashMapObject.getKeys()[index];
            Object value = hashMapObject.getMap().get(KEY);
            if (value instanceof Integer) {
                return ((Integer)value).doubleValue();
            } else if (value instanceof Double) {
                return (Double)value;
            } else if (value instanceof Long) {
                return ((Long)value).doubleValue();
            }
            else if (value instanceof String ) {
                return Double.parseDouble(value.toString().trim());
            }
            else {
                System.out.println(value.toString());
                throw new SQLException("Error: Expected a Double or Integer value at index: " + index);
            }
        }
    }

    public double getDouble(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value instanceof Integer) {
                return ((Integer)value).doubleValue();
            } else if (value instanceof Double) {
                return (Double)value;
            } else if (value instanceof Long) {
                return ((Long)value).doubleValue();
            }
            else if (value instanceof String ) {
                return Double.parseDouble(value.toString().trim());
            }
            else {
                System.out.println(value.toString());
                throw new SQLException("Error: Expected a Double or Integer value at index: " + this.index);
            }
        }
    }

    public float getFloat(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Object value = map.getMap().get(KEY);
            if (value instanceof Float) {
                return (Float)value;
            } else if (value instanceof Short) {
                return ((Short)value).floatValue();
            } else if (value instanceof Integer) {
                return ((Integer)value).floatValue();
            } else if (value instanceof Double) {
                return ((Double)value).floatValue();
            }
            else if (value instanceof String ) {
                return Float.parseFloat(value.toString().trim());
            }
            else {
                System.out.println(value.toString());
                throw new SQLException("Error: Expected a Float or Integer value at index: " + index);
            }
        }
    }

    public float getFloat(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value instanceof Float) {
                return (Float)value;
            } else if (value instanceof Short) {
                return ((Short)value).floatValue();
            } else if (value instanceof Integer) {
                return ((Integer)value).floatValue();
            } else if (value instanceof Double) {
                return ((Double)value).floatValue();
            }
            else if (value instanceof String ) {
                return Float.parseFloat(value.toString().trim());
            }

            else {
                System.out.println(value.toString());
                throw new SQLException("Error: Expected a Float or Integer value at index: " + this.index);
            }
        }
    }

    public byte getByte(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Byte value = (Byte)map.getMap().get(KEY);
            return value;
        }
    }

    public byte getByte(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Byte value = (Byte)hashMapObject.getMap().get(str.trim().toUpperCase());
            return value;
        }
    }

    public long getLong(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Object value = map.getMap().get(KEY);

            if (value instanceof Integer) {
                return ((Integer)value).longValue();
            }
            else if (value instanceof  Long) {
                return ((Long) value).longValue();
            }
            else if (value instanceof String) {
                return Long.parseLong(value.toString().trim());
            }
            return (Long)value;
        }
    }

    public long getLong(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value instanceof Integer) {
                return ((Integer)value).longValue();
            }
            else if (value instanceof  Long) {
                return ((Long) value).longValue();
            }
            else if (value instanceof String) {
                return Long.parseLong(value.toString().trim());
            }
            return (Long)value;
        }
    }
    private   Date extractSqlDate(Timestamp date) {
        if (date == null)
            return null;

        return new Date(date.getTime());
    }

    public short getShort(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Short value = (Short)map.getMap().get(KEY);
            return value;
        }
    }

    public short getShort(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Short value = (Short)hashMapObject.getMap().get(str.trim().toUpperCase());
            return value;
        }
    }

    public Boolean getBoolean(int index) throws SQLException {
        index = index -1 ;

        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            //Boolean value = (Boolean)map.getMap().get(KEY);
            Object obj= map.getMap().get(KEY);

            //Object obj=map.get(KEY);

            if (!isNumeric(obj)) return ((Boolean) obj);

            else {

                int x=Integer.parseInt((String) obj);
                if (x==0) return false;
                else

                    return true;

            }


        }
    }

    public Boolean getBoolean(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {

            Object obj=hashMapObject.getMap().get(str.trim().toUpperCase());
            if (obj==null) return false;

            if (isNumeric(obj)) {
                int x=Integer.parseInt(obj.toString());
                if (x==0) return false;
                else

                    return true;


            }

            return ((Boolean) obj);

        }
    }
    @Override
    public Timestamp getTimeStamp(int index) throws SQLException {
        index = index - 1;

        HashMapObject hashMapObject = this.getMap();
        if (index >= hashMapObject.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = hashMapObject.getKeys()[index];
            String value = (String) hashMapObject.getMap().get(KEY);
            if (value == null) {
                return null;
            } else {
                value = value.replace("[UTC]", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
                Instant instant = Instant.from(formatter.parse(value));
                Timestamp timestamp = Timestamp.from(instant);

                return timestamp;
            }
        }
    }

    @Override
    public Timestamp getTimeStamp(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            String value = (String) hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value == null) {
                return null;
            } else {
                // Custom formatter to handle [UTC] in the input
                value = value.replace("[UTC]", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
                Instant instant = Instant.from(formatter.parse(value));
                Timestamp timestamp = Timestamp.from(instant);
                return timestamp;
            }
        }
    }

    @Override
    public Object getObject(int index) throws SQLException {

        index = index -1 ;
        HashMapObject map = this.getMap();
        if (index >= map.getKeys().length) {
            throw new SQLException("Error Column not found at the provided index : " + index);
        } else {
            String KEY = map.getKeys()[index];
            Object value = map.getMap().get(KEY);
            if (value == null) {
                return  null;
            }
            return value;
        }
    }

    @Override
    public Object getObject(String str) throws SQLException {
        HashMapObject hashMapObject = this.getMap();
        if (!this.isValidCoulmnName(str)) {
            throw new SQLException("Error for Column not found : " + str);
        } else {
            Object value = hashMapObject.getMap().get(str.trim().toUpperCase());
            if (value == null) {
                return  null;
            }
            return value;
        }

    }


    protected boolean isNumeric(Object val)   {

        boolean num=false;

        try {
            // System.out.println ("VALLL="+val);
            int x=Integer.parseInt(val.toString());
            num=true;
            //System.out.println ("NUM TRUE");

        } catch (Exception e)  {

        }

        return num;
    }



}
