create table suse( reg varchar(20) primary key,pass varchar(20),name varchar(30),dept varchar(20),mn varchar(20),addr varchar(200));
create table tuse( un varchar(20) primary key,pass varchar(20));
create table sems(s varchar(10))
insert into sems values('sem1')
insert into sems values('sem2')
delete from sems where s='sem3'
insert into suse values('953622104092','123','Sathasin P','CSE','6379442179','123 RC church Road,malaiyaqdipatti')
insert into tuse values('T1','123')
create table sem1( reg varchar(20) primary key, tamil varchar(5),python varchar(5),english varchar(5),M1 varchar(5),phy varchar(5),che varchar(5))
insert into sem1 values('953622104092','B+','O','B+','o','A+','A')
create table sem2( reg varchar(20) primary key, tamil2 varchar(5),c varchar(5),english2 varchar(5),M2 varchar(5),phy2 varchar(5),BEEE varchar(5),EG varchar(5))
insert into sem2 values('953622104092','B+','O','B+','O','A+','B','A+')

select * from sems

