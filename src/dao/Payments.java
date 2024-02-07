package dao;

import Entity.Products;
import Exceptions.PaymentFailedException;
import util.DatabaseConnector;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Payments {
    private int paymentId;
    Products product;

    private double price;
    private String status;

    public Payments(int paymentId,Products product, double price,String status){
        this.paymentId = paymentId;
        this.product = product;
        this.price = price;
        this.status = status;
    }

    public Products getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void registerPayments() throws PaymentFailedException {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement checkProductPriceStatement = connection.prepareStatement(
                     "SELECT price FROM products WHERE productId = ?"
             )) {

            // Check if the entered price matches the product price
            checkProductPriceStatement.setInt(1, product.getProductID());

            try (ResultSet resultSet = checkProductPriceStatement.executeQuery()) {
                if (resultSet.next()) {
                    double productPrice = resultSet.getDouble("price");

                    if (productPrice != price) {
                        throw new PaymentFailedException("Entered price does not match the product price.");
                    }

                    // Prices match, proceed with payment registration
                    try (PreparedStatement insertPaymentStatement = connection.prepareStatement(
                            "INSERT INTO payments (paymentId, productId, price, status) VALUES (?, ?, ?, ?)"
                    )) {
                        insertPaymentStatement.setInt(1, paymentId);
                        insertPaymentStatement.setInt(2, product.getProductID());
                        insertPaymentStatement.setDouble(3, price);
                        insertPaymentStatement.setString(4, status);

                        insertPaymentStatement.executeUpdate();
                        System.out.println("Payment data entered successfully");
                    }
                } else {
                    throw new PaymentFailedException("Product not found for productId: " + product.getProductID());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePayment(int paymentId,double price,String status){
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update payments set price = ?, status = ? where paymentId = ?"
        )){
            preparedStatement.setDouble(1,price);
            preparedStatement.setString(2,status);
            preparedStatement.setInt(1,paymentId);

            int rowsaffected = preparedStatement.executeUpdate();

            if(rowsaffected>0){
                System.out.println("payment status is updated");
            }
            else{
                System.out.println("Something went wrong");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }


}
