#----------------------------
# Table structure for mail
#----------------------------
CREATE TABLE `mail` (
  `id` int(10) unsigned NOT NULL default '0',
  `type` int(10) unsigned NOT NULL default '0',
  `sender` varchar(16) default NULL,
  `receiver` varchar(16) default NULL,
  `date` varchar(16) default NULL,
  `read_status` tinyint(1) unsigned NOT NULL default '0',
  `subject` BLOB,
  `content` BLOB,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No Records for table mail
#----------------------------


