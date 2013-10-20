#----------------------------
# Table structure for castle
#----------------------------
CREATE TABLE `castle` (
  `castle_id` int(11) NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `war_time` datetime,
  `tax_rate` int(11) NOT NULL default '0',
  `public_money` int(11) NOT NULL default '0',
  PRIMARY KEY  (`castle_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


#----------------------------
# Records for table castle
#----------------------------
insert  into castle values 
('1', 'ケント城', '2007-10-03 22:00:00', '10', '0'),
('2', 'オークの森', '2007-10-04 22:00:00', '10', '0'),
('3', 'ウィンダウッド城', '2007-10-05 22:00:00', '10', '0'),
('4', 'ギラン城', '2007-10-06 22:00:00', '10', '0'),
('5', 'ハイネ城', '2007-10-03 22:00:00', '10', '0'),
('6', 'ドワーフ城', '2007-10-04 22:00:00', '10', '0'),
('7', 'アデン城', '2007-10-05 22:00:00', '10', '0'),
('8', 'ディアド要塞', '2007-10-06 22:00:00', '10', '0');
