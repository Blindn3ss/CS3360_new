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
CREATE TABLE yard (
    yardId INT AUTO_INCREMENT PRIMARY KEY,
    yardName VARCHAR(255) NOT NULL,
    yardLocation VARCHAR(255) NOT NULL,
    yardCapacity INT NOT NULL,
    pricePerTimeSlot DOUBLE NOT NULL,  -- Price per time slot to rent the yard
    description TEXT
);

-- gui workbench

CREATE TABLE `cs3360`.`yard` (
  `yardId` INT NOT NULL AUTO_INCREMENT,
  `yardName` VARCHAR(255) NOT NULL,
  `yardLocation` VARCHAR(255) NOT NULL,
  `yardCapacity` INT NOT NULL,
  `pricePerTimeSlot` DOUBLE NOT NULL,
  `description` TEXT(65535) NULL,
  PRIMARY KEY (`yardId`));

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
  `pricePerTimeSlot` DOUBLE NOT NULL,
  `description` TEXT(65535) NULL,
  PRIMARY KEY (`yardId`)
);



CREATE TABLE time_slot (
    yardId INT,
    timeSlotId INT,
    startTime TIME,
    endTime TIME,
    PRIMARY KEY (yardId, timeSlotId),  -- Composite primary key
    FOREIGN KEY (yardId) REFERENCES yards(yardId)
);

-- Why we need composite primary key here? :))

CREATE TABLE `cs3360`.`timeslot` (
  `yardId` INT NOT NULL,
  `timeSlotId` INT NOT NULL,
  `startTime` TIME NULL,
  `endTime` TIME NULL,
  PRIMARY KEY (yardId, timeSlotId),
  FOREIGN KEY (yardId) REFERENCES yard(yardId)
);

ALTER TABLE `cs3360`.`timeslot`
DROP FOREIGN KEY `timeslot_ibfk_1`;
ALTER TABLE `cs3360`.`timeslot`
ADD CONSTRAINT `timeslot_ibfk_1`
  FOREIGN KEY (`yardId`)
  REFERENCES `cs3360`.`yard` (`yardId`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- Inserting Time Slots for Yard 1
INSERT INTO `cs3360`.`timeslot` (`yardId`, `timeSlotId`, `startTime`, `endTime`) VALUES
(1, 1, '06:00:00', '11:00:00'),
(1, 2, '13:00:00', '17:00:00'),
(1, 3, '19:00:00', '23:00:00');

-- Inserting Time Slots for Yard 2
INSERT INTO `cs3360`.`timeslot` (`yardId`, `timeSlotId`, `startTime`, `endTime`) VALUES
(2, 1, '06:00:00', '11:00:00'),
(2, 2, '13:00:00', '17:00:00'),
(2, 3, '19:00:00', '23:00:00');

-- Inserting Time Slots for Yard 3
INSERT INTO `cs3360`.`timeslot` (`yardId`, `timeSlotId`, `startTime`, `endTime`) VALUES
(3, 1, '06:00:00', '11:00:00'),
(3, 2, '13:00:00', '17:00:00'),
(3, 3, '19:00:00', '23:00:00');

-- Inserting Time Slots for Yard 4
INSERT INTO `cs3360`.`timeslot` (`yardId`, `timeSlotId`, `startTime`, `endTime`) VALUES
(4, 1, '06:00:00', '11:00:00'),
(4, 2, '13:00:00', '17:00:00'),
(4, 3, '19:00:00', '23:00:00');

-- Inserting Time Slots for Yard 5
INSERT INTO `cs3360`.`timeslot` (`yardId`, `timeSlotId`, `startTime`, `endTime`) VALUES
(5, 1, '06:00:00', '11:00:00'),
(5, 2, '13:00:00', '17:00:00'),
(5, 3, '19:00:00', '23:00:00');


CREATE TABLE booking (
    bookingId INT AUTO_INCREMENT PRIMARY KEY,
    yardId INT,
    customerId INT,
    bookingDate DATE,
    timeSlotId INT,
    bookingPrice DOUBLE,
    bookingStatus VARCHAR(50),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (yardId) REFERENCES yard(yardId),
    FOREIGN KEY (customerId) REFERENCES customer(customerId),
    FOREIGN KEY (yardId, timeSlotId) REFERENCES time_slot(yardId, timeSlotId),
    --UNIQUE (yardId, timeSlotId, bookingDate)  -- Ensure unique combination of yard_id, time_slot_id, and booking_date
);
