#----------------------------
# Table structure for beginner
#----------------------------
CREATE TABLE `beginner` (
  `id` int(10) NOT NULL auto_increment,
  `item_id` int(6) NOT NULL default '0',
  `count` int(10) NOT NULL default '0',
  `charge_count` int(10) NOT NULL default '0',
  `enchantlvl` int(6) NOT NULL default '0',
  `item_name` varchar(50) NOT NULL default '',
  `activate` char(1) NOT NULL default 'A',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# Records for table beginner
#----------------------------


insert into `beginner` values 
(1, 40005, 1, 0, 0, 'キャンドル', 'A'),
(2, 40005, 1, 0, 0, 'キャンドル', 'A'),
(3, 40641, 1, 0, 0, 'トーキングスクロール', 'A'),
(4, 40383, 1, 0, 0, '地図：歌う島', 'P'),
(5, 40378, 1, 0, 0, '地図：エルフの森', 'E'),
(6, 40380, 1, 0, 0, '地図：シルバーナイトの村', 'E'),
(7, 40384, 1, 0, 0, '地図：隠された渓谷', 'K'),
(8, 40383, 1, 0, 0, '地図：歌う島', 'W'),
(9, 40389, 1, 0, 0, '地図：沈黙の洞窟', 'D'),
(10, 40383, 1, 0, 0, '地図：歌う島', 'D');
