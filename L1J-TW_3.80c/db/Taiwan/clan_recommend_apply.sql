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
-- Table structure for clan_recommend_apply
-- ----------------------------
CREATE TABLE IF NOT EXISTS `clan_recommend_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `clan_id` int(10) unsigned NOT NULL,
  `clan_name` varchar(45) NOT NULL,
  `char_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='血盟推薦玩家申請';

-- ----------------------------
-- Records 
-- ----------------------------
