-- Create Project_User_Assigment table
CREATE TABLE `project_user_assignment` (
  `project_id` int(19) NOT NULL,
  `user_id` int(19) NOT NULL,
  PRIMARY KEY (`project_id`,`user_id`),
  UNIQUE KEY `PRIMARY_KEY` (`project_id`,`user_id`),
  KEY `FK_Project_User_Assigment_User_user_id` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;