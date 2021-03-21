# noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `parameter`;
DROP TABLE IF EXISTS `product_parameter`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `order_content`;

-- roles dictionary
CREATE TABLE `role` (
                        id int NOT NULL AUTO_INCREMENT,
                        name varchar(20) NOT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
                        id int NOT NULL AUTO_INCREMENT,
                        name varchar(255) NOT NULL,
                        email varchar(320) NOT NULL,
                        password varchar(128) NOT NULL, -- hex string representation of 512 bits PBKDF2 hash
                        salt varchar(32) NOT NULL,      -- hex string representation of 128 bits salt
                        phone_number varchar(13),
                        role_id int,
                        blocked bit NOT NULL,           -- user is blocked if <> 0
                        PRIMARY KEY (id),
                        FOREIGN KEY (role_id) REFERENCES role(id) ON UPDATE CASCADE ON DELETE SET NULL,
                        UNIQUE KEY (email)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category` (
                            id int NOT NULL AUTO_INCREMENT,
                            name varchar(50) NOT NULL,
                            description varchar(1000),
                            PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product` (
                           id int NOT NULL AUTO_INCREMENT,
                           name varchar(50) NOT NULL,
                           description varchar(1000),
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           price decimal(10, 2) NOT NULL,
                           image_url VARCHAR(2083),
                           category_id int,
                           PRIMARY KEY (id),
                           FOREIGN KEY (category_id) REFERENCES category(id) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- product parameters dictionary (i.e. color, weight, size...)
CREATE TABLE `parameter` (
                             id int NOT NULL AUTO_INCREMENT,
                             name varchar(50) NOT NULL,
                             PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product_parameter` (
                                     product_id int NOT NULL,
                                     parameter_id int NOT NULL,
                                     `value` varchar(100) NOT NULL,
                                     FOREIGN KEY (product_id) REFERENCES product(id) ON UPDATE CASCADE ON DELETE CASCADE,
                                     FOREIGN KEY (parameter_id) REFERENCES parameter(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order` (
                         id int NOT NULL AUTO_INCREMENT,
                         user_id int NOT NULL,
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         FOREIGN KEY (user_id) REFERENCES user(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order_content` (
                                 order_id int NOT NULL,
                                 product_id int NOT NULL,
                                 quantity int NOT NULL DEFAULT 1,
                                 price decimal(10, 2) NOT NULL,
                                 FOREIGN KEY (order_id) REFERENCES `order`(id) ON UPDATE CASCADE ON DELETE CASCADE,
                                 FOREIGN KEY (product_id) REFERENCES product(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
