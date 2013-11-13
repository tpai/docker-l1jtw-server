/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2013-11-13 22:35:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for clan_warehouse_history
-- ----------------------------
CREATE TABLE IF NOT EXISTS `clan_warehouse_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水號',
  `clan_id` int(10) NOT NULL COMMENT '角色id',
  `char_name` varchar(45) NOT NULL COMMENT '角色名稱',
  `type` tinyint(2) NOT NULL COMMENT '領出: 1, 存入:0',
  `item_name` varchar(45) NOT NULL COMMENT '物品名稱',
  `item_count` int(10) NOT NULL COMMENT '數量',
  `record_time` datetime NOT NULL COMMENT '提領時間',
  PRIMARY KEY (`id`),
  KEY `char_name` (`char_name`),
  KEY `clan_id` (`clan_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='盟倉使用記錄';

-- ----------------------------
-- Records 
-- ----------------------------
