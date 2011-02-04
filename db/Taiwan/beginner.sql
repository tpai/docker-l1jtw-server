/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-02-04 06:56:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for beginner
-- ----------------------------
CREATE TABLE `beginner` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `item_id` int(6) NOT NULL DEFAULT '0',
  `count` int(10) NOT NULL DEFAULT '0',
  `charge_count` int(10) NOT NULL DEFAULT '0',
  `enchantlvl` int(6) NOT NULL DEFAULT '0',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `activate` char(1) NOT NULL DEFAULT 'A',
  `bless` int(11) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `beginner` VALUES ('1', '40308', '10000', '0', '0', '金幣', 'A', '1');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蠟燭', 'A', '1');
INSERT INTO `beginner` VALUES ('3', '40641', '1', '0', '0', '說話卷軸', 'A', '1');
INSERT INTO `beginner` VALUES ('4', '40383', '1', '0', '0', '地圖:歌唱之島', 'P', '1');
INSERT INTO `beginner` VALUES ('5', '40378', '1', '0', '0', '地圖:妖精森林', 'E', '1');
INSERT INTO `beginner` VALUES ('6', '40380', '1', '0', '0', '地圖:銀騎士村莊', 'E', '1');
INSERT INTO `beginner` VALUES ('7', '40384', '1', '0', '0', '地圖:隱藏之谷', 'K', '1');
INSERT INTO `beginner` VALUES ('8', '40383', '1', '0', '0', '地圖:歌唱之島', 'W', '1');
INSERT INTO `beginner` VALUES ('9', '40389', '1', '0', '0', '地圖:沉默洞穴', 'D', '1');
INSERT INTO `beginner` VALUES ('10', '40383', '1', '0', '0', '地圖:歌唱之島', 'D', '1');
