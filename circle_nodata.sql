/*
Navicat MySQL Data Transfer

Source Server         : 本地测试
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : circle

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2018-12-16 14:03:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `ann_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ann_status` smallint(5) unsigned NOT NULL DEFAULT '1',
  `ann_create_time` datetime DEFAULT NULL,
  `ann_last_modify_time` datetime DEFAULT NULL,
  `ann_creator_id` bigint(20) unsigned zerofill NOT NULL,
  `ann_mender_id` bigint(20) unsigned zerofill DEFAULT NULL,
  `ann_gro_id` bigint(20) unsigned zerofill NOT NULL,
  `ann_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ann_details` varchar(4095) DEFAULT NULL,
  PRIMARY KEY (`ann_id`),
  KEY `fk_ann_creator_id` (`ann_creator_id`),
  KEY `fk_ann_mender_id` (`ann_mender_id`),
  KEY `fk_ann_gro_id` (`ann_gro_id`),
  CONSTRAINT `fk_ann_creator_id` FOREIGN KEY (`ann_creator_id`) REFERENCES `users` (`u_id`),
  CONSTRAINT `fk_ann_gro_id` FOREIGN KEY (`ann_gro_id`) REFERENCES `thegroup` (`gro_id`),
  CONSTRAINT `fk_ann_mender_id` FOREIGN KEY (`ann_mender_id`) REFERENCES `users` (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcement
-- ----------------------------


-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `com_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `com_status` smallint(5) unsigned NOT NULL DEFAULT '1',
  `com_create_time` datetime DEFAULT NULL,
  `com_last_modify_time` datetime DEFAULT NULL,
  `com_creator_id` bigint(20) unsigned zerofill NOT NULL,
  `com_obj_id` bigint(20) unsigned zerofill NOT NULL,
  `com_href` varchar(255) DEFAULT NULL,
  `com_details` varchar(4095) DEFAULT NULL,
  PRIMARY KEY (`com_id`),
  KEY `fk_com_creator_id` (`com_creator_id`),
  KEY `fk_com_obj_id` (`com_obj_id`),
  CONSTRAINT `fk_com_creator_id` FOREIGN KEY (`com_creator_id`) REFERENCES `users` (`u_id`),
  CONSTRAINT `fk_com_obj_id` FOREIGN KEY (`com_obj_id`) REFERENCES `object` (`obj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------


-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `pm_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `pm_details` varchar(1023) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pm_sender_id` bigint(20) unsigned zerofill NOT NULL,
  `pm_receiver_id` bigint(20) unsigned zerofill NOT NULL,
  `pm_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  `pm_sending_time` datetime NOT NULL,
  PRIMARY KEY (`pm_id`),
  KEY `fk_pm_sender` (`pm_sender_id`),
  KEY `fk_pm_receiver` (`pm_receiver_id`),
  CONSTRAINT `fk_pm_receiver` FOREIGN KEY (`pm_receiver_id`) REFERENCES `users` (`u_id`),
  CONSTRAINT `fk_pm_sender` FOREIGN KEY (`pm_sender_id`) REFERENCES `users` (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for object
-- ----------------------------
DROP TABLE IF EXISTS `object`;
CREATE TABLE `object` (
  `obj_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `obj_name` varchar(255) NOT NULL,
  `obj_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  `obj_type` enum('tv','sport','game') NOT NULL DEFAULT 'game',
  `obj_creator_id` bigint(20) unsigned zerofill NOT NULL,
  `obj_create_time` datetime DEFAULT NULL,
  `obj_last_modify_time` datetime DEFAULT NULL,
  `obj_href` varchar(255) DEFAULT NULL,
  `obj_get_href` varchar(255) DEFAULT NULL,
  `obj_img_href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '../image/favicon.ico',
  `obj_details` varchar(8191) DEFAULT NULL,
  `obj_distribution_company` varchar(63) DEFAULT NULL,
  `obj_distribution_time` year(4) DEFAULT NULL,
  PRIMARY KEY (`obj_id`),
  UNIQUE KEY `obj_name` (`obj_name`),
  KEY `fk_obj_creator_id` (`obj_creator_id`),
  CONSTRAINT `fk_obj_creator_id` FOREIGN KEY (`obj_creator_id`) REFERENCES `users` (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of object
-- ----------------------------
INSERT INTO `object` VALUES ('00000000000000000001', 'RimWorld', '1', 'game', '00000000000000000002', '2018-10-24 21:19:21', '2018-10-24 21:19:24', 'https://store.steampowered.com/app/294100/RimWorld/', null, '../../image/object_img/RimWorld.jpg', null, null, null);

-- ----------------------------
-- Table structure for thegroup
-- ----------------------------
DROP TABLE IF EXISTS `thegroup`;
CREATE TABLE `thegroup` (
  `gro_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `gro_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `gro_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  `gro_manager_id` bigint(20) unsigned zerofill NOT NULL,
  `gro_uni_id` bigint(20) unsigned zerofill NOT NULL,
  `gro_obj_id` bigint(20) unsigned zerofill NOT NULL,
  `gro_details` varchar(511) DEFAULT NULL,
  PRIMARY KEY (`gro_id`),
  UNIQUE KEY `gro_name` (`gro_name`),
  KEY `fk_gro_mana_id` (`gro_manager_id`),
  KEY `fk_gro_uni_id` (`gro_uni_id`),
  KEY `fk_gro_obj_id` (`gro_obj_id`),
  CONSTRAINT `fk_gro_mana_id` FOREIGN KEY (`gro_manager_id`) REFERENCES `users` (`u_id`),
  CONSTRAINT `fk_gro_obj_id` FOREIGN KEY (`gro_obj_id`) REFERENCES `object` (`obj_id`),
  CONSTRAINT `fk_gro_uni_id` FOREIGN KEY (`gro_uni_id`) REFERENCES `university` (`uni_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of thegroup
-- ----------------------------

-- ----------------------------
-- Table structure for university
-- ----------------------------
DROP TABLE IF EXISTS `university`;
CREATE TABLE `university` (
  `uni_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uni_name` varchar(255) NOT NULL,
  PRIMARY KEY (`uni_id`),
  UNIQUE KEY `uni_name` (`uni_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of university
-- ----------------------------
INSERT INTO `university` VALUES ('00000000000000000001', '北京航空航天大学');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `u_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `u_nickname` varchar(31) NOT NULL,
  `u_password` varchar(127) NOT NULL,
  `u_photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '../../image/favicon.ico',
  `u_sex` enum('female','male') NOT NULL,
  `u_href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `u_permissions` smallint(5) unsigned NOT NULL DEFAULT '1',
  `u_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  `u_email` varchar(127) NOT NULL,
  `u_uni_id` bigint(20) unsigned zerofill DEFAULT NULL,
  `u_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_nickname` (`u_nickname`),
  UNIQUE KEY `u_email` (`u_email`),
  KEY `fk_users_uni_id` (`u_uni_id`),
  CONSTRAINT `fk_users_uni_id` FOREIGN KEY (`u_uni_id`) REFERENCES `university` (`uni_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('00000000000000000001', 'diralpo', '1', 'https://avatars2.githubusercontent.com/u/34290859?s=460&v=4', 'male', null, '256', '1', '295536022@qq.com', '00000000000000000001', '2018-10-18 10:39:51');

-- ----------------------------
-- Table structure for user_announcement_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_announcement_relation`;
CREATE TABLE `user_announcement_relation` (
  `uar_user_id` bigint(20) unsigned zerofill NOT NULL,
  `uar_ann_id` bigint(20) unsigned zerofill NOT NULL,
  `uar_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`uar_user_id`,`uar_ann_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_announcement_relation
-- ----------------------------

-- ----------------------------
-- Table structure for user_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_group_relation`;
CREATE TABLE `user_group_relation` (
  `ugr_user_id` bigint(20) unsigned zerofill NOT NULL,
  `ugr_gro_id` bigint(20) unsigned zerofill NOT NULL,
  `ugr_permissions` smallint(5) unsigned NOT NULL DEFAULT '1',
  `ugr_status` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ugr_join_time` datetime NOT NULL,
  PRIMARY KEY (`ugr_user_id`,`ugr_gro_id`),
  KEY `fk_ugr_gro_id` (`ugr_gro_id`),
  CONSTRAINT `fk_ugr_gro_id` FOREIGN KEY (`ugr_gro_id`) REFERENCES `thegroup` (`gro_id`),
  CONSTRAINT `fk_ugr_user_id` FOREIGN KEY (`ugr_user_id`) REFERENCES `users` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_group_relation
-- ----------------------------

-- ----------------------------
-- Table structure for user_object_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_object_relation`;
CREATE TABLE `user_object_relation` (
  `uor_user_id` bigint(20) unsigned zerofill NOT NULL,
  `uor_obj_id` bigint(20) unsigned zerofill NOT NULL,
  `uor_add_attention_time` datetime NOT NULL,
  PRIMARY KEY (`uor_user_id`,`uor_obj_id`),
  KEY `fk_uor_obj_id` (`uor_obj_id`),
  CONSTRAINT `fk_uor_obj_id` FOREIGN KEY (`uor_obj_id`) REFERENCES `object` (`obj_id`),
  CONSTRAINT `fk_uor_user_id` FOREIGN KEY (`uor_user_id`) REFERENCES `users` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_object_relation
-- ----------------------------


DROP TRIGGER IF EXISTS `trg_ann_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_ann_insert` BEFORE INSERT ON `announcement` FOR EACH ROW set new.ann_create_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_com_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_com_insert` BEFORE INSERT ON `comment` FOR EACH ROW set new.com_create_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_pm_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_pm_insert` BEFORE INSERT ON `message` FOR EACH ROW set new.pm_sending_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_object_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_object_insert` BEFORE INSERT ON `object` FOR EACH ROW set new.obj_create_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_users_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_users_insert` BEFORE INSERT ON `users` FOR EACH ROW set new.u_create_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_ugr_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_ugr_insert` BEFORE INSERT ON `user_group_relation` FOR EACH ROW set new.ugr_join_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_uor_insert`;
DELIMITER ;;
CREATE TRIGGER `trg_uor_insert` BEFORE INSERT ON `user_object_relation` FOR EACH ROW set new.uor_add_attention_time=NOW()
;;
DELIMITER ;
