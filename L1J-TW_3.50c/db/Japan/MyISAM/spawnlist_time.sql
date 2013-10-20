#----------------------------
# Table structure for spawnlist_time
#----------------------------
CREATE TABLE `spawnlist_time` (
  `spawn_id` int(11) NOT NULL,
  `time_start` time default NULL,
  `time_end` time default NULL,
  `delete_at_endtime` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`spawn_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
#----------------------------
# Records for table spawnlist_time
#----------------------------
INSERT INTO `spawnlist_time` VALUES
(62086, '18:00:00', '04:00:00', 1),
(62087, '18:00:00', '04:00:00', 1),
(62088, '18:00:00', '04:00:00', 1),
(62089, '18:00:00', '04:00:00', 1),
(62092, '18:00:00', '04:00:00', 1);
