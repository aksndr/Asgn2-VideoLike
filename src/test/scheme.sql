CREATE DATABASE `videos` /*!40100 DEFAULT CHARACTER SET utf8 */;

DROP TABLE IF EXISTS `videos`.`likes`;
CREATE TABLE  `videos`.`likes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `video_id` int(10) unsigned DEFAULT NULL,
  `user` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_likes_1` (`video_id`) USING BTREE,
  CONSTRAINT `FK_likes_1` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `videos`.`videos`;
CREATE TABLE  `videos`.`videos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL,
  `duration` int(10) unsigned NOT NULL,
  `likes` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;