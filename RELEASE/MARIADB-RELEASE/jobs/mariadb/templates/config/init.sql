SET password=PASSWORD('<%= p("password") %>');
GRANT ALL PRIVILEGES ON *.* TO '<%= p("username") %>'@'%' IDENTIFIED BY '<%= p("password") %>' WITH GRANT OPTION;
FLUSH PRIVILEGES;


CREATE DATABASE  IF NOT EXISTS `<%= p("database") %>` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
