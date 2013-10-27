#----------------------------
# Table structure for board
#----------------------------
CREATE TABLE `board` (
  `id` int(10) NOT NULL default '0',
  `name` varchar(16) default NULL,
  `date` varchar(16) default NULL,
  `title` varchar(16) default NULL,
  `content` varchar(1000) default NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No Records for table board
#----------------------------


