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
-- Table structure for commands
-- ----------------------------
CREATE TABLE `commands` (
  `name` varchar(255) NOT NULL,
  `access_level` int(10) NOT NULL DEFAULT '200',
  `class_name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `commands` VALUES ('accbankick', '200', 'L1AccountBanKick');
INSERT INTO `commands` VALUES ('action', '200', 'L1Action');
INSERT INTO `commands` VALUES ('addskill', '200', 'L1AddSkill');
INSERT INTO `commands` VALUES ('adena', '200', 'L1Adena');
INSERT INTO `commands` VALUES ('allbuff', '200', 'L1AllBuff');
INSERT INTO `commands` VALUES ('banip', '200', 'L1BanIp');
INSERT INTO `commands` VALUES ('buff', '200', 'L1Buff');
INSERT INTO `commands` VALUES ('castgfx', '200', 'L1CastGfx');
INSERT INTO `commands` VALUES ('chat', '200', 'L1Chat');
INSERT INTO `commands` VALUES ('chatng', '200', 'L1ChatNG');
INSERT INTO `commands` VALUES ('cleaning', '200', 'L1DeleteGroundItem');
INSERT INTO `commands` VALUES ('death', '200', 'L1Kill');
INSERT INTO `commands` VALUES ('desc', '200', 'L1Describe');
INSERT INTO `commands` VALUES ('echo', '200', 'L1Echo');
INSERT INTO `commands` VALUES ('f', '200', 'L1Favorite');
INSERT INTO `commands` VALUES ('findinvis', '200', 'L1FindInvis');
INSERT INTO `commands` VALUES ('gfxid', '200', 'L1GfxId');
INSERT INTO `commands` VALUES ('gm', '200', 'L1GM');
INSERT INTO `commands` VALUES ('gmroom', '200', 'L1GMRoom');
INSERT INTO `commands` VALUES ('help', '200', 'L1CommandHelp');
INSERT INTO `commands` VALUES ('hometown', '200', 'L1HomeTown');
INSERT INTO `commands` VALUES ('hpbar', '200', 'L1HpBar');
INSERT INTO `commands` VALUES ('insert', '200', 'L1InsertSpawn');
INSERT INTO `commands` VALUES ('invgfxid', '200', 'L1InvGfxId');
INSERT INTO `commands` VALUES ('invisible', '200', 'L1Invisible');
INSERT INTO `commands` VALUES ('item', '200', 'L1CreateItem');
INSERT INTO `commands` VALUES ('itemset', '200', 'L1CreateItemSet');
INSERT INTO `commands` VALUES ('kick', '200', 'L1Kick');
INSERT INTO `commands` VALUES ('level', '200', 'L1Level');
INSERT INTO `commands` VALUES ('loc', '200', 'L1Loc');
INSERT INTO `commands` VALUES ('lvpresent', '200', 'L1LevelPresent');
INSERT INTO `commands` VALUES ('move', '200', 'L1Move');
INSERT INTO `commands` VALUES ('partyrecall', '200', 'L1PartyRecall');
INSERT INTO `commands` VALUES ('patrol', '200', 'L1Patrol');
INSERT INTO `commands` VALUES ('poly', '200', 'L1Poly');
INSERT INTO `commands` VALUES ('powerkick', '200', 'L1PowerKick');
INSERT INTO `commands` VALUES ('present', '200', 'L1Present');
INSERT INTO `commands` VALUES ('recall', '200', 'L1Recall');
INSERT INTO `commands` VALUES ('reloadtrap', '200', 'L1ReloadTrap');
INSERT INTO `commands` VALUES ('resettrap', '200', 'L1ResetTrap');
INSERT INTO `commands` VALUES ('ress', '200', 'L1Ress');
INSERT INTO `commands` VALUES ('setting', '200', 'L1Status');
INSERT INTO `commands` VALUES ('showtrap', '200', 'L1ShowTrap');
INSERT INTO `commands` VALUES ('shutdown', '200', 'L1Shutdown');
INSERT INTO `commands` VALUES ('skick', '200', 'L1SKick');
INSERT INTO `commands` VALUES ('spawn', '200', 'L1SpawnCmd');
INSERT INTO `commands` VALUES ('speed', '200', 'L1Speed');
INSERT INTO `commands` VALUES ('summon', '200', 'L1Summon');
INSERT INTO `commands` VALUES ('tile', '200', 'L1Tile');
INSERT INTO `commands` VALUES ('topc', '200', 'L1ToPC');
INSERT INTO `commands` VALUES ('tospawn', '200', 'L1ToSpawn');
INSERT INTO `commands` VALUES ('visible', '200', 'L1Visible');
INSERT INTO `commands` VALUES ('weather', '200', 'L1ChangeWeather');
INSERT INTO `commands` VALUES ('who', '200', 'L1Who');
