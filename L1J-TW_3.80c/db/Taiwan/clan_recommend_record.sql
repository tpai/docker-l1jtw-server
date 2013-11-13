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
-- Table structure for clan_recommend_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `clan_recommend_record` (
  `clan_id` int(10) unsigned NOT NULL COMMENT '血盟ID',
  `clan_name` varchar(20) NOT NULL COMMENT '血盟名稱',
  `crown_name` varchar(20) NOT NULL COMMENT '王族名稱',
  `clan_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '血盟登錄類型',
  `type_message` varchar(50) NOT NULL,
  PRIMARY KEY (`clan_id`),
  KEY `clan_name` (`clan_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='血盟推薦紀錄';

-- ----------------------------
-- Records 
-- ----------------------------
