#----------------------------
# Table structure for town
#----------------------------
CREATE TABLE `town` (
  `town_id` int(10) unsigned NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `leader_id` int(10) unsigned NOT NULL default '0',
  `leader_name` varchar(45) default NULL,
  `tax_rate` int(10) unsigned NOT NULL default '0',
  `tax_rate_reserved` int(10) unsigned NOT NULL default '0',
  `sales_money` int(10) unsigned NOT NULL default '0',
  `sales_money_yesterday` int(10) unsigned NOT NULL default '0',
  `town_tax` int(10) unsigned NOT NULL default '0',
  `town_fix_tax` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`town_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#----------------------------
# Records 
#----------------------------
INSERT INTO `town` VALUES ('1', '話せる島の村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('2', 'シルバーナイトタウン', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('3', 'グルーディン村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('4', '火田民村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('5', 'ウッドベック村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('6', 'ケント村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('7', 'ギラン都市', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('8', 'ハイネ都市', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('9', 'ウェルダン村', '0', null, '0', '0', '0', '0', '0', '0');
INSERT INTO `town` VALUES ('10', '象牙の塔の村', '0', null, '0', '0', '0', '0', '0', '0');
