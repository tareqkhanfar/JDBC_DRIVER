package com.khanfar.clientside.Converter;

import com.khanfar.clientside.Transaction.Transaction;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class XConnection implements Connection {

    private String url ;

    private XStatement statement ;
    private XDataBaseMetaData xDataBaseMetaData ;
   private LinkedHashMap<String , Object> DB_META_DATA ;
   private XPreparedStatement preparedStatement ;
   private Properties info ;

   private Transaction transaction ;
   private boolean autoCommit = true  ;


    public XConnection(String url, Properties info, LinkedHashMap<String, Object> dataBaseMetaData) {
        this.url = url ;
        this.DB_META_DATA = dataBaseMetaData;
        this.info = info ;
        this.transaction = new Transaction(this.url);
System.out.println("TOKEN : "+transaction.getToken());

    }




    @Override
    public Statement createStatement() throws SQLException {
        this.statement = new XStatement(transaction, url , this.info);
        return this.statement ;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        this.preparedStatement = new XPreparedStatement(transaction , url , sql ,this.info ) ;
        return this.preparedStatement;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
            if (!autoCommit && this.autoCommit) {
                try {
                   this.transaction.beginTransaction();
                    System.out.println("TOKEN : "+transaction.getToken());


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (autoCommit && !this.autoCommit && transaction.getToken() != null) {
                try {
                    this.transaction.setCommitAutoAsTrue(); ;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        this.autoCommit = autoCommit;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }

    @Override
    public void commit() throws SQLException {
     if (this.autoCommit || transaction.getToken() == null) {
         throw new SQLException("Can't commit with autoCommit enabled or no transaction started");
     }
        try {
            transaction.commit();
        //    transaction.setToken(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (autoCommit || transaction.getToken() == null) {
            throw new SQLException("Cant rollback with autoCommit enabled or no transaction started");
        }
        try {
            transaction.rollback();
            //transaction.setToken(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
           transaction.closeConnection();
        } catch (Exception e) {
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        this.xDataBaseMetaData = new XDataBaseMetaData(this.DB_META_DATA) ;
        return xDataBaseMetaData;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {

    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {

    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {

    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {

    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
