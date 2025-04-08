insert into trade_union(tu_name,abbreviation,address,phone,logo,tu_percentage) values('ZESSCWU','ZESSCWU','Nicoz Diamond House','+263774123654',null,4.5);

insert into sector(sector_name,trade_union) values('NGO',1);
insert into sector(sector_name,trade_union) values('Independent A- Trust Schools',1);
insert into sector(sector_name,trade_union) values('Mission Boarding Schools',1);
insert into sector(sector_name,trade_union) values('Welfare C',1);
insert into sector(sector_name,trade_union) values('Welfare A',1);
insert into sector(sector_name,trade_union) values('Welfare B',1);



insert into grade(grade_name,sector,weekly_wage,fortnightly_wage,basic_wage) values('A',1,33,66.3,400);
insert into grade(grade_name,sector,weekly_wage,fortnightly_wage,basic_wage) values('B',2,40.0,80.0,600);
insert into grade(grade_name,sector,weekly_wage,fortnightly_wage,basic_wage) values('C',3,35.2,70,700);
insert into grade(grade_name,sector,weekly_wage,fortnightly_wage,basic_wage) values('D',4,65,120,900);

insert into institution(sector,address,phone,institution_name) values(1,'Box 21 Gweru','+263774028681','Mkoba Teachers College');



insert into member(firstname,surname,national_id,gender,marital_status,dob,cell_number,email,
					next_of_kin,nok_phone,relationship,grade,job_title,status)
					values('Taka','Haku','12116786T12','M','Married','2019-01-05','+263774589652','tttttt@mmm.nnn',
							'Taka Haku','263789547852','Father',1,'Teacher','Active');






insert into roles(name) VALUES('ROLE_SUPER');
insert into roles(name) VALUES('ROLE_ADMIN');
insert into roles(name) VALUES('ROLE_MEMBERSHIP');
insert into roles(name) VALUES('ROLE_ACCOUNTS');


insert into branch(branch_name,email,branch_phone,branch_town,trade_union) VALUES('Harare','harare@gmail.com','0242225896','Harare',1);
insert into branch(branch_name,email,branch_phone,branch_town,trade_union) VALUES('Gweru','gweru@gmail.com','0542225896','Gweru',1);
insert into branch(branch_name,email,branch_phone,branch_town,trade_union) VALUES('Mutare','mutare@gmail.com','0392225896','Mutare',1);

insert into employees(trade_union_id,employee_name,email,phone) values(1,'Taka Haku','tkzhaku@gmail.com','0774444444');

insert into stop_order(stop_order_number,member,date_employed,date_joined,recruitment_branch,institution,date_terminated) VALUES(
1,1,'2020-01-01','2020-01-01',1,1,null);