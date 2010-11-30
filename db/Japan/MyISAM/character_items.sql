#----------------------------
# Table structure for character_items
#----------------------------
CREATE TABLE `character_items` (
  `id` int(11) NOT NULL default '0',
  `item_id` int(11) default NULL,
  `char_id` int(11) default NULL,
  `item_name` varchar(255) default NULL,
  `count` int(11) default NULL,
  `is_equipped` int(11) default NULL,
  `enchantlvl` int(11) default NULL,
  `is_id` int(11) default NULL,
  `durability` int(11) default NULL,
  `charge_count` int(11) default NULL,
  `remaining_time` int(11) default NULL,
  `last_used` datetime default NULL,
  `bless` int(11) default NULL,
  `attr_enchant_kind` int(11) default NULL,
  `attr_enchant_level` int(11) default NULL,
 PRIMARY KEY  (`id`),
  KEY `key_id` (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# No records for table character_items
#----------------------------


