package productmanagement.util;

import productmanagement.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR      = "data/";
    private static final String PRODUCTS_FILE = DATA_DIR + "products.csv";
    private static final String CUSTOMERS_FILE= DATA_DIR + "customers.csv";
    private static final String SALES_LOG     = DATA_DIR + "sales_log.txt";

    public FileManager() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void saveProducts(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            bw.write("# Product Management System — Products File");
            bw.newLine();
            bw.write("# Format: TYPE,id,name,price,stock,[extra fields]");
            bw.newLine();
            for (Product p : products) {
                bw.write(p.toCsv());
                bw.newLine();
            }
            System.out.println("  ✔ Products saved to " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.out.println("  ✘ Error saving products: " + e.getMessage());
        }
    }

    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) return products;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                try {
                    String type = parts[0].toUpperCase();
                    switch (type) {
                        case "ELECTRONICS":

                            products.add(new Electronics(
                                    parts[1], parts[2],
                                    Double.parseDouble(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    Integer.parseInt(parts[5])));
                            break;
                        case "FOOD":

                            products.add(new Food(
                                    parts[1], parts[2],
                                    Double.parseDouble(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    parts[5]));
                            break;
                        case "CLOTHING":

                            products.add(new Clothing(
                                    parts[1], parts[2],
                                    Double.parseDouble(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    parts[5], parts[6]));
                            break;
                        default:
                            System.out.println("  ⚠ Unknown product type on line " + lineNum + ": " + type);
                    }
                } catch (Exception e) {
                    System.out.println("  ⚠ Skipping malformed line " + lineNum + ": " + e.getMessage());
                }
            }
            System.out.println("  ✔ Loaded " + products.size() + " product(s) from " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.out.println("  ✘ Error loading products: " + e.getMessage());
        }
        return products;
    }

    public void saveCustomers(List<Customer> customers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            bw.write("# Product Management System — Customers File");
            bw.newLine();
            bw.write("# Format: id,name,email,walletBalance");
            bw.newLine();
            for (Customer c : customers) {
                bw.write(c.toCsv());
                bw.newLine();
            }
            System.out.println("  ✔ Customers saved to " + CUSTOMERS_FILE);
        } catch (IOException e) {
            System.out.println("  ✘ Error saving customers: " + e.getMessage());
        }
    }

    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return customers;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                try {
                    customers.add(new Customer(
                            parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3])));
                } catch (Exception e) {
                    System.out.println("  ⚠ Skipping malformed customer line " + lineNum + ": " + e.getMessage());
                }
            }
            System.out.println("  ✔ Loaded " + customers.size() + " customer(s) from " + CUSTOMERS_FILE);
        } catch (IOException e) {
            System.out.println("  ✘ Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    public void logSale(String customerName, String productName, int qty, double total) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SALES_LOG, true))) {
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write(timestamp + " | Customer: " + customerName
                    + " | Product: " + productName
                    + " | Qty: " + qty
                    + " | Total: $" + String.format("%.2f", total));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  ⚠ Could not write to sales log: " + e.getMessage());
        }
    }

    public void printSalesLog() {
        File file = new File(SALES_LOG);
        if (!file.exists() || file.length() == 0) {
            System.out.println("  No sales recorded yet.");
            return;
        }
        System.out.println("\n  ── Sales Log ──────────────────────────────────");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("  ✘ Error reading sales log: " + e.getMessage());
        }
        System.out.println("  ────────────────────────────────────────────────");
    }
}
