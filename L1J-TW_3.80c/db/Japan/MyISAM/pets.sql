#----------------------------
# Table structure for pets
#----------------------------
CREATE TABLE `pets` (
  `item_obj_id` int(10) unsigned NOT NULL default '0',
  `objid` int(10) unsigned NOT NULL default '0',
  `npcid` int(10) unsigned NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `lvl` int(10) unsigned NOT NULL default '0',
  `hp` int(10) unsigned NOT NULL default '0',
  `mp` int(10) unsigned NOT NULL default '0',
  `exp` int(10) unsigned NOT NULL default '0',
  `lawful` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`item_obj_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table pets
#----------------------------
