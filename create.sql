create sequence app_user_seq start with 1 increment by 50;
create sequence facial_validation_data_seq start with 1 increment by 50;
create sequence passport_validation_data_seq start with 1 increment by 50;
create sequence report_seq start with 1 increment by 50;
create sequence student_app_user_seq start with 1 increment by 50;
create sequence student_seq start with 1 increment by 50;
create table app_user (id bigint not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), username varchar(255) not null unique, primary key (id));
create table facial_validation_data (is_valid boolean, percentage float(53), id bigint not null, passport_number varchar(255), primary key (id));
create table passport_validation_data (birth_date date, gender smallint check (gender between 0 and 1), passport_date_of_expiry date, passport_date_of_issue date, id bigint not null, timestamp timestamp(6), country_of_citizenship varchar(255), first_name varchar(255), last_name varchar(255), passport_number varchar(255), place_of_birth varchar(255), primary key (id));
create table report (id bigint not null, timestamp timestamp(6) not null, description varchar(1000) not null, issuer_username varchar(255), subject varchar(255), primary key (id));
create table student (birth_date date, gender smallint check (gender between 0 and 1), passport_date_of_expiry date, passport_date_of_issue date, valid boolean not null, id bigint not null, country_of_citizenship varchar(255), first_name varchar(255), last_name varchar(255), passport_number varchar(255) not null unique, place_of_birth varchar(255), primary key (id));
create table student_app_user (id bigint not null, student_id bigint, username varchar(255), primary key (id));
create table student_image (student_id bigint not null, passport_image oid, selfie_image oid, primary key (student_id));
create table user_roles (user_id bigint not null, role varchar(255) check (role in ('USER','ADMIN')));
alter table if exists user_roles add constraint FK6fql8djp64yp4q9b3qeyhr82b foreign key (user_id) references app_user;
create sequence app_user_seq start with 1 increment by 50;
create sequence facial_validation_data_seq start with 1 increment by 50;
create sequence passport_validation_data_seq start with 1 increment by 50;
create sequence report_seq start with 1 increment by 50;
create sequence student_app_user_seq start with 1 increment by 50;
create sequence student_seq start with 1 increment by 50;
create table app_user (id bigint not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), username varchar(255) not null unique, primary key (id));
create table facial_validation_data (is_valid boolean, percentage float(53), id bigint not null, passport_number varchar(255), primary key (id));
create table passport_validation_data (birth_date date, gender smallint check (gender between 0 and 1), passport_date_of_expiry date, passport_date_of_issue date, id bigint not null, timestamp timestamp(6), country_of_citizenship varchar(255), first_name varchar(255), last_name varchar(255), passport_number varchar(255), place_of_birth varchar(255), primary key (id));
create table report (id bigint not null, timestamp timestamp(6) not null, description varchar(1000) not null, issuer_username varchar(255), subject varchar(255), primary key (id));
create table student (birth_date date, gender smallint check (gender between 0 and 1), passport_date_of_expiry date, passport_date_of_issue date, valid boolean not null, id bigint not null, country_of_citizenship varchar(255), first_name varchar(255), last_name varchar(255), passport_number varchar(255) not null unique, place_of_birth varchar(255), primary key (id));
create table student_app_user (id bigint not null, student_id bigint, username varchar(255), primary key (id));
create table student_image (student_id bigint not null, passport_image oid, selfie_image oid, primary key (student_id));
create table user_roles (user_id bigint not null, role varchar(255) check (role in ('USER','ADMIN')));
alter table if exists user_roles add constraint FK6fql8djp64yp4q9b3qeyhr82b foreign key (user_id) references app_user;
