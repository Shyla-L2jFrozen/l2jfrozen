-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `accounts_servers` (
  `email` VARCHAR(45) NOT NULL,
  `serverMAC` VARCHAR(20),
  PRIMARY KEY (`email`,`serverMAC`)
);
