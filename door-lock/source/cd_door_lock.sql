/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : cd_door_lock

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 12/03/2022 16:07:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for z_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `z_cabinet`;
CREATE TABLE `z_cabinet`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '柜id',
  `cabinet_id` int NULL DEFAULT NULL COMMENT '设备id',
  `cabinet_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '柜名称',
  `station_id` int NULL DEFAULT NULL COMMENT '站id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Station_id`(`station_id`) USING BTREE,
  CONSTRAINT `FK_Station_id` FOREIGN KEY (`station_id`) REFERENCES `z_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_cabinet
-- ----------------------------
INSERT INTO `z_cabinet` VALUES (41, 1, '22P变电站辅助设备监控系统', 1);
INSERT INTO `z_cabinet` VALUES (42, 2, '23P变电站辅助设备监控系统', 1);

-- ----------------------------
-- Table structure for z_menu
-- ----------------------------
DROP TABLE IF EXISTS `z_menu`;
CREATE TABLE `z_menu`  (
  `id` int NOT NULL COMMENT 'id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `path` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'path',
  `component` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名',
  `require_auth` tinyint(1) NULL DEFAULT NULL COMMENT '是否要求权限',
  `parent_id` int NULL DEFAULT NULL COMMENT '父id',
  `enabled` int NULL DEFAULT NULL COMMENT '是否启用',
  `icon_cls` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_menu
-- ----------------------------
INSERT INTO `z_menu` VALUES (1, '/', NULL, NULL, '所有', 1, NULL, 1, NULL);
INSERT INTO `z_menu` VALUES (2, '/', '/home', 'Home', '系统管理', 1, 1, 1, 'el-icon-s-tools');
INSERT INTO `z_menu` VALUES (3, '/', '/home', 'Home', '门锁管理', 1, 1, 1, 'el-icon-s-home');
INSERT INTO `z_menu` VALUES (4, '/system/sysuser', '/sys/user', 'SysUser', '用户管理', 1, 2, 1, 'el-icon-s-custom');
INSERT INTO `z_menu` VALUES (5, '/system/permiss', '/sys/permiss', 'SysPermiss', '权限管理', 1, 2, 1, 'el-icon-s-opportunity');
INSERT INTO `z_menu` VALUES (6, '/system/menu', '/sys/menu', 'SysMenu', '菜单管理', 1, 2, 0, 'el-icon-menu');
INSERT INTO `z_menu` VALUES (7, '/station', '/doorlock/station', 'DoorlockStation', '站管理', 1, 3, 1, 'el-icon-office-building');
INSERT INTO `z_menu` VALUES (8, '/cabinet', '/doorlock/cabinet', 'DoorlockCabinet', '柜门管理', 1, 3, 1, 'el-icon-refrigerator');
INSERT INTO `z_menu` VALUES (9, '/system/sysuser/addUser', NULL, NULL, '新增用户', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (10, '/system/sysuser/deleteUser', NULL, NULL, '删除用户', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (11, '/system/sysuser/updateUser', NULL, NULL, '更新用户信息', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (12, '/system/sysuser/getAllUser', NULL, NULL, '获得所有用户', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (13, '/system/sysuser/userRole', NULL, NULL, '更新用户角色', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (14, '/system/sysuser/roles', NULL, NULL, '获取所有角色', 1, 4, 1, NULL);
INSERT INTO `z_menu` VALUES (15, '/system/permiss/getAllRoles', NULL, NULL, '获取所有角色', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (16, '/system/permiss/addRole', NULL, NULL, '添加角色', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (17, '/system/permiss/deleteRole/{rid}', NULL, NULL, '删除角色', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (18, '/system/permiss/getAllMenus', NULL, NULL, '获取所有菜单', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (19, '/system/permiss/getMidByRid/{rid}', NULL, NULL, '根据角色id获取菜单id', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (20, '/system/permiss/updateMenuRole', NULL, NULL, '更新角色菜单', 1, 5, 1, NULL);
INSERT INTO `z_menu` VALUES (21, '/system/menu/getMenuBycurrentUser', NULL, NULL, '通过当前用户id获取菜单列表', 0, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (22, '/system/menu/getAllFirstChildMenu', NULL, NULL, '获取一级子菜单', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (23, '/system/menu/addMenu', NULL, NULL, '添加子菜单', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (24, '/system/menu/getAllMenusNullFZ', NULL, NULL, '分页获取所有站的（没有父子关系）', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (25, '/system/menu/getAllMenusDetail', NULL, NULL, '获取所有的菜单有父子关系且详细', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (26, '/system/menu/updateMenu', NULL, NULL, '修改菜单的信息', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (27, '/system/menu/deleteMenu', NULL, NULL, '删除菜单', 1, 6, 0, NULL);
INSERT INTO `z_menu` VALUES (28, '/station/getAllStation', NULL, NULL, '分页获取所有站的', 1, 7, 1, NULL);
INSERT INTO `z_menu` VALUES (29, '/station/addStation', NULL, NULL, '添加站', 1, 7, 1, NULL);
INSERT INTO `z_menu` VALUES (30, '/station/updateStation', NULL, NULL, '更新站的信息', 1, 7, 1, NULL);
INSERT INTO `z_menu` VALUES (31, '/station/findStationById', NULL, NULL, '通过id查询站的信息', 1, 7, 1, NULL);
INSERT INTO `z_menu` VALUES (32, '/station/deleteStation', NULL, NULL, '删除站', 1, 7, 1, NULL);
INSERT INTO `z_menu` VALUES (33, '/cabinet/addCabinet', NULL, NULL, '添加柜门', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (34, '/cabinet/deleteCabinet', NULL, NULL, '删除柜门', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (35, '/cabinet/updateCabinet', NULL, NULL, '更新柜门信息', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (36, '/cabinet/getAllCabinet', NULL, NULL, '分页获取所有柜门', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (37, '/cabinet/findCabinetById', NULL, NULL, '通过id查询站的消息', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (38, '/cabinet/findCabinetByStationIp', NULL, NULL, '获取某个站（Ip）下的柜门', 1, 8, 1, NULL);
INSERT INTO `z_menu` VALUES (39, '/operateDoor/{stationIp}/{cabinetId}/{door}/{switch}', '', '', '控制门的开关', 1, 8, 1, '');
INSERT INTO `z_menu` VALUES (40, '/cabinet//getCabinetStateLog/{cabinetId}/{stationIp}/{start}/{end}/{door}/{state}/{type}', NULL, NULL, '查看日志', 1, 8, 1, NULL);

-- ----------------------------
-- Table structure for z_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `z_menu_role`;
CREATE TABLE `z_menu_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  `role_id` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `z_menu_role_fk1`(`menu_id`) USING BTREE,
  INDEX `z_menu_role_fk2`(`role_id`) USING BTREE,
  CONSTRAINT `z_menu_role_fk1` FOREIGN KEY (`menu_id`) REFERENCES `z_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `z_menu_role_fk2` FOREIGN KEY (`role_id`) REFERENCES `z_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 765 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_menu_role
-- ----------------------------
INSERT INTO `z_menu_role` VALUES (697, 1, 2);
INSERT INTO `z_menu_role` VALUES (698, 2, 2);
INSERT INTO `z_menu_role` VALUES (699, 4, 2);
INSERT INTO `z_menu_role` VALUES (700, 9, 2);
INSERT INTO `z_menu_role` VALUES (701, 10, 2);
INSERT INTO `z_menu_role` VALUES (702, 11, 2);
INSERT INTO `z_menu_role` VALUES (703, 12, 2);
INSERT INTO `z_menu_role` VALUES (704, 13, 2);
INSERT INTO `z_menu_role` VALUES (705, 14, 2);
INSERT INTO `z_menu_role` VALUES (706, 5, 2);
INSERT INTO `z_menu_role` VALUES (707, 15, 2);
INSERT INTO `z_menu_role` VALUES (708, 16, 2);
INSERT INTO `z_menu_role` VALUES (709, 17, 2);
INSERT INTO `z_menu_role` VALUES (710, 18, 2);
INSERT INTO `z_menu_role` VALUES (711, 19, 2);
INSERT INTO `z_menu_role` VALUES (712, 20, 2);
INSERT INTO `z_menu_role` VALUES (713, 3, 2);
INSERT INTO `z_menu_role` VALUES (714, 7, 2);
INSERT INTO `z_menu_role` VALUES (715, 28, 2);
INSERT INTO `z_menu_role` VALUES (716, 29, 2);
INSERT INTO `z_menu_role` VALUES (717, 30, 2);
INSERT INTO `z_menu_role` VALUES (718, 31, 2);
INSERT INTO `z_menu_role` VALUES (719, 32, 2);
INSERT INTO `z_menu_role` VALUES (720, 8, 2);
INSERT INTO `z_menu_role` VALUES (721, 33, 2);
INSERT INTO `z_menu_role` VALUES (722, 34, 2);
INSERT INTO `z_menu_role` VALUES (723, 35, 2);
INSERT INTO `z_menu_role` VALUES (724, 36, 2);
INSERT INTO `z_menu_role` VALUES (725, 37, 2);
INSERT INTO `z_menu_role` VALUES (726, 38, 2);
INSERT INTO `z_menu_role` VALUES (727, 39, 2);
INSERT INTO `z_menu_role` VALUES (728, 1, 1);
INSERT INTO `z_menu_role` VALUES (729, 2, 1);
INSERT INTO `z_menu_role` VALUES (730, 4, 1);
INSERT INTO `z_menu_role` VALUES (731, 9, 1);
INSERT INTO `z_menu_role` VALUES (732, 10, 1);
INSERT INTO `z_menu_role` VALUES (733, 11, 1);
INSERT INTO `z_menu_role` VALUES (734, 12, 1);
INSERT INTO `z_menu_role` VALUES (735, 13, 1);
INSERT INTO `z_menu_role` VALUES (736, 14, 1);
INSERT INTO `z_menu_role` VALUES (737, 5, 1);
INSERT INTO `z_menu_role` VALUES (738, 15, 1);
INSERT INTO `z_menu_role` VALUES (739, 16, 1);
INSERT INTO `z_menu_role` VALUES (740, 17, 1);
INSERT INTO `z_menu_role` VALUES (741, 18, 1);
INSERT INTO `z_menu_role` VALUES (742, 19, 1);
INSERT INTO `z_menu_role` VALUES (743, 20, 1);
INSERT INTO `z_menu_role` VALUES (744, 3, 1);
INSERT INTO `z_menu_role` VALUES (745, 7, 1);
INSERT INTO `z_menu_role` VALUES (746, 28, 1);
INSERT INTO `z_menu_role` VALUES (747, 29, 1);
INSERT INTO `z_menu_role` VALUES (748, 30, 1);
INSERT INTO `z_menu_role` VALUES (749, 31, 1);
INSERT INTO `z_menu_role` VALUES (750, 32, 1);
INSERT INTO `z_menu_role` VALUES (751, 8, 1);
INSERT INTO `z_menu_role` VALUES (752, 33, 1);
INSERT INTO `z_menu_role` VALUES (753, 34, 1);
INSERT INTO `z_menu_role` VALUES (754, 35, 1);
INSERT INTO `z_menu_role` VALUES (755, 36, 1);
INSERT INTO `z_menu_role` VALUES (756, 37, 1);
INSERT INTO `z_menu_role` VALUES (757, 38, 1);
INSERT INTO `z_menu_role` VALUES (758, 39, 1);
INSERT INTO `z_menu_role` VALUES (759, 40, 1);
INSERT INTO `z_menu_role` VALUES (760, 28, 3);
INSERT INTO `z_menu_role` VALUES (761, 31, 3);
INSERT INTO `z_menu_role` VALUES (762, 36, 3);
INSERT INTO `z_menu_role` VALUES (763, 37, 3);
INSERT INTO `z_menu_role` VALUES (764, 38, 3);

-- ----------------------------
-- Table structure for z_role
-- ----------------------------
DROP TABLE IF EXISTS `z_role`;
CREATE TABLE `z_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色英文名称',
  `name_zh` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色中文名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_role
-- ----------------------------
INSERT INTO `z_role` VALUES (1, 'ROLE_admin', '超级管理员');
INSERT INTO `z_role` VALUES (2, 'ROLE_testadmin', '管理员');
INSERT INTO `z_role` VALUES (3, 'ROLE_user', '普通用户');
INSERT INTO `z_role` VALUES (4, 'ROLE_zzk', '测试添加角色2');

-- ----------------------------
-- Table structure for z_station
-- ----------------------------
DROP TABLE IF EXISTS `z_station`;
CREATE TABLE `z_station`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '站id',
  `station_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站名字',
  `station_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站ip',
  `station_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站地址',
  `station_status` int NULL DEFAULT NULL COMMENT '站是否在线',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_station
-- ----------------------------
INSERT INTO `z_station` VALUES (1, '德山区变电站', '127.0.0.1', '常德市德山区', 0);
INSERT INTO `z_station` VALUES (2, '鼎城区变电站', '127.0.0.2', '常德市鼎城区区', 0);

-- ----------------------------
-- Table structure for z_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `z_sys_user`;
CREATE TABLE `z_sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名（昵称）',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `enabled` int NULL DEFAULT NULL COMMENT '是否启用',
  `gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_sys_user
-- ----------------------------
INSERT INTO `z_sys_user` VALUES (1, 'admin', '$2a$10$nz9tzK3p83o/WAa1vBwb4uHzyTnqRjXpctrC.WDxmw9mcT1DPUEdu', '超级管理员', NULL, 1, '男');
INSERT INTO `z_sys_user` VALUES (2, 'testadmin', '$2a$10$j8nx6lbbbmdgj4nJk3AzjuyiRaPwDMQ00RkE1BPOjHhlFkSkW9buG', '管理员', NULL, 1, '男');
INSERT INTO `z_sys_user` VALUES (3, 'testuser', '$2a$10$HAcfQPMvhi13lhavVeAzM.PdjiGK7P4DTncA1uKKvcsYa.RL71tLq', '普通用户', NULL, 1, '男');
INSERT INTO `z_sys_user` VALUES (9, 'testedituser', '$2a$10$qYd2AfTamlIxCX/dH4M38.ads5lSyttpRpail8NJ.w3dYEgYqQcNG', '测试编辑1', NULL, 1, '男');
INSERT INTO `z_sys_user` VALUES (17, 'testadd', '$2a$10$69U6U/oLMFy/DWZs3xiWcOC6Y3Q7fKyQs4bGrbjaVxLt9hVFfUPz6', '测试添加角色', NULL, 0, '男');

-- ----------------------------
-- Table structure for z_user_role
-- ----------------------------
DROP TABLE IF EXISTS `z_user_role`;
CREATE TABLE `z_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户和角色依赖表id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `z_user_role_fk1`(`user_id`) USING BTREE,
  INDEX `z_user_role_fk2`(`role_id`) USING BTREE,
  CONSTRAINT `z_user_role_fk1` FOREIGN KEY (`user_id`) REFERENCES `z_sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `z_user_role_fk2` FOREIGN KEY (`role_id`) REFERENCES `z_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z_user_role
-- ----------------------------
INSERT INTO `z_user_role` VALUES (17, 1, 1);
INSERT INTO `z_user_role` VALUES (61, 9, 3);
INSERT INTO `z_user_role` VALUES (83, 2, 2);
INSERT INTO `z_user_role` VALUES (84, 2, 2);
INSERT INTO `z_user_role` VALUES (87, 17, 4);
INSERT INTO `z_user_role` VALUES (92, 3, 3);

SET FOREIGN_KEY_CHECKS = 1;
