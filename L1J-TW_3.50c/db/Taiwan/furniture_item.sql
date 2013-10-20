/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2011-05-28 14:25:16
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `furniture_item`
-- ----------------------------
DROP TABLE IF EXISTS `furniture_item`;
CREATE TABLE `furniture_item` (
  `item_id` int(10) unsigned NOT NULL,
  `npc_id` int(10) NOT NULL,
  `note` varchar(45) default '',
  PRIMARY KEY  (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of furniture_item
-- ----------------------------
INSERT INTO `furniture_item` VALUES ('41383', '80109', '巨大兵蟻標本');
INSERT INTO `furniture_item` VALUES ('41384', '80110', '熊標本');
INSERT INTO `furniture_item` VALUES ('41385', '80113', '蛇女標本');
INSERT INTO `furniture_item` VALUES ('41386', '80114', '黑虎標本');
INSERT INTO `furniture_item` VALUES ('41387', '80115', '鹿標本');
INSERT INTO `furniture_item` VALUES ('41388', '80124', '哈維標本');
INSERT INTO `furniture_item` VALUES ('41389', '80118', '青銅騎士');
INSERT INTO `furniture_item` VALUES ('41390', '80119', '青銅馬');
INSERT INTO `furniture_item` VALUES ('41391', '80120', '燭台');
INSERT INTO `furniture_item` VALUES ('41392', '80121', '茶几');
INSERT INTO `furniture_item` VALUES ('41393', '80126', '火爐');
INSERT INTO `furniture_item` VALUES ('41394', '80125', '火把');
INSERT INTO `furniture_item` VALUES ('41395', '80111', '君主用講台');
INSERT INTO `furniture_item` VALUES ('41396', '80112', '旗幟');
INSERT INTO `furniture_item` VALUES ('41397', '80117', '茶几椅子');
INSERT INTO `furniture_item` VALUES ('41398', '80116', '茶几椅子');
INSERT INTO `furniture_item` VALUES ('41399', '80122', '屏風');
INSERT INTO `furniture_item` VALUES ('41400', '80123', '屏風');
INSERT INTO `furniture_item` VALUES ('49065', '80154', '噴水池');
INSERT INTO `furniture_item` VALUES ('49066', '80155', '花園柱子');
INSERT INTO `furniture_item` VALUES ('49067', '80156', '花園柱子');
INSERT INTO `furniture_item` VALUES ('49068', '80157', '屏風2');
INSERT INTO `furniture_item` VALUES ('49069', '80158', '屏風2');
INSERT INTO `furniture_item` VALUES ('49070', '80159', '花瓶架');
INSERT INTO `furniture_item` VALUES ('49071', '80160', '蛋糕');
INSERT INTO `furniture_item` VALUES ('49072', '80161', '惡魔標本');
INSERT INTO `furniture_item` VALUES ('49073', '80162', '飛龍標本');
INSERT INTO `furniture_item` VALUES ('49074', '80163', '黑豹標本');
INSERT INTO `furniture_item` VALUES ('49075', '80164', '艾莉絲標本');
INSERT INTO `furniture_item` VALUES ('49076', '80165', '巨大牛人標本');
