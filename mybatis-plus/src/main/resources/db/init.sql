CREATE DATABASE IF NOT EXISTS `mybatis`CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `mybatis`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '姓名',
  `age` int(10) NOT NULL COMMENT '年龄',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT IGNORE INTO `user`(`id`,`name`,`age`,`email`) VALUES
(1,'Jone',18,'test1@baomidou.com'),
(2,'Jack',20,'test2@baomidou.com'),
(3,'Tom',28,'test3@baomidou.com'),
(4,'Sandy',21,'test4@baomidou.com'),
(5,'Billie',24,'test5@baomidou.com');