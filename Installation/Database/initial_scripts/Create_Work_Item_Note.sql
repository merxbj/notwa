-- Create Work_Items_Notes table
CREATE TABLE `work_item_note` (
  `note_id` int(19) NOT NULL,
  `work_item_id` int(19) NOT NULL,
  `author_user_id` int(19) NOT NULL,
  `note` text,
  PRIMARY KEY (`note_id`,`work_item_id`),
  KEY `FK_Work_Item_Note_Work_Item_work_item_id` (`work_item_id`),
  KEY `FK_Work_Item_Note_User_author_user_id` (`author_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;