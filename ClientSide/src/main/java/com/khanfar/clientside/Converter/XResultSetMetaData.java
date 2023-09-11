package com.khanfar.clientside.Converter;

import com.khanfar.clientside.META_NAME;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class XResultSetMetaData implements ResultSetMetaData {
    private List<LinkedHashMap<String , Object>> metaNameObjectHashMap;



  public  XResultSetMetaData (List<LinkedHashMap<String , Object>> metaNameObjectHashMap) {
      this.metaNameObjectHashMap = metaNameObjectHashMap;

  }


    @Override
    public int getColumnCount() throws SQLException {
        return this.metaNameObjectHashMap.size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return (boolean)this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_AUTO_INCREMENT);
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return (boolean)this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_CASE_SENSITIVE);
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return (boolean)this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_SEARCHABLE);
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return (boolean)this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_CURRENCY);
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return (Integer) this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_NULLABLE);
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return (boolean)this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_SIGNED);
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {

        return (Integer) this.metaNameObjectHashMap.get(column-1).get(META_NAME.COLUMN_DISPLAY_SIZE);
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return (String) this.metaNameObjectHashMap.get(column-1).get(META_NAME.COLUMN_LABEL);
    }

    @Override
    public String getColumnName(int column) throws SQLException {

        return (String) this.metaNameObjectHashMap.get(column-1).get(META_NAME.COLUMN_NAME);

    }

    @Override
    public String getSchemaName(int column) throws SQLException {
      return null ;
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return (int)this.metaNameObjectHashMap.get(column-1).get(META_NAME.PRECISION);
    }

    @Override
    public int getScale(int column) throws SQLException {
        return (Integer) this.metaNameObjectHashMap.get(column-1).get(META_NAME.SCALE);
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return (String) this.metaNameObjectHashMap.get(column-1).get(META_NAME.TABLE_NAME);
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return null;
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        String typeName = getColumnClassName(column);
        if (typeName == null) {
            return Integer.MAX_VALUE;
        }

        switch (typeName) {
            case "java.lang.Integer": return java.sql.Types.INTEGER;
            case "java.lang.Long": return java.sql.Types.BIGINT;
            case "java.lang.Short": return java.sql.Types.SMALLINT;
            case "java.lang.Byte": return java.sql.Types.TINYINT;
            case "java.lang.Boolean": return java.sql.Types.BOOLEAN;
            case "java.lang.Float": return java.sql.Types.FLOAT;
            case "java.lang.Double": return java.sql.Types.DOUBLE;
            case "java.math.BigDecimal": return java.sql.Types.DECIMAL;
            case "java.lang.String": return java.sql.Types.VARCHAR;
            case "java.sql.Date": return java.sql.Types.DATE;
            case "java.sql.Time": return java.sql.Types.TIME;
            case "java.sql.Timestamp": return java.sql.Types.TIMESTAMP;
            case "[B": return java.sql.Types.BINARY;
            case "[Ljava.lang.Byte;": return java.sql.Types.VARBINARY;
            case "java.sql.Array": return java.sql.Types.ARRAY;
            case "java.sql.Blob": return java.sql.Types.BLOB;
            case "java.sql.Clob": return java.sql.Types.CLOB;
            case "java.sql.NClob": return java.sql.Types.NCLOB;
            case "java.sql.Ref": return java.sql.Types.REF;
            case "java.lang.Object": return java.sql.Types.JAVA_OBJECT;
            case "java.net.URL": return java.sql.Types.DATALINK;
            case "java.sql.RowId": return java.sql.Types.ROWID;
            default: throw new SQLException("Unknown type name: " + typeName);
        }
    }


    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return (String) this.metaNameObjectHashMap.get(column-1).get(META_NAME.COLUMN_TYPE_NAME);
  }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return (Boolean) this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_READ_ONLY);
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return (Boolean) this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_WRITABLE);
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return (Boolean) this.metaNameObjectHashMap.get(column-1).get(META_NAME.IS_DEFINITELY_WRITABLE);
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
     return (String) this.metaNameObjectHashMap.get(column-1).get(META_NAME.COLUMN_CLASS_NAME);
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
