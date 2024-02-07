package Entity;

import util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void orderDetailsEntry() {
        try (Connection connection = DatabaseConnector.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Entity.OrderDetails (orderDetailId, orderId, productId, quantity) VALUES (?, ?, ?, ?)"
             )) {
            preparedStatement.setInt(1,orderDetailID);
            preparedStatement.setInt(2, order.getOrderID());
            preparedStatement.setInt(3, product.getProductID());
            preparedStatement.setInt(4, quantity);

            preparedStatement.executeUpdate();
            System.out.println("order deatils mentioned");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("something went wrong");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public void updateQuantity(int orderDetailID, int quantity){
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update Entity.OrderDetails set quantity = ? where orderDetailId = ?"
        )) {
            preparedStatement.setInt(1,quantity);
            preparedStatement.setInt(2,orderDetailID);
            int rowsaffected = preparedStatement.executeUpdate();
            if(rowsaffected>0){
                System.out.println("Quantity updated successfully");
            }
            else
                System.out.println("check the data that you have enetered");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void calSubTotal(int orderDetailID){
        double total = 0;
        try(Connection connection = DatabaseConnector.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "select sum(p.price) as total from products p join orderDetails o on o.orderDetailID = o.productID where o.orderDetailId = ? group by p.price"
        )) {
            preparedStatement.setInt(1,orderDetailID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getDouble("total");
                }
            }
            System.out.println(total);

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void getOrderDetails(int orderDetailID){
        List<Integer>ord = new ArrayList<>();
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select orderDetailID, quantity from Entity.OrderDetails where orderDetailID = ? "
        )) {
            preparedStatement.setInt(1,orderDetailID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int orderDetailId = resultSet.getInt("orderDetailId");
                    int quantity = resultSet.getInt("quantity");

                    ord.add(orderDetailId);
                    ord.add(quantity);
                }
            }

            for(int ordDetails: ord)
                System.out.print(ordDetails+" ");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addDiscount(int orderDetailID, double discountPercentage) {
        int totalQuantity = 0;
        try(Connection connection = DatabaseConnector.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select quantity from Entity.OrderDetails where orderDetailId = ? "
        )){
            preparedStatement.setInt(1,orderDetailID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalQuantity = resultSet.getInt("quantity");
                }
            }

            double discountAmount = totalQuantity * discountPercentage / 100.0;

            System.out.println(discountAmount);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        OrdersData od = new OrdersData(3, new Customers(1,"Abhi","kona","abhi@gmail.com","3812321","12 street"),"22-12-2023",4213,"pending");
        Products p1 = new Products(3,"TV","Watching",3183);
       OrderDetails od1 = new OrderDetails(3,od,p1,200);
//       od1.orderDetailsEntry();
//        od1.updateQuantity(1,200);
//        od1.getOrderDetails(1);

//        od1.addDiscount(1,10);
        od1.calSubTotal(1);
    }
}
