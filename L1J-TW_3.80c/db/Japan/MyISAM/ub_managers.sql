#----------------------------
# Table structure for ub_managers
#----------------------------
CREATE TABLE `ub_managers` (
  `ub_id` int(10) unsigned NOT NULL default '0',
  `ub_manager_npc_id` int(10) unsigned NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#----------------------------
# Records for table ub_managers
#----------------------------
insert  into ub_managers values 
(1, 50037),
(1, 50038),
(2, 50041),
(2, 50042),
(3, 50028),
(3, 50029),
(4, 50018),
(4, 50019),
(5, 50061),
(5, 50062);