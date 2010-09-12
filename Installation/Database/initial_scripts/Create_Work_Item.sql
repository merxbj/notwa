-- Create Work_Items table
CREATE TABLE `work_item` (
  `work_item_id` int(19) NOT NULL,
  `assigned_user_id` int(19) DEFAULT NULL,
  `project_id` int(19) NOT NULL,
  `parent_work_item_id` int(19) DEFAULT NULL,
  `subject` varchar(255) NOT NULL,
  `status_id` smallint(6) NOT NULL,
  `working_priority` smallint(6) NOT NULL,
  `description` text,
  `expected_timestamp` datetime DEFAULT NULL,
  `last_modified_timestamp` datetime NOT NULL,
  PRIMARY KEY (`work_item_id`),
  KEY `FK_Work_Item_Project_assigned_project_id` (`assigned_user_id`),
  KEY `FK_Work_Item_User_parent_work_item_id` (`parent_work_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;