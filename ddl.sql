DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS card CASCADE;
DROP TABLE IF EXISTS product_info CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS course_info CASCADE;
DROP TABLE IF EXISTS lesson_info CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS lesson CASCADE;
DROP TABLE IF EXISTS report CASCADE;
DROP TABLE IF EXISTS section CASCADE;
DROP TABLE IF EXISTS study_direction CASCADE;
DROP TABLE IF EXISTS tag CASCADE;
DROP TABLE IF EXISTS tag_product CASCADE;
DROP TABLE IF EXISTS tag_section CASCADE;
DROP TABLE IF EXISTS user_course CASCADE;
DROP TABLE IF EXISTS user_lesson CASCADE;
DROP TABLE IF EXISTS section_direction CASCADE;

create table card
(
    card_id bigserial primary key,
    cvc varchar(3),
    expiration_date varchar(5),
    full_name varchar(50),
    number varchar(16)
);

create table account
(
    user_id bigserial primary key,
    email      varchar(50) unique,
    first_name varchar(50),
    last_name  varchar(50),
    password   varchar(255),
    role       varchar(50),
	card_id bigint,
	constraint fk_card
		foreign key(card_id) references card(card_id)
);

create table product_info
(
    product_info_id bigserial primary key,
    description text,
	name varchar(50),
	capacity integer,
    price numeric,
	product_type varchar(255),
    author_id bigint,
	status varchar(255),
	constraint fk_user
		foreign key(author_id) references account(user_id) ON DELETE CASCADE
);

create table payment
(
    payment_id bigserial primary key,
    buyer_id bigint,
	seller_id bigint,
	product_realization bigint,
	product_info_id bigint,
	payment_type varchar(255),
	payment_price numeric,
	checked bool,
	payment_date timestamp without time zone,
	constraint fk_product
		foreign key(product_info_id) references product_info(product_info_id) ON DELETE CASCADE,
	constraint fk_buyer
		foreign key(buyer_id) references account(user_id) ON DELETE CASCADE,
	constraint fk_seller
		foreign key(seller_id) references account(user_id) ON DELETE CASCADE
);

create table course_info
(
    course_info_id bigserial primary key,
    product_info_id bigint,
	constraint fk_product
		foreign key(product_info_id) references product_info(product_info_id) ON DELETE CASCADE
);

create table lesson_info
(
    lesson_info_id bigserial primary key,
	course_info_id bigint,
	product_info_id bigint,
	constraint fk_product
		foreign key(product_info_id) references product_info(product_info_id) ON DELETE CASCADE,
	constraint fk_course
		foreign key(course_info_id) references course_info(course_info_id) ON DELETE CASCADE
);

create table course
(
    course_id bigserial primary key,
    capacity integer,
	min_donation numeric,
    notedate date,
	status varchar(255),
    course_info_id bigint,
	constraint fk_course
		foreign key(course_info_id) references course_info(course_info_id) ON DELETE CASCADE
);

create table lesson
(
    lesson_id bigserial primary key,
	min_donation numeric,
	capacity integer,
    startdate timestamp without time zone,
	lesson_link varchar(255),
	homework_link varchar(255),
	status varchar(255),
    lesson_info_id bigint,
	course_id bigint,
	constraint fk_lesson
		foreign key(lesson_info_id) references lesson_info(lesson_info_id) ON DELETE CASCADE,
	constraint fk_course
		foreign key(course_id) references course(course_id) ON DELETE CASCADE
);

create table report
(
    report_id bigserial primary key,
	message text,
	creation_date timestamp without time zone,
    author_id bigint,
	product_info_id bigint,
	constraint fk_user
		foreign key(author_id) references account(user_id) ON DELETE CASCADE,
	constraint fk_product_report
		foreign key(product_info_id) references product_info(product_info_id) ON DELETE CASCADE
);

create table tag
(
    tag_id bigserial primary key,
    name varchar(50)
);

create table study_direction
(
    direction_id bigserial primary key,
	tag_id bigint,
	constraint fk_tag
		foreign key(tag_id) references tag(tag_id) ON DELETE CASCADE
);

create table section
(
    section_id bigserial primary key,
	name varchar(255)
);

create table tag_product
(
    tag_id bigint,
    product_id bigint,
    constraint tag_product_pk primary key (tag_id, product_id),
	constraint fk_tag foreign key(tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE,
    constraint fk_product foreign key(product_id) references product_info(product_info_id) ON DELETE CASCADE
);

create table tag_section
(
    tag_id bigint,
    section_id bigint,
    constraint tag_section_pk primary key (tag_id, section_id),
	constraint fk_tag foreign key(tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE,
    constraint fk_section foreign key(section_id) REFERENCES section(section_id) ON DELETE CASCADE
);

create table user_course
(
    user_id bigint,
    course_id bigint,
    constraint user_course_pk primary key (user_id, course_id),
	constraint fk_user foreign key(user_id) REFERENCES account(user_id) ON DELETE CASCADE,
    constraint fk_course foreign key(course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

create table user_lesson
(
    user_id bigint,
    lesson_id bigint,
    constraint user_lesson_pk primary key (user_id, lesson_id),
	constraint fk_user foreign key(user_id) REFERENCES account(user_id) ON DELETE CASCADE,
    constraint fk_lesson foreign key(lesson_id) REFERENCES lesson(lesson_id) ON DELETE CASCADE
);

create table section_direction
(
    section_id bigint,
    direction_id bigint,
    constraint section_direction_pk primary key (section_id, direction_id),
	constraint fk_section foreign key(section_id) REFERENCES section(section_id) ON DELETE CASCADE,
    constraint fk_direction foreign key(direction_id) REFERENCES study_direction(direction_id) ON DELETE CASCADE
);