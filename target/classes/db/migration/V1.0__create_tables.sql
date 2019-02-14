CREATE TABLE if not Exists `project_type` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(224) NOT NULL,
  `type` varchar(224) NOT NULL,
  PRIMARY KEY  (`id`)
);
CREATE TABLE if not Exists `framework` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(224) NOT NULL,
  `core_library` varchar(224) NOT NULL,
  `type_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
);
CREATE TABLE if not Exists `entry_point` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(224) NOT NULL,
  `type` varchar(224) NOT NULL,
  `location` varchar(224) NOT NULL,
  `framework_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
);

