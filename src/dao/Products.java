package dao;

import Entity.Inventory;
import Exceptions.DuplicateFoundException;
import util.DatabaseConnector;

import java.sql.*;

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





}
