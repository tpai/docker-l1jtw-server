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
-- Table structure for getback_restart
-- ----------------------------
CREATE TABLE `getback_restart` (
  `area` int(10) NOT NULL DEFAULT '0',
  `note` varchar(50) DEFAULT NULL,
  `locx` int(10) NOT NULL DEFAULT '0',
  `locy` int(10) NOT NULL DEFAULT '0',
  `mapid` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`area`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `getback_restart` VALUES ('5', 'Talking Island Ship to Aden Mainland', '32631', '32983', '0');
INSERT INTO `getback_restart` VALUES ('6', 'Aden Mainland Ship to Talking Island', '32543', '32728', '4');
INSERT INTO `getback_restart` VALUES ('70', '忘れられた島', '32828', '32848', '70');
INSERT INTO `getback_restart` VALUES ('75', '象牙の塔:1階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('76', '象牙の塔:2階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('77', '象牙の塔:3階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('78', '象牙の塔:4階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('79', '象牙の塔:5階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('80', '象牙の塔:6階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('81', '象牙の塔:7階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('82', '象牙の塔:8階', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('83', 'Aden Mainland Ship to Forgotten Island', '33426', '33499', '4');
INSERT INTO `getback_restart` VALUES ('84', 'Forgotten Island Ship to Aden Mainland', '32936', '33057', '70');
INSERT INTO `getback_restart` VALUES ('88', 'Giran Colosseum', '33442', '32797', '0');
INSERT INTO `getback_restart` VALUES ('91', 'Talking island Colosseum', '32580', '32931', '4');
INSERT INTO `getback_restart` VALUES ('92', 'Gludio Colosseum', '32612', '32734', '0');
INSERT INTO `getback_restart` VALUES ('95', 'Silver knight Colosseum', '33080', '33392', '4');
INSERT INTO `getback_restart` VALUES ('98', 'Welldone Colosseum', '33705', '32504', '4');
INSERT INTO `getback_restart` VALUES ('101', '傲慢の塔1F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('102', '傲慢の塔2F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('103', '傲慢の塔3F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('104', '傲慢の塔4F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('105', '傲慢の塔5F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('106', '傲慢の塔6F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('107', '傲慢の塔7F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('108', '傲慢の塔8F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('109', '傲慢の塔9F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('110', '傲慢の塔10F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('111', '傲慢の塔11F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('112', '傲慢の塔12F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('113', '傲慢の塔13F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('114', '傲慢の塔14F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('115', '傲慢の塔15F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('116', '傲慢の塔16F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('117', '傲慢の塔17F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('118', '傲慢の塔18F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('119', '傲慢の塔19F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('120', '傲慢の塔20F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('121', '傲慢の塔21F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('122', '傲慢の塔22F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('123', '傲慢の塔23F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('124', '傲慢の塔24F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('125', '傲慢の塔25F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('126', '傲慢の塔26F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('127', '傲慢の塔27F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('128', '傲慢の塔28F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('129', '傲慢の塔29F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('130', '傲慢の塔30F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('131', '傲慢の塔31F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('132', '傲慢の塔32F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('133', '傲慢の塔33F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('134', '傲慢の塔34F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('135', '傲慢の塔35F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('136', '傲慢の塔36F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('137', '傲慢の塔37F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('138', '傲慢の塔38F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('139', '傲慢の塔39F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('140', '傲慢の塔40F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('141', '傲慢の塔41F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('142', '傲慢の塔42F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('143', '傲慢の塔43F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('144', '傲慢の塔44F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('145', '傲慢の塔45F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('146', '傲慢の塔46F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('147', '傲慢の塔47F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('148', '傲慢の塔48F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('149', '傲慢の塔49F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('150', '傲慢の塔50F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('151', '傲慢の塔51F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('152', '傲慢の塔52F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('153', '傲慢の塔53F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('154', '傲慢の塔54F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('155', '傲慢の塔55F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('156', '傲慢の塔56F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('157', '傲慢の塔57F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('158', '傲慢の塔58F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('159', '傲慢の塔59F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('160', '傲慢の塔60F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('161', '傲慢の塔61F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('162', '傲慢の塔62F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('163', '傲慢の塔63F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('164', '傲慢の塔64F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('165', '傲慢の塔65F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('166', '傲慢の塔66F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('167', '傲慢の塔67F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('168', '傲慢の塔68F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('169', '傲慢の塔69F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('170', '傲慢の塔70F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('171', '傲慢の塔71F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('172', '傲慢の塔72F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('173', '傲慢の塔73F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('174', '傲慢の塔74F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('175', '傲慢の塔75F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('176', '傲慢の塔76F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('177', '傲慢の塔77F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('178', '傲慢の塔78F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('179', '傲慢の塔79F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('180', '傲慢の塔80F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('181', '傲慢の塔81F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('182', '傲慢の塔82F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('183', '傲慢の塔83F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('184', '傲慢の塔84F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('185', '傲慢の塔85F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('186', '傲慢の塔86F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('187', '傲慢の塔87F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('188', '傲慢の塔88F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('189', '傲慢の塔89F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('190', '傲慢の塔90F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('191', '傲慢の塔91F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('192', '傲慢の塔92F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('193', '傲慢の塔93F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('194', '傲慢の塔94F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('195', '傲慢の塔95F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('196', '傲慢の塔96F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('197', '傲慢の塔97F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('198', '傲慢の塔98F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('199', '傲慢の塔99F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('200', '傲慢の塔100F', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('303', '夢幻の島', '33976', '32936', '4');
INSERT INTO `getback_restart` VALUES ('446', 'Ship Pirate island to Hidden dock', '32297', '33087', '440');
INSERT INTO `getback_restart` VALUES ('447', 'Ship Hidden dock to Pirate island', '32750', '32874', '445');
INSERT INTO `getback_restart` VALUES ('451', 'ラスタバ城:集会場1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('452', 'ラスタバ城:突撃隊訓練場1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('453', 'ラスタバ城:魔獣軍王の執務室1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('454', 'ラスタバ城:野獣調教室1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('455', 'ラスタバ城:野獣訓練室1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('456', 'ラスタバ城:魔獣召喚室1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('460', 'ラスタバ城:黒魔法訓練場2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('461', 'ラスタバ城:黒魔法研究室2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('462', 'ラスタバ城:魔霊軍王の執務室2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('463', 'ラスタバ城:魔霊軍王の書斎2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('464', 'ラスタバ城:精霊召喚室2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('465', 'ラスタバ城:精霊の生息地2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('466', 'ラスタバ城:闇の精霊研究室2F', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('470', 'ラスタバ城:悪霊の祭壇3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('471', 'ラスタバ城:デビルロードの祭壇3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('472', 'ラスタバ城:傭兵訓練場3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('473', 'ラスタバ城:冥法軍の訓練場3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('474', 'ラスタバ城:オーム実験室3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('475', 'ラスタバ城:冥法軍王の執務室3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('476', 'ラスタバ城:中央コントロールルーム3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('477', 'ラスタバ城:デビルロードの傭兵室3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('478', 'ラスタバ城:立入禁止エリア3F', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('490', 'ラスタバ城:地下訓練場B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('491', 'ラスタバ城:地下通路B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('492', 'ラスタバ城:暗殺軍王の執務室B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('493', 'ラスタバ城:地下コントロールルームB1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('494', 'ラスタバ城:地下処刑場B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('495', 'ラスタバ城:地下決闘場B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('496', 'ラスタバ城:地下牢B1F', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('530', 'ラスタバ城:グランカインの神殿/ケイナの部屋', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('531', 'ラスタバ城:ビアタス/バロメス/エンディアスの部屋', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('532', 'ラスタバ城:庭園/イデアの部屋', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('533', 'ラスタバ城:ティアメス/ラミアス/バロードの部屋', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('534', 'ラスタバ城:カサンドラ/ダンテスの部屋', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('535', 'ダークエルフの聖地', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('550', '船の墓場:地上層', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('551', '船の墓場:大型船内1F', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('552', '船の墓場:大型船内1F(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('554', '船の墓場:大型船内2F', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('555', '船の墓場:大型船内2F(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('557', '船の墓場:船内', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('558', '船の墓場:深海層', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('600', '欲望の洞窟外周部', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('601', '欲望の洞窟ロビー', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('608', 'ヤヒの実験室', '34053', '32284', '4');
INSERT INTO `getback_restart` VALUES ('777', '見棄てられた者たちの地(空間の歪)', '34043', '32184', '4');
INSERT INTO `getback_restart` VALUES ('778', '見棄てられた者たちの地(次元の門・地上)', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('779', '見棄てられた者たちの地(次元の門・海底)', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('780', 'テーベ砂漠', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('781', 'テーベ ピラミッド内部', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('782', 'テーベ オシリス祭壇', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('5124', 'Fishing place', '32815', '32809', '5124');
INSERT INTO `getback_restart` VALUES ('5125', 'petmatch place', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5131', 'petmatch place', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5132', 'petmatch place', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5133', 'petmatch place', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5134', 'petmatch place', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5140', 'お化け屋敷', '32624', '32813', '4');
INSERT INTO `getback_restart` VALUES ('5143', 'race', '32628', '32772', '4');
INSERT INTO `getback_restart` VALUES ('16384', 'Talking Island Hotel', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('16896', 'Talking Island Hotel', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('17408', 'Gludio Hotel', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('17920', 'Gludio Hotel', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('18432', 'Giran Hotel', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('18944', 'Giran Hotel', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('19456', 'Oren Hotel', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('19968', 'Oren Hotel', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('20480', 'Windawood Hotel', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('20992', 'Windawood Hotel', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('21504', 'SKT Hotel', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22016', 'SKT Hotel', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22528', 'Heine Hotel', '33604', '33276', '4');
INSERT INTO `getback_restart` VALUES ('23040', 'Heine Hotel', '33604', '33276', '4');
INSERT INTO `getback_restart` VALUES ('2005', '新隱藏之谷', '32682', '32874', '2005');
