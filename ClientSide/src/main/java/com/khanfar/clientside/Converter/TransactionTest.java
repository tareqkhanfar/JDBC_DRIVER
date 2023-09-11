package com.khanfar.clientside.Converter;

import java.sql.*;
import java.util.Properties;

public class TransactionTest {

    private static final String JDBC_URL = "http://localhost:8080/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        try {
            testComplexTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testComplexTransaction() throws SQLException, ClassNotFoundException {
        Class.forName("com.khanfar.clientside.Converter.XDriver");
        Properties properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);

        try (Connection conn = DriverManager.getConnection(JDBC_URL, properties)) {
            conn.setAutoCommit(false);

            try {
                // Determine loyalty discount for user
                double loyaltyDiscount = 0.0;
                try (PreparedStatement userStatement = conn.prepareStatement("SELECT total_spent , BOD FROM Users WHERE id = ?")) {
                    userStatement.setInt(1, 1);
                    try (ResultSet userResult = userStatement.executeQuery()) {
                        if (userResult.next()) {
                            System.out.println(userResult.getDate("BOD"));
                            double totalSpent = userResult.getDouble("total_spent");
                            if (totalSpent > 2000) {
                                loyaltyDiscount = 0.10;
                            } else if (totalSpent > 1000) {
                                loyaltyDiscount = 0.05;
                            }
                        }
                    }
                }

                // Insert order
                String orderInsertSQL = "INSERT INTO Orders (user_id, order_date) VALUES (?, ?)";
                PreparedStatement orderStatement = conn.prepareStatement(orderInsertSQL);
                orderStatement.setInt(1, 1);
                orderStatement.setDate(2, Date.valueOf("2023-10-10"));
                orderStatement.executeUpdate();

                // Retrieve the order ID based on known criteria (user_id and order_date)
                String getOrderIDSQL = "SELECT id FROM rders WHERE user_id = ? AND order_date = ?";
                PreparedStatement getOrderIDStatement = conn.prepareStatement(getOrderIDSQL);
                getOrderIDStatement.setInt(1, 1);
                getOrderIDStatement.setDate(2, Date.valueOf("2023-10-10"));

                int orderId = -1;
                try (ResultSet rs = getOrderIDStatement.executeQuery()) {
                    if (rs.next()) {
                        orderId = rs.getInt("id");
                    }
                }

                if (orderId == -1) {
                    throw new SQLException("Failed to retrieve order ID.");
                }

                // Add products to the order
                int[][] products = {
                        {6, 6}  // Gaming Keyboard ID 6, Quantity 6
                };

                double totalOrderValue = 0.0;

                for (int[] product : products) {
                    int productId = product[0];
                    int quantity = product[1];

                    // Get product price
                    double productPrice;
                    String getPriceSQL = "SELECT price FROM Products WHERE id = ?";
                    PreparedStatement priceStatement = conn.prepareStatement(getPriceSQL);
                    priceStatement.setInt(1, productId);

                    try (ResultSet priceResult = priceStatement.executeQuery()) {
                        if (priceResult.next()) {
                            productPrice = priceResult.getDouble("price");
                        } else {
                            throw new SQLException("Failed to retrieve product price for ID: " + productId);
                        }
                    }

                    totalOrderValue += productPrice * quantity;

                    // Insert into OrderDetails
                    String insertOrderDetailsSQL = "INSERT INTO OrderDetails (order_id, product_id, quantity) VALUES (?, ?, ?)";
                    PreparedStatement orderDetailStatement = conn.prepareStatement(insertOrderDetailsSQL);
                    orderDetailStatement.setInt(1, orderId);
                    orderDetailStatement.setInt(2, productId);
                    orderDetailStatement.setInt(3, quantity);
                    orderDetailStatement.executeUpdate();
                }

                // Apply discounts
                double effectiveDiscount = loyaltyDiscount;

                if (totalOrderValue > 5 * 70.00) {
                    effectiveDiscount += 0.10;  // Additional 10% discount for bulk purchase
                }

                effectiveDiscount = Math.min(effectiveDiscount, 0.15);
                totalOrderValue *= (1 - effectiveDiscount);

                if (effectiveDiscount > 0) {
                    System.out.println("A discount of " + (effectiveDiscount * 100) + "% has been applied to your order.");
                }

                // Update the total spent for the user
                String updateUserSpentSQL = "UPDATE Users SET total_spent = total_spent + ? WHERE id = ?";
                PreparedStatement updateUserSpentStatement = conn.prepareStatement(updateUserSpentSQL);
                updateUserSpentStatement.setDouble(1, totalOrderValue);
                updateUserSpentStatement.setInt(2, 1);
                updateUserSpentStatement.executeUpdate();

                conn.commit();

                System.out.println("Order placed successfully with a total value of: " + totalOrderValue);

            } catch (Exception ex) {
                System.out.println("Exception caught: " + ex.getMessage());
                conn.rollback();
                System.out.println("Transaction rolled back.");
            }
        }
    }
}
