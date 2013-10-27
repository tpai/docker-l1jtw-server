#----------------------------
# Table structure for clan_data
#----------------------------
CREATE TABLE `clan_data` (
  `clan_id` int(10) unsigned NOT NULL auto_increment,
  `clan_name` varchar(45) NOT NULL default '',
  `leader_id` int(10) unsigned NOT NULL default '0',
  `leader_name` varchar(45) NOT NULL default '',
  `hascastle` int(10) unsigned NOT NULL default '0',
  `hashouse` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`clan_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table clan_data
#----------------------------


