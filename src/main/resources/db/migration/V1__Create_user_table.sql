CREATE TABLE USER
(
 id int auto_increment PRIMARY KEY NOT NULL ,
 account_id VARCHAR(100),
 name VARCHAR (50),
 token VARCHAR (36),
 gmt_create BIGINT,
 gmt_modified bigint
);