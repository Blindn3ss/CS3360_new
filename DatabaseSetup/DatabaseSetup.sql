-- This .sql file I created to save all commands / queries to setup for and used in the app.
-- And Testing as well.


-- Create table customer - use mysql workbench gui
CREATE TABLE `cs3360`.`customer` (
    `customerId` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `fullName` VARCHAR(45) NOT NULL,
    `phoneNumber` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`customerId`),
UNIQUE INDEX `customerId_UNIQUE` (`customerId` ASC) VISIBLE);

INSERT INTO customer (username, password, fullName, phoneNumber, email) VALUES ('johndoe', '1234', 'John Doe', '012345678', 'john.doe@example.com');

-- Create table manager - mysql workbench gui
CREATE TABLE `cs3360`.`manager` (
    `managerId` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `fullName` VARCHAR(45) NULL,
    `phoneNumber` VARCHAR(45) NULL,
    `email` VARCHAR(45) NULL,
PRIMARY KEY (`managerId`));

INSERT INTO manager (username, password, fullName, phoneNumber, email) VALUES ('adamsmith', '1234', 'Adam Smith', '0987654321', 'adam.smith@example.com');


-- Create Yard

ALTER TABLE `cs3360`.`yard`
ADD COLUMN `surfaceType` VARCHAR(90) NULL AFTER `yardCapacity`;
-- Grass, Turf, indoor
-- Become
CREATE TABLE `cs3360`.`yard` (
  `yardId` INT NOT NULL AUTO_INCREMENT,
  `yardName` VARCHAR(255) NOT NULL,
  `yardLocation` VARCHAR(255) NOT NULL,
  `yardCapacity` INT NOT NULL,
  `surfaceType` VARCHAR(90) NULL,  -- Type of surface (e.g., grass, turf, indoor)
  `pricePerDay` DOUBLE NOT NULL,
  `description` TEXT(65535) NULL,
  PRIMARY KEY (`yardId`)
);

CREATE TABLE booking (
    bookingId INT AUTO_INCREMENT PRIMARY KEY,
    yardId INT  NULL,
    customerId INT  NULL,
    bookingDate DATE NOT NULL,
    bookingPrice DOUBLE NOT NULL,
    bookingStatus ENUM('PENDING', 'CANCELLED', 'CONFIRMED', 'COMPLETED') NOT NULL,
    CONSTRAINT fk_booking_yard
        FOREIGN KEY (yardId) REFERENCES yard(yardId)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_booking_customer
        FOREIGN KEY (customerId) REFERENCES customer(customerId)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);


CREATE TABLE permission (
    managerId INT NOT NULL,
    yardId INT NOT NULL,
    CONSTRAINT fk_permission_manager
        FOREIGN KEY (managerId) REFERENCES manager(managerId)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_permission_yard
        FOREIGN KEY (yardId) REFERENCES yard(yardId)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    PRIMARY KEY (managerId, yardId)
);

DELIMITER //

CREATE EVENT updateConfirmedAndPendingBookingsEvent
ON SCHEDULE EVERY 3 HOUR
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    -- Update bookings with status 'CONFIRMED' to 'COMPLETED' if the booking date is before the current date
    UPDATE booking
    SET bookingStatus = 'COMPLETED'
    WHERE bookingDate < CURRENT_DATE
      AND bookingStatus = 'CONFIRMED';

    -- Update bookings with status 'PENDING' to 'CANCEL' if the booking date is before the current date
    UPDATE booking
    SET bookingStatus = 'CANCEL'
    WHERE bookingDate < CURRENT_DATE
      AND bookingStatus = 'PENDING';
END //

DELIMITER ;

SHOW EVENTS;

DROP EVENT updateConfirmedAndPendingBookingsEvent;

-- Note change the field column of yard table from 'pricePerTimeSlot' to 'pricePerDay'
ALTER TABLE booking
MODIFY bookingStatus ENUM('PENDING', 'CANCELLED', 'CONFIRMED', 'COMPLETED') NOT NULL;


