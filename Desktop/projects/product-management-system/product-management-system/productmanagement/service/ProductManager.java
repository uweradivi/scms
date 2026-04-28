package productmanagement.service;

import productmanagement.util.FileManager;
import productmanagement.exception.InsufficientBalanceException;
import productmanagement.exception.InvalidPriceException;
import productmanagement.exception.OutOfStockException;
import productmanagement.model.*;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private List<Product>  inventory;
    private List<Customer> customers;
    private String         storeName;
    private FileManager fileManager;

    public ProductManager(String storeName) {
        this.storeName   = storeName;
        this.fileManager = new FileManager();
        this.inventory   = new ArrayList<>(fileManager.loadProducts());
        this.customers   = new ArrayList<>(fileManager.loadCustomers());
    }


    public boolean addProduct(Product product) {
        // Prevent duplicate IDs
        if (findProductById(product.getProductId()) != null) {
            System.out.println("  [!] Product ID already exists: " + product.getProductId());
            return false;
        }
        inventory.add(product);
        fileManager.saveProducts(inventory);
        System.out.println("  [+] Added: " + product.getName()
                + " [" + product.getCategory() + "]");
        return true;
    }

    public boolean removeProduct(String productId) {
        boolean removed = inventory.removeIf(p -> p.getProductId().equalsIgnoreCase(productId));
        if (removed) {
            fileManager.saveProducts(inventory);
            System.out.println("  [-] Removed product ID: " + productId);
        } else {
            System.out.println("  [!] Product ID not found: " + productId);
        }
        return removed;
    }

    public Product findProductById(String id) {
        for (Product p : inventory)
            if (p.getProductId().equalsIgnoreCase(id)) return p;
        return null;
    }

    public Product findProductByName(String name) {
        for (Product p : inventory)
            if (p.getName().equalsIgnoreCase(name)) return p;
        return null;
    }

    public void displayAllProducts() {
        System.out.println();
        System.out.println("  ==========================================");
        System.out.printf ("     %s  --  Inventory (%d items)%n",
                storeName, inventory.size());
        System.out.println("  ==========================================");
        if (inventory.isEmpty()) {
            System.out.println("  (No products in inventory)");
            return;
        }
        for (Product p : inventory) p.displayInfo();
    }

    public void displayByCategory(String category) {
        System.out.println("\n  -- Category: " + category + " --");
        boolean found = false;
        for (Product p : inventory) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                p.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("  No products in category: " + category);
    }

    public void restockProduct(String productId, int quantity) {
        Product p = findProductById(productId);
        if (p == null) { System.out.println("  [!] Product not found: " + productId); return; }
        p.restock(quantity);
        fileManager.saveProducts(inventory);
    }

    public void updateProductPrice(String productId, double newPrice) {
        Product p = findProductById(productId);
        if (p == null) { System.out.println("  [!] Product not found: " + productId); return; }
        try {
            p.setPrice(newPrice);
            fileManager.saveProducts(inventory);
            System.out.printf("  [OK] Price updated to $%.2f for %s%n", newPrice, p.getName());
        } catch (InvalidPriceException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }


    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("  [!] Customer ID already exists: " + customer.getCustomerId());
            return false;
        }
        customers.add(customer);
        fileManager.saveCustomers(customers);
        System.out.println("  [+] Customer registered: " + customer.getName());
        return true;
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId().equalsIgnoreCase(id)) return c;
        return null;
    }

    public void displayAllCustomers() {
        System.out.println("\n  -- Registered Customers (" + customers.size() + ") --");
        if (customers.isEmpty()) { System.out.println("  (No customers registered)"); return; }
        for (Customer c : customers) c.showProfile();
    }

    public void topUpCustomerWallet(String customerId, double amount) {
        Customer c = findCustomerById(customerId);
        if (c == null) { System.out.println("  [!] Customer not found: " + customerId); return; }
        try {
            c.topUpWallet(amount);
            fileManager.saveCustomers(customers);
        } catch (IllegalArgumentException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    public void processSale(String customerId, String productId, int qty) {
        Customer customer = findCustomerById(customerId);
        Product  product  = findProductById(productId);

        if (customer == null) { System.out.println("  [!] Customer not found: " + customerId); return; }
        if (product  == null) { System.out.println("  [!] Product not found: "  + productId);  return; }
        if (qty <= 0)         { System.out.println("  [!] Quantity must be at least 1.");        return; }

        try {
            double discountedPrice = product.getPrice() - product.applyDiscount();
            double total = discountedPrice * qty;
            customer.purchase(product, qty);
            fileManager.logSale(customer.getName(), product.getName(), qty, total);
            fileManager.saveProducts(inventory);
            fileManager.saveCustomers(customers);
        } catch (OutOfStockException | InsufficientBalanceException e) {
            System.out.println("\n  [!] " + e.getMessage());
        }
    }

    public void printSalesLog() {
        fileManager.printSalesLog();
    }

    public void printInventorySummary() {
        int elec = 0, food = 0, cloth = 0;
        double totalValue = 0;
        for (Product p : inventory) {
            totalValue += p.getPrice() * p.getStockQuantity();
            if      (p instanceof Electronics) elec++;
            else if (p instanceof Food)        food++;
            else if (p instanceof Clothing)    cloth++;
        }
        System.out.println("\n  ==========================================");
        System.out.println("     INVENTORY SUMMARY");
        System.out.println("  ==========================================");
        System.out.println("  Total products      : " + inventory.size());
        System.out.println("  Electronics         : " + elec  + " items");
        System.out.println("  Food & Grocery      : " + food  + " items");
        System.out.println("  Clothing & Apparel  : " + cloth + " items");
        System.out.printf ("  Total stock value   : $%.2f%n", totalValue);
        System.out.println("  Total customers     : " + customers.size());
    }

    public void printLowStockAlert(int threshold) {
        System.out.println("\n  -- Low Stock Alert (< " + threshold + " units) --");
        boolean found = false;
        for (Product p : inventory) {
            if (p.getStockQuantity() < threshold) {
                System.out.printf("  [!] %-22s [%s]  --  Only %d unit(s) left%n",
                        p.getName(), p.getProductId(), p.getStockQuantity());
                found = true;
            }
        }
        if (!found) System.out.println("  [OK] All products have sufficient stock.");
    }

    public List<Product>  getInventory()      { return inventory; }
    public List<Customer> getCustomers()      { return customers; }
    public String         getStoreName()      { return storeName; }
    public int            getTotalProducts()  { return inventory.size(); }
}
