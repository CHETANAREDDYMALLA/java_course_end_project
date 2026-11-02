import java.io.*;
import java.util.*;
public class ShoppingCartSystem {
    static class Product implements Serializable {
        private String id;
        private String name;
        private String category;
        private double price;
        public Product(String id, String name, String category, double price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
        }
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getCategory() {
            return category;
        }
        public double getPrice() {
            return price;
        }
        @Override
        public String toString() {
            return id + " | " + name + " | " + category + " | $" + price;
        }
    }
    static class CartItem implements Serializable {
        private Product product;
        private int quantity;
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        public Product getProduct() {
            return product;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        @Override
        public String toString() {
            return product.getName() + " (Qty: " + quantity + ") - $" + (product.getPrice() * quantity);
        }
    }
    @FunctionalInterface
    interface ProductFilter {
        boolean test(Product p);
    }
    public static List<Product> filterProducts(Collection<Product> products, ProductFilter filter) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (filter.test(p)) {
                result.add(p);
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Map<String, Product> productCatalog = new HashMap<>();
        productCatalog.put("P1", new Product("P1", "Laptop", "Electronics", 1200.0));
        productCatalog.put("P2", new Product("P2", "Headphones", "Electronics", 150.0));
        productCatalog.put("P3", new Product("P3", "Coffee Maker", "Home", 80.0));
        productCatalog.put("P4", new Product("P4", "Desk Chair", "Furniture", 200.0));
        productCatalog.put("P5", new Product("P5", "Notebook", "Stationery", 5.0));
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(productCatalog.get("P1"), 1));
        cart.add(new CartItem(productCatalog.get("P3"), 2));
        System.out.println("=== INITIAL CART ===");
        for (CartItem item : cart) {
            System.out.println(item);
        }
        ProductFilter priceFilter = (p) -> p.getPrice() < 100.0;
        List<Product> cheapProducts = filterProducts(productCatalog.values(), priceFilter);
        System.out.println("\n=== PRODUCTS UNDER $100 ===");
        for (Product p : cheapProducts) {
            System.out.println(p);
        }
        ProductFilter categoryFilter = (p) -> p.getCategory().equalsIgnoreCase("Electronics");
        List<Product> electronicProducts = filterProducts(productCatalog.values(), categoryFilter);
        System.out.println("\n=== ELECTRONICS PRODUCTS ===");
        for (Product p : electronicProducts) {
            System.out.println(p);
        }
        String filename = "cart_data.ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(cart);
            System.out.println("\nCart successfully serialized to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CartItem> restoredCart = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            restoredCart = (List<CartItem>) ois.readObject();
            System.out.println("\n=== RESTORED CART FROM FILE ===");
            for (CartItem item : restoredCart) {
                System.out.println(item);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("\n=== COLLECTION PERFORMANCE & SUITABILITY COMPARISON ===");
        System.out.println("1. HashMap: Optimal for the product catalog due to O(1) constant time lookups by product ID.");
        System.out.println("2. ArrayList: Optimal for customer shopping carts where sequence preservation and item iteration order are critical.");
    }
}