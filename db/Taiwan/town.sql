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
-- Table structure for town
-- ----------------------------
CREATE TABLE `town` (
  `town_id` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL DEFAULT '',
  `leader_id` int(10) unsigned NOT NULL DEFAULT '0',
  `leader_name` varchar(45) DEFAULT NULL,
  `tax_rate` int(10) unsigned NOT NULL DEFAULT '0',
  `tax_rate_reserved` int(10) unsigned NOT NULL DEFAULT '0',
  `sales_money` int(10) unsigned NOT NULL DEFAULT '0',
  `sales_money_yesterday` int(10) unsigned NOT NULL DEFAULT '0',
  `town_tax` int(10) unsigned NOT NULL DEFAULT '0',
  `town_fix_tax` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`town_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `town` VALUES ('1', '說話之島', '0', null, '0', '0', '0', '0', '0', '14112');
INSERT INTO `town` VALUES ('2', '銀騎士村莊', '0', null, '0', '0', '0', '1500', '0', '1658532');
INSERT INTO `town` VALUES ('3', '古魯丁村莊', '0', null, '0', '0', '0', '0', '0', '1104804');
INSERT INTO `town` VALUES ('4', '燃柳村莊', '0', null, '0', '0', '0', '0', '0', '266');
INSERT INTO `town` VALUES ('5', '風木村莊', '0', null, '0', '0', '0', '0', '0', '59400');
INSERT INTO `town` VALUES ('6', '肯特村莊', '0', null, '0', '0', '0', '0', '0', '296');
INSERT INTO `town` VALUES ('7', '奇岩村莊', '0', null, '0', '0', '0', '0', '0', '538414');
INSERT INTO `town` VALUES ('8', '海音村莊', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('9', '威頓村莊', '0', null, '0', '0', '0', '0', '0', '19620');
INSERT INTO `town` VALUES ('10', '歐瑞村莊', '0', null, '0', '0', '0', '0', '0', '38062');
