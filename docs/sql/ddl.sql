-- Generated 2025-07-10 10:22:08-0600 for database version 1

CREATE TABLE IF NOT EXISTS `user` (`user_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL COLLATE NOCASE, `oauth_key` TEXT NOT NULL, `display_name` TEXT NOT NULL COLLATE NOCASE, `created` INTEGER, `modified` INTEGER NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_oauth_key` ON `user` (`oauth_key`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_display_name` ON `user` (`display_name`);

CREATE TABLE IF NOT EXISTS `player` (`player_id` TEXT NOT NULL, `owner_id` INTEGER NOT NULL, `modified` INTEGER NOT NULL, `player_name` TEXT NOT NULL, `position` TEXT, `team_mlb` TEXT, `avg` REAL NOT NULL, `babip` REAL NOT NULL, `barrel_rate` REAL NOT NULL, `delta` INTEGER NOT NULL, `exit_velo` REAL NOT NULL, `launch_angle` REAL NOT NULL, PRIMARY KEY(`player_id`));

CREATE UNIQUE INDEX IF NOT EXISTS `index_player_player_id` ON `player` (`player_id`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_player_player_name` ON `player` (`player_name`);

CREATE TABLE IF NOT EXISTS `team` (`team_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL COLLATE NOCASE, `team_name` TEXT NOT NULL COLLATE NOCASE, `owner_id` INTEGER NOT NULL, `created` INTEGER, `modified` INTEGER NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS `index_team_team_id` ON `team` (`team_id`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_team_team_name` ON `team` (`team_name`);

CREATE INDEX IF NOT EXISTS `index_team_owner_id` ON `team` (`owner_id`);

CREATE TABLE IF NOT EXISTS `note` (`note_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL COLLATE NOCASE, `description` TEXT, `created` INTEGER NOT NULL, `modified` INTEGER, `user_id` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE );

CREATE INDEX IF NOT EXISTS `index_note_title` ON `note` (`title`);

CREATE INDEX IF NOT EXISTS `index_note_created` ON `note` (`created`);

CREATE INDEX IF NOT EXISTS `index_note_modified` ON `note` (`modified`);

CREATE INDEX IF NOT EXISTS `index_note_user_id` ON `note` (`user_id`);

CREATE TABLE IF NOT EXISTS `image` (`image_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `caption` TEXT COLLATE NOCASE, `mime_type` TEXT, `uri` TEXT NOT NULL, `created` INTEGER NOT NULL, `note_id` INTEGER NOT NULL, FOREIGN KEY(`note_id`) REFERENCES `note`(`note_id`) ON UPDATE NO ACTION ON DELETE CASCADE );

CREATE INDEX IF NOT EXISTS `index_image_mime_type` ON `image` (`mime_type`);

CREATE INDEX IF NOT EXISTS `index_image_created` ON `image` (`created`);

CREATE INDEX IF NOT EXISTS `index_image_note_id` ON `image` (`note_id`);

CREATE TABLE IF NOT EXISTS `player_team` (`player_id` TEXT NOT NULL, `team_id` INTEGER NOT NULL, PRIMARY KEY(`player_id`, `team_id`), FOREIGN KEY(`team_id`) REFERENCES `team`(`team_id`) ON UPDATE NO ACTION ON DELETE CASCADE );

CREATE INDEX IF NOT EXISTS `index_player_team_player_id` ON `player_team` (`player_id`);

CREATE INDEX IF NOT EXISTS `index_player_team_team_id` ON `player_team` (`team_id`);

CREATE TABLE IF NOT EXISTS `user_player` (`user_id` TEXT NOT NULL, `player_id` TEXT NOT NULL, PRIMARY KEY(`user_id`, `player_id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE );