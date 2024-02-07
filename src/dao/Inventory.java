package dao;

import Entity.Products;
import Exceptions.InsufficientStockException;
import util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Inventory {

    int inventoryID;
    Products product;
    int quantityInStock;
    String lastStockUpdate;

    public Inventory(int inventoryID, Products product, int quantityInStock,String lastStockUpdate){
        this.inventoryID = inventoryID;
        this.product = product;
        this.quantityInStock = quantityInStock;
        this.lastStockUpdate = lastStockUpdate;
    }

    public Inventory() {

    }

    public String getLastStockUpdate() {
        return lastStockUpdate;
    }

    public Products getProduct() {
        return product;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }


    public void isQuantityInStock(int inventoryID, int quantityInStock)throws InsufficientStockException{
        int stockpresent = 0;
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select i.quantityInStock from inventory i join products p on i.inventoryId = i.productId where i.inventoryId = ?"
        )){
            preparedStatement.setInt(1,inventoryID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    stockpresent = resultSet.getInt("quantityInStock");
                }
            }
//            decrementing product stock quantity based on productId

            if(stockpresent>quantityInStock){
                    int newStock = stockpresent - quantityInStock;

                try (PreparedStatement updateStockStatement = connection.prepareStatement(
                        "UPDATE inventory SET quantityInStock = ? WHERE inventoryId = ?"
                )) {
                    updateStockStatement.setInt(1, newStock);
                    updateStockStatement.setInt(2, inventoryID);

                    updateStockStatement.executeUpdate();
                    System.out.println("Stock updated successfully. New stock: " + newStock);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            else
                throw new InsufficientStockException("Stock is not present");

//            System.out.print(stockpresent);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

//    Inventory management

    public Map<Integer, Integer> inventoryManagement() {
        Map<Integer, Integer> inventoryList = new TreeMap<>();

        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT productId, quantityInStock FROM inventory"
             );
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                int quantityInStock = resultSet.getInt("quantityInStock");

                inventoryList.put(productId, quantityInStock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }

}
