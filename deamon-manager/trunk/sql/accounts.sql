-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `accounts` (
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) ,
  `serverIP` VARCHAR(20),
  `serverMAC` VARCHAR(20),
  `expirationTime` DECIMAL(20),
  PRIMARY KEY (`login`)
);
