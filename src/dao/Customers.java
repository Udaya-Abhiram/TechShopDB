package dao;

import Exceptions.InvalidDataException;
import util.DatabaseConnector;

import java.sql.*;

public class Customers {

    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public Customers(int customerID,String firstName, String lastName, String email, String phone,String address) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress(){
        return address;
    }

    public void registerCustomer() throws InvalidDataException {

        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement checkEmailStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM customers WHERE email = ?"
             )) {
            // Check if email already exists
            checkEmailStatement.setString(1, email);
            try (ResultSet resultSet = checkEmailStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new InvalidDataException("Error: Email address already exists. Please use a different email.");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO customers (customerId, firstName, lastName, email, phone,address) VALUES (?,?, ?, ?, ?,?)"
             )) {
            preparedStatement.setInt(1,customerID);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6,address);

            preparedStatement.executeUpdate();
            System.out.println("Customer registered successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Email address already exists. Please use a different email.");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }
}
