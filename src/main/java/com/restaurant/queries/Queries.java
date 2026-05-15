package com.restaurant.queries;

public final class Queries {
    private Queries() {}

    public static final String INSERT_USER = "INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)";
    public static final String FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    public static final String COUNT_USERS = "SELECT COUNT(*) FROM users";

    public static final String INSERT_RESTAURANT = "INSERT INTO restaurants(name,address,phone,cuisine_type,opening_hours,image_url) VALUES(?,?,?,?,?,?)";
    public static final String UPDATE_RESTAURANT = "UPDATE restaurants SET name=?,address=?,phone=?,cuisine_type=?,opening_hours=?,image_url=? WHERE id=?";
    public static final String DELETE_RESTAURANT = "DELETE FROM restaurants WHERE id=?";
    public static final String FIND_RESTAURANT = "SELECT * FROM restaurants WHERE id=?";
    public static final String LIST_RESTAURANTS = "SELECT * FROM restaurants ORDER BY name";
    public static final String COUNT_RESTAURANTS = "SELECT COUNT(*) FROM restaurants";

    public static final String INSERT_MENU_ITEM = "INSERT INTO menu_items(restaurant_id,name,description,price,category,availability) VALUES(?,?,?,?,?,?)";
    public static final String UPDATE_MENU_ITEM = "UPDATE menu_items SET restaurant_id=?,name=?,description=?,price=?,category=?,availability=? WHERE id=?";
    public static final String DELETE_MENU_ITEM = "DELETE FROM menu_items WHERE id=?";
    public static final String FIND_MENU_ITEM = "SELECT mi.*, r.name restaurant_name FROM menu_items mi JOIN restaurants r ON r.id=mi.restaurant_id WHERE mi.id=?";
    public static final String LIST_MENU_ITEMS = "SELECT mi.*, r.name restaurant_name FROM menu_items mi JOIN restaurants r ON r.id=mi.restaurant_id ORDER BY r.name, mi.name";
    public static final String LIST_MENU_BY_RESTAURANT = "SELECT mi.*, r.name restaurant_name FROM menu_items mi JOIN restaurants r ON r.id=mi.restaurant_id WHERE mi.restaurant_id=? ORDER BY mi.category, mi.name";
    public static final String COUNT_MENU_ITEMS = "SELECT COUNT(*) FROM menu_items";

    public static final String INSERT_ORDER = "INSERT INTO orders(user_id,restaurant_id,total_amount,status) VALUES(?,?,?,?)";
    public static final String INSERT_ORDER_ITEM = "INSERT INTO order_items(order_id,menu_item_id,quantity,price) VALUES(?,?,?,?)";
    public static final String LIST_ORDERS_FOR_USER = "SELECT o.*, r.name restaurant_name FROM orders o JOIN restaurants r ON r.id=o.restaurant_id WHERE o.user_id=? ORDER BY o.order_date DESC";
    public static final String LIST_ALL_ORDERS = "SELECT o.*, r.name restaurant_name FROM orders o JOIN restaurants r ON r.id=o.restaurant_id ORDER BY o.order_date DESC";
    public static final String LIST_ORDER_ITEMS = "SELECT oi.*, mi.name item_name FROM order_items oi JOIN menu_items mi ON mi.id=oi.menu_item_id WHERE oi.order_id=?";
    public static final String COUNT_ORDERS = "SELECT COUNT(*) FROM orders";
}
