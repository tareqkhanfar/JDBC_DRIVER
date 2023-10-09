package com.asd.Service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TransactionService {

    @Inject
    JdbcService jdbcService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);




    public String startTransaction(String token) throws SQLException {
        Connection connection = null;
        String transactionToken = null;
        try {

            System.out.println("token from client : " + token);
            connection = this.jdbcService.getActiveConnection(token);

            System.out.println("all connection from start transaction : " + this.jdbcService.getAllActiveConnection());
            connection.setAutoCommit(false);
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



    public void commit(String token) throws InvalidTransactionException, SQLException {
        Connection connection = this.jdbcService.getActiveConnection(token);
        logger.info("Started Commit = with token: {}", token);

        if (connection == null) {
            logger.info("All Connections from Commit : " , this.jdbcService.getAllActiveConnection());
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
        Connection connection = this.jdbcService.getActiveConnection(token);

        System.out.println("all connections " + this.jdbcService.getAllActiveConnection());

        if (connection == null) {
            logger.info("All Connections from rollback : " , this.jdbcService.getAllActiveConnection());
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



    public void SetCommitAutoAsTrue(String token) throws InvalidTransactionException, SQLException {
        Connection connection = this.jdbcService.getActiveConnection(token);

        if (connection != null) {

           // activeTransactions.remove(token);

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                //connection.close();
                logger.info("Commited as True its done  with token: {}", token);
                logger.info("All Connections from Commited as true : " , this.jdbcService.getAllActiveConnection());

            }
        }
    }

    public void CloseConnection(String token) throws InvalidTransactionException, SQLException {
        Connection connection = this.jdbcService.getActiveConnection(token);

        if (connection != null) {

            this.jdbcService.getAllActiveConnection().remove(token);

            try {
                connection.setAutoCommit(true);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection.close();

                logger.info("Closed Succcessfully : {}" , token);
                logger.info("All Connections after  Close Connection : " , this.jdbcService.getAllActiveConnection());

            }
        }
        else {
            logger.info("There is not connection in hash map data structure : {}" , token);
            logger.info("All Connections from Close Connection : " , this.jdbcService.getAllActiveConnection());
        }



    }





}
