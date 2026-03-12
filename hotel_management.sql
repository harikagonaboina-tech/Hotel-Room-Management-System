CREATE DATABASE hotel_management;

USE hotel_management;

CREATE TABLE users(
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    ph_no BIGINT
);

CREATE TABLE hotel_room(
    room_id INT PRIMARY KEY,
    guest_name VARCHAR(50),
    block VARCHAR(20),
    room_rent DOUBLE,
    contact BIGINT,
    email VARCHAR(50),
    checkin_date DATE
);
