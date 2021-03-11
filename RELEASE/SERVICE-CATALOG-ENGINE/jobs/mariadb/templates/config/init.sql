SET password=PASSWORD('<%= p("admin.password") %>');
INSTALL SONAME 'simple_password_check';
set global simple_password_check_minimal_length=9;


CREATE DATABASE  IF NOT EXISTS `broker` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `broker`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for JpaServiceMeteringInfo
-- ----------------------------
DROP TABLE IF EXISTS `JpaServiceMeteringInfo`;
CREATE TABLE `JpaServiceMeteringInfo`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `begin_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `end_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mesur_ty` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_plan_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `prjct_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_instn_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `unit_ty` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `unit_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_log
-- ----------------------------
DROP TABLE IF EXISTS `batch_log`;
CREATE TABLE `batch_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '아이디',
  `batch_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '프로젝트 아이디',
  `suces_stts` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '성공여부',
  `begin_de` timestamp(0) DEFAULT 0 COMMENT '시작 시간',
  `end_de` timestamp(0) DEFAULT 0 COMMENT '종료 시간',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1828 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '배치테이블' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `logs` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metering_info
-- ----------------------------
DROP TABLE IF EXISTS `metering_info`;
CREATE TABLE `metering_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '아이디',
  `svc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 아이디',
  `svc_instn_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 인스턴스 아이디',
  `svc_plan_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 플랜 아이디',
  `prjct_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '프로젝트 아이디',
  `mesur_ty` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '계량 유형 (MT1: 할당형, MT2: 사용량형)',
  `begin_de` datetime(0) DEFAULT NULL COMMENT '시작 일자',
  `end_de` datetime(0) DEFAULT NULL COMMENT '종료 일자',
  `unit_ty` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '단위유형 (MU1: kb, MU2: request count)',
  `unit_value` bigint(20) UNSIGNED DEFAULT NULL COMMENT '단위값',
  `use_at` int(1) DEFAULT NULL COMMENT '사용 여부(1 : 사용, 0 : 미사용)',
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '생성 일자',
  `creat_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '생성자 아이디',
  `updt_de` timestamp(0) NOT NULL COMMENT '수정 일자',
  `updt_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '수정자 아이디',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 350 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metering_request_type
-- ----------------------------
DROP TABLE IF EXISTS `metering_request_type`;
CREATE TABLE `metering_request_type`  (
  `unit_ty` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '단위유형',
  `unit_value` bigint(20) DEFAULT NULL COMMENT '단위값',
  `dc` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'DC',
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '생성 일자',
  `creat_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '생성자 아이디',
  `updt_de` timestamp(0) NOT NULL COMMENT '수정 일자',
  `updt_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '수정자 아이디',
  PRIMARY KEY (`unit_ty`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metering_use_info
-- ----------------------------
DROP TABLE IF EXISTS `metering_use_info`;
CREATE TABLE `metering_use_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '아이디',
  `prjct_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '프로젝트 아이디',
  `svc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 아이디',
  `svc_instn_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 인스턴스 아이디',
  `svc_plan_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '서비스 플랜 아이디',
  `mesur_at` int(1) DEFAULT NULL COMMENT '사용 여부(1 : 사용, 0 : 미사용)',
  `svc_sttus` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '인프라 서비스의 상태값',
  `unit_ty` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '단위유형 (MU1: kb, MU2: request count)',
  `unit_value` bigint(20) UNSIGNED DEFAULT NULL COMMENT '단위값',
  `begin_de` datetime(0) DEFAULT NULL COMMENT '시작 일자',
  `end_de` datetime(0) DEFAULT NULL COMMENT '종료 일자',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '미터링 사용정보' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bind_at` tinyint(4) DEFAULT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dc` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `svc_nm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `tag` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `updt_de` timestamp(0) NOT NULL,
  `unit_ty` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  `svc_broker_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKfxwxcqs4rvurb5h0fqk24ekyr`(`svc_broker_id`) USING BTREE,
  CONSTRAINT `FKfxwxcqs4rvurb5h0fqk24ekyr` FOREIGN KEY (`svc_broker_id`) REFERENCES `service_broker` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_broker
-- ----------------------------
DROP TABLE IF EXISTS `service_broker`;
CREATE TABLE `service_broker`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `broker_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  `broker_ty` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_image
-- ----------------------------
DROP TABLE IF EXISTS `service_image`;
CREATE TABLE `service_image`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `image_nm` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `image_ty` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_platform_ty` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_ty` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_instn
-- ----------------------------
DROP TABLE IF EXISTS `service_instn`;
CREATE TABLE `service_instn`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `conect_info` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `conect_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `instn_nm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `broker_code` int(4) DEFAULT NULL,
  `broker_value` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `sttus_value` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_plan_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `project_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mesur_ty` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `client_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqqwlxtsl1fotay594wkaie7bx`(`svc_id`) USING BTREE,
  INDEX `FK9o8by3s3h2lt47vwqemede6dq`(`svc_plan_id`) USING BTREE,
  CONSTRAINT `FK9o8by3s3h2lt47vwqemede6dq` FOREIGN KEY (`svc_plan_id`) REFERENCES `service_plan` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKqqwlxtsl1fotay594wkaie7bx` FOREIGN KEY (`svc_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_plan
-- ----------------------------
DROP TABLE IF EXISTS `service_plan`;
CREATE TABLE `service_plan`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ct_unit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dc` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `detail_info` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `free_at` tinyint(4) DEFAULT NULL,
  `mntnc_ver` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `plan_nm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rntfee` int(11) DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  `svc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9k9kba9x9x9ontk910nyniw6v`(`svc_id`) USING BTREE,
  CONSTRAINT `FK9k9kba9x9x9ontk910nyniw6v` FOREIGN KEY (`svc_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_statistics
-- ----------------------------
DROP TABLE IF EXISTS `service_statistics`;
CREATE TABLE `service_statistics`  (
  `search_year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_cnt` int(11) DEFAULT NULL,
  PRIMARY KEY (`search_year`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_tmplat
-- ----------------------------
DROP TABLE IF EXISTS `service_tmplat`;
CREATE TABLE `service_tmplat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dc` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `tag` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `tmplat_file` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `tmplat_nm` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_tmplat_detail
-- ----------------------------
DROP TABLE IF EXISTS `service_tmplat_detail`;
CREATE TABLE `service_tmplat_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_tmplat_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9jys92yr5x4mgqlqgpp0mwnm8`(`svc_id`) USING BTREE,
  INDEX `FKisodmsabnbf5q0ju65qcv0s1r`(`svc_tmplat_id`) USING BTREE,
  CONSTRAINT `FK9jys92yr5x4mgqlqgpp0mwnm8` FOREIGN KEY (`svc_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKisodmsabnbf5q0ju65qcv0s1r` FOREIGN KEY (`svc_tmplat_id`) REFERENCES `service_tmplat` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_use_info
-- ----------------------------
DROP TABLE IF EXISTS `service_use_info`;
CREATE TABLE `service_use_info`  (
  `id` int(11) NOT NULL,
  `begin_de` datetime(6) DEFAULT NULL,
  `end_de` datetime(6) DEFAULT NULL,
  `rntfee` int(11) DEFAULT NULL,
  `svc_instn_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `svc_plan_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKo571dqpb51x5s0nijq66fkkql`(`svc_instn_id`) USING BTREE,
  INDEX `FKmj64tvbgjl72ldpk48173lw7g`(`svc_plan_id`) USING BTREE,
  CONSTRAINT `FKmj64tvbgjl72ldpk48173lw7g` FOREIGN KEY (`svc_plan_id`) REFERENCES `service_plan` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKo571dqpb51x5s0nijq66fkkql` FOREIGN KEY (`svc_instn_id`) REFERENCES `service_instn` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Function structure for BIG_SEC_TO_TIME
-- ----------------------------
DROP FUNCTION IF EXISTS `BIG_SEC_TO_TIME`;
delimiter ;;
CREATE DEFINER=`admin`@`%` FUNCTION `BIG_SEC_TO_TIME`(SECS BIGINT) RETURNS text CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
BEGIN
   DECLARE HEURES TEXT;
   DECLARE MINUTES CHAR(5);
   DECLARE SECONDES CHAR(5);

   IF (SECS IS NULL) THEN RETURN NULL; END IF;

   SET HEURES = FLOOR(SECS / 3600);

   SET MINUTES = FLOOR((SECS - (HEURES*3600)) / 60);

   SET SECONDES = MOD(SECS, 60);
  
   IF LENGTH(HEURES) < 2 THEN SET HEURES = CONCAT("0", HEURES); END IF;
  
   IF MINUTES < 10 THEN SET MINUTES = CONCAT( "0", MINUTES); END IF;
  
   IF SECONDES < 10 THEN SET SECONDES = CONCAT( "0", SECONDES); END IF;
  
   -- 31일 일때 00:00:01 일 초 가산 
   IF HEURES = 743 and MINUTES = 59 and SECONDES = 59 then
   	  SET HEURES = "744";
   	  SET MINUTES = "00";
   	  SET SECONDES = "00";
   END IF;
  
   -- 30일 일때 00:00:01 일 초 가산
   IF HEURES = 719 and MINUTES = 59 and SECONDES = 59 then
   	  SET HEURES = "720";
   	  SET MINUTES = "00";
   	  SET SECONDES = "00";
   END IF;
   

   RETURN CONCAT(HEURES, ":", MINUTES, ":", SECONDES);
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

CREATE DATABASE  IF NOT EXISTS `cfbroker` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cfbroker`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for deployments
-- ----------------------------
DROP TABLE IF EXISTS `deployments`;
CREATE TABLE `deployments`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `deployments_yml` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of deployments
-- ----------------------------
INSERT INTO `deployments` VALUES ('binary_storage', 'addons:\n- jobs:\n  - name: syslog_forwarder\n    properties:\n      syslog:\n        address: <%= p("syslog.ip") %>\n        custom_rule: |\n          if ($msg contains \"DEBUG\") then stop\n          if ($msg contains \"INFO\") then stop\n          if ($programname startswith \"vcap.\") then stop\n        fallback_servers: []\n        port: 2514\n        tls_enabled: false\n        transport: relp\n    release: syslog\n  name: syslog_forwarder\n- jobs:\n  - name: metrics_agent\n    properties:\n      metrics_agent:\n        deployment: cloud_engine\n        influxdb:\n          database: service_metric_db\n          measurement: cf_metric\n          processMeasurement: cf_process_metrics\n          url: <%= p("metric.url") %>\n        metadata: backend_service\n        origin: service_catalog\n    release: egov-monitoring-agent\n  name: egov-monitoring-agent\ninstance_groups:\n- azs:\n  - z1\n  instances: 1\n  jobs:\n  - name: binary_storage\n    release: storage-release\n  name: binary_storage\n  networks:\n  - name: default\n  persistent_disk_type: 10GB\n  stemcell: default\n  syslog_aggregator: null\n  vm_type: large\nname: storage\nproperties:\n  password: default\nreleases:\n- name: storage-release\n  version: latest\n- name: syslog\n  version: latest\n- name: egov-monitoring-agent\n  version: latest\nstemcells:\n- alias: default\n  os: ubuntu-xenial\n  version: 315.64\nupdate:\n  canaries: 1\n  canary_watch_time: 5000-120000\n  max_in_flight: 1\n  serial: false\n  update_watch_time: 5000-120000');
INSERT INTO `deployments` VALUES ('cubrid', 'addons:\n- jobs:\n  - name: syslog_forwarder\n    properties:\n      syslog:\n        address: <%= p("syslog.ip") %>\n        custom_rule: |\n          if ($msg contains \"DEBUG\") then stop\n          if ($msg contains \"INFO\") then stop\n          if ($programname startswith \"vcap.\") then stop\n        fallback_servers: []\n        port: 2514\n        tls_enabled: false\n        transport: relp\n    release: syslog\n  name: syslog_forwarder\n- jobs:\n  - name: metrics_agent\n    properties:\n      metrics_agent:\n        deployment: cloud_engine\n        influxdb:\n          database: service_metric_db\n          measurement: cf_metric\n          processMeasurement: cf_process_metrics\n          url: <%= p("metric.url") %>\n        metadata: backend_service\n        origin: service_catalog\n    release: egov-monitoring-agent\n  name: egov-monitoring-agent\ninstance_groups:\n- azs:\n  - z1\n  instances: 1\n  jobs:\n  - name: cubrid\n    release: on-demand-cubrid\n  name: cubrid\n  networks:\n  - name: default\n  persistent_disk_type: 10GB\n  stemcell: default\n  vm_type: small\nname: on-cubrid\nreleases:\n- name: on-demand-cubrid\n  version: latest\n- name: syslog\n  version: latest\n- name: egov-monitoring-agent\n  version: latest\nstemcells:\n- alias: default\n  os: ubuntu-xenial\n  version: latest\nupdate:\n  canaries: 1\n  canary_watch_time: 30000-180000\n  max_in_flight: 6\n  update_watch_time: 30000-180000\nproperties:\n  database_name: pch1234\n  port: 1524');
INSERT INTO `deployments` VALUES ('mariadb', 'addons:\n- jobs:\n  - name: syslog_forwarder\n    properties:\n      syslog:\n        address: <%= p("syslog.ip") %>\n        custom_rule: |\n          if ($msg contains \"DEBUG\") then stop\n          if ($msg contains \"INFO\") then stop\n          if ($programname startswith \"vcap.\") then stop\n        fallback_servers: []\n        port: 2514\n        tls_enabled: false\n        transport: relp\n    release: syslog\n  name: syslog_forwarder\n- jobs:\n  - name: metrics_agent\n    properties:\n      metrics_agent:\n        deployment: cloud_engine\n        influxdb:\n          database: service_metric_db\n          measurement: cf_metric\n          processMeasurement: cf_process_metrics\n          url: <%= p("metric.url") %>\n        metadata: backend_service\n        origin: service_catalog\n    release: egov-monitoring-agent\n  name: egov-monitoring-agent\ninstance_groups:\n- azs:\n  - z5\n  instances: 1\n  jobs:\n  - name: mariadb\n    release: mariadb-on-demand-release\n  name: mariadb\n  networks:\n  - name: default\n  persistent_disk_type: 1GB\n  stemcell: xenial\n  vm_type: small\nname: mariadb\nproperties:\n  password: password\n  port: \"3306\"\n  username: username\n  database: userdb\nreleases:\n- name: mariadb-on-demand-release\n  version: latest\n- name: syslog\n  version: latest\n- name: egov-monitoring-agent\n  version: latest\nstemcells:\n- alias: xenial\n  os: ubuntu-xenial\n  version: \"315.64\"\nupdate:\n  canaries: 1\n  canary_watch_time: 30000-120000\n  max_in_flight: 1\n  update_watch_time: 30000-120000');
INSERT INTO `deployments` VALUES ('redis', 'addons:\n- jobs:\n  - name: bpm\n    release: bpm\n  name: bpm\n- jobs:\n  - name: syslog_forwarder\n    properties:\n      syslog:\n        address: <%= p("syslog.ip") %>\n        custom_rule: |\n          if ($msg contains \"DEBUG\") then stop\n          if ($msg contains \"INFO\") then stop\n          if ($programname startswith \"vcap.\") then stop\n        fallback_servers: []\n        port: 2514\n        tls_enabled: false\n        transport: relp\n    release: syslog\n  name: syslog_forwarder\n- jobs:\n  - name: metrics_agent\n    properties:\n      metrics_agent:\n        deployment: cloud_engine\n        influxdb:\n          database: service_metric_db\n          measurement: cf_metric\n          processMeasurement: cf_process_metrics\n          url: <%= p("metric.url") %>\n        metadata: backend_service\n        origin: service_catalog\n    release: egov-monitoring-agent\n  name: egov-monitoring-agent\n\ninstance_groups:\n- azs:\n  - z5\n  instances: 1\n  jobs:\n  - name: redis\n    release: redis-service-release\n  name: redis\n  networks:\n  - name: service\n  persistent_disk_type: 2GB\n  stemcell: default\n  vm_type: medium\nname: redis\nproperties:\n  password: hoho1234\n  port: \'3658\'\nreleases:\n- name: bpm\n  version: latest\n- name: redis-service-release\n  version: latest\n- name: syslog\n  version: latest\n- name: egov-monitoring-agent\n  version: latest\nstemcells:\n- alias: default\n  os: ubuntu-xenial\n  version: 315.64\nupdate:\n  canaries: 1\n  canary_watch_time: 5000-120000\n  max_in_flight: 1\n  serial: false\n  update_watch_time: 5000-120000');
INSERT INTO `deployments` VALUES ('rmq', 'addons:\n- jobs:\n  - name: syslog_forwarder\n    properties:\n      syslog:\n        address: <%= p("syslog.ip") %>\n        custom_rule: |\n          if ($msg contains \"DEBUG\") then stop\n          if ($msg contains \"INFO\") then stop\n          if ($programname startswith \"vcap.\") then stop\n        fallback_servers: []\n        port: 2514\n        tls_enabled: false\n        transport: relp\n    release: syslog\n  name: syslog_forwarder\n- jobs:\n  - name: metrics_agent\n    properties:\n      metrics_agent:\n        deployment: cloud_engine\n        influxdb:\n          database: service_metric_db\n          measurement: cf_metric\n          processMeasurement: cf_process_metrics\n          url: <%= p("metric.url") %>\n        metadata: backend_service\n        origin: service_catalog\n    release: egov-monitoring-agent\n  name: egov-monitoring-agent\ninstance_groups:\n- azs:\n  - z6\n  instances: 1\n  jobs:\n  - name: rabbitmq-server\n    release: rabbitmq-on-demand-release\n  name: rmq\n  networks:\n  - name: default\n  persistent_disk_type: 1GB\n  stemcell: xenial\n  vm_type: small\nname: rabbitmq\nproperties:\n  password: cheolhan1234\n  port: 1444\n  dashboard_port: 2224\n  admin: ohoh\nreleases:\n- name: rabbitmq-on-demand-release\n  version: latest\n- name: syslog\n  version: latest\n- name: egov-monitoring-agent\n  version: latest\nstemcells:\n- alias: xenial\n  os: ubuntu-xenial\n  version: 315.64\nupdate:\n  canaries: 1\n  canary_watch_time: 30000-180000\n  max_in_flight: 1\n  serial: false\n  update_watch_time: 30000-180000');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `bind_at` tinyint(4) DEFAULT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `svc_nm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tag` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service
-- ----------------------------
INSERT INTO `service` VALUES ('073859ba-f7cb-4922-b35b-3a0d1dc964e1', 0, '2021-01-19 06:28:53', NULL, 'Cloud-Foundry On-Demand MariaDB Service', 'mariadb', 'cf,mariadb', '2020-09-24 09:27:44', '', 1);
INSERT INTO `service` VALUES ('5e17bbb3-1be6-4e25-bc34-91f70b15e0f7', 0, '2021-01-19 06:30:00', NULL, 'Cloud-Foundry On-Demand RabbitMQ Service', 'rmq', 'cf,rabbitmq', '2020-09-24 09:27:44', '', 1);
INSERT INTO `service` VALUES ('c47cf3fa-42ca-4b67-b4b4-63825c99158f', 0, '2021-01-19 06:29:54', NULL, 'Cloud-Foundry On-Demand Redis Service', 'redis', 'cf,redis', '2020-09-24 09:27:44', '', 1);
INSERT INTO `service` VALUES ('c47cf3fa-42ca-4b67-b4b4-91f70b15e0f7', 0, '2021-01-19 06:30:08', NULL, 'Cloud-Foundry On-Demand Storage Service', 'binary_storage', 'cf,storage', '2020-12-29 17:23:43', '', 1);
INSERT INTO `service` VALUES ('c47cf3fa-42ca-4b67-b4b4-91f70b15e0f9', 0, '2021-01-21 03:05:07', NULL, 'Cloud-Foundry On-Demand Cubrid Service', 'cubrid', 'cf.cubrid', '0000-00-00 00:00:00', NULL, 1);

-- ----------------------------
-- Table structure for service_instn
-- ----------------------------
DROP TABLE IF EXISTS `service_instn`;
CREATE TABLE `service_instn`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `conect_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `conect_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `wdtb_nm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `instn_nm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sttus_value` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `virtl_mchn_instn_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `svc_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `svc_plan_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqqwlxtsl1fotay594wkaie7bx`(`svc_id`) USING BTREE,
  INDEX `FK9o8by3s3h2lt47vwqemede6dq`(`svc_plan_id`) USING BTREE,
  CONSTRAINT `FK9o8by3s3h2lt47vwqemede6dq` FOREIGN KEY (`svc_plan_id`) REFERENCES `service_plan` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKqqwlxtsl1fotay594wkaie7bx` FOREIGN KEY (`svc_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for service_plan
-- ----------------------------
DROP TABLE IF EXISTS `service_plan`;
CREATE TABLE `service_plan`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cntnc_disk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `creat_de` timestamp(0) NOT NULL DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP(0),
  `creat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ct_unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `detail_info` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `free_at` tinyint(4) DEFAULT NULL,
  `mntnc_ver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `plan_nm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `rntfee` int(11) DEFAULT NULL,
  `updt_de` timestamp(0) NOT NULL,
  `updt_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `use_at` tinyint(4) NOT NULL,
  `virtl_mchn_ty` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `svc_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9k9kba9x9x9ontk910nyniw6v`(`svc_id`) USING BTREE,
  CONSTRAINT `FK9k9kba9x9x9ontk910nyniw6v` FOREIGN KEY (`svc_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_plan
-- ----------------------------
INSERT INTO `service_plan` VALUES ('235d6917-42dc-4c7f-bd8c-59c884b769c6', '1GB', '2020-09-24 09:27:44', NULL, 'MONTHLY', 'redis service plan1', 'plan 1 info', 0, '2.1.1+abcdef', 'dedicated-redis1', 10, '2020-09-24 09:27:44', NULL, 1, 'small', 'c47cf3fa-42ca-4b67-b4b4-63825c99158f');
INSERT INTO `service_plan` VALUES ('326c92df-916a-4a27-a6fe-7deaca01f40f', '2GB', '2020-09-24 09:27:44', NULL, 'MONTHLY', 'rabbitmq service plan1', 'plan 1 info', 0, '2.1.1+abcdef', 'dedicated-rmq2', 20, '2020-09-24 09:27:44', NULL, 1, 'small', '5e17bbb3-1be6-4e25-bc34-91f70b15e0f7');
INSERT INTO `service_plan` VALUES ('8b548f5a-6ecf-4ec1-ad45-a9e4cae4b13f', '4GB', '2020-09-24 09:27:44', NULL, 'MONTHLY', 'rabbitmq service plan2', 'plan 2 info', 0, '2.1.1+abcdef', 'dedicated-rmq1', 30, '2020-09-24 09:27:44', NULL, 1, 'medium', '5e17bbb3-1be6-4e25-bc34-91f70b15e0f7');
INSERT INTO `service_plan` VALUES ('d0277f16-8c09-41e2-8e2c-632329eb0a7d', '10GB', '2020-10-07 05:50:26', NULL, 'MONTHLY', 'mariadb service plan2', 'plan 2 info', 0, '2.1.1+abcdef', 'dedicated-maria2', 60, '2020-09-24 09:27:44', NULL, 1, 'medium', '073859ba-f7cb-4922-b35b-3a0d1dc964e1');
INSERT INTO `service_plan` VALUES ('d0897483-de5b-47c0-9553-1253d7fed156', '2GB', '2020-09-24 09:27:44', NULL, 'MONTHLY', 'redis service plan2', 'plan 2 info', 0, '2.1.1+abcdef', 'dedicated-redis2', 40, '2020-09-24 09:27:44', NULL, 1, 'medium', 'c47cf3fa-42ca-4b67-b4b4-63825c99158f');
INSERT INTO `service_plan` VALUES ('d30f7462-0c90-4bb7-9538-303ee2427ec2', '50GB', '2020-12-29 08:27:57', NULL, 'MONTHLY', 'storage service plan2', 'plan 2 info', 0, '2.1.1+abcdef', 'dedicated-storage2', 10, '0000-00-00 00:00:00', NULL, 1, 'medium', 'c47cf3fa-42ca-4b67-b4b4-91f70b15e0f7');
INSERT INTO `service_plan` VALUES ('d30f7462-0c90-4bb7-9538-53becb8f1e81', '10GB', '2020-12-29 08:27:57', NULL, 'MONTHLY', 'storage service plan1', 'plan 1 info', 0, '2.1.1+abcdef', 'dedicated-storage1', 10, '0000-00-00 00:00:00', NULL, 1, 'medium', 'c47cf3fa-42ca-4b67-b4b4-91f70b15e0f7');
INSERT INTO `service_plan` VALUES ('e7b15536-78e9-4e5e-bac9-303ee2427ec2', '5GB', '2020-10-07 05:50:21', NULL, 'MONTHLY', 'mariadb service plan1', 'plan 1 info', 0, '2.1.1+abcdef', 'dedicated-maria1', 50, '2020-09-24 09:27:44', NULL, 1, 'small', '073859ba-f7cb-4922-b35b-3a0d1dc964e1');
INSERT INTO `service_plan` VALUES ('e7b15536-78e9-4e5e-bac9-303ee2428fc2', '10GB', '2021-01-21 03:07:38', NULL, 'MONTHLY', 'cubriddb service plan1', 'plan 1 info', 0, '2.1.1+abcdef', 'dedicated-cubrid1', 10, '0000-00-00 00:00:00', NULL, 1, 'small', 'c47cf3fa-42ca-4b67-b4b4-91f70b15e0f9');
INSERT INTO `service_plan` VALUES ('e7b15536-78e9-4e5e-bac9-303ee2852fc2', '20GB', '2021-01-21 03:07:28', NULL, 'MONTHLY', 'cubriddb service plan2', 'plan 2 info', 0, '2.1.1+abcdef', 'dedicated-cubrid2', 20, '0000-00-00 00:00:00', NULL, 1, 'medium', 'c47cf3fa-42ca-4b67-b4b4-91f70b15e0f9');

SET FOREIGN_KEY_CHECKS = 1;


