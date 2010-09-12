-- Create Projects table

CREATE TABLE `project` (
  `project_id` int(19) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;