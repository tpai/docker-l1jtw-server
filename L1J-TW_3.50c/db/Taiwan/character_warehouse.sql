/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-04-08 11:52:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `character_warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `character_warehouse`;
CREATE TABLE `character_warehouse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(13) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_equipped` int(11) DEFAULT NULL,
  `enchantlvl` int(11) DEFAULT NULL,
  `is_id` int(11) DEFAULT NULL,
  `durability` int(11) DEFAULT NULL,
  `charge_count` int(11) DEFAULT NULL,
  `remaining_time` int(11) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  `bless` int(11) DEFAULT NULL,
  `attr_enchant_kind` int(11) DEFAULT NULL,
  `attr_enchant_level` int(11) DEFAULT NULL,
  `firemr` int(11) DEFAULT NULL,
  `watermr` int(11) DEFAULT NULL,
  `earthmr` int(11) DEFAULT NULL,
  `windmr` int(11) DEFAULT NULL,
  `addsp` int(11) DEFAULT NULL,
  `addhp` int(11) DEFAULT NULL,
  `addmp` int(11) DEFAULT NULL,
  `hpr` int(11) DEFAULT NULL,
  `mpr` int(11) DEFAULT NULL,
  `m_def` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_id` (`account_name`)
) ENGINE=MyISAM AUTO_INCREMENT=271237027 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of character_warehouse
-- ----------------------------
