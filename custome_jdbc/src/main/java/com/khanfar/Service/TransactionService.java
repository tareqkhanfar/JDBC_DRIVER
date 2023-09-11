package com.khanfar.Service;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.InvalidTransactionException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TransactionService {

    @Inject
    JdbcService jdbcService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private static final long STALE_TRANSACTION_THRESHOLD = 60 * 60 * 1000; // 1 hour in milliseconds

    private static Map<String, Connection> activeTransactions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    public String startTransaction() throws SQLException {
        Connection connection = null;
        String transactionToken = null;
        try {
            connection = this.jdbcService.getConnection();
            connection.setAutoCommit(false);
            transactionToken = generateToken();
            this.activeTransactions.put(transactionToken, connection);
            logger.info("Started transaction successfully with token: {}", transactionToken);
        } catch (SQLException ex) {
            logger.error("Failed to start transaction", ex);
            if (connection != null) {
                connection.close();
            }
            throw ex;
        }
        return transactionToken;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void commit(String token) throws InvalidTransactionException, SQLException {
        Connection connection = activeTransactions.get(token.trim());
        logger.info("Started Commit = with token: {}", token);

        if (connection == null) {
            logger.info("All Connections from Commit : " , activeTransactions);
            logger.info("Invalid Transaction from Commit = with token: {}", token);

            throw new InvalidTransactionException("Invalid transaction token");
        }

        try {
            connection.commit();
            logger.info("Commit Success = with token: {}", token);

        } finally {
         //   logger.info("End Transaction from Commit = with token: {}", token);

          //  endTransaction(token);
        }
    }

    public void rollback(String token) throws InvalidTransactionException, SQLException {
        Connection connection = activeTransactions.get(token.trim());

        System.out.println("all connections " + activeTransactions);

        if (connection == null) {
            logger.info("All Connections from rollback : " , activeTransactions);
            logger.info("Invalid Transaction from rollback = with token: {}", token);
            throw new InvalidTransactionException("Invalid transaction token");
        }
        try {
            connection.rollback();
            logger.info("RollBack Success = with token: {}", token);

        } finally {
           // logger.info("End Transaction from RollBack = with token: {}", token);

           // endTransaction(token);
        }
    }

    public Map<String, Connection> getActiveTransactions() {
        return this.activeTransactions;
    }

    public void SetCommitAutoAsTrue(String token) throws InvalidTransactionException, SQLException {
        Connection connection = activeTransactions.get(token);

        if (connection != null) {

           // activeTransactions.remove(token);

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                //connection.close();
                logger.info("Commited as True its done  with token: {}", token);
                logger.info("All Connections from Commited as true : " , activeTransactions);

            }
        }
    }

    public void CloseConnection(String token) throws InvalidTransactionException, SQLException {
        Connection connection = activeTransactions.get(token);

        if (connection != null) {

            activeTransactions.remove(token);

            try {
                connection.setAutoCommit(true);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection.close();

                logger.info("Closed Succcessfully : {}" , token);
                logger.info("All Connections after  Close Connection : " , activeTransactions);

            }
        }
        else {
            logger.info("There is not connection in hash map data structure : {}" , token);
            logger.info("All Connections from Close Connection : " , activeTransactions);
        }



    }





}
