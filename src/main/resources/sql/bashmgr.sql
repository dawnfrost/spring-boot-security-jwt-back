/*
SQLyog Ultimate v12.3.3 (64 bit)
MySQL - 5.7.16-log : Database - bashmgr
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bashmgr` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bashmgr`;

/*Table structure for table `bm_auth` */

DROP TABLE IF EXISTS `bm_auth`;

CREATE TABLE `bm_auth` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER` int(11) DEFAULT NULL,
  `NAME` varchar(32) DEFAULT NULL,
  `TAG` varchar(64) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `bm_auth` */

insert  into `bm_auth`(`ID`,`ORDER`,`NAME`,`TAG`,`DESCRIPTION`) values 
(1,1,'管理员','admin','最高权限，可管理一切'),
(2,2,'高级用户','master','只可管理普通用户增删查改'),
(3,3,'普通用户','user','普通用户'),
(4,4,'访客','guest','限制访问');

/*Table structure for table `bm_group` */

DROP TABLE IF EXISTS `bm_group`;

CREATE TABLE `bm_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER` int(11) DEFAULT NULL,
  `NAME` varchar(32) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `bm_group` */

insert  into `bm_group`(`ID`,`ORDER`,`NAME`,`DESCRIPTION`) values 
(1,1,'管理员组','管理员组'),
(2,2,'超级用户组','超级用户组'),
(3,3,'普通用户组','普通用户组'),
(4,4,'访客组','访客组');

/*Table structure for table `bm_group_role` */

DROP TABLE IF EXISTS `bm_group_role`;

CREATE TABLE `bm_group_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `bm_group_role` */

insert  into `bm_group_role`(`ID`,`GROUP_ID`,`ROLE_ID`) values 
(1,1,1),
(2,1,2),
(3,1,3),
(4,2,2),
(5,2,3),
(6,3,3),
(7,4,4);

/*Table structure for table `bm_role` */

DROP TABLE IF EXISTS `bm_role`;

CREATE TABLE `bm_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER` int(11) DEFAULT NULL,
  `NAME` varchar(32) DEFAULT NULL,
  `ROLE` varchar(64) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `bm_role` */

insert  into `bm_role`(`ID`,`ORDER`,`NAME`,`ROLE`,`DESCRIPTION`) values 
(1,1,'管理员','ROLE_ADMIN','具备管理员、超级用户、普通用户等权限'),
(2,2,'超级用户','ROLE_MASTER','具备超级用户、普通用户等权限'),
(3,3,'普通用户','ROLE_USER','具备普通用户等权限'),
(4,4,'访客','ROLE_GUEST','具备访客权限');

/*Table structure for table `bm_role_auth` */

DROP TABLE IF EXISTS `bm_role_auth`;

CREATE TABLE `bm_role_auth` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) DEFAULT NULL,
  `AUTH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `bm_role_auth` */

insert  into `bm_role_auth`(`ID`,`ROLE_ID`,`AUTH_ID`) values 
(1,1,1),
(2,1,2),
(3,1,3);

/*Table structure for table `bm_token` */

DROP TABLE IF EXISTS `bm_token`;

CREATE TABLE `bm_token` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) DEFAULT NULL,
  `USER_AGENT` varchar(255) DEFAULT NULL,
  `REMOTE_HOST` varchar(255) DEFAULT NULL,
  `REFRESH_TOKEN` varchar(64) DEFAULT NULL,
  `REFRESH_TOKEN_CREATE_TIME` datetime DEFAULT NULL,
  `REFRESH_TOKEN_EXPIRE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `bm_token` */

insert  into `bm_token`(`ID`,`USER_ID`,`USER_AGENT`,`REMOTE_HOST`,`REFRESH_TOKEN`,`REFRESH_TOKEN_CREATE_TIME`,`REFRESH_TOKEN_EXPIRE_TIME`) values 
(1,1,'PostmanRuntime/7.1.1',NULL,'0a24515d-bc0f-4b18-b84e-7fd6b6d7c89f','2017-12-19 18:30:33','2018-01-03 18:30:33'),
(2,2,'PostmanRuntime/7.1.1',NULL,'2d77941f-b65a-44be-b447-14375ef172b3','2017-12-19 18:29:08','2018-01-03 18:29:08');

/*Table structure for table `bm_user` */

DROP TABLE IF EXISTS `bm_user`;

CREATE TABLE `bm_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MARK_CODE` varchar(255) DEFAULT NULL,
  `USER_NAME` varchar(64) DEFAULT NULL,
  `PASSWORD` varchar(128) DEFAULT NULL,
  `EMAIL` varchar(128) DEFAULT NULL,
  `PHONE` varchar(32) DEFAULT NULL,
  `DISPLAY_NAME` varchar(32) DEFAULT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `LAST_LOGIN_TIME` datetime DEFAULT NULL,
  `IS_ENABLE` tinyint(4) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `EXPIRE_TIME` datetime NOT NULL,
  `IS_LOCKED` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER_NAME_index` (`USER_NAME`) USING BTREE,
  UNIQUE KEY `MARK_CODE_index` (`MARK_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `bm_user` */

insert  into `bm_user`(`ID`,`MARK_CODE`,`USER_NAME`,`PASSWORD`,`EMAIL`,`PHONE`,`DISPLAY_NAME`,`CREATE_TIME`,`LAST_LOGIN_TIME`,`IS_ENABLE`,`STATUS`,`EXPIRE_TIME`,`IS_LOCKED`) values 
(1,'8aab883392c787a8c62033d22d72e4fe271e4957fcc37305adf25de34362cbf2','root','password','lingo.wxy@qq.com','15866682816','wxy41','2017-12-17 13:43:33',NULL,1,0,'2017-12-30 13:43:42',0),
(2,'8aab883392c787a8c62033d22d72e4fe271e4957fcc37305ad565de34362cbf2','user','password','1111','22333','wxy44','2017-12-19 17:48:38',NULL,1,0,'2017-12-30 17:48:51',0);

/*Table structure for table `bm_user_group` */

DROP TABLE IF EXISTS `bm_user_group`;

CREATE TABLE `bm_user_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `bm_user_group` */

insert  into `bm_user_group`(`ID`,`USER_ID`,`GROUP_ID`) values 
(1,1,1),
(2,2,3);

/*Table structure for table `bm_user_role` */

DROP TABLE IF EXISTS `bm_user_role`;

CREATE TABLE `bm_user_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `bm_user_role` */

insert  into `bm_user_role`(`ID`,`USER_ID`,`ROLE_ID`) values 
(1,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
