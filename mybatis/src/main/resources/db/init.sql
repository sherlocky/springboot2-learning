CREATE DATABASE IF NOT EXISTS `mybatis`CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `mybatis`;

CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `phone` varchar(255) NOT NULL COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

insert ignore into `t_user`(`id`,`name`,`password`,`phone`) values
(1,'张三222','000000','12345678900'),
(3,'张三','000000','12345678900'),
(4,'张三','000000','12345678900'),
(5,'张三','000000','12345678900'),
(6,'张三','000000','12345678900'),
(7,'张三','000000','12345678900'),
(8,'张三','000000','12345678900'),
(9,'张三','000000','12345678900'),
(10,'张三','000000','12345678900'),
(11,'张三','000000','12345678900'),
(12,'张三','000000','12345678900'),
(13,'张三','000000','12345678900');