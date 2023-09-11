package com.khanfar.clientside.Converter;

import com.khanfar.clientside.Transaction.Transaction;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Properties;

public class XPreparedStatement extends XStatement implements PreparedStatement {

    private LinkedList<Object> parametersList ;
    private String sql ;
    private Properties properties ;



    public XPreparedStatement (Transaction transaction , String url , String sql , Properties properties) {
        super();
        this.parametersList =new LinkedList<>( ) ;
        this.sql = sql;
        super.uri = url ;
        this.properties = properties ;
        super.properties = properties ;
        super.transaction = transaction ;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        String afterBinding = BindingData(this.sql) ;
        super.properties = this.properties ;

        return super.executeQuery(afterBinding);
    }

    @Override
    public int executeUpdate() throws SQLException {

        String afterBinding = BindingData(this.sql) ;
       // super.properties = this.properties ;
           return super.executeUpdate(afterBinding) ;


    }
    private String BindingData (String query) throws SQLException {
        int counterOfQuestionMark = 0;

        for (int i = 0 ; i < query.length() ; i++) {

            if (query.charAt(i) == '?') {
                StringBuilder builder = new StringBuilder(query);
                if (parametersList.get(counterOfQuestionMark).getClass() == String.class || parametersList.get(counterOfQuestionMark).getClass() == java.sql.Date.class) {
                    String temp = "\'" +  parametersList.get(counterOfQuestionMark) + "\'";
                    builder.replace(i, i + 1, temp);

                }


                else {
                    builder.replace(i, i + 1, parametersList.get(counterOfQuestionMark).toString());
                }
                query = builder.toString();
                counterOfQuestionMark++;
            }
        }

        if (counterOfQuestionMark != parametersList.size()) {
            throw new SQLException("number of attributes is not equal than number of \'?\'");
        }

        return query ;
    }


    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        parametersList.add(parameterIndex-1 , null);

    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        parametersList.add(parameterIndex-1  , (Boolean)x);

    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        parametersList.add(parameterIndex-1  , (Byte)x);

    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        parametersList.add(parameterIndex-1  , (Short)x);

    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
             parametersList.add(parameterIndex-1  , (Integer)x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        parametersList.add(parameterIndex-1  , (Long)x);

    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        parametersList.add(parameterIndex-1  , (Float)x);

    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.parametersList.add(parameterIndex-1  , (Double)x);

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {

        this.parametersList.add(parameterIndex-1  , x);

    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {

    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.parametersList.add(parameterIndex-1  , x);

    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void clearParameters() throws SQLException {
   this.parametersList.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
      this.parametersList.add(parameterIndex-1  , x);
    }

    @Override
    public boolean execute() throws SQLException {
        return false;
    }

    @Override
    public void addBatch() throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,x );

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,x );

    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,x );
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {

    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {

    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        this.parametersList.add(parameterIndex ,x );

    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        this.parametersList.add(parameterIndex -1 ,value );

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,value );

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        this.parametersList.add(parameterIndex -1 ,xmlObject );

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,x );

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,x );

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,reader );

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,value );

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,reader );

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        this.parametersList.add(parameterIndex-1  ,reader );

    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        String afterBinding = this.BindingData(sql);

        return super.executeQuery(afterBinding);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        String afterBinding = this.BindingData(sql);

        return super.executeUpdate(afterBinding);
    }

    @Override
    public void close() throws SQLException {
          super.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {


        if (rows >0)
         SharedPage.setPageSize(rows);
        else
            throw new SQLException("Error : the page size must be > 1 ");
    }

    @Override
    public int getFetchSize() throws SQLException {
        return SharedPage.getPageSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
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
