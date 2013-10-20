#----------------------------
# Table structure for character_buff
#----------------------------
CREATE TABLE `character_buff` (
  `char_obj_id` int(10) NOT NULL default '0',
  `skill_id` int(10) unsigned NOT NULL default '0',
  `remaining_time` int(10) NOT NULL default '0',
  `poly_id` int(10) default '0',
  PRIMARY KEY  (`char_obj_id`,`skill_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB; MyISAM free: 10240 kB';
#----------------------------
# No records for table character_buff
#----------------------------

