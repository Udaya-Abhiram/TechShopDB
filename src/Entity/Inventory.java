package Entity;

import util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void createInventoryData() {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Entity.Inventory (inventoryID, productId, quantityInStock, lastStockUpdate) VALUES (?, ?, ?, ?)"
             )) {
            preparedStatement.setInt(1, inventoryID);
            preparedStatement.setInt(2, product.getProductID());
            preparedStatement.setInt(3, quantityInStock);
            preparedStatement.setString(4, lastStockUpdate);

            preparedStatement.executeUpdate();
            System.out.println("Data added in inventory!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error:some thing is wrong.");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }


//    get product

    public void getProduct(int inventoryID){
        List<String> products = new ArrayList<>();
        try(Connection connection = DatabaseConnector.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "select p.productName from products p join inventory i on i.inventoryId = p.productId where i.inventoryId = ? "
        )){
            preparedStatement.setInt(1,inventoryID);
            try(ResultSet resultSet =  preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String productName = resultSet.getString("productName");

                    products.add(productName);
                }
            }

            for(String prodName: products)
                System.out.print(prodName+" ");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
// quantity in stock

    public void quantityInStock(int inventoryID){
        int quantity = 0;
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select quantityInStock from inventory where inventoryId = ?"
        )){
            preparedStatement.setInt(1,inventoryID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    quantity = resultSet.getInt("quantityInStock");
                }
            }
            System.out.println(quantity);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateQuantity(int inventoryID,int quantityInStock){
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "Update inventory set quantityInStock = ? where inventoryId = ?"
        )){
            preparedStatement.setInt(1,quantityInStock);
            preparedStatement.setInt(2,inventoryID);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0)
                System.out.println("Quantity is updated");
            else
                System.out.println("Something is wrong");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeQuantity(int inventoryID){
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "Delete * from inventory where inventoryId = ? "
        )){
            preparedStatement.setInt(1,inventoryID);

            preparedStatement.executeUpdate();
            System.out.print("product removed");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void isProductAvailable(int inventoryID, int quantityInStock){
        int stockPresent = 0;
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select quantityInStock from inventory where inventoryId = ?"
        )){
            preparedStatement.setInt(1,quantityInStock);
            preparedStatement.setInt(1,inventoryID);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    stockPresent = resultSet.getInt("quantityInStock");
                }
            }

            if(quantityInStock>stockPresent)
                System.out.println("no stock");
            else
                System.out.println("Stock available");

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

//    list all products
    public static List<Products> getAllProducts() {
        List<Products> productList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = ((Statement) statement).executeQuery("SELECT * FROM Entity.Products p join inventory i on p.productId = i.inventoryId")) {

            while (resultSet.next()) {
                int productID = resultSet.getInt("productID");
                String productName = resultSet.getString("productName");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                Products product = new Products(productID, productName, description, price);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return productList;
    }

//    list stock

    public void isProductAvailable(int inventoryID, String productName){
        String prod="";
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select p.productName from products p join inventory i on p.productId = i.inventoryId where inventoryId = ?"
        )){
            preparedStatement.setInt(1,inventoryID);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    prod = resultSet.getString("productName");
                }
            }

            if(prod.equals(productName))
                System.out.println("product is available");
            else
                System.out.println("product unavailable");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Inventory i = new Inventory(1,new Products(1,"TV","Watching",3183),201,"2023-10-1");
//        i.createInventoryData();
//        i.getProduct(1);
//        i.quantityInStock(1);
//        i.updateQuantity(1,300);
//        i.isProductAvailable(1,200);
//        System.out.print(i.getAllProducts());
        i.isProductAvailable(1,"TV");
    }

}
