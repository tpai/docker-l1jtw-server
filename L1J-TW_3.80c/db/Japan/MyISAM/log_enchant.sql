#----------------------------
# Table structure for log_enchant
#----------------------------
CREATE TABLE `log_enchant` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `char_id` int(10) NOT NULL default '0',
  `item_id` int(10) unsigned NOT NULL default '0',
  `old_enchantlvl` int(3) NOT NULL default '0',
  `new_enchantlvl` int(3) default '0',
  PRIMARY KEY  (`id`),
  KEY `key_id` (`char_id`)  
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB; MyISAM free: 10240 kB';
#----------------------------
# No records for table log_enchant
#----------------------------

