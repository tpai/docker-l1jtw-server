#----------------------------
# Table structure for character_buddys
#----------------------------
CREATE TABLE `character_buddys` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `char_id` int(10) NOT NULL default '0',
  `buddy_id` int(10) unsigned NOT NULL default '0',
  `buddy_name` varchar(45) NOT NULL, 
  PRIMARY KEY  (`char_id`,`buddy_id`),
  KEY `key_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB; MyISAM free: 10240 kB';
#----------------------------
# No records for table character_buddys
#----------------------------


