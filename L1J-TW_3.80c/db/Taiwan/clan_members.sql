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
-- Table structure for clan_members
-- ----------------------------
CREATE TABLE IF NOT EXISTS `clan_members` (
  `clan_id` int(10) unsigned NOT NULL DEFAULT '0',
  `index_id` int(11) unsigned NOT NULL DEFAULT '0',
  `char_id` int(11) unsigned NOT NULL DEFAULT '0',
  `char_name` varchar(45) NOT NULL,
  `notes` varchar(60) NOT NULL,
  PRIMARY KEY (`index_id`),
  KEY `char_id` (`char_id`),
  KEY `clan_id` (`clan_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='血盟成員記錄';

-- ----------------------------
-- Records 
-- ----------------------------
