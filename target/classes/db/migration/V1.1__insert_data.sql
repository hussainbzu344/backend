INSERT INTO `project_type` (name,type) VALUES('DesktopApplication','cmd');
INSERT INTO `project_type` (name,type) VALUES('DesktopApplication','gui-swing');
INSERT INTO `project_type` (name,type) VALUES('DesktopApplication','gui-awt');
INSERT INTO `project_type` (name,type) VALUES('DesktopApplication','gui-javaFX');
INSERT INTO `project_type` (name,type) VALUES('MobileApplication','android');
INSERT INTO `project_type` (name,type) VALUES('MobileApplication','java mobile');
INSERT INTO `project_type` (name,type) VALUES('EmbeddedApplication','javaME');
INSERT INTO `project_type` (name,type) VALUES('WebServer','apache');
INSERT INTO `project_type` (name,type) VALUES('WebService','soap');
INSERT INTO `project_type` (name,type) VALUES('WebService','rest');
INSERT INTO `project_type` (name,type) VALUES('--','--');

INSERT INTO `framework` (`name`, `core_library`, `type_id`) VALUES ('aspectj ajde', 'org.aspectj.ajde', '11');
INSERT INTO `framework` (`name`, `core_library`, `type_id`) VALUES ('aspectj ajdt', 'org.aspectj.ajdt', '11');

INSERT INTO `framework` (name,core_library,type_id) VALUES ('spring boot','org.springframework.boot',10);
INSERT INTO `framework` (name,core_library,type_id) VALUES('apache log4j','org.apache.logging.log4j',11);
INSERT INTO `framework` (name,core_library,type_id) VALUES('junit','org.junit',11);
INSERT INTO `framework` (`name`, `core_library`, `type_id`) VALUES ('spring mvc', 'org.springframework.webmvc', '10');
INSERT INTO `framework` (`name`, `core_library`, `type_id`) VALUES ('spring web', 'org.springframework.web', '10');



INSERT INTO `entry_point` (name,type,location,framework_id) VALUES ('org.springframework.boot.autoconfigure.SpringBootApplication','Entry point','main method',1);
INSERT INTO `entry_point` (`name`, `type`, `location`, `framework_id`) VALUES ('org.springframework.web.servlet.DispatcherServlet', 'Entry point', 'Xml', '4');
INSERT INTO `entry_point` (`name`, `type`, `location`, `framework_id`) VALUES ('org.springframework.web.servlet.DispatcherServlet', 'Entry point', 'main method', '4');
INSERT INTO `entry_point` (`name`, `type`, `location`, `framework_id`) VALUES ('org.springframework.web.servlet.DispatcherServlet', 'Entry point', 'Xml', '5');
INSERT INTO `entry_point` (`name`, `type`, `location`, `framework_id`) VALUES ('org.springframework.web.servlet.DispatcherServlet', 'Entry point', 'main method', '5');
