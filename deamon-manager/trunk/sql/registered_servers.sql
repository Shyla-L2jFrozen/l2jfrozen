-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `registered_servers` (
  `serverMAC` VARCHAR(20) not NULL,
  `serverIP` VARCHAR(20) not NULL,
  `expirationTime` DECIMAL(20) default 0,
  PRIMARY KEY (`serverMAC`)
);
