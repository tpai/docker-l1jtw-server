#----------------------------
# Table structure for character_teleport
#----------------------------
CREATE TABLE `character_teleport` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `char_id` int(10) unsigned NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `locx` int(10) unsigned NOT NULL default '0',
  `locy` int(10) unsigned NOT NULL default '0',
  `mapid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `key_id` (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table character_teleport
#----------------------------

