insert into card(cvc, expiration_date, full_name, number)
	values
		('234', '03-32', 'Vitaly/Vitaliev', '4356745387654326'),
		('643', '04-32', 'Artem/Kirchaev', '6545673456765439'),
		('243', '05-32', 'Constantin/Kirchaev', '1029384756475837'),
		('843', '06-32', 'Dmitry/Paratasi', '3948574389210293'),
		('846', '07-32', 'Maxim/Curavnik', '9480394837623432'),
		('852', '08-32', 'Alexey/Marativi', '1234567654567654');
		
insert into account(card_id, email, first_name, last_name, password, role) 
	values
		(1, 'vitaliev@mail.ru', 'Виталий', 'Виталиев', '$2a$12$Sa6H0QLAg/6hHJN/t/ODy.8lX9ExCxQUbycqFW0bGQiLceeRAJRcS', 'USER'),
		(2, 'kirchaev@mail.ru', 'Артем', 'Кирчаев', '$2a$12$jAdgjNtK9hbt9yTwXhJaLejW3ub/p1V0f8lN13DaYr8OCzjk81T7K', 'USER'),
		(3, 'viltacov@mail.ru', 'Константин', 'Вилтаков', '$2a$12$uwTAnI5wrWFqF3lVBJSmeuOr.gg5eh7kNN.cLhFi.Dez5DEXP59Cy', 'USER'),
		(4, 'paratasi@mail.ru', 'Дмитрий', 'Паратаси', '$2a$12$.UUtqDPYcIMtlGbQd8rRCu4Whcww/580TJs6oPSmfPR2a0IIZX9GS', 'USER'),
		(5, 'kuravnik@mail.ru', 'Максим', 'Куравник', '$2a$12$yvEkAfrgTHLLLK99FBhI0u/HP22P6A/2xW9Av1xnAVZyyO5TxDjXi', 'ADMIN'),
		(6, 'marativi@mail.ru', 'Алексей', 'Маративи', '$2a$12$nmBOWw.lHKU0FUiCeFUYYOFyqnOhs/roVMFmIvprYi04Zs8rV3IN6', 'ADMIN');

insert into product_info(product_type, name, description, author_id, capacity, price, status)
	values
		('COURSE', 'first', 'this is first course i created', 5, 30, 1000, 'ACTIVE'),
		('COURSE', 'second', 'this is second course i created', 5, 30, 1000, 'ACTIVE'),
		('COURSE', 'third', 'this is third course i created', 5, 30, 1000, 'ACTIVE'),
		('COURSE', 'fourth', 'this is fourth course i created', 5, 30, 1000, 'ACTIVE'),
		('COURSE', 'fifth', 'this is fifth course i created', 6, 30, 1000, 'ACTIVE'),
		('COURSE', 'sixth', 'this is sixth course i created', 6, 30, 1000, 'ACTIVE'),
		('LESSON', 'first', 'this is first lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'second', 'this is second lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'third', 'this is third lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'fourth', 'this is fourth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'fifth', 'this is fifth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'sixth', 'this is sixth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'seventh', 'this is seventh lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'eighth', 'this is eighth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'ninth', 'this is ninth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'tenth', 'this is tenth lesson i created', 5, 30, 1000, 'ACTIVE'),
		('LESSON', 'eleventh', 'this is eleventh lesson i created', 6, 30, 1000, 'ACTIVE'),
		('LESSON', 'twelfth', 'this is twelfth lesson i created', 6, 30, 1000, 'ACTIVE'),
		('LESSON', 'thirteenth', 'this is thirteenth lesson i created', 6, 30, 1000, 'ACTIVE'),
		('LESSON', 'fourteenth', 'this is fourteenth lesson i created', 6, 30, 1000, 'ACTIVE'),
		('LESSON', 'first solo', 'this is first lesson without course i created', 1, 30, 1000, 'ACTIVE'),
		('LESSON', 'second solo', 'this is second lesson without course i created', 1, 30, 1000, 'ACTIVE'),
		('LESSON', 'third solo', 'this is third lesson without course i created', 1, 30, 1000, 'ACTIVE'),
		('LESSON', 'fourth solo', 'this is fourth lesson without course i created', 6, 30, 1000, 'ACTIVE');
		
insert into course_info(product_info_id)
	values
		(1),
		(2),
		(3),
		(4),
		(5),
		(6);
		
insert into lesson_info(product_info_id, course_info_id)
	values
		(7, 1),
		(8, 1),
		(9, 1),
		(10, 2),
		(11, 2),
		(12, 2),
		(13, 5),
		(14, 3),
		(15, 4),
		(16, 4),
		(17, 5),
		(18, 5),
		(19, 6),
		(20, 6);
		
insert into lesson_info(product_info_id)
	values
		(21),
		(22),
		(23),
		(24);
		
insert into course(capacity, notedate, course_info_id, min_donation, status)
	values
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 1, 100, 'ACTIVE'),
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 2, 100, 'ACTIVE'),
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 3, 100, 'ACTIVE'),
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 4, 100, 'ACTIVE'),
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 5, 100, 'ACTIVE'),
		(30, to_date('30-08-2022', 'DD-MM-YYYY'), 6, 100, 'ACTIVE');
		
insert into lesson(startdate, lesson_info_id, course_id, min_donation, status)
	values
		('2022-08-30 18:00:00', 1, 1, 100, 'ACTIVE'),
		('2022-09-06 18:00:00', 2, 1, 100, 'ACTIVE'),
		('2022-09-13 18:00:00', 3, 1, 100, 'ACTIVE'),
		('2022-08-30 19:00:00', 4, 2, 100, 'ACTIVE'),
		('2022-09-06 19:00:00', 5, 2, 100, 'ACTIVE'),
		('2022-09-13 19:00:00', 6, 2, 100, 'ACTIVE'),
		('2022-08-30 17:00:00', 7, 3, 100, 'ACTIVE'),
		('2022-09-06 17:00:00', 8, 3, 100, 'ACTIVE'),
		('2022-08-30 16:00:00', 9, 4, 100, 'ACTIVE'),
		('2022-09-06 16:00:00', 10, 4, 100, 'ACTIVE'),
		('2022-08-30 15:00:00', 11, 5, 100, 'ACTIVE'),
		('2022-09-06 15:00:00', 12, 5, 100, 'ACTIVE'),
		('2022-08-30 14:00:00', 13, 6, 100, 'ACTIVE'),
		('2022-09-06 14:00:00', 14, 6, 100, 'ACTIVE');
		
insert into lesson(startdate, lesson_info_id, min_donation, status)
	values
		('2022-08-31 18:00:00', 15, 100, 'Active'),
		('2022-09-07 18:00:00', 16, 100, 'Active'),
		('2022-09-14 18:00:00', 17, 100, 'Active'),
		('2022-08-29 19:00:00', 18, 100, 'Active');
		
insert into report(message, creation_date, author_id, product_info_id)
	values
		('good', '2022-07-30 18:00:00', 3, 1),
		('not bad', '2022-07-01 18:00:00', 4, 1),
		('very nice', '2022-07-08 18:00:00', 1, 1),
		('so so', '2022-07-29 19:00:00', 3, 2),
		('good', '2022-07-30 18:00:00', 3, 21),
		('not bad', '2022-07-01 18:00:00', 4, 21),
		('very nice', '2022-07-08 18:00:00', 1, 21),
		('so so', '2022-07-29 19:00:00', 3, 24);
		
insert into tag(name)
	values
		('Information technology'),
		('Programming'),
		('Analytics'),
		('Python'),
		('C++'),
		('Java'),
		('Spring'),
		('Testing'),
		('Project management'),
		('C'),
		('SQL'),
		('HTML/CSS'),
		('Linux'),
		('Git'),
		('Postgres'),
		('JUnit'),
		('Docker'),
		('Django'),
		('Intern'),
		('Junior'),
		('Middle'),
		('Backend'),
		('Frontend'),
		('Fullstack'),
		('NoSQL');

insert into study_direction(tag_id)
	values
		(1),
		(2),
		(3),
		(8);
		
insert into section(name)
	values
		('Specializations'),
		('Skill level'),
		('Languages and technologies'),
		('Programming');

insert into tag_section(tag_id, section_id)
	values
		(2, 1),
		(3, 1),
		(8, 1),
		(19, 2),
		(20, 2),
		(21, 2),
		(4, 3),
		(5, 3),
		(6, 3),
		(7, 3),
		(10, 3),
		(11, 3),
		(12, 3),
		(18, 3),
		(25, 3),
		(22, 4),
		(23, 4),
		(24, 4);
		
insert into section_direction(section_id, direction_id)
	values
		(1, 1),
		(2, 1),
		(2, 2),
		(3, 2),
		(4, 2),
		(2, 3),
		(2, 4);
		
insert into tag_product(tag_id, product_id)
	values
		(1, 1),
		(1, 2),
		(1, 3),
		(1, 4),
		(1, 5),
		(1, 6),
		(2, 1),
		(2, 2),
		(3, 4),
		(3, 3),
		(8, 5),
		(8, 6),
		(4, 1),
		(5, 2),
		(6, 3),
		(7, 4),
		(9, 5),
		(10, 6),
		(15, 1),
		(16, 2),
		(17, 3),
		(18, 4),
		(19, 5),
		(20, 6),
		(25, 1),
		(21, 21),
		(22, 22),
		(23, 23),
		(24, 24),
		(11, 21),
		(12, 22),
		(13, 23),
		(14, 24),
		(8, 22),
		(8, 21),
		(3, 24),
		(2, 23),
		(1, 21),
		(1, 22),
		(1, 23),
		(1, 24);

insert into user_course(user_id, course_id)
	values
		(1, 1),
		(2, 1),
		(3, 2),
		(4, 2),
		(5, 3),
		(6, 3),
		(1, 3),
		(2, 4),
		(3, 4),
		(4, 4),
		(5, 5),
		(6, 5),
		(1, 6),
		(2, 6),
		(3, 6),
		(4, 6);
		
insert into user_lesson(user_id, lesson_id)
	values
		(1, 15),
		(2, 15),
		(3, 15),
		(4, 15),
		(5, 16),
		(1, 17),
		(2, 17);