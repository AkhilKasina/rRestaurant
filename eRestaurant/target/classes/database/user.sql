CREATE DATABASE IF NOT EXISTS erestaurantdb;

ALTER DATABASE erestaurantdb
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON erestaurantdb.* TO 'sesgroup1'@'%' IDENTIFIED BY 'ThePassword';