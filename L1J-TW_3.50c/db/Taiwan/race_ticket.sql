/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-03-26 12:18:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `race_ticket`
-- ----------------------------
CREATE TABLE `race_ticket` (
  `item_obj_id` int(11) NOT NULL,
  `round` int(7) NOT NULL,
  `allotment_percentage` double NOT NULL,
  `victory` int(1) NOT NULL,
  `runner_num` int(2) NOT NULL,
  PRIMARY KEY (`item_obj_id`,`round`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
