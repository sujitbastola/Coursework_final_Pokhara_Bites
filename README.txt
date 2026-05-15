Pokhara Bites - Restaurant Management System

Stack:
- Java 11
- JSP and Servlets
- JDBC with HikariCP connection pooling
- MySQL
- Maven WAR packaging
- No JavaScript

Database setup:
1. Create the database and sample restaurants/menu items:
   mysql -u root -p < src/main/resources/database.sql

2. Configure database credentials using environment variables if needed:
   DB_URL=jdbc:mysql://localhost:3306/pokhara_bites?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   DB_USER=root
   DB_PASSWORD=your_password

Build:
   mvn clean package

Deploy:
   Copy target/pokhara-bites.war to Tomcat 9 webapps, or deploy from your IDE.

Default flow:
- Register a user as admin to manage restaurants and menu items.
- Register a user as customer to browse restaurants, select one restaurant, add menu items to the session cart, checkout, and view order history.

Important behavior:
- All actions are server-side GET or POST requests.
- The selected restaurant controls which menu items are displayed.
- The cart is stored in the HTTP session.
- If a customer tries to switch restaurants while the cart contains items from another restaurant, the app blocks the switch and asks the customer to clear the cart first.
- Orders are saved to orders and order_items in a single JDBC transaction.
