-- Create Projects table
CREATE TABLE `database_upgrade` (
  `update_script_id` int(19) unsigned NOT NULL AUTO_INCREMENT,
  `full_file_name` varchar(2048) NOT NULL,
  `executed_timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`update_script_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;