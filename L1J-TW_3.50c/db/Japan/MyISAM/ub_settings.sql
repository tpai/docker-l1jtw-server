#----------------------------
# Table structure for ub_settings
#----------------------------
CREATE TABLE `ub_settings` (
  `ub_id` int(10) unsigned NOT NULL default '0',
  `ub_name` varchar(45) NOT NULL default '',
  `ub_mapid` int(10) unsigned NOT NULL default '0',
  `ub_area_x1` int(10) unsigned NOT NULL default '0', -- モンスターの出現範囲など
  `ub_area_y1` int(10) unsigned NOT NULL default '0',
  `ub_area_x2` int(10) unsigned NOT NULL default '0',
  `ub_area_y2` int(10) unsigned NOT NULL default '0',
  `min_lvl` int(10) unsigned NOT NULL default '0',
  `max_lvl` int(10) unsigned NOT NULL default '0',
  `max_player` int(10) unsigned NOT NULL default '0',
  `enter_royal` tinyint unsigned NOT NULL default '0',
  `enter_knight` tinyint unsigned NOT NULL default '0',
  `enter_mage` tinyint unsigned NOT NULL default '0',
  `enter_elf` tinyint unsigned NOT NULL default '0',
  `enter_darkelf` tinyint unsigned NOT NULL default '0',
  `enter_dragonknight` tinyint unsigned NOT NULL default '0',
  `enter_illusionist` tinyint unsigned NOT NULL default '0',
  `enter_male` tinyint unsigned NOT NULL default '0',
  `enter_female` tinyint unsigned NOT NULL default '0',
  `use_pot` tinyint unsigned NOT NULL default '0',
  `hpr_bonus` int(10) NOT NULL default '0',
  `mpr_bonus` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ub_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#----------------------------
# Records for table ub_settings
#----------------------------
insert  into ub_settings values 
(1, 'ギランUB', 88, 33494, 32724, 33516, 32746, 52, 99, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0),
(2, 'ウェルダンUB', 98, 32682, 32878, 32717, 32913, 45, 60, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0),
(3, 'グルーディンUB', 92, 32682, 32878, 32717, 32913, 31, 51, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0),
(4, 'TIUB', 91, 32682, 32878, 32717, 32913, 25, 44, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0),
(5, 'SKTUB', 95, 32682, 32878, 32717, 32913, 1, 30, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0);
