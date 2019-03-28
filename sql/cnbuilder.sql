/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : cnbuilder

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 28/03/2019 11:25:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cd_user
-- ----------------------------
DROP TABLE IF EXISTS `cd_user`;
CREATE TABLE `cd_user`  (
  `id` bigint(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_user
-- ----------------------------
INSERT INTO `cd_user` VALUES (1, '张三', 12);
INSERT INTO `cd_user` VALUES (2, '李四', 15);
INSERT INTO `cd_user` VALUES (3, '王五', 18);

SET FOREIGN_KEY_CHECKS = 1;
