package Entity;

import util.DatabaseConnector;

import java.sql.*;
import java.util.*;

public class OrdersData {
    int orderID;
    Customers customer;
    String orderDate;
    double totalAmount;
    String status;


    public OrdersData(int orderID,Customers customer, String orderDate, double totalAmount, String status) {
        this.orderID = orderID;
        this.customer = customer;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void orderEntry() {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Entity.OrdersData (orderId, customerID, OrderDate, totalAmount,status) VALUES (?, ?, ?, ?,?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {
            preparedStatement.setInt(1,orderID);
            preparedStatement.setInt(2, customer.getCustomerID());
            preparedStatement.setString(3, orderDate);
            preparedStatement.setDouble(4, totalAmount);
            preparedStatement.setString(5,status);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedCustomerId = generatedKeys.getInt(1);
                        System.out.println("Generated customerID: " + generatedCustomerId);
                    } else {
                        System.out.println("order enter successfully.");
                    }
                }
            } else {
                System.out.println("Failed to insert order.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("SQL Integrity Constraint Violation Exception: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public void calculateTotalAmount(int orderID){
        try (Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select sum(totalAmount) as TotalAmount,c.firstName from Entity.OrdersData o join customers c on" +
                        " o.orderId = o.customerId where orderId = ? group by c.firstName"
        )){
            preparedStatement.setInt(1,orderID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    double totalAmount = resultSet.getDouble("totalAmount");
                    String name = resultSet.getString("firstName");

                    System.out.println(totalAmount+" "+name);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateOrderStatus(int orderID, String status){
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update Entity.OrdersData set status = ? where orderId= ? "
        )) {
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,orderID);


            int rowsaffected = preparedStatement.executeUpdate();
            if(rowsaffected>0)
                System.out.println("status updated successfully");
            else
                System.out.println("Order id is not present");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

//    cancel order

    public void cancelOrder(int orderID) {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Entity.OrdersData WHERE orderID = ?"
             )) {
            preparedStatement.setInt(1, orderID);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order removed successfully.");
            } else {
                System.out.println("Order ID is not present.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    sorting by date

    public void sortingData() {
        List<String> sortData = new ArrayList<>();
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT orderDate, status FROM OrdersData ORDER BY orderDate ASC"
             )) {
//            preparedStatement.setInt(1, orderID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String orderDate = resultSet.getString("orderDate");
                    String status = resultSet.getString("status");

                    sortData.add(orderDate+" "+status);
                }
            }

            for (String ord : sortData) {
                System.out.println(ord + " ");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void getOrderDetails(int orderID) {
//
////        int customerId = customer.getCustomerID();
//        List<OrdersData> orderData = new ArrayList<>();
//
//        try (Connection connection = util.DatabaseConnector.openConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(
//                     "SELECT orderID, orderDate, totalAmount FROM Entity.OrdersData WHERE orderID = ?"
//             )) {
//            preparedStatement.setInt(1, orderID);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    int retrievedOrderID = resultSet.getInt("orderID");
//                    String orderDate = resultSet.getString("orderDate");
//                    double totalAmount = resultSet.getDouble("totalAmount");
//
//                    Entity.OrdersData order = new Entity.OrdersData(retrievedOrderID,null, orderDate, totalAmount);
//                    orderData.add(order);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        // Process the list of order details as needed (print or return)
//        for (Entity.OrdersData order : orderData) {
//            System.out.println(order);
//        }
//    }

    public String toString() {
        return "Orders Data: " +
                "orderID='"+orderID+'\''+
                "customerID='" + customer.getCustomerID() + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", totalAmount='" + totalAmount + '\'' ;
    }



    public static void main(String[] args) {
//        Entity.OrdersData od = new Entity.OrdersData(1, new Entity.Customers(1,"Abhi","kona","abhi@gmail.com","3812321","12 street"),"22-12-2023",4213,"pending");
//        Entity.OrdersData od1 = new Entity.OrdersData(2, new Entity.Customers(3,"sandep","setti","sandeep@gmail.com","32121","123 street"),"23-12-2023",231);
////        od.orderEntry();
////        od1.orderEntry();
////        od.calculateTotalAmount(1);
//        od.getOrderDetails(1);
        OrdersData od = new OrdersData(4,new Customers(4,"hari","dev","hari@gmail.com","3727312","fdauf"),"22-12-2023",3213,"pending");
//        od.orderEntry();

//        od.updateOrderStatus(4,"completed");
//            od.cancelOrder(4);
        od.sortingData();
    }
}




