/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50743 (5.7.43-log)
 Source Host           : localhost:3306
 Source Schema         : pratical

 Target Server Type    : MySQL
 Target Server Version : 50743 (5.7.43-log)
 File Encoding         : 65001

 Date: 13/09/2023 22:00:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo1
-- ----------------------------
DROP TABLE IF EXISTS `demo1`;
CREATE TABLE `demo1`  (
  `lv` int(11) NULL DEFAULT NULL COMMENT '光照强度',
  `speed` int(11) NULL DEFAULT NULL COMMENT '点击转速，最大100',
  `num` double NULL DEFAULT NULL COMMENT '已存储个数，最大8MB',
  `createtime` datetime NULL DEFAULT NULL COMMENT '收集到数据的时间，作为是否重复存储的判断',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一识别，',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
