#----------------------------
# Table structure for weapon_skill
#----------------------------
CREATE TABLE `weapon_skill` (
  `weapon_id` int(11) unsigned NOT NULL auto_increment,
  `note` varchar(255) default NULL,
  `probability` int(11) unsigned NOT NULL default '0',
  `fix_damage` int(11) unsigned NOT NULL default '0',
  `random_damage` int(11) unsigned NOT NULL default '0',
  `area` int(11) NOT NULL default '0',
  `skill_id` int(11) unsigned NOT NULL default '0',
  `skill_time` int(11) unsigned NOT NULL default '0',
  `effect_id` int(11) unsigned NOT NULL default '0',
  `effect_target` int(11) unsigned NOT NULL default '0',
  `arrow_type` int(11) unsigned NOT NULL default '0',
  `attr` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`weapon_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MyISAM free: 10240 kB';
#----------------------------
# Records for table weapon_skill
#----------------------------


insert  into weapon_skill values 
(47, 'サイレンス ソード', 2, 0, 0, 0, 64, 16, 2177, 0, 0, 0),
(54, 'カーツ ソード', 15, 35, 25, 0, 0, 0, 10, 0, 0, 8),
(58, 'デスナイト フレイムブレード', 7, 75, 15, 0, 0, 0, 1811, 0, 0, 2),
(76, 'ロンドゥ デュアル ブレード', 15, 35, 25, 0, 0, 0, 1805, 0, 0, 1),
(121, 'アイスクイーン スタッフ', 25, 95, 55, 0, 0, 0, 1810, 0, 0, 4),
(203, 'バルログのツーハンド ソード', 15, 90, 90, 2, 0, 0, 762, 0, 0, 2),
(205, 'ルナ ロング ボウ', 5, 8, 0, 0, 0, 0, 6288, 0, 1, 0),
(256, 'ハロウィン パンプキン ロングソード', 8, 35, 25, 0, 0, 0, 2750, 0, 0, 1),
(257, 'ハロウィン ロングソード', 8, 35, 25, 0, 0, 0, 2750, 0, 0, 1),
(258, 'アルティメット ハロウィン ロングソード', 8, 35, 25, 0, 0, 0, 2750, 0, 0, 1);
