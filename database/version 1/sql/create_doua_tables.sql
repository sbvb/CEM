CREATE TABLE IF NOT EXISTS UserGroup(
  UserGroupId integer unsigned not null auto_increment,
  name VARCHAR(100),
  Primary Key (UserGroupId)
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS ResourceGroup(
  ResourceGroupId integer unsigned not null auto_increment,
  name VARCHAR(100),
  Primary Key (ResourceGroupId)
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS Global(
  GlobalId integer unsigned not null auto_increment,
  sessionExpTime integer unsigned not null,
  Primary Key (GlobalId)
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS Authorization(
  AuthorizationId integer unsigned not null auto_increment,
  RscGroupId integer unsigned not null,
  UsrGroupId integer unsigned not null,
  Primary Key (AuthorizationId),
  index(RscGroupId),
  index(UsrGroupId),
  Foreign Key (RscGroupId) references ResourceGroup(ResourceGroupId) ON DELETE CASCADE ON UPDATE CASCADE,
  Foreign Key (UsrGroupId) references UserGroup(UserGroupId)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS User(
  UserId integer unsigned not null auto_increment,
  name VARCHAR(100) not null,
  pwd_hash VARCHAR(100) not null,
  UsrGroupId integer unsigned not null,
  Primary Key (UserId),
  index(UsrGroupId),
  Foreign Key (UsrGroupId) references UserGroup(UserGroupId)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS Resource(
  ResourceId integer unsigned not null auto_increment,
  short_name VARCHAR(50),
  long_name VARCHAR(100),
  link VARCHAR(50),
  Primary Key (ResourceId),
  RscGroupId integer unsigned not null,
  index(RscGroupId),
  Foreign Key (RscGroupId) references ResourceGroup(ResourceGroupId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS Session(
  sessionId VARCHAR(42) not null,
  expiration DATETIME NOT NULL,
  UsrId integer unsigned not null,
  ip VARCHAR(20) NOT NULL,
  Primary Key (sessionId),
  index(UsrId),
  Foreign Key (UsrId) references User(UserId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS LoginReport(
  LoginReportId integer unsigned not null auto_increment,
  username VARCHAR(50) NOT NULL,
  ip VARCHAR(20) NOT NULL,
  event VARCHAR(20) NOT NULL,
  curtime DATETIME NOT NULL,
  Primary Key (LoginReportId)
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;