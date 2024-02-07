package Entity;

import util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void registerCustomer() {
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

    public static void updateCustomerInformation(int customerID, String firstName, String lastName, String email, String phone, String address) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE customers SET firstName = ?, lastName = ?, email = ?, phone = ?, address = ? WHERE customerId = ?"
             )) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, address);
            preparedStatement.setInt(6, customerID); // Set customerID as the 6th parameter

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer information updated successfully!");
            } else {
                System.out.println("Customer with ID " + customerID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void calculateTotalOrders(int customerId) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM customers c JOIN Entity.OrdersData o ON c.customerId = o.customerId WHERE c.customerId = ?"
             )) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int result = resultSet.getInt(1);
                    if (result > 0) {
                        System.out.println("The orders placed by the customer is: " + result);
                    } else {
                        System.out.println("No orders placed");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    get customer details

    public void getCustomerDetails(int customerID){
        List<Customers> customers = new ArrayList<>();
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from customers where customerID = ?"
        )){
            preparedStatement.setInt(1,customerID);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    customers.add(new Customers(
                            resultSet.getInt("customerId"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("address")
                    ));
                }
                if(customers.isEmpty()){
                    System.out.println("customer is not present");
                }
                else
                    System.out.println(customers);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Customer Details: " +
                "customerID='"+customerID+'\''+
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'';
    }




    public static void main(String[] args) {
        // Example usage for customer registration
//        Entity.Customers newCustomer = new Entity.Customers("John", "Doe", "john.doe@example.com", "1234567890");
//        Entity.Customers c2 = new Entity.Customers("Abhi","Kona","abhi@gmail.com","37127163");
////        newCustomer.registerCustomer();
//        c2.registerCustomer();

        Customers c = new Customers(1,"uday","ram","uday@gmail.com","832821","123 street");
        Customers c1 = new Customers(2,"Abhi","ram","abhi@gmail.com","281831","82 street");
        Customers c2 = new Customers(3,"sandep","setti","sandeep@gmail.com","32121","123 street");
        Customers c3 = new Customers(4,"hari","dev","hari@gmail.com","37212","72 street");

//        c.registerCustomer();
//        c1.registerCustomer();
//        c2.registerCustomer();
//        c3.registerCustomer();

//        c.getCustomerDetails(1);
        c.calculateTotalOrders(3);
    }
}
