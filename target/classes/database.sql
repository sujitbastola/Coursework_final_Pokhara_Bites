CREATE DATABASE IF NOT EXISTS pokhara_bites CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pokhara_bites;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('admin','customer') NOT NULL DEFAULT 'customer',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    cuisine_type VARCHAR(80) NOT NULL,
    opening_hours VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    category ENUM('veg','non-veg') NOT NULL,
    availability BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_menu_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(40) NOT NULL DEFAULT 'PLACED',
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_order_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_menu FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);


INSERT INTO users(name, email, password_hash, role) VALUES
('Admin User', 'admin@pokhara.com', 'cG9raGFyYS1iaXRlcy0wMA==:mvAsiUm0Oy1yRCetZoIwm6zgHPHqJvScbygdMSh1tVQ=', 'admin');

INSERT INTO restaurants(name, address, phone, cuisine_type, opening_hours, image_url) VALUES
('Lakeside Thakali Kitchen', 'Baidam Road, Lakeside, Pokhara', '+977 9745450737', 'Thakali and Nepali', '8:00 AM - 10:00 PM', ''),
('Phewa Momo House', 'Hallan Chowk, Pokhara', '+977 9745450737', 'Momo and Snacks', '10:00 AM - 9:30 PM', ''),
('Sarangkot Grill Cafe', 'Sarangkot View Road, Pokhara', '+977 9745450737', 'Grill and Cafe', '7:00 AM - 10:30 PM', ''),
('Newari Khaja Ghar', 'Chipledhunga, Pokhara', '+977 9745450737', 'Newari Khaja', '9:00 AM - 9:00 PM', ''),
('Begnas Fish Corner', 'Begnas Lake Road, Pokhara', '+977 9745450737', 'Fish and Local Food', '10:00 AM - 8:30 PM', ''),
('Daal Bhat Station', 'Mahendrapul, Pokhara', '+977 9745450737', 'Nepali Meals', '7:30 AM - 9:30 PM', ''),
('Gurung Kitchen', 'Rambazar, Pokhara', '+977 9745450737', 'Gurung and Local', '8:00 AM - 9:00 PM', ''),
('Tibetan Laphing Hub', 'Lakeside Street 13, Pokhara', '+977 9745450737', 'Tibetan Snacks', '11:00 AM - 8:00 PM', ''),
('Himalayan Tea and Lassi', 'Prithvi Chowk, Pokhara', '+977 9745450737', 'Local Drinks', '7:00 AM - 8:00 PM', ''),
('Pokhara Sel Roti Pasal', 'Srijana Chowk, Pokhara', '+977 9745450737', 'Breakfast and Sweets', '6:30 AM - 7:30 PM', '');

INSERT INTO menu_items(restaurant_id, name, description, price, category, availability) VALUES
(1, 'Chicken Thakali Set', 'Rice, dal, curry, greens, pickles, gundruk, and chicken curry.', 520.00, 'non-veg', TRUE),
(1, 'Veg Thakali Set', 'Vegetarian Thakali platter with local greens, achar, and gundruk.', 390.00, 'veg', TRUE),
(1, 'Fapar Roti', 'Buckwheat roti served with achar and local butter.', 180.00, 'veg', TRUE),
(1, 'Gundruk Soup', 'Warm fermented greens soup, simple and local.', 160.00, 'veg', TRUE),
(2, 'Buff Momo', 'Steamed buff momo with tomato sesame achar.', 220.00, 'non-veg', TRUE),
(2, 'Veg Jhol Momo', 'Vegetable momo in tangy sesame jhol.', 210.00, 'veg', TRUE),
(2, 'Chicken C-Momo', 'Fried chicken momo tossed in spicy chili sauce.', 280.00, 'non-veg', TRUE),
(2, 'Masala Tea', 'Milk tea cooked with Nepali spices.', 70.00, 'veg', TRUE),
(3, 'Chicken Sekuwa', 'Charcoal grilled chicken with timur spice.', 420.00, 'non-veg', TRUE),
(3, 'Paneer Sizzler', 'Paneer with vegetables and pepper sauce.', 480.00, 'veg', TRUE),
(3, 'Himalayan Coffee', 'Fresh brewed hot coffee.', 140.00, 'veg', TRUE),
(3, 'Aloo Sadeko', 'Spicy potato salad with mustard oil and sesame.', 150.00, 'veg', TRUE),
(4, 'Samay Baji', 'Beaten rice, bara, achar, potato, and local spices.', 350.00, 'non-veg', TRUE),
(4, 'Bara', 'Lentil pancake cooked Newari style.', 120.00, 'veg', TRUE),
(4, 'Yomari', 'Steamed rice flour sweet with chaku filling.', 130.00, 'veg', TRUE),
(4, 'Chatamari', 'Newari rice crepe with egg and toppings.', 240.00, 'non-veg', TRUE),
(5, 'Begnas Fried Fish', 'Local lake fish fried with Nepali masala.', 520.00, 'non-veg', TRUE),
(5, 'Fish Curry Rice', 'Fish curry served with rice, dal, and achar.', 480.00, 'non-veg', TRUE),
(5, 'Poleko Makai', 'Roasted corn with salt, chili, and lemon.', 90.00, 'veg', TRUE),
(5, 'Lemon Soda', 'Fresh lemon soda with black salt.', 100.00, 'veg', TRUE),
(6, 'Daal Bhat Tarkari', 'Rice, lentil soup, vegetable curry, achar, and greens.', 280.00, 'veg', TRUE),
(6, 'Chicken Daal Bhat', 'Classic daal bhat with chicken curry.', 420.00, 'non-veg', TRUE),
(6, 'Curd Chiura', 'Beaten rice with fresh curd and banana.', 180.00, 'veg', TRUE),
(6, 'Chiya', 'Nepali milk tea.', 50.00, 'veg', TRUE),
(7, 'Gurung Bread Set', 'Gurung bread with curry and achar.', 260.00, 'veg', TRUE),
(7, 'Kodo Ko Dhido', 'Millet dhido served with gundruk and curry.', 340.00, 'veg', TRUE),
(7, 'Local Chicken Curry', 'Village-style chicken curry with rice.', 520.00, 'non-veg', TRUE),
(7, 'Moh Dahi', 'Fresh local curd drink.', 120.00, 'veg', TRUE),
(8, 'Yellow Laphing', 'Cold spicy mung bean noodle roll.', 160.00, 'veg', TRUE),
(8, 'Tingmo with Aloo', 'Steamed bread with potato curry.', 210.00, 'veg', TRUE),
(8, 'Thenthuk', 'Hand-pulled noodle soup with vegetables.', 260.00, 'veg', TRUE),
(8, 'Butter Tea', 'Warm salty Tibetan butter tea.', 110.00, 'veg', TRUE),
(9, 'Matka Chiya', 'Milk tea served in clay cup style.', 80.00, 'veg', TRUE),
(9, 'Sweet Lassi', 'Fresh yogurt drink with sugar and cardamom.', 150.00, 'veg', TRUE),
(9, 'Mohito Pudina', 'Mint lemon cooler, local style.', 140.00, 'veg', TRUE),
(9, 'Sattu Drink', 'Roasted gram flour drink with lemon and black salt.', 130.00, 'veg', TRUE),
(10, 'Sel Roti Set', 'Crispy rice flour ring served with achar.', 120.00, 'veg', TRUE),
(10, 'Aloo Chop', 'Potato fritter with chutney.', 80.00, 'veg', TRUE),
(10, 'Jeri Swari', 'Sweet jeri served with soft swari bread.', 140.00, 'veg', TRUE),
(10, 'Kheer', 'Rice pudding with milk, cardamom, and nuts.', 160.00, 'veg', TRUE);
