package Interfacess;

import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductManagement implements ProductManaging{
    private int productId;
    private String productName;
    private String description;
    private double price;

    public ProductManagement(int productId, String productName, String description, double price){
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }
    @Override
    public void updateProductInformation(int productId,String productName, String newDescription, double newPrice) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement updateProductStatement = connection.prepareStatement(
                     "UPDATE Products SET description = ?, price = ? WHERE productId = ?"
             )) {
            updateProductStatement.setString(1, newDescription);
            updateProductStatement.setDouble(2, newPrice);
            updateProductStatement.setInt(3, productId);

            int rowsAffected = updateProductStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product information updated successfully!");
            } else {
                System.out.println("Product not found for update.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

}
