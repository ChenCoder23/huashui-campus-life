-- ============================================================
-- 华水校园生活服务平台 · 数据库初始化脚本
-- 仅创建数据库和表结构，不创建用户，不设置密码。
-- 密码由部署者自行创建 MySQL 用户并配置。
-- 推荐 MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS huashui_rbac DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_storage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_dormitory DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_utility DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_evaluation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_attendance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_leave DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_task DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS huashui_message DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- ============================================================
-- huashui_rbac
-- ============================================================
USE huashui_rbac;

CREATE TABLE IF NOT EXISTS sys_menu (
  id BIGINT NOT NULL AUTO_INCREMENT,
  parent_id BIGINT DEFAULT 0,
  menu_name VARCHAR(64) NOT NULL,
  menu_type VARCHAR(32) DEFAULT 'MENU',
  path VARCHAR(255),
  component VARCHAR(255),
  icon VARCHAR(128),
  permission VARCHAR(128),
  sort_order INT DEFAULT 0,
  is_home INT DEFAULT 0,
  hidden INT DEFAULT 0,
  status INT DEFAULT 1,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(64) NOT NULL,
  role_code VARCHAR(64) NOT NULL,
  description VARCHAR(255),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role_menu (
  id BIGINT NOT NULL AUTO_INCREMENT,
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_role_id (role_id),
  KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  password VARCHAR(255) NOT NULL,
  real_name VARCHAR(64),
  phone VARCHAR(32),
  email VARCHAR(128),
  avatar VARCHAR(512),
  gender VARCHAR(16),
  campus_id BIGINT,
  building_id BIGINT,
  major VARCHAR(128),
  grade VARCHAR(32),
  last_login_time DATETIME,
  status INT DEFAULT 1,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_system
-- ============================================================
USE huashui_system;

CREATE TABLE IF NOT EXISTS sys_config (
  id BIGINT NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(128) NOT NULL,
  config_value VARCHAR(512),
  config_group VARCHAR(128),
  config_name VARCHAR(128),
  description VARCHAR(512),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  create_by BIGINT,
  update_by BIGINT,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_dict_type (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dict_name VARCHAR(128) NOT NULL,
  dict_type VARCHAR(128) NOT NULL,
  description VARCHAR(512),
  status INT DEFAULT 1,
  create_by BIGINT,
  update_by BIGINT,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_dict_data (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dict_type VARCHAR(128) NOT NULL,
  dict_label VARCHAR(128) NOT NULL,
  dict_value VARCHAR(128) NOT NULL,
  sort_order INT DEFAULT 0,
  is_default TINYINT(1) DEFAULT 0,
  remark VARCHAR(512),
  status INT DEFAULT 1,
  create_by BIGINT,
  update_by BIGINT,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_login_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT,
  username VARCHAR(64),
  login_time DATETIME,
  ip_address VARCHAR(64),
  device VARCHAR(64),
  browser VARCHAR(128),
  os VARCHAR(128),
  status INT DEFAULT 1,
  fail_reason VARCHAR(255),
  create_time DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS operation_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  operator_id BIGINT,
  operator_name VARCHAR(64),
  operation_type VARCHAR(64),
  operation_module VARCHAR(128),
  operation_desc VARCHAR(512),
  target_type VARCHAR(64),
  target_id BIGINT,
  request_ip VARCHAR(64),
  request_method VARCHAR(16),
  request_url VARCHAR(512),
  request_params TEXT,
  old_data TEXT,
  new_data TEXT,
  cost_time INT,
  status INT DEFAULT 1,
  error_msg TEXT,
  create_time DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_exception_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  exception_name VARCHAR(255),
  exception_msg TEXT,
  stack_trace LONGTEXT,
  service_name VARCHAR(128),
  request_url VARCHAR(512),
  request_method VARCHAR(16),
  request_params TEXT,
  operator_id BIGINT,
  ip_address VARCHAR(64),
  status INT DEFAULT 0,
  handler_id BIGINT,
  handle_time DATETIME,
  handle_remark VARCHAR(512),
  create_time DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_storage
-- ============================================================
USE huashui_storage;

CREATE TABLE IF NOT EXISTS sys_file (
  id BIGINT NOT NULL AUTO_INCREMENT,
  original_name VARCHAR(255),
  object_name VARCHAR(512),
  access_url VARCHAR(1024),
  file_size BIGINT,
  mime_type VARCHAR(128),
  file_ext VARCHAR(32),
  file_hash VARCHAR(128),
  biz_type VARCHAR(32),
  biz_id BIGINT,
  uploader_id BIGINT,
  status INT DEFAULT 1,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_dormitory
-- ============================================================
USE huashui_dormitory;

CREATE TABLE IF NOT EXISTS sys_campus (
  id BIGINT NOT NULL AUTO_INCREMENT,
  campus_name VARCHAR(128) NOT NULL,
  campus_code VARCHAR(64) NOT NULL,
  address VARCHAR(255),
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_campus_code (campus_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_building (
  id BIGINT NOT NULL AUTO_INCREMENT,
  campus_id BIGINT NOT NULL,
  area VARCHAR(128),
  building_name VARCHAR(128) NOT NULL,
  building_code VARCHAR(64) NOT NULL,
  room_type VARCHAR(32) DEFAULT 'FOUR',
  total_floors INT,
  accommodation_fee INT,
  description VARCHAR(512),
  is_standardized TINYINT(1) DEFAULT 0,
  sort_order INT DEFAULT 0,
  status INT DEFAULT 1,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_building_code (building_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_building_config (
  id BIGINT NOT NULL AUTO_INCREMENT,
  building_id BIGINT NOT NULL,
  has_private_bath TINYINT(1) DEFAULT 0,
  bath_type VARCHAR(32),
  has_balcony TINYINT(1) DEFAULT 0,
  balcony_type VARCHAR(32),
  bed_type VARCHAR(64),
  floor_type VARCHAR(64),
  hot_water_type VARCHAR(32),
  hot_water_hours VARCHAR(64),
  has_ac TINYINT(1) DEFAULT 0,
  has_heating TINYINT(1) DEFAULT 0,
  has_drinking_water TINYINT(1) DEFAULT 0,
  has_laundry TINYINT(1) DEFAULT 0,
  has_study_room TINYINT(1) DEFAULT 0,
  bed_size VARCHAR(64),
  PRIMARY KEY (id),
  UNIQUE KEY uk_building_id (building_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_building_manager (
  id BIGINT NOT NULL AUTO_INCREMENT,
  building_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  status INT DEFAULT 1,
  PRIMARY KEY (id),
  KEY idx_building_id (building_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_room (
  id BIGINT NOT NULL AUTO_INCREMENT,
  building_id BIGINT NOT NULL,
  room_number VARCHAR(32) NOT NULL,
  floor_number INT,
  room_type VARCHAR(32) DEFAULT 'FOUR',
  total_beds INT DEFAULT 4,
  occupied_beds INT DEFAULT 0,
  status VARCHAR(32) DEFAULT 'EMPTY',
  remark VARCHAR(512),
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_building_id (building_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_bed (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  bed_number VARCHAR(16),
  student_id BIGINT,
  status INT DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dorm_student_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  campus_id BIGINT,
  building_id BIGINT,
  room_id BIGINT,
  bed_id BIGINT,
  check_in_time DATETIME,
  check_out_time DATETIME,
  status VARCHAR(32) DEFAULT 'LIVING',
  PRIMARY KEY (id),
  KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_utility
-- ============================================================
USE huashui_utility;

CREATE TABLE IF NOT EXISTS water_balance (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  balance DECIMAL(12,2) DEFAULT 0,
  free_quota DECIMAL(12,2) DEFAULT 0,
  total_usage DECIMAL(12,2) DEFAULT 0,
  status INT DEFAULT 1,
  stopped_time DATETIME,
  restored_time DATETIME,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS water_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  record_date DATE,
  usage_amount DECIMAL(12,2) DEFAULT 0,
  unit_price DECIMAL(12,2) DEFAULT 0,
  amount DECIMAL(12,2) DEFAULT 0,
  balance_before DECIMAL(12,2) DEFAULT 0,
  balance_after DECIMAL(12,2) DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS electricity_balance (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  balance DECIMAL(12,2) DEFAULT 0,
  free_quota DECIMAL(12,2) DEFAULT 0,
  total_usage DECIMAL(12,2) DEFAULT 0,
  status INT DEFAULT 1,
  stopped_time DATETIME,
  restored_time DATETIME,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS electricity_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  record_date DATE,
  usage_amount DECIMAL(12,2) DEFAULT 0,
  unit_price DECIMAL(12,2) DEFAULT 0,
  amount DECIMAL(12,2) DEFAULT 0,
  balance_before DECIMAL(12,2) DEFAULT 0,
  balance_after DECIMAL(12,2) DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payment_order (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_no VARCHAR(64) NOT NULL,
  user_id BIGINT,
  room_id BIGINT,
  payment_type VARCHAR(32),
  amount DECIMAL(12,2) DEFAULT 0,
  pay_method VARCHAR(32),
  status VARCHAR(32),
  transaction_id VARCHAR(128),
  paid_time DATETIME,
  refund_time DATETIME,
  refund_reason VARCHAR(512),
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_evaluation
-- ============================================================
USE huashui_evaluation;

CREATE TABLE IF NOT EXISTS evaluation_questionnaire (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  creator_id BIGINT,
  start_time DATETIME,
  end_time DATETIME,
  status VARCHAR(32) DEFAULT 'WAITING',
  target_type VARCHAR(32),
  target_scope TEXT,
  total_count INT DEFAULT 0,
  submit_count INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS evaluation_question_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  questionnaire_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  type VARCHAR(32) DEFAULT 'SCORE',
  min_score INT DEFAULT 0,
  max_score INT DEFAULT 5,
  required_flag INT DEFAULT 0,
  sort INT DEFAULT 0,
  create_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_questionnaire_id (questionnaire_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS evaluation_response (
  id BIGINT NOT NULL AUTO_INCREMENT,
  questionnaire_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  status VARCHAR(32) DEFAULT 'DRAFT',
  submit_time DATETIME,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_questionnaire_id (questionnaire_id),
  KEY idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS evaluation_answer (
  id BIGINT NOT NULL AUTO_INCREMENT,
  response_id BIGINT NOT NULL,
  question_id BIGINT NOT NULL,
  score INT,
  content TEXT,
  create_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_response_id (response_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_attendance
-- ============================================================
USE huashui_attendance;

CREATE TABLE IF NOT EXISTS attendance_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  worker_id BIGINT NOT NULL,
  worker_name VARCHAR(64),
  campus_id BIGINT,
  building_id BIGINT,
  attendance_date DATE,
  check_in_time DATETIME,
  check_in_type VARCHAR(32),
  check_in_location VARCHAR(255),
  check_in_photo VARCHAR(1024),
  check_in_status VARCHAR(32) DEFAULT 'ABSENT',
  is_holiday TINYINT(1) DEFAULT 0,
  remark VARCHAR(512),
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_worker_id (worker_id),
  KEY idx_attendance_date (attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_leave
-- ============================================================
USE huashui_leave;

CREATE TABLE IF NOT EXISTS leave_request (
  id BIGINT NOT NULL AUTO_INCREMENT,
  applicant_id BIGINT NOT NULL,
  applicant_name VARCHAR(64),
  applicant_type VARCHAR(32),
  leave_type VARCHAR(64),
  campus_id BIGINT,
  start_time DATETIME,
  end_time DATETIME,
  reason VARCHAR(1024),
  proof_images TEXT,
  status VARCHAR(32) DEFAULT 'PENDING',
  approver_id BIGINT,
  approve_time DATETIME,
  approve_opinion VARCHAR(512),
  reject_reason VARCHAR(512),
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_applicant_id (applicant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_task
-- ============================================================
USE huashui_task;

CREATE TABLE IF NOT EXISTS repair_order (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_no VARCHAR(64) NOT NULL,
  student_id BIGINT,
  student_name VARCHAR(64),
  campus_id BIGINT,
  building_id BIGINT,
  room_id BIGINT,
  repair_type VARCHAR(64),
  description TEXT,
  images TEXT,
  contact_phone VARCHAR(32),
  appointment_time VARCHAR(64),
  status VARCHAR(32) DEFAULT 'PENDING',
  repairer_name VARCHAR(64),
  repairer_id BIGINT,
  assigner_id BIGINT,
  assigned_time DATETIME,
  repair_time DATETIME,
  repair_result TEXT,
  repair_images TEXT,
  rating INT,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS task (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255),
  content TEXT,
  worker_id BIGINT,
  worker_name VARCHAR(64),
  creator_id BIGINT,
  creator_name VARCHAR(64),
  campus_id BIGINT,
  building_id BIGINT,
  status VARCHAR(32) DEFAULT 'TODO',
  deadline DATETIME,
  finish_time DATETIME,
  image_urls TEXT,
  remark VARCHAR(512),
  task_date DATE,
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_worker_id (worker_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS task_template (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128),
  worker_id BIGINT,
  worker_name VARCHAR(64),
  campus_id BIGINT,
  building_id BIGINT,
  area_desc VARCHAR(255),
  task_content TEXT,
  enabled INT DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- huashui_message
-- ============================================================
USE huashui_message;

CREATE TABLE IF NOT EXISTS message (
  id BIGINT NOT NULL AUTO_INCREMENT,
  type VARCHAR(32),
  title VARCHAR(255),
  content TEXT,
  receiver_id BIGINT,
  sender_id BIGINT,
  business_type VARCHAR(64),
  business_id BIGINT,
  status VARCHAR(32) DEFAULT 'UNREAD',
  read_time DATETIME,
  priority VARCHAR(32) DEFAULT 'NORMAL',
  create_time DATETIME,
  update_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_receiver_id (receiver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS system_notice (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255),
  content LONGTEXT,
  notice_type VARCHAR(64),
  attachment VARCHAR(512),
  push_scope VARCHAR(32),
  target_campus_ids TEXT,
  target_roles TEXT,
  target_building_ids TEXT,
  is_top INT DEFAULT 0,
  publisher_id BIGINT,
  publish_time DATETIME,
  view_count INT DEFAULT 0,
  status VARCHAR(32) DEFAULT 'DRAFT',
  create_time DATETIME,
  update_time DATETIME,
  is_deleted INT DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notice_read_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  notice_id BIGINT,
  user_id BIGINT,
  read_time DATETIME,
  PRIMARY KEY (id),
  KEY idx_notice_id (notice_id),
  KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;