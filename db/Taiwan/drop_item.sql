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
-- Table structure for drop_item
-- ----------------------------
CREATE TABLE `drop_item` (
  `item_id` int(10) NOT NULL DEFAULT '0',
  `drop_rate` float unsigned NOT NULL DEFAULT '0',
  `drop_amount` float unsigned NOT NULL DEFAULT '0',
  `note` varchar(45) NOT NULL DEFAULT '',
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `drop_item` VALUES ('1', '1', '1', '歐西斯匕首');
INSERT INTO `drop_item` VALUES ('21', '1', '1', '歐西斯短劍');
INSERT INTO `drop_item` VALUES ('23', '1', '1', '闊劍');
INSERT INTO `drop_item` VALUES ('25', '1', '1', '銀劍');
INSERT INTO `drop_item` VALUES ('26', '1', '1', '小侏儒短劍');
INSERT INTO `drop_item` VALUES ('27', '1', '1', '彎刀');
INSERT INTO `drop_item` VALUES ('31', '1', '1', '長劍');
INSERT INTO `drop_item` VALUES ('91', '1', '1', '歐西斯之矛');
INSERT INTO `drop_item` VALUES ('93', '1', '1', '三叉戟');
INSERT INTO `drop_item` VALUES ('94', '1', '1', '帕提森');
INSERT INTO `drop_item` VALUES ('96', '1', '1', '矛');
INSERT INTO `drop_item` VALUES ('98', '1', '1', '闊矛');
INSERT INTO `drop_item` VALUES ('102', '1', '1', '露西錘');
INSERT INTO `drop_item` VALUES ('103', '1', '1', '戟');
INSERT INTO `drop_item` VALUES ('136', '1', '1', '斧');
INSERT INTO `drop_item` VALUES ('137', '1', '1', '亞連');
INSERT INTO `drop_item` VALUES ('138', '1', '1', '木棒');
INSERT INTO `drop_item` VALUES ('139', '1', '1', '弗萊爾');
INSERT INTO `drop_item` VALUES ('140', '1', '1', '釘錘');
INSERT INTO `drop_item` VALUES ('143', '1', '1', '戰斧');
INSERT INTO `drop_item` VALUES ('144', '1', '1', '侏儒鐵斧');
INSERT INTO `drop_item` VALUES ('171', '1', '1', '歐西斯弓');
INSERT INTO `drop_item` VALUES ('172', '1', '1', '弓');
INSERT INTO `drop_item` VALUES ('173', '1', '1', '短弓');
INSERT INTO `drop_item` VALUES ('20007', '1', '1', '侏儒鐵盔');
INSERT INTO `drop_item` VALUES ('20034', '1', '1', '歐西斯頭盔');
INSERT INTO `drop_item` VALUES ('20043', '1', '1', '鋼盔');
INSERT INTO `drop_item` VALUES ('20052', '1', '1', '侏儒斗篷');
INSERT INTO `drop_item` VALUES ('20072', '1', '1', '歐西斯斗篷');
INSERT INTO `drop_item` VALUES ('20089', '1', '1', '小籐甲');
INSERT INTO `drop_item` VALUES ('20096', '1', '1', '環甲');
INSERT INTO `drop_item` VALUES ('20101', '1', '1', '皮甲');
INSERT INTO `drop_item` VALUES ('20115', '1', '1', '籐甲');
INSERT INTO `drop_item` VALUES ('20122', '1', '1', '鱗甲');
INSERT INTO `drop_item` VALUES ('20125', '1', '1', '鏈甲');
INSERT INTO `drop_item` VALUES ('20135', '1', '1', '歐西斯環甲');
INSERT INTO `drop_item` VALUES ('20136', '1', '1', '歐西斯鏈甲');
INSERT INTO `drop_item` VALUES ('20147', '1', '1', '銀釘皮甲');
INSERT INTO `drop_item` VALUES ('20149', '1', '1', '青銅盔甲');
INSERT INTO `drop_item` VALUES ('20162', '1', '1', '皮手套');
INSERT INTO `drop_item` VALUES ('20182', '1', '1', '手套');
INSERT INTO `drop_item` VALUES ('20205', '1', '1', '長靴');
INSERT INTO `drop_item` VALUES ('20213', '1', '1', '短統靴');
INSERT INTO `drop_item` VALUES ('20223', '1', '1', '侏儒圓盾');
INSERT INTO `drop_item` VALUES ('20237', '1', '1', '阿克海盾牌');
INSERT INTO `drop_item` VALUES ('20239', '1', '1', '小盾牌');
INSERT INTO `drop_item` VALUES ('20242', '1', '1', '大盾牌');
INSERT INTO `drop_item` VALUES ('40001', '1', '1', '燈');
INSERT INTO `drop_item` VALUES ('40002', '1', '1', '燈籠');
INSERT INTO `drop_item` VALUES ('40005', '1', '1', '蠟燭');
INSERT INTO `drop_item` VALUES ('100025', '1', '1', '銀劍');
INSERT INTO `drop_item` VALUES ('100027', '1', '1', '彎刀');
INSERT INTO `drop_item` VALUES ('100098', '1', '1', '闊矛');
INSERT INTO `drop_item` VALUES ('100102', '1', '1', '露西錘');
INSERT INTO `drop_item` VALUES ('100103', '1', '1', '戟');
INSERT INTO `drop_item` VALUES ('100143', '1', '1', '戰斧');
INSERT INTO `drop_item` VALUES ('100172', '1', '1', '弓');
INSERT INTO `drop_item` VALUES ('120043', '1', '1', '鋼盔');
INSERT INTO `drop_item` VALUES ('120101', '1', '1', '皮甲');
INSERT INTO `drop_item` VALUES ('120149', '1', '1', '青銅盔甲');
INSERT INTO `drop_item` VALUES ('120182', '1', '1', '手套');
INSERT INTO `drop_item` VALUES ('120242', '1', '1', '大盾牌');
INSERT INTO `drop_item` VALUES ('200001', '1', '1', '歐西斯匕首');
INSERT INTO `drop_item` VALUES ('200027', '1', '1', '彎刀');
INSERT INTO `drop_item` VALUES ('200171', '1', '1', '歐西斯弓');
INSERT INTO `drop_item` VALUES ('220034', '1', '1', '歐西斯頭盔');
INSERT INTO `drop_item` VALUES ('220043', '1', '1', '鋼盔');
INSERT INTO `drop_item` VALUES ('220101', '1', '1', '皮甲');
INSERT INTO `drop_item` VALUES ('220115', '1', '1', '籐甲');
INSERT INTO `drop_item` VALUES ('220122', '1', '1', '鱗甲');
INSERT INTO `drop_item` VALUES ('220125', '1', '1', '鏈甲');
INSERT INTO `drop_item` VALUES ('220135', '1', '1', '歐西斯環甲');
INSERT INTO `drop_item` VALUES ('220136', '1', '1', '歐西斯鏈甲');
INSERT INTO `drop_item` VALUES ('220147', '1', '1', '銀釘皮甲');
INSERT INTO `drop_item` VALUES ('220213', '1', '1', '短統靴');
INSERT INTO `drop_item` VALUES ('220237', '1', '1', '阿克海盾牌');
