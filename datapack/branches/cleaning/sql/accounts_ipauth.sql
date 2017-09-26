SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `accounts_ipauth`
-- ----------------------------
DROP TABLE IF EXISTS `accounts_ipauth`;
CREATE TABLE `accounts_ipauth` (
  `login` varchar(45) NOT NULL,
  `ip` char(15) NOT NULL,
  `type` enum('deny','allow') DEFAULT 'allow'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accounts_ipauth
-- ----------------------------
