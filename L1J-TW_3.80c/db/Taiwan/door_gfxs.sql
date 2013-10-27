/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2011-06-12 12:32:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for door_gfxs
-- ----------------------------
DROP TABLE IF EXISTS `door_gfxs`;
CREATE TABLE `door_gfxs` (
  `gfxid` int(11) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `direction` int(11) NOT NULL,
  `left_edge_offset` int(11) NOT NULL,
  `right_edge_offset` int(11) NOT NULL,
  PRIMARY KEY (`gfxid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of door_gfxs
-- ----------------------------
INSERT INTO `door_gfxs` VALUES ('88', '話せる島の洞窟2階', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('89', '話せる島の洞窟2階', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('92', 'Pagos Room', '0', '0', '1');
INSERT INTO `door_gfxs` VALUES ('93', 'Pagos Room', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('339', 'Kent Castle', '0', '-1', '0');
INSERT INTO `door_gfxs` VALUES ('442', 'Windawood', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('443', 'Dwarf Castle', '1', '0', '1');
INSERT INTO `door_gfxs` VALUES ('444', 'SKT', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('446', 'SKT', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('447', 'SKT', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('448', 'SKT', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('766', 'Orcish Forest', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('767', 'Orcish Forest', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('768', 'Orcish Forest', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('845', 'SKT', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1010', 'SKT', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1327', 'Giran', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1329', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1330', 'Giran', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1331', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1332', 'Giran', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1333', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1334', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1336', 'Giran Castle', '0', '-1', '0');
INSERT INTO `door_gfxs` VALUES ('1338', 'Giran Castle', '1', '0', '1');
INSERT INTO `door_gfxs` VALUES ('1341', 'Orcish Forest', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1342', 'Giran', '1', '0', '1');
INSERT INTO `door_gfxs` VALUES ('1347', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1348', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1349', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1350', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1351', 'Giran', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1352', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1371', 'Giran', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1373', 'Giran', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1487', 'Giran Dog Race', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1664', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1665', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1688', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1689', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1690', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1691', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1692', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1700', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1734', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1735', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1736', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1737', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1738', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1739', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1740', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1741', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1743', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1744', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1745', 'Heine Castle', '0', '-2', '0');
INSERT INTO `door_gfxs` VALUES ('1750', 'Heine', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1751', 'Heine', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('1826', 'Dwarf Castle', '1', '-2', '1');
INSERT INTO `door_gfxs` VALUES ('1827', 'Dwarf Castle', '0', '-1', '2');
INSERT INTO `door_gfxs` VALUES ('2083', 'Hidden Valley', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2089', 'Hidden Valley', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2114', 'Hidden Valley', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2128', 'Hidden Valley', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2160', 'Oren', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2161', 'Oren', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2162', 'Oren', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2163', 'Oren', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2164', 'Oren', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2190', 'Oren', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2191', 'Oren', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2301', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2303', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2304', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2305', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2306', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2344', 'Talking Island', '1', '-364', '-364');
INSERT INTO `door_gfxs` VALUES ('2345', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2346', 'Talking Island', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2510', 'race', '1', '-3', '4');
INSERT INTO `door_gfxs` VALUES ('2556', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2558', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2574', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2575', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2578', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2579', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2580', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2581', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2582', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2583', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2585', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2588', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2589', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2590', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2591', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2596', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2597', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2598', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2599', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2600', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2603', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2605', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2606', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2608', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2610', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2628', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2629', 'Aden', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2630', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2631', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2634', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2635', 'Aden', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('2732', 'OT Castle', '0', '-1', '2');
INSERT INTO `door_gfxs` VALUES ('2736', 'Kent Castle', '1', '-2', '1');
INSERT INTO `door_gfxs` VALUES ('2744', 'Giran Castle', '0', '0', '3');
INSERT INTO `door_gfxs` VALUES ('2745', 'Giran Castle', '0', '-1', '2');
INSERT INTO `door_gfxs` VALUES ('2746', 'Giran Castle', '1', '-2', '1');
INSERT INTO `door_gfxs` VALUES ('3234', 'Kent Castle', '0', '-2', '1');
INSERT INTO `door_gfxs` VALUES ('6026', 'Gludio', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6027', 'Gludio', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6028', 'Gludio', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6029', 'Gludio', '0', '0', '1');
INSERT INTO `door_gfxs` VALUES ('6030', 'Gludio', '0', '0', '1');
INSERT INTO `door_gfxs` VALUES ('6031', 'Gludio', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6032', 'Gludio', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6033', 'Gludio', '1', '0', '0');
INSERT INTO `door_gfxs` VALUES ('6336', 'Haunted House', '0', '-1', '1');
INSERT INTO `door_gfxs` VALUES ('6351', 'Haunted House', '1', '-3', '3');
INSERT INTO `door_gfxs` VALUES ('6379', 'Haunted House', '1', '0', '2');
INSERT INTO `door_gfxs` VALUES ('6640', '水晶の洞窟1階', '1', '-1', '3');
INSERT INTO `door_gfxs` VALUES ('6642', '水晶の洞窟2階', '0', '-2', '2');
INSERT INTO `door_gfxs` VALUES ('6677', 'race', '1', '-3', '4');
INSERT INTO `door_gfxs` VALUES ('6692', 'Death match', '0', '0', '1');
INSERT INTO `door_gfxs` VALUES ('6694', 'Death match', '1', '-1', '0');
INSERT INTO `door_gfxs` VALUES ('7536', 'Hardin Quest', '0', '0', '0');
INSERT INTO `door_gfxs` VALUES ('7556', '安塔瑞斯洞穴', '1', '-2', '2');
INSERT INTO `door_gfxs` VALUES ('7858', '法利昂洞穴', '0', '-2', '2');
INSERT INTO `door_gfxs` VALUES ('7859', '法利昂洞穴', '1', '-2', '2');
