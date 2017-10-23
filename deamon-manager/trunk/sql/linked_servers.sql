-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `linked_servers` (
  `RealIp` VARCHAR(20) not NULL,
  `RealMac` VARCHAR(20) not NULL,
  `ServerName` VARCHAR(20) not NULL,
  `LoginServerIp` VARCHAR(20) not NULL,
  `LoginServerPort` VARCHAR(20) not NULL,
  `GameServerIp` VARCHAR(20) not NULL,
  `GameServerPort` VARCHAR(20) not NULL,
  `ActiveClients` VARCHAR(20) not NULL,
  `Date` VARCHAR(20) not NULL,
  `MaxOnline` VARCHAR(20) not NULL,
  PRIMARY KEY (`RealMac`)
);
