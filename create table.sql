CREATE DATABASE BTL_LTW;
USE BTL_LTW;

SET SQL_SAFE_UPDATES = 0;

create table users
(
	user_id bigint not null auto_increment,
	address varchar(250),
    email varchar(100),
    full_name varchar(100),
    password varchar(255),
    phone varchar(15),
    created_at bigint,
    role int,
    username varchar(50),
    last_action bigint,
    status int not null,
    constraint user_pk primary key (user_id)
);

create table liked
(
	user_id bigint not null ,
    blog_id bigint not null,
    constraint liked_pk primary key (user_id, blog_id)
);
alter table liked add constraint like_user foreign key (user_id) references users (user_id);
alter table liked add constraint like_blog foreign key (blog_id) references blogs (blog_id);

create table blogs
(
	blog_id bigint not null auto_increment,
	content text,
    created_at bigint,
    image_title varchar(255),
    title varchar(255),
    user_id bigint not null,
    status int not null,
    constraint blog_pk primary key (blog_id)
);
alter table blogs add constraint blog_user foreign key (user_id) references users (user_id);

create table comments
(
	comment_id bigint not null auto_increment,
    content text,
    created_at bigint,
    blog_id bigint not null, 
    user_id bigint not null,
    status int not null,
    constraint comment_pk primary key (comment_id)
);
alter table comments add constraint comment_user foreign key (user_id) references users( user_id);
alter table comments add constraint comment_blog foreign key (blog_id) references blogs( blog_id);
 
create table blogs_categories
(
	blog_id bigint not null,
    category_id bigint not null,
    constraint blog_category_pk primary key(blog_id, category_id)
);
alter table blogs_categories add constraint blog_category_blog foreign key (blog_id) references blogs(blog_id);
alter table blogs_categories add constraint blog_category_category foreign key (category_id) references categories( category_id);
create table categories
(
	category_id bigint not null auto_increment,
    name varchar(50),
    constraint category_id primary key( category_id),
    user_id bigint,
    status int not null,
    created_at bigint
);

alter table categories add constraint categories_user foreign key (user_id) references users(user_id);