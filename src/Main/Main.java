package Main;

import Exceptions.*;
import dao.Inventory;
//import dao.Customers;
import dao.OrderDetails;
import dao.Payments;
import Entity.Products;

import Entity.Customers;

import Entity.OrdersData;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InvalidDataException, InsufficientStockException, PaymentFailedException, DuplicateFoundException {
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        String password = sc.next();

        try {
            if (name.equals("Abhi") && password.equals("4321")) {
                System.out.println("valid user");

                System.out.println("1.Register Customer");
                System.out.println("2.Update Customer");
                System.out.println("3.Is Quantity in Stock");
                System.out.println("4.Update orderStatus");
                System.out.println("5. Remove order");
                System.out.println("6. Register payment");
                System.out.println("7. Update Payment");
                System.out.println("8. isProductAvailable");

                int choice;

                do {
                    System.out.println("Enter your choice (1-8, 0 to exit): ");
                    choice = sc.nextInt();

                    switch (choice) {
                        case 1:
                            Customers c = new Customers(5, "Naveen", "Dumpa", "naveen@gmail.com", "328", "12 street");
                            c.registerCustomer();
                            break;
                        case 2:
                            Customers c1 = new Customers(5, "Naveen", "Dumpa", "naveen@gmail.com", "328", "12 street");
                            c1.updateCustomerInformation(5, "arjun", "ko", "arjun@gmail.com", "321", "34 street");
                            break;
                        case 3:
                            Inventory i = new Inventory(4, new Products(3, "dds", "dsa", 213), 100, "30-10-2022");
                            i.isQuantityInStock(4, 200);
                            break;
                        case 4:
                            OrdersData od = new Entity.OrdersData(4, new Customers(5, "Naveen", "Dumpa", "naveen@gmail.com", "328", "12 street"), "10-12-2024", 42312, "sent");
                            od.updateOrderStatus(1, "pending");
                            break;
                        case 5:
                            OrdersData od1 = new Entity.OrdersData(4, new Customers(5, "Naveen", "Dumpa", "naveen@gmail.com", "328", "12 street"), "10-12-2024", 42312, "sent");
                            od1.cancelOrder(4);
                            break;
                        case 6:
                            Payments payment = new Payments(3, new Products(3, "TV", "Watching", 3183), 2222, "completed");
                            payment.registerPayments();
                            break;
                        case 7:
                            Payments payment1 = new Payments(3, new Products(3, "TV", "Watching", 3183), 2222, "completed");
                            payment1.updatePayment(3, 432, "completed");
                            break;
                        case 8:
                            OrderDetails od3 = new OrderDetails(4, null, null, 100);
                            od3.isProductAvailable(4, 20);
                            break;
                        case 0:
                            System.out.println("Exiting the program. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 0 and 8.");
                    }
                } while (choice != 0);


            } else {
                throw new AuthenticationException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }



//        Customers c = new Customers(5,"naveen","dumpa","naveen@gmail.com","82818218","43 street");
//        c.registerCustomer();

//        Inventory i = new Inventory(1,new Products(1,"TV","Watching",3183),201,"2023-10-1");
//        i.isQuantityInStock(1,100);

//        Payments payment = new Payments(1,new Products(1,"TV","Watching",3183),2222,"completed");
//        payment.registerPayments();

//
//        Products p = new Products(1,"TV","Watching",3183);
//        p.productEntry();


    }
}
