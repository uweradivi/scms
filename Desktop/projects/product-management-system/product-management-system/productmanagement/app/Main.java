package productmanagement.app;

import productmanagement.exception.InvalidPriceException;
import productmanagement.model.*;
import productmanagement.service.ProductManager;

import java.util.Scanner;


public class Main {

    static Scanner       scanner;
    static ProductManager manager;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        printBanner();
        System.out.print("  Enter your store name (press Enter for 'SmartStore'): ");
        String storeName = scanner.nextLine().trim();
        if (storeName.isEmpty()) storeName = "SmartStore";

        System.out.println("\n  Loading saved data...");
        manager = new ProductManager(storeName);
        System.out.println("  Welcome to " + storeName + "!\n");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": productMenu();   break;
                case "2": customerMenu();  break;
                case "3": salesMenu();     break;
                case "4": reportsMenu();   break;
                case "0": running = false; break;
                default:  System.out.println("  [!] Invalid option. Choose 0-4.");
            }
        }

        System.out.println("\n  All data saved. Goodbye!\n");
        scanner.close();
    }

    static void printMainMenu() {
        System.out.println("\n  ==========================================");
        System.out.println("              MAIN MENU");
        System.out.println("  ==========================================");
        System.out.println("   1.  Product Management");
        System.out.println("   2.  Customer Management");
        System.out.println("   3.  Sales");
        System.out.println("   4.  Reports & Logs");
        System.out.println("   0.  Exit");
        System.out.println("  ==========================================");
        System.out.print("   Choose: ");
    }

    static void productMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ---- Product Management ----------------");
            System.out.println("   1. Add Electronics");
            System.out.println("   2. Add Food Product");
            System.out.println("   3. Add Clothing");
            System.out.println("   4. View All Products");
            System.out.println("   5. View Products by Category");
            System.out.println("   6. Search Product by ID");
            System.out.println("   7. Restock a Product");
            System.out.println("   8. Update Product Price");
            System.out.println("   9. Remove a Product");
            System.out.println("   0. Back to Main Menu");
            System.out.print("   Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1": addElectronics();            break;
                case "2": addFood();                   break;
                case "3": addClothing();               break;
                case "4": manager.displayAllProducts(); break;
                case "5": viewByCategory();            break;
                case "6": searchById();                break;
                case "7": restockProduct();            break;
                case "8": updatePrice();               break;
                case "9": removeProduct();             break;
                case "0": back = true;                 break;
                default:  System.out.println("  [!] Invalid option.");
            }
        }
    }

    static void addElectronics() {
        System.out.println("\n  -- Add Electronics --");
        try {
            String id       = prompt("  Product ID       : ");
            String name     = prompt("  Product Name     : ");
            double price    = parseDouble(prompt("  Price ($)        : "));
            int    stock    = parseInt(prompt("  Stock (units)    : "));
            int    warranty = parseInt(prompt("  Warranty (years) : "));
            manager.addProduct(new Electronics(id, name, price, stock, warranty));
        } catch (InvalidPriceException e) {
            System.out.println("  [!] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number entered. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void addFood() {
        System.out.println("\n  -- Add Food Product --");
        try {
            String id     = prompt("  Product ID       : ");
            String name   = prompt("  Product Name     : ");
            double price  = parseDouble(prompt("  Price ($)        : "));
            int    stock  = parseInt(prompt("  Stock (units)    : "));
            String expiry = prompt("  Expiry Date (YYYY-MM-DD): ");
            manager.addProduct(new Food(id, name, price, stock, expiry));
        } catch (InvalidPriceException e) {
            System.out.println("  [!] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number entered. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void addClothing() {
        System.out.println("\n  -- Add Clothing --");
        try {
            String id       = prompt("  Product ID       : ");
            String name     = prompt("  Product Name     : ");
            double price    = parseDouble(prompt("  Price ($)        : "));
            int    stock    = parseInt(prompt("  Stock (units)    : "));
            String size     = prompt("  Size (S/M/L/XL)  : ");
            String material = prompt("  Material         : ");
            manager.addProduct(new Clothing(id, name, price, stock, size, material));
        } catch (InvalidPriceException e) {
            System.out.println("  [!] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number entered. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    static void viewByCategory() {
        System.out.println("  Available: Electronics | Food & Grocery | Clothing & Apparel");
        String cat = prompt("  Enter category: ");
        manager.displayByCategory(cat);
    }

    static void searchById() {
        String id = prompt("  Enter Product ID: ");
        Product p = manager.findProductById(id);
        if (p != null) p.displayInfo();
        else System.out.println("  [!] Product not found: " + id);
    }

    static void restockProduct() {
        String id = prompt("  Enter Product ID   : ");
        try {
            int qty = parseInt(prompt("  Quantity to add    : "));
            manager.restockProduct(id, qty);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number.");
        }
    }

    static void updatePrice() {
        String id = prompt("  Enter Product ID   : ");
        try {
            double price = parseDouble(prompt("  New Price ($)      : "));
            manager.updateProductPrice(id, price);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number.");
        }
    }

    static void removeProduct() {
        String id = prompt("  Enter Product ID to remove: ");
        System.out.print("  Are you sure? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            manager.removeProduct(id);
        } else {
            System.out.println("  Cancelled.");
        }
    }

    static void customerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ---- Customer Management ---------------");
            System.out.println("   1. Register New Customer");
            System.out.println("   2. View All Customers");
            System.out.println("   3. Search Customer by ID");
            System.out.println("   4. Top Up Wallet");
            System.out.println("   0. Back to Main Menu");
            System.out.print("   Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1": registerCustomer();          break;
                case "2": manager.displayAllCustomers(); break;
                case "3": searchCustomer();            break;
                case "4": topUpWallet();               break;
                case "0": back = true;                 break;
                default:  System.out.println("  [!] Invalid option.");
            }
        }
    }

    static void registerCustomer() {
        System.out.println("\n  -- Register Customer --");
        try {
            String id      = prompt("  Customer ID      : ");
            String name    = prompt("  Full Name        : ");
            String email   = prompt("  Email Address    : ");
            double wallet  = parseDouble(prompt("  Initial Wallet ($): "));
            manager.addCustomer(new Customer(id, name, email, wallet));
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount entered.");
        }
    }

    static void searchCustomer() {
        String id = prompt("  Enter Customer ID: ");
        Customer c = manager.findCustomerById(id);
        if (c != null) c.showProfile();
        else System.out.println("  [!] Customer not found: " + id);
    }

    static void topUpWallet() {
        String id = prompt("  Customer ID      : ");
        try {
            double amount = parseDouble(prompt("  Top-up amount ($): "));
            manager.topUpCustomerWallet(id, amount);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount.");
        }
    }

    static void salesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ---- Sales ---------------------------------");
            System.out.println("   1. Process a Sale");
            System.out.println("   2. View Sales Log");
            System.out.println("   0. Back to Main Menu");
            System.out.print("   Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1": processSale();          break;
                case "2": manager.printSalesLog(); break;
                case "0": back = true;            break;
                default:  System.out.println("  [!] Invalid option.");
            }
        }
    }

    static void processSale() {
        System.out.println("\n  -- Process Sale --");
        try {
            String custId = prompt("  Customer ID  : ");
            String prodId = prompt("  Product ID   : ");
            int    qty    = parseInt(prompt("  Quantity     : "));
            manager.processSale(custId, prodId, qty);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid quantity entered.");
        }
    }

    static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ---- Reports & Logs --------------------");
            System.out.println("   1. Inventory Summary");
            System.out.println("   2. Low Stock Alert");
            System.out.println("   3. View All Products");
            System.out.println("   4. Sales Log");
            System.out.println("   0. Back to Main Menu");
            System.out.print("   Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1": manager.printInventorySummary();  break;
                case "2": manager.printLowStockAlert(5);    break;
                case "3": manager.displayAllProducts();     break;
                case "4": manager.printSalesLog();          break;
                case "0": back = true;                      break;
                default:  System.out.println("  [!] Invalid option.");
            }
        }
    }

    static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    static double parseDouble(String s) {
        return Double.parseDouble(s);
    }

    static int parseInt(String s) {
        return Integer.parseInt(s);
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  ==========================================");
        System.out.println("      PRODUCT MANAGEMENT SYSTEM ");
        System.out.println("  ==========================================");
        System.out.println("   Data is saved automatically to:");
        System.out.println("     data/products.csv");
        System.out.println("     data/customers.csv");
        System.out.println("     data/sales_log.txt");
        System.out.println("  ==========================================");
        System.out.println();
    }
}
