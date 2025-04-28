import java.util.*;

// 1. FoodItem class
class FoodItem {
    private int id;
    private String name;
    private double price;
    
    public FoodItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public String toString() {
        return "Food Item ID: " + id + ", Name: " + name + ", Price: Rs. " + price;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof FoodItem)) return false;
        FoodItem other = (FoodItem) obj;
        return this.id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

// 2. User (Base Class)
class User {
    protected int userId;
    protected String username;
    protected long contactNo;
    
    public User(int userId, String username, long contactNo) {
        this.userId = userId;
        this.username = username;
        this.contactNo = contactNo;
    }
    
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public long getContactNo() { return contactNo; }
    
    @Override
    public String toString() {
        return username;
    }
}

// 3. Cart class
class Cart {
    private Map<FoodItem, Integer> items = new HashMap<>();
    
    public void addItem(FoodItem foodItem, int quantity) {
        items.put(foodItem, items.getOrDefault(foodItem, 0) + quantity);
    }
    
    public void removeItem(FoodItem foodItem) {
        items.remove(foodItem);
    }
    
    public Map<FoodItem, Integer> getItems() {
        return items;
    }
    
    @Override
    public String toString() {
        if(items.isEmpty()) {
            return "Cart is empty.";
        }
        StringBuilder sb = new StringBuilder();
        double totalCost = 0;
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            double cost = entry.getKey().getPrice() * entry.getValue();
            sb.append("Food Item: ").append(entry.getKey().getName())
              .append(", Quantity: ").append(entry.getValue())
              .append(", Cost: Rs. ").append(cost).append("\n");
            totalCost += cost;
        }
        sb.append("Total Cost: Rs. ").append(totalCost);
        return sb.toString();
    }
}

// 4. Customer (Inherits from User)
class Customer extends User {
    private Cart cart;
    
    public Customer(int userId, String username, long contactNo) {
        super(userId, username, contactNo);
        this.cart = new Cart();
    }
    
    public Cart getCart() {
        return cart;
    }
}

// 5. DeliveryPerson class
class DeliveryPerson {
    private int deliveryPersonId;
    private String name;
    private long contactNo;
    
    public DeliveryPerson(int deliveryPersonId, String name, long contactNo) {
        this.deliveryPersonId = deliveryPersonId;
        this.name = name;
        this.contactNo = contactNo;
    }
    
    public int getDeliveryPersonId() { return deliveryPersonId; }
    public String getName() { return name; }
    public long getContactNo() { return contactNo; }
    
    @Override
    public String toString() {
        return name;
    }
}

// 6. Restaurant class
class Restaurant {
    private int id;
    private String name;
    private List<FoodItem> menu;
    
    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
        this.menu = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public List<FoodItem> getMenu() { return menu; }
    
    public void addFoodItem(FoodItem item) {
        menu.add(item);
    }
    
    public void removeFoodItem(int foodItemId) {
        menu.removeIf(item -> item.getId() == foodItemId);
    }
    
    @Override
    public String toString() {
        return "Restaurant ID: " + id + ", Name: " + name;
    }
}

// 7. Order class
class Order {
    private int orderId;
    private Customer customer;
    private Map<FoodItem, Integer> items;
    private String status;
    private DeliveryPerson deliveryPerson;
    private String deliveryAddress; // not used in sample but defined per spec
    
    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new HashMap<>(customer.getCart().getItems());
        this.status = "Pending";
        this.deliveryAddress = "";
    }
    
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public Map<FoodItem, Integer> getItems() { return items; }
    public String getStatus() { return status; }
    public DeliveryPerson getDeliveryPerson() { return deliveryPerson; }
    
    public void setStatus(String status) { this.status = status; }
    public void setDeliveryPerson(DeliveryPerson dp) { this.deliveryPerson = dp; }
    public void setDeliveryAddress(String address) { this.deliveryAddress = address; }
    
    @Override
    public String toString() {
        String dpName = (deliveryPerson != null) ? deliveryPerson.getName() : "Not Assigned";
        return "Order{orderId=" + orderId + ", customer=" + customer.getUsername() +
               ", items=" + items + ", status='" + status + "', deliveryPerson=" + dpName + "}";
    }
}

// Main class with driver program
public class FoodDeliverySystem {
    
    // Collections for system data
    static Map<Integer, Restaurant> restaurants = new HashMap<>();
    static Map<Integer, Customer> customers = new HashMap<>();
    static Map<Integer, Order> orders = new HashMap<>();
    static Map<Integer, DeliveryPerson> deliveryPersons = new HashMap<>();
    static int orderCounter = 1;
    
    static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt();
            if (choice == 1) {
                adminMenu();
            } else if (choice == 2) {
                customerMenu();
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    // Admin module
    private static void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Restaurant");
            System.out.println("2. Add Food Item to Restaurant");
            System.out.println("3. Remove Food Item from Restaurant");
            System.out.println("4. View Restaurants and Menus");
            System.out.println("5. View Orders");
            System.out.println("6. Add Delivery Person");
            System.out.println("7. Assign Delivery Person to Order");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    addRestaurant();
                    break;
                case 2:
                    addFoodItemToRestaurant();
                    break;
                case 3:
                    removeFoodItemFromRestaurant();
                    break;
                case 4:
                    viewRestaurantsAndMenus();
                    break;
                case 5:
                    viewAllOrders();
                    break;
                case 6:
                    addDeliveryPerson();
                    break;
                case 7:
                    assignDeliveryPersonToOrder();
                    break;
                case 8:
                    System.out.println("Exiting Admin Module");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private static void addRestaurant() {
        System.out.print("Enter Restaurant ID: ");
        int id = readInt();
        System.out.print("Enter Restaurant Name: ");
        String name = scanner.nextLine();
        Restaurant r = new Restaurant(id, name);
        restaurants.put(id, r);
        System.out.println("Restaurant added successfully!");
    }
    
    private static void addFoodItemToRestaurant() {
        System.out.print("Enter Restaurant ID: ");
        int rId = readInt();
        Restaurant r = restaurants.get(rId);
        if (r == null) {
            System.out.println("Restaurant not found.");
            return;
        }
        System.out.print("Enter Food Item ID: ");
        int fId = readInt();
        System.out.print("Enter Food Item Name: ");
        String fName = scanner.nextLine();
        System.out.print("Enter Food Item Price: ");
        double price = readDouble();
        FoodItem item = new FoodItem(fId, fName, price);
        r.addFoodItem(item);
        System.out.println("Food item added successfully!");
    }
    
    private static void removeFoodItemFromRestaurant() {
        System.out.print("Enter Restaurant ID: ");
        int rId = readInt();
        Restaurant r = restaurants.get(rId);
        if (r == null) {
            System.out.println("Restaurant not found.");
            return;
        }
        System.out.print("Enter Food Item ID: ");
        int fId = readInt();
        r.removeFoodItem(fId);
        System.out.println("Food item removed successfully!");
    }
    
    private static void viewRestaurantsAndMenus() {
        System.out.println("Restaurants and Menus:");
        for (Restaurant r : restaurants.values()) {
            System.out.println(r);
            for (FoodItem item : r.getMenu()) {
                System.out.println("- " + item);
            }
            System.out.println();
        }
    }
    
    private static void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
        } else {
            System.out.println("Orders:");
            for (Order order : orders.values()) {
                System.out.println(order);
            }
        }
    }
    
    private static void addDeliveryPerson() {
        System.out.print("Enter Delivery Person ID: ");
        int id = readInt();
        System.out.print("Enter Delivery Person Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact No.: ");
        long contact = readLong();
        DeliveryPerson dp = new DeliveryPerson(id, name, contact);
        deliveryPersons.put(id, dp);
        System.out.println("Delivery person added successfully!");
    }
    
    private static void assignDeliveryPersonToOrder() {
        System.out.print("Enter Order ID: ");
        int oId = readInt();
        Order order = orders.get(oId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        System.out.print("Enter Delivery Person ID: ");
        int dpId = readInt();
        DeliveryPerson dp = deliveryPersons.get(dpId);
        if (dp == null) {
            System.out.println("Delivery person not found.");
            return;
        }
        order.setDeliveryPerson(dp);
        System.out.println("Delivery person assigned to order successfully!");
    }
    
    // Customer module
    private static void customerMenu() {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Add Customer");
            System.out.println("2. View Food Items");
            System.out.println("3. Add Food to Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Place Order");
            System.out.println("6. View Orders");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt();
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewRestaurantsAndMenus();
                    break;
                case 3:
                    addFoodToCart();
                    break;
                case 4:
                    viewCart();
                    break;
                case 5:
                    placeOrder();
                    break;
                case 6:
                    viewCustomerOrders();
                    break;
                case 7:
                    System.out.println("Exiting Customer Module");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private static void addCustomer() {
        System.out.print("Enter User ID: ");
        int id = readInt();
        System.out.print("Enter Username: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact No.: ");
        long contact = readLong();
        Customer c = new Customer(id, name, contact);
        customers.put(id, c);
        System.out.println("Customer created successfully!");
    }
    
    private static void addFoodToCart() {
        System.out.print("Enter Customer ID: ");
        int cId = readInt();
        Customer customer = customers.get(cId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.print("Enter Restaurant ID: ");
        int rId = readInt();
        Restaurant r = restaurants.get(rId);
        if (r == null) {
            System.out.println("Restaurant not found.");
            return;
        }
        System.out.print("Enter Food Item ID: ");
        int fId = readInt();
        FoodItem selectedItem = null;
        for (FoodItem item : r.getMenu()) {
            if (item.getId() == fId) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem == null) {
            System.out.println("Food item not found.");
            return;
        }
        System.out.print("Enter Quantity: ");
        int quantity = readInt();
        customer.getCart().addItem(selectedItem, quantity);
        System.out.println("Food item added to cart!");
    }
    
    private static void viewCart() {
        System.out.print("Enter Customer ID: ");
        int cId = readInt();
        Customer customer = customers.get(cId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.println("Cart:");
        System.out.println(customer.getCart());
    }
    
    private static void placeOrder() {
        System.out.print("Enter Customer ID: ");
        int cId = readInt();
        Customer customer = customers.get(cId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Cart is empty. Please add food items to cart.");
            return;
        }
        Order order = new Order(orderCounter++, customer);
        orders.put(order.getOrderId(), order);
        // Clear customer's cart after placing order
        customer.getCart().getItems().clear();
        System.out.println("Order placed successfully! Your order ID is: " + order.getOrderId());
    }
    
    private static void viewCustomerOrders() {
        System.out.print("Enter Customer ID: ");
        int cId = readInt();
        Customer customer = customers.get(cId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.println("Orders:");
        boolean found = false;
        for (Order order : orders.values()) {
            if (order.getCustomer().equals(customer)) {
                System.out.println(order);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders found for this customer.");
        }
    }
    
    // Utility methods to safely read integers, doubles, and longs
    private static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    private static double readDouble() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Double.parseDouble(line.trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    private static long readLong() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Long.parseLong(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
