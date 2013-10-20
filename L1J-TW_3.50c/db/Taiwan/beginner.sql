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
INSERT INTO `beginner` VALUES ('1', '40005', '1', '0', '0', '蠟燭', 'A', '1');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蠟燭', 'A', '1');
INSERT INTO `beginner` VALUES ('3', '40641', '1', '0', '0', '說話卷軸', 'A', '1');
INSERT INTO `beginner` VALUES ('4', '35', '1', '0', '0', '象牙塔單手劍', 'K', '1');
INSERT INTO `beginner` VALUES ('5', '48', '1', '0', '0', '象牙塔雙手劍', 'K', '1');
INSERT INTO `beginner` VALUES ('6', '147', '1', '0', '0', '象牙塔斧頭', 'K', '1');
INSERT INTO `beginner` VALUES ('7', '105', '1', '0', '0', '象牙塔長矛', 'K', '1');
INSERT INTO `beginner` VALUES ('8', '174', '1', '0', '0', '象牙塔石弓', 'K', '1');
INSERT INTO `beginner` VALUES ('9', '7', '1', '0', '0', '象牙塔短劍', 'K', '1');
INSERT INTO `beginner` VALUES ('10', '49309', '1', '0', '0', '象牙塔箭筒', 'K', '1');
INSERT INTO `beginner` VALUES ('11', '35', '1', '0', '0', '象牙塔單手劍', 'P', '1');
INSERT INTO `beginner` VALUES ('12', '48', '1', '0', '0', '象牙塔雙手劍', 'P', '1');
INSERT INTO `beginner` VALUES ('13', '147', '1', '0', '0', '象牙塔斧頭', 'P', '1');
INSERT INTO `beginner` VALUES ('14', '7', '1', '0', '0', '象牙塔短劍', 'P', '1');
INSERT INTO `beginner` VALUES ('15', '35', '1', '0', '0', '象牙塔單手劍', 'E', '1');
INSERT INTO `beginner` VALUES ('16', '175', '1', '0', '0', '象牙塔長弓', 'E', '1');
INSERT INTO `beginner` VALUES ('17', '174', '1', '0', '0', '象牙塔石弓', 'E', '1');
INSERT INTO `beginner` VALUES ('18', '7', '1', '0', '0', '象牙塔短劍', 'E', '1');
INSERT INTO `beginner` VALUES ('19', '49309', '1', '0', '0', '象牙塔箭筒', 'E', '1');
INSERT INTO `beginner` VALUES ('20', '35', '1', '0', '0', '象牙塔單手劍', 'W', '1');
INSERT INTO `beginner` VALUES ('21', '224', '1', '0', '0', '象牙塔魔杖', 'W', '1');
INSERT INTO `beginner` VALUES ('22', '7', '1', '0', '0', '象牙塔短劍', 'W', '1');
INSERT INTO `beginner` VALUES ('23', '35', '1', '0', '0', '象牙塔單手劍', 'D', '1');
INSERT INTO `beginner` VALUES ('24', '174', '1', '0', '0', '象牙塔石弓', 'D', '1');
INSERT INTO `beginner` VALUES ('25', '73', '1', '0', '0', '象牙塔雙刀', 'D', '1');
INSERT INTO `beginner` VALUES ('26', '156', '1', '0', '0', '象牙塔鋼爪', 'D', '1');
INSERT INTO `beginner` VALUES ('27', '7', '1', '0', '0', '象牙塔短劍', 'D', '1');
INSERT INTO `beginner` VALUES ('28', '49309', '1', '0', '0', '象牙塔箭筒', 'D', '1');
INSERT INTO `beginner` VALUES ('29', '35', '1', '0', '0', '象牙塔單手劍', 'R', '1');
INSERT INTO `beginner` VALUES ('30', '48', '1', '0', '0', '象牙塔雙手劍', 'R', '1');
INSERT INTO `beginner` VALUES ('31', '147', '1', '0', '0', '象牙塔斧頭', 'R', '1');
INSERT INTO `beginner` VALUES ('32', '147', '1', '0', '0', '象牙塔斧頭', 'I', '1');
INSERT INTO `beginner` VALUES ('33', '174', '1', '0', '0', '象牙塔石弓', 'I', '1');
INSERT INTO `beginner` VALUES ('34', '224', '1', '0', '0', '象牙塔魔杖', 'I', '1');
INSERT INTO `beginner` VALUES ('35', '49309', '1', '0', '0', '象牙塔箭筒', 'I', '1');
