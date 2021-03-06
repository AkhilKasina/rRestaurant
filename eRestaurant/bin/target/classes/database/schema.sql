
-- Customer
CREATE TABLE IF NOT EXISTS customers (
    id INT(7) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30),
    password VARCHAR(30),
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    telephone VARCHAR(20),
    address VARCHAR(200)
);



CREATE TABLE IF NOT EXISTS bookings (
    id INT(7) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    booking_time TIMESTAMP,
    table_position VARCHAR(5),

    customer_id INT(7) UNSIGNED NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE IF NOT EXISTS items (
    id INT(7) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    image_data MEDIUMBLOB,
    menu_type VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS bookingitems (
    bookingitem_id INT(7) UNSIGNED NOT NULL PRIMARY KEY,
    booking_id INT(7) UNSIGNED NOT NULL,
    item_id INT(7) UNSIGNED NOT NULL,

    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

-- System User
CREATE TABLE IF NOT EXISTS managers (
    manager_id INT(7) UNSIGNED NOT NULL PRIMARY KEY,
    email VARCHAR(40),
    manager_password VARCHAR(30),
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    telephone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS staffs (
    staff_id INT(7) UNSIGNED NOT NULL PRIMARY KEY,
    manager_password VARCHAR(30),
    email VARCHAR(40),
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    telephone VARCHAR(20)
);