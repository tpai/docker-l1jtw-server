/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-04-24 19:43:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for petitem
-- ----------------------------
DROP TABLE IF EXISTS `petitem`;
CREATE TABLE `petitem` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `note` varchar(45) NOT NULL DEFAULT '',
  `use_type` varchar(20) NOT NULL DEFAULT '',
  `hitmodifier` int(3) NOT NULL DEFAULT '0',
  `dmgmodifier` int(3) NOT NULL DEFAULT '0',
  `ac` int(3) NOT NULL DEFAULT '0',
  `add_str` int(2) NOT NULL DEFAULT '0',
  `add_con` int(2) NOT NULL DEFAULT '0',
  `add_dex` int(2) NOT NULL DEFAULT '0',
  `add_int` int(2) NOT NULL DEFAULT '0',
  `add_wis` int(2) NOT NULL DEFAULT '0',
  `add_hp` int(10) NOT NULL DEFAULT '0',
  `add_mp` int(10) NOT NULL DEFAULT '0',
  `add_sp` int(10) NOT NULL DEFAULT '0',
  `m_def` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM AUTO_INCREMENT=40767 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of petitem
-- ----------------------------
INSERT INTO `petitem` VALUES ('40749', '獵犬之牙', 'tooth', '5', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40750', '破滅之牙', 'tooth', '-3', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40751', '斗犬之牙', 'tooth', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40752', '黃金之牙', 'tooth', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0');
INSERT INTO `petitem` VALUES ('40753', '龍之牙', 'tooth', '0', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40754', '不滅之牙', 'tooth', '-2', '2', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0');
INSERT INTO `petitem` VALUES ('40755', '黑暗之牙', 'tooth', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '0');
INSERT INTO `petitem` VALUES ('40756', '神之牙', 'tooth', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '0');
INSERT INTO `petitem` VALUES ('40757', '鋼鐵之牙', 'tooth', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40758', '勝利之牙', 'tooth', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40759', '寵物祝福盔甲', 'armor', '0', '0', '-10', '0', '0', '0', '0', '2', '0', '0', '0', '20');
INSERT INTO `petitem` VALUES ('40760', '寵物光明盔甲', 'armor', '0', '0', '-6', '0', '0', '0', '3', '0', '0', '0', '0', '10');
INSERT INTO `petitem` VALUES ('40761', '寵物皮盔甲', 'armor', '0', '0', '-4', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40762', '寵物骷髏盔甲', 'armor', '0', '0', '-7', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40763', '寵物鋼鐵盔甲', 'armor', '0', '0', '-8', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40764', '寵物米索莉盔甲', 'armor', '0', '0', '-12', '0', '0', '0', '1', '1', '0', '0', '0', '10');
INSERT INTO `petitem` VALUES ('40765', '寵物十字盔甲', 'armor', '0', '0', '-13', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `petitem` VALUES ('40766', '寵物鏈甲', 'armor', '0', '0', '-20', '0', '0', '0', '0', '0', '0', '0', '0', '0');
