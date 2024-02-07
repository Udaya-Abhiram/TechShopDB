package Entity;

import Exceptions.DuplicateFoundException;
import util.DatabaseConnector;

import java.sql.*;
import java.util.*;
public class Products {

    private static int productID;
    private static String productName;
    private String description;
    private double price;
    Inventory inventory;

    public Products(int productID,String productName, String description, double price){
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductID() {
        return productID;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void productEntry() throws DuplicateFoundException {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement checkDuplicateStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Products WHERE productName = ?"
             );
             PreparedStatement insertProductStatement = connection.prepareStatement(
                     "INSERT INTO Products (productId, productName, description, price) VALUES (?, ?, ?, ?)"
             )) {

            checkDuplicateStatement.setString(1, productName);

            int duplicateCount = 0;
            try (ResultSet resultSet = checkDuplicateStatement.executeQuery()) {
                if (resultSet.next()) {
                    duplicateCount = resultSet.getInt(1);
                }
            }

            if (duplicateCount > 0) {
                throw new DuplicateFoundException("Duplicate product found");
            } else {
                insertProductStatement.setInt(1, productID);
                insertProductStatement.setString(2, productName);
                insertProductStatement.setString(3, description);
                insertProductStatement.setDouble(4, price);

                insertProductStatement.executeUpdate();
                System.out.println("Product entered successfully!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(" Duplicate product.");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static List<Products> getAllProducts() {
        List<Products> productList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = ((Statement) statement).executeQuery("SELECT * FROM Entity.Products")) {

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
    public static void updateProductInformation(int productID, double newPrice, String newDescription) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE products SET price = ?, description = ? WHERE productID = ?"
             )) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setString(2, newDescription);
            preparedStatement.setInt(3, productID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product information updated successfully!");
            } else {
                System.out.println("Product with ID " + productID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

//    retrive certain product
public static Products getProductByName(String productNam) {
    Products product = null;
    try (Connection connection = DatabaseConnector.openConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Entity.Products WHERE productName = ?")) {

        preparedStatement.setString(1, productName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String productName = resultSet.getString("productName");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                product = new Products(productID,productName, description, price);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Handle or log the exception as needed
    }
    return product;
}

    public static void updateStockQuantity(String productName, int quantity) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Entity.Inventory SET quantityInStock = ? WHERE productName = ?"
             )) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, productName);

            preparedStatement.executeUpdate();
            System.out.println("Stock quantity updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static void removeProduct(String productName) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Entity.Products WHERE productName = ?"
             )) {
            preparedStatement.setString(1, productName);

            preparedStatement.executeUpdate();
            System.out.println("Product removed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



//    isproduct in stock

    public void isproductInStock(int productID) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT p.productID, i.quantityInStock FROM products p JOIN inventory i ON p.productID = i.productID WHERE p.productID = ?"
             )) {
            preparedStatement.setInt(1, productID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int retrievedProductID = resultSet.getInt("productID");
                    int quantityInStock = resultSet.getInt("quantityInStock");

                    if (quantityInStock <= 10) {
                        System.out.println("Stock is not available");
                    } else {
                        System.out.println("Product ID: " + retrievedProductID + ", Stock quantity: " + quantityInStock);
                    }
                } else {
                    System.out.println("Product with ID " + productID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "Product Details: " +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price;
    }




    public static void main(String[] args) throws DuplicateFoundException {
//        Entity.Inventory i = new Entity.Inventory(1,new Entity.Products(1,"TV","Watching",3183),201,"2023-10-1");
        Products p1 = new Products(1,"TV","Watching",3183);
        Products p2 = new Products(2,"Laptop","working",21931);
        Products p3 = new Products(3,"mobile","playing",2131);
        Products p4 = new Products(4,"charger","charging",213);
////        p4.productEntry();
//
////        System.out.print(p1.getAllProducts());
//
//        Inventory inventoryForP1 = new Inventory();
//        p1.isproductInStock(1);
        p1.productEntry();

    }

}
