package com.asd.Service;

import com.asd.Entity.Request;
import com.asd.Entity.RequestSelect;
import com.asd.Exception.NoMorePagesToFetchException;
import com.asd.MetaData.MetaData;
import io.agroal.api.AgroalDataSource;

import io.agroal.api.AgroalDataSourceMetrics;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.InvalidTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class JdbcService {

    @Inject
    AgroalDataSource defaultDataSource;

    @Inject
    TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    public static Map<String, Connection> activeConnection = new ConcurrentHashMap<>();

    private String dataBaseName;

    public Map<String , Connection> getAllActiveConnection() {
       return this.activeConnection ;
    }

    public List<LinkedHashMap<String, Object>> fetchByQuery(String token, String query) throws SQLException, InvalidTransactionException {
        Connection connection = null;
        try {
             connection = getActiveConnection(token);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<LinkedHashMap<String, Object>> metaDataList = retrieveResultSetMetaData(resultSet);
            List<LinkedHashMap<String, Object>> dataList = resultSetToList(resultSet);
            metaDataList.addAll(dataList);
            System.out.println("TOKEN1 : " + token);
            System.out.println("query : " + query);

            statement.close();
            resultSet.close();
            return metaDataList;

        }
        catch (SQLException e ) {
            if (connection != null && !connection.getAutoCommit()) {
                this.transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }

    }

    public List<LinkedHashMap<String, Object>> fetchByQueryWithPaging(String token, RequestSelect requestSelect) throws SQLException, InvalidTransactionException {
        Connection connection = getActiveConnection(token);
        try {


            PreparedStatement preparedStatement = createPreparedStatementWithPaging(connection, requestSelect);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("max rows : " + resultSet.next());

            List<LinkedHashMap<String, Object>> metaDataList = retrieveResultSetMetaData(resultSet);
            List<LinkedHashMap<String, Object>> dataList = resultSetToList(resultSet);
            metaDataList.addAll(dataList);
            if (dataList.isEmpty()) {
                throw new NoMorePagesToFetchException("No more pages to fetch");
            }




            preparedStatement.close();
            resultSet.close();
            return metaDataList;

        }
        catch (SQLException e) {
            if (connection != null && !connection.getAutoCommit()) {
                this.transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }
    }


    public int insert(String token, String query) throws SQLException, InvalidTransactionException {
        Connection connection = getActiveConnection(token);
         int count = 0 ;

        try (Statement statement = connection.createStatement()) {
            count = statement.executeUpdate(query);


        } catch (SQLException e) {
            if (connection != null && !connection.getAutoCommit()) {
                transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }

        return count;
    }

    public int Update(String token, String query) throws SQLException, InvalidTransactionException {
        Connection connection = getActiveConnection(token);
        int count = 0 ;

        try (Statement statement = connection.createStatement()) {
            count = statement.executeUpdate(query);

            // If standalone query (token is "N/A"), commit and close


        } catch (SQLException e) {
            if (connection != null && !connection.getAutoCommit()) {
                transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }

        return count;

    }

    public int Delete(String token, String query) throws SQLException, InvalidTransactionException {
        Connection connection = getActiveConnection(token);
        int count = 0 ;

        try (Statement statement = connection.createStatement()) {
            count = statement.executeUpdate(query);



        } catch (SQLException e) {
            if (connection != null && !connection.getAutoCommit()) {
                transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }

        return count;
    }

    public int createTable(String token, String query) throws SQLException, InvalidTransactionException {
        Connection connection = getActiveConnection(token);
        int count = 0 ;

        try (Statement statement = connection.createStatement()) {
            count = statement.executeUpdate(query);



        } catch (SQLException e) {
            if (connection != null && !connection.getAutoCommit()) {
                transactionService.rollback(token); // If there's an exception, roll back.
            }
            throw e;  // rethrowing the exception to inform the caller.
        }

        return count;
    }

    public MetaData Authunticate(Request request) throws SQLException {
        AgroalDataSourceMetrics metrics = defaultDataSource.getMetrics();

// Print or log the number of open connections
        System.out.println("Active connections: " + metrics.activeCount());
        System.out.println("Max used connections: " + metrics.maxUsedCount());
        System.out.println("Available connections: " + metrics.availableCount());
        System.out.println(metrics.acquireCount());
        if (request.getUsername().equals("root") && request.getPassword().equals("1234")) {

                Connection connection = this.defaultDataSource.getConnection();
                    String connectionToken = generateToken() ;

             this.activeConnection.put(connectionToken, connection) ;

            System.out.println("all connection from authunticate : " + this.getAllActiveConnection());
            DatabaseMetaData databaseMetaData = connection.getMetaData();
                this.dataBaseName = databaseMetaData.getDatabaseProductName();
                initializeConnection(connection);


                return new MetaData(
                        databaseMetaData.getDriverName(),
                        databaseMetaData.getDriverVersion(),
                        databaseMetaData.getUserName(),
                        databaseMetaData.getDatabaseProductName(),
                        databaseMetaData.getURL(),
                        databaseMetaData.getDatabaseProductVersion() , connectionToken
                );

        } else {
            throw new IllegalArgumentException("username or password not correct!");
        }
    }




    private PreparedStatement createPreparedStatementWithPaging(Connection connection, RequestSelect requestSelect) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        this.dataBaseName = databaseMetaData.getDatabaseProductName();

        PreparedStatement preparedStatement  ;
        String paginationSql = "";
        if ("MySQL".equalsIgnoreCase(dataBaseName.trim())) {
            paginationSql = " LIMIT ? OFFSET ?";
            String query = requestSelect.getQuery() + paginationSql;
            preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setInt(1, requestSelect.getPageSize());
            preparedStatement.setInt(2, requestSelect.getOffset());
            return preparedStatement;
        } else if ("Oracle".equalsIgnoreCase(dataBaseName.trim())) {
            paginationSql = " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            String query = requestSelect.getQuery() + paginationSql;
            preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setInt(1, requestSelect.getOffset());
            preparedStatement.setInt(2, requestSelect.getPageSize());
            return preparedStatement;

        }
        else if ("PostgreSQL".equalsIgnoreCase(dataBaseName.trim())) {
            paginationSql = " OFFSET ? LIMIT ?";
            String query = requestSelect.getQuery() + paginationSql;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, requestSelect.getOffset());
            preparedStatement.setInt(2, requestSelect.getPageSize());
            return preparedStatement;
        }


        return null;
    }


    private List<LinkedHashMap<String, Object>> retrieveResultSetMetaData(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<LinkedHashMap<String, Object>> metaDataList = new LinkedList<>();

        for (int i = 1; i <= columnCount; i++) {
            LinkedHashMap<String, Object> metaRow = new LinkedHashMap<>();
            metaRow.put("ColumnName", metaData.getColumnName(i));
            metaRow.put("ColumnType", metaData.getColumnTypeName(i));
            metaRow.put("ColumnClassName", metaData.getColumnClassName(i));
            metaRow.put("ColumnLabel", metaData.getColumnLabel(i));
            metaRow.put("ColumnDisplaySize", metaData.getColumnDisplaySize(i));
            metaRow.put("Precision", metaData.getPrecision(i));
            metaRow.put("Scale", metaData.getScale(i));
            metaRow.put("TableName", metaData.getTableName(i));
            metaRow.put("ColumnTypeName", metaData.getColumnTypeName(i));
            metaRow.put("IsNullable", metaData.isNullable(i));
            metaRow.put("IsAutoIncrement", metaData.isAutoIncrement(i));
            metaRow.put("IsCaseSensitive", metaData.isCaseSensitive(i));
            metaRow.put("IsCurrency", metaData.isCurrency(i));
            metaRow.put("IsDefinitelyWritable", metaData.isDefinitelyWritable(i));
            metaRow.put("IsReadOnly", metaData.isReadOnly(i));
            metaRow.put("IsSearchable", metaData.isSearchable(i));
            metaRow.put("IsSigned", metaData.isSigned(i));
            metaRow.put("IsWritable", metaData.isWritable(i));

            metaDataList.add(metaRow);
        }

        return metaDataList;
    }

    private List<LinkedHashMap<String, Object>> resultSetToList(ResultSet resultSet) throws SQLException {
        List<LinkedHashMap<String, Object>> rows = new LinkedList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            rows.add(row);
        }

        return rows;
    }

    private void initializeConnection(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        this.dataBaseName = databaseMetaData.getDatabaseProductName();


        if (dataBaseName.trim().equalsIgnoreCase("Oracle")) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("ALTER SESSION SET NLS_DATE_FORMAT = 'DD/MM/YYYY'");
            }
        }
    }



    public Connection getActiveConnection(String token) {
     return this.activeConnection.get(token.trim()) ;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AgroalDataSourceMetrics getMetrics() {
        AgroalDataSourceMetrics metrics = defaultDataSource.getMetrics();
        System.out.println(metrics.availableCount());
       return defaultDataSource.getMetrics();
    }
}
