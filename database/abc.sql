alter table bhazi_db.order_history
add constraint `FK_Shop_OrderHistory`
foreign key (`shop_id`) references `shop` (`id`);

alter table bhazi_db.address
add column is_active boolean not null default true;

alter table bhazi_db.address
drop column is_inactive;

create table if not exists bhazi_db.notification (
	`id` bigint auto_increment primary key,
    `title` varchar(64) not null,
    `body` varchar(255) not null,
    `already_read` boolean not null default false,
    `created_at` datetime(3) NOT NULL,
	`updated_at` datetime(3) NOT NULL,
    `profile_id` int NOT NULL,
    CONSTRAINT `FK_Notification_Profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB;

alter table bhazi_db.notification
add column category varchar(20) not null;

alter table bhazi_db.profile
add column firebase_token varchar(200),
add UNIQUE KEY `UK_Profile_FirebaseToken` (`firebase_token`);

create table if not exists bhazi_db.coupon (
	`coupon_code` varchar(8) primary key,
    `discount` int not null,
    `description` varchar(255) not null,
    `expiry_date` date not null,
    `type` varchar(20) not null,
    `valid` boolean not null,
    `created_at` datetime(3) NOT NULL,
	`updated_at` datetime(3) NOT NULL
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS bhazi_db.coupon_applied (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `valid` BOOLEAN NOT NULL,
    `profile_id` INT NOT NULL,
    `coupon_code` VARCHAR(8) NOT NULL,
    `created_at` DATETIME(3) NOT NULL,
    `updated_at` DATETIME(3) NOT NULL,
    CONSTRAINT `FK_CouponApplied_Profile` FOREIGN KEY (`profile_id`)
        REFERENCES `profile` (`id`),
    CONSTRAINT `FK_CouponApplied_Coupon` FOREIGN KEY (`coupon_code`)
        REFERENCES `coupon` (`coupon_code`)
)  ENGINE=INNODB;


select count(*) from bhazi_db.profile;
select count(*) from bhazi_db.order_history;

alter table bhazi_db.product
add column `prime` boolean not null default false;

alter table bhazi_db.product
modify column `prime` boolean after `out_of_stock`;

CREATE TABLE if not exists bhazi_db.cart (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `profile_id` int NOT NULL,
  `active` boolean not null,
  `coupon_applied` boolean NOT null,
  `coupon_code` varchar(8),
  `coupon_discount` float NOT NULL,
  `wallet_used` boolean not null,
  `wallet_balance_used` float not NULL,
  `referral_balance_used` float not NULL,
  `delivery_charge` float NOT NULL,
  `packaging_charge` float NOT NULL,
  `total_amount` float NOT NULL,
  `total_discount` float NOT NULL,
  `payable` float NOT NULL,
  `delivery_type` varchar(20) NOT NULL,
  `packaging_type` varchar(20) NOT NULL,
  `delivery_time_preference` varchar(20) NOT NULL,
  `created_at` datetime(3) NOT NULL,
  `updated_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Cart_Profile` (`profile_id`),
  CONSTRAINT `FK_Cart_Profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB;

CREATE TABLE if not exists bhazi_db.cart_item (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `counter` int not null,
  `price` float NOT NULL,
  `product_id` int not NULL,
  `cart_id` bigint NOT NULL,
  `created_at` datetime(3) NOT NULL,
  `updated_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CartItem_Cart` (`cart_id`),
  CONSTRAINT `FK_CartItem_Cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

alter table bhazi_db.cart_item
add column `active` boolean not null after `product_id`;

alter table bhazi_db.cart_item
add column `sub_product_id` int not null after `product_id`;

CREATE TABLE if not exists bhazi_db.sub_product (
  `id` int NOT NULL AUTO_INCREMENT,
  `weight` int NOT NULL,
  `price` float NOT NULL,
  `product_id` int not NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SubProduct_Product` (`product_id`),
  CONSTRAINT `FK_SubProduct_Product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) on update cascade on DELETE CASCADE
) ENGINE=InnoDB;



-- Stored Procedure
DELIMITER $$
DROP PROCEDURE IF EXISTS get_random_profiles$$
CREATE PROCEDURE get_random_profiles(IN cnt INT)
BEGIN
  DROP TEMPORARY TABLE IF EXISTS random_profiles;
  CREATE TEMPORARY TABLE random_profiles ( random_id INT );

loop_me: LOOP
    IF cnt < 1 THEN
      LEAVE loop_me;
    END IF;

    INSERT INTO random_profiles
       SELECT r1.id
         FROM bhazi_db.profile AS r1 JOIN
              (SELECT (RAND() *
                            (SELECT MAX(id)
                               FROM bhazi_db.profile)) AS id)
               AS r2
        WHERE r1.id >= r2.id
        ORDER BY r1.id ASC
        LIMIT 1;

    SET cnt = cnt - 1;
  END LOOP loop_me;
END$$
DELIMITER ;

call get_random_profiles(10);
SELECT 
    *
FROM
    bhazi_db.random_profiles;