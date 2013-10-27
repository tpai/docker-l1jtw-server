#----------------------------
# Table structure for letter
#----------------------------
CREATE TABLE `letter` (
  `item_object_id` int(10) unsigned NOT NULL default '0',
  `code` int(10) unsigned NOT NULL default '0',
  `sender` varchar(16) default NULL,
  `receiver` varchar(16) default NULL,
  `date` varchar(16) default NULL,
  `template_id` int(5) unsigned NOT NULL default '0',
  `subject` BLOB,
  `content` BLOB,
  PRIMARY KEY (`item_object_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No Records for table letter
#----------------------------


