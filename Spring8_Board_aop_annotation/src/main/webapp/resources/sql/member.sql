
DROP TABLE member CASCADE CONSTRAINTS;

create table member(
 	id 			 varchar2(15),
 	password	 varchar2(10),
 	name		 varchar2(15),
 	age 		 Number,
 	gender 		 varchar2(5),
 	email  		 varchar2(130),
 	PRIMARY KEY(id)
 	);


 select * from member;
 update member set password=1 where id='admin'
 
 delete from member
 where id='admin';
 
 delete from member
 where id='marimari';
 
 insert into member
 values('admin',1234,'관리자',99,'여','admin@a.com');
 
 insert into member
 values('marimari',1234,'마숭이',7,'여','mari@a.com');
 
 ALTER TABLE member MODIFY(password VARCHAR2(60));
 commit