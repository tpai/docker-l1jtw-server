/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-04-04 20:01:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for character_elf_warehouse
-- ----------------------------
CREATE TABLE `character_elf_warehouse` (
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
  PRIMARY KEY (`id`),
  KEY `key_id` (`account_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
