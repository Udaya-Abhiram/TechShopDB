package dao;

import Entity.OrdersData;
import Entity.Products;
import Exceptions.IncompleteOrderException;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetails {
    int orderDetailID;
    OrdersData order;
    Products product;
    int quantity;

    public OrderDetails(int orderDetailID, OrdersData order, Products product, int quantity){
        this.orderDetailID = orderDetailID;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }


    public Products getProduct() {
        return product;
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrdersData getOrder() {
        return order;
    }

    public void processOrder(int orderDetailID) throws IncompleteOrderException {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement checkProductReferenceStatement = connection.prepareStatement(
                     "SELECT productId FROM orderDetails WHERE orderDetailID = ?"
             )) {
            checkProductReferenceStatement.setInt(1, orderDetailID);

            try (ResultSet resultSet = checkProductReferenceStatement.executeQuery()) {
                if (resultSet.next()) {
                    int productId = resultSet.getInt("productId");
                    if (productId > 0) {
                        System.out.println("Order processed successfully!");
                    } else {
                        throw new IncompleteOrderException("Order detail is incomplete: missing product reference.");
                    }
                } else {
                    throw new IncompleteOrderException("Order detail not found for orderDetailID: " + orderDetailID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public boolean isProductAvailable(int productId, int quantity) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement checkAvailabilityStatement = connection.prepareStatement(
                     "SELECT quantityInStock FROM Inventory WHERE productId = ?"
             )) {
            checkAvailabilityStatement.setInt(1, productId);

            try (ResultSet resultSet = checkAvailabilityStatement.executeQuery()) {
                if (resultSet.next()) {
                    int quantityInStock = resultSet.getInt("quantityInStock");
                    return quantityInStock >= quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
