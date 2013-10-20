#----------------------------
# Table structure for character_quests
#----------------------------
CREATE TABLE `character_quests` (
  `char_id` int(10) unsigned NOT NULL,
  `quest_id` int(10) unsigned NOT NULL default '0',
  `quest_step` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`char_id`, `quest_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table character_quests
#----------------------------


