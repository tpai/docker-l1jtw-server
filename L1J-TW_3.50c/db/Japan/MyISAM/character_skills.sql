#----------------------------
# Table structure for character_skills
#----------------------------
CREATE TABLE `character_skills` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `char_obj_id` int(10) NOT NULL default '0',
  `skill_id` int(10) unsigned NOT NULL default '0',
  `skill_name` varchar(45) NOT NULL default '',
  `is_active` int(10) default NULL,
  `activetimeleft` int(10) default NULL,
  PRIMARY KEY  (`char_obj_id`,`skill_id`),
  KEY `key_id` (`id`)  
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB; MyISAM free: 10240 kB';
#----------------------------
# No records for table character_skills
#----------------------------

