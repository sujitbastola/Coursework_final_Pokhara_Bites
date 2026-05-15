package com.restaurant.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnection {
    private static final String DB_NAME = "pokhara_bites";
    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final HikariDataSource DATA_SOURCE;

    static {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_DB_URL);
        String username = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        createDatabaseAndTables(username, password);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setInitializationFailTimeout(-1);
        config.setPoolName("PokharaBitesPool");
        DATA_SOURCE = new HikariDataSource(config);
    }

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    private static void createDatabaseAndTables(String username, String password) {
        String serverUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection server = DriverManager.getConnection(serverUrl, username, password);
             Statement st = server.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (SQLException e) {
            return;
        }

        try (Connection con = DriverManager.getConnection(DEFAULT_DB_URL, username, password);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "email VARCHAR(150) NOT NULL UNIQUE,"
                    + "password_hash VARCHAR(255) NOT NULL,"
                    + "role ENUM('admin','customer') NOT NULL DEFAULT 'customer',"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS restaurants ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(120) NOT NULL,"
                    + "address VARCHAR(255) NOT NULL,"
                    + "phone VARCHAR(20) NOT NULL,"
                    + "cuisine_type VARCHAR(80) NOT NULL,"
                    + "opening_hours VARCHAR(100) NOT NULL,"
                    + "image_url VARCHAR(500),"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS menu_items ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "restaurant_id INT NOT NULL,"
                    + "name VARCHAR(120) NOT NULL,"
                    + "description VARCHAR(500),"
                    + "price DECIMAL(10,2) NOT NULL,"
                    + "category ENUM('veg','non-veg') NOT NULL,"
                    + "availability BOOLEAN NOT NULL DEFAULT TRUE,"
                    + "CONSTRAINT fk_menu_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS orders ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "user_id INT NOT NULL,"
                    + "restaurant_id INT NOT NULL,"
                    + "total_amount DECIMAL(10,2) NOT NULL,"
                    + "status VARCHAR(40) NOT NULL DEFAULT 'PLACED',"
                    + "order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),"
                    + "CONSTRAINT fk_order_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS order_items ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "order_id INT NOT NULL,"
                    + "menu_item_id INT NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "price DECIMAL(10,2) NOT NULL,"
                    + "CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,"
                    + "CONSTRAINT fk_order_item_menu FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)"
                    + ")");

            seed(st);
        } catch (SQLException ignored) {
            // Normal DAO calls will show a friendly page-level error if MySQL is unavailable.
        }
    }

    private static boolean isEmpty(Statement st, String table) throws SQLException {
        try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    private static void seed(Statement st) throws SQLException {
        st.executeUpdate("INSERT INTO users(name, email, password_hash, role) "
                + "SELECT 'Admin User', 'admin@pokhara.com', 'cG9raGFyYS1iaXRlcy0wMA==:mvAsiUm0Oy1yRCetZoIwm6zgHPHqJvScbygdMSh1tVQ=', 'admin' "
                + "WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@pokhara.com')");

        addRestaurant(st, "Lakeside Thakali Kitchen", "Baidam Road, Lakeside, Pokhara", "+977 9745450737", "Thakali and Nepali", "8:00 AM - 10:00 PM");
        addRestaurant(st, "Phewa Momo House", "Hallan Chowk, Pokhara", "+977 9745450737", "Momo and Snacks", "10:00 AM - 9:30 PM");
        addRestaurant(st, "Sarangkot Grill Cafe", "Sarangkot View Road, Pokhara", "+977 9745450737", "Grill and Cafe", "7:00 AM - 10:30 PM");
        addRestaurant(st, "Newari Khaja Ghar", "Chipledhunga, Pokhara", "+977 9745450737", "Newari Khaja", "9:00 AM - 9:00 PM");
        addRestaurant(st, "Begnas Fish Corner", "Begnas Lake Road, Pokhara", "+977 9745450737", "Fish and Local Food", "10:00 AM - 8:30 PM");
        addRestaurant(st, "Daal Bhat Station", "Mahendrapul, Pokhara", "+977 9745450737", "Nepali Meals", "7:30 AM - 9:30 PM");
        addRestaurant(st, "Gurung Kitchen", "Rambazar, Pokhara", "+977 9745450737", "Gurung and Local", "8:00 AM - 9:00 PM");
        addRestaurant(st, "Tibetan Laphing Hub", "Lakeside Street 13, Pokhara", "+977 9745450737", "Tibetan Snacks", "11:00 AM - 8:00 PM");
        addRestaurant(st, "Himalayan Tea and Lassi", "Prithvi Chowk, Pokhara", "+977 9745450737", "Local Drinks", "7:00 AM - 8:00 PM");
        addRestaurant(st, "Pokhara Sel Roti Pasal", "Srijana Chowk, Pokhara", "+977 9745450737", "Breakfast and Sweets", "6:30 AM - 7:30 PM");

        addItem(st, "Lakeside Thakali Kitchen", "Chicken Thakali Set", "Rice, dal, curry, greens, pickles, gundruk, and chicken curry.", 520, "non-veg");
        addItem(st, "Lakeside Thakali Kitchen", "Veg Thakali Set", "Vegetarian Thakali platter with local greens, achar, and gundruk.", 390, "veg");
        addItem(st, "Lakeside Thakali Kitchen", "Fapar Roti", "Buckwheat roti served with achar and local butter.", 180, "veg");
        addItem(st, "Lakeside Thakali Kitchen", "Gundruk Soup", "Warm fermented greens soup, simple and local.", 160, "veg");

        addItem(st, "Phewa Momo House", "Buff Momo", "Steamed buff momo with tomato sesame achar.", 220, "non-veg");
        addItem(st, "Phewa Momo House", "Veg Jhol Momo", "Vegetable momo in tangy sesame jhol.", 210, "veg");
        addItem(st, "Phewa Momo House", "Chicken C-Momo", "Fried chicken momo tossed in spicy chili sauce.", 280, "non-veg");
        addItem(st, "Phewa Momo House", "Masala Tea", "Milk tea cooked with Nepali spices.", 70, "veg");

        addItem(st, "Sarangkot Grill Cafe", "Chicken Sekuwa", "Charcoal grilled chicken with timur spice.", 420, "non-veg");
        addItem(st, "Sarangkot Grill Cafe", "Paneer Sizzler", "Paneer with vegetables and pepper sauce.", 480, "veg");
        addItem(st, "Sarangkot Grill Cafe", "Himalayan Coffee", "Fresh brewed hot coffee.", 140, "veg");
        addItem(st, "Sarangkot Grill Cafe", "Aloo Sadeko", "Spicy potato salad with mustard oil and sesame.", 150, "veg");

        addItem(st, "Newari Khaja Ghar", "Samay Baji", "Beaten rice, bara, achar, potato, and local spices.", 350, "non-veg");
        addItem(st, "Newari Khaja Ghar", "Bara", "Lentil pancake cooked Newari style.", 120, "veg");
        addItem(st, "Newari Khaja Ghar", "Yomari", "Steamed rice flour sweet with chaku filling.", 130, "veg");
        addItem(st, "Newari Khaja Ghar", "Chatamari", "Newari rice crepe with egg and toppings.", 240, "non-veg");

        addItem(st, "Begnas Fish Corner", "Begnas Fried Fish", "Local lake fish fried with Nepali masala.", 520, "non-veg");
        addItem(st, "Begnas Fish Corner", "Fish Curry Rice", "Fish curry served with rice, dal, and achar.", 480, "non-veg");
        addItem(st, "Begnas Fish Corner", "Poleko Makai", "Roasted corn with salt, chili, and lemon.", 90, "veg");
        addItem(st, "Begnas Fish Corner", "Lemon Soda", "Fresh lemon soda with black salt.", 100, "veg");

        addItem(st, "Daal Bhat Station", "Daal Bhat Tarkari", "Rice, lentil soup, vegetable curry, achar, and greens.", 280, "veg");
        addItem(st, "Daal Bhat Station", "Chicken Daal Bhat", "Classic daal bhat with chicken curry.", 420, "non-veg");
        addItem(st, "Daal Bhat Station", "Curd Chiura", "Beaten rice with fresh curd and banana.", 180, "veg");
        addItem(st, "Daal Bhat Station", "Chiya", "Nepali milk tea.", 50, "veg");

        addItem(st, "Gurung Kitchen", "Gurung Bread Set", "Gurung bread with curry and achar.", 260, "veg");
        addItem(st, "Gurung Kitchen", "Kodo Ko Dhido", "Millet dhido served with gundruk and curry.", 340, "veg");
        addItem(st, "Gurung Kitchen", "Local Chicken Curry", "Village-style chicken curry with rice.", 520, "non-veg");
        addItem(st, "Gurung Kitchen", "Moh Dahi", "Fresh local curd drink.", 120, "veg");

        addItem(st, "Tibetan Laphing Hub", "Yellow Laphing", "Cold spicy mung bean noodle roll.", 160, "veg");
        addItem(st, "Tibetan Laphing Hub", "Tingmo with Aloo", "Steamed bread with potato curry.", 210, "veg");
        addItem(st, "Tibetan Laphing Hub", "Thenthuk", "Hand-pulled noodle soup with vegetables.", 260, "veg");
        addItem(st, "Tibetan Laphing Hub", "Butter Tea", "Warm salty Tibetan butter tea.", 110, "veg");

        addItem(st, "Himalayan Tea and Lassi", "Matka Chiya", "Milk tea served in clay cup style.", 80, "veg");
        addItem(st, "Himalayan Tea and Lassi", "Sweet Lassi", "Fresh yogurt drink with sugar and cardamom.", 150, "veg");
        addItem(st, "Himalayan Tea and Lassi", "Mohito Pudina", "Mint lemon cooler, local style.", 140, "veg");
        addItem(st, "Himalayan Tea and Lassi", "Sattu Drink", "Roasted gram flour drink with lemon and black salt.", 130, "veg");

        addItem(st, "Pokhara Sel Roti Pasal", "Sel Roti Set", "Crispy rice flour ring served with achar.", 120, "veg");
        addItem(st, "Pokhara Sel Roti Pasal", "Aloo Chop", "Potato fritter with chutney.", 80, "veg");
        addItem(st, "Pokhara Sel Roti Pasal", "Jeri Swari", "Sweet jeri served with soft swari bread.", 140, "veg");
        addItem(st, "Pokhara Sel Roti Pasal", "Kheer", "Rice pudding with milk, cardamom, and nuts.", 160, "veg");
    }

    private static void addRestaurant(Statement st, String name, String address, String phone, String cuisine, String hours) throws SQLException {
        String sql = "INSERT INTO restaurants(name, address, phone, cuisine_type, opening_hours, image_url) "
                + "SELECT '" + q(name) + "', '" + q(address) + "', '" + q(phone) + "', '" + q(cuisine) + "', '" + q(hours) + "', '' "
                + "WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name='" + q(name) + "')";
        st.executeUpdate(sql);
    }

    private static void addItem(Statement st, String restaurant, String name, String description, int price, String category) throws SQLException {
        String sql = "INSERT INTO menu_items(restaurant_id, name, description, price, category, availability) "
                + "SELECT r.id, '" + q(name) + "', '" + q(description) + "', " + price + ", '" + q(category) + "', TRUE "
                + "FROM restaurants r WHERE r.name='" + q(restaurant) + "' "
                + "AND NOT EXISTS (SELECT 1 FROM menu_items mi WHERE mi.restaurant_id=r.id AND mi.name='" + q(name) + "')";
        st.executeUpdate(sql);
    }

    private static String q(String value) {
        return value.replace("'", "''");
    }

}
