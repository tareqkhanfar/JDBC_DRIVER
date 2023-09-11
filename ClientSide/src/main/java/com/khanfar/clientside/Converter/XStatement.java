//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.khanfar.clientside.Converter;

import com.khanfar.clientside.Exception.NoMorePagesToFetchException;
import com.khanfar.clientside.Request.Response;
import com.khanfar.clientside.Transaction.Transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Properties;

public class XStatement implements Statement {
    private XResultSet resultSet;
    protected Properties properties;
    protected String uri;
    private JsonToList jsonToList = new JsonToList();
    protected Transaction transaction ;

    public XStatement() {
    }

    public XStatement(Transaction transaction , String uri, Properties properties) {
        this.properties = properties;
        this.uri = uri;
        this.transaction = transaction ;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
       if (sql.trim().endsWith(";")) {
           StringBuilder builder = new StringBuilder(sql) ;
           builder.deleteCharAt(builder.length()-1);
           sql = builder.toString() ;
       }

        this.properties.put("query", sql);

        JsonToList jsonToList = new JsonToList();

        if (sql.trim().toUpperCase().startsWith("SELECT")) {
            STATUS status = STATUS.SELECT;

            try {
                String response;
                Response responseDataAndMetaData;
                ResultSetAdapter resultSetAdapter;
                if (this.properties.getProperty("Paging").trim().equalsIgnoreCase("ENABLE")) {
                    String token ;
                    if (transaction.getToken() == null) {
                        token = "N/A" ;
                    }
                    else {
                        token = transaction.getToken();
                    }

                    response = this.jsonToList.retriveDataWithPaging(this.properties.getProperty("URL"),token ,  STATUS.SELECT_PAGING, sql);
                    responseDataAndMetaData = this.jsonToList.convertJsonToList(response);
                    resultSetAdapter = new ResultSetAdapter(transaction ,responseDataAndMetaData, this.properties);
                    this.resultSet = new XResultSet(resultSetAdapter , this);
                    return this.resultSet;
                } else {
                    String token ;
                    if (transaction.getToken() == null) {
                        token = "N/A" ;
                    }
                    else {
                        token = transaction.getToken();
                    }

                    response = jsonToList.sendRequestAndGetResponse(this.uri, token ,status, sql);
                    responseDataAndMetaData = jsonToList.convertJsonToList(response);
                    resultSetAdapter = new ResultSetAdapter(transaction , responseDataAndMetaData, this.properties);
                    this.resultSet = new XResultSet(resultSetAdapter , this);
                    return this.resultSet;
                }
            } catch (IOException var7) {
                throw new RuntimeException(var7);
            }
            catch (NoMorePagesToFetchException e ) {
              //  return new XResultSet();
                throw new NoMorePagesToFetchException ("No more page to fetch ") ;
            }

        } else {
            throw new SQLException("Error : the Query is Wrong :" + sql);
        }

    }

    public int executeUpdate(String sql) throws SQLException {
        if (sql.trim().endsWith(";")) {
            StringBuilder builder = new StringBuilder(sql) ;
            builder.deleteCharAt(builder.length()-1);
            sql = builder.toString() ;
        }

        STATUS status;
        if (sql.trim().toUpperCase().startsWith("INSERT")) {
            status = STATUS.INSERT;
        } else if (sql.trim().toUpperCase().startsWith("UPDATE")) {
            status = STATUS.UPDATE;
        } else if (sql.trim().toUpperCase().startsWith("DELETE")) {
            status = STATUS.DELETE;
        }
        else if (sql.trim().toUpperCase().startsWith("ALTER")) {
            status = STATUS.UPDATE;
        }

        else {
            if (!sql.trim().toUpperCase().startsWith("CREATE")) {
                throw new SQLException();
            }

            status = STATUS.CREATE_TABLE;
        }

        JsonToList jsonToList = new JsonToList();

        try {
            String token ;
            if (transaction.getToken() == null) {
                token = "N/A" ;
            }
            else {
                token = transaction.getToken();
            }
            String response = jsonToList.sendRequestAndGetResponse(this.uri,token , status, sql);
            return Integer.parseInt(response);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public void close() throws SQLException {
        try {
        //    this.transaction.endTransaction();
        } catch (Exception e) {
        }
    }

    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public void setMaxFieldSize(int max) throws SQLException {
    }

    public int getMaxRows() throws SQLException {
        return 0;
    }

    public void setMaxRows(int max) throws SQLException {
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
    }

    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    public void setQueryTimeout(int seconds) throws SQLException {
    }

    public void cancel() throws SQLException {
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public void setCursorName(String name) throws SQLException {
    }

    public boolean execute(String sql) throws SQLException {
        return false;
    }

    public ResultSet getResultSet() throws SQLException {
        return executeQuery(this.properties.get("query").toString());
    }

    public int getUpdateCount() throws SQLException {
        return 0;
    }

    public boolean getMoreResults() throws SQLException {
        return false;
    }

    public void setFetchDirection(int direction) throws SQLException {
    }

    public int getFetchDirection() throws SQLException {
        return 0;
    }

    public void setFetchSize(int rows) throws SQLException {
        rows = 100 ;
        if (rows >= 0) {
            SharedPage.setPageSize(rows);
        } else {
            throw new IllegalArgumentException("cant be assign rows as a less than or equal zero");
        }
    }

    public int getFetchSize() throws SQLException {
        return SharedPage.getPageSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    public int getResultSetType() throws SQLException {
        return 0;
    }

    public void addBatch(String sql) throws SQLException {
    }

    public void clearBatch() throws SQLException {
    }

    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    public Connection getConnection() throws SQLException {
        return null;
    }

    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    public boolean isClosed() throws SQLException {
        return false;
    }

    public void setPoolable(boolean poolable) throws SQLException {
    }

    public boolean isPoolable() throws SQLException {
        return false;
    }

    public void closeOnCompletion() throws SQLException {
    }

    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
