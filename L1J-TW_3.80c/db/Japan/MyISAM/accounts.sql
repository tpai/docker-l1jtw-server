#----------------------------
# Table structure for accounts
#----------------------------
CREATE TABLE `accounts` (
  `login` varchar(50) NOT NULL default '',
  `password` varchar(50) default NULL,
  `lastactive` datetime default NULL,
  `access_level` int(11) default NULL,
  `ip` varchar(20) NOT NULL default '',
  `host` varchar(255) NOT NULL default '',
  `banned` int(11) unsigned NOT NULL default '0',
  `character_slot` int(2) unsigned NOT NULL default '0',
  PRIMARY KEY  (`login`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 3072 kB';
#----------------------------
# No Records for table accounts
#----------------------------
