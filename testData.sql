-- Adminer 5.3.0 MariaDB 11.7.2-MariaDB-ubu2404 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `author_id` varchar(255) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKht8dpg0ct5cw7qe7t62d9lpxd` (`author_id`),
  KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
  CONSTRAINT `FKht8dpg0ct5cw7qe7t62d9lpxd` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `comment` (`id`, `content`, `timestamp`, `author_id`, `post_id`) VALUES
(1,	'Yes! And now you can even leave a comment to a post!',	'2025-06-14 16:05:24.353823',	'f9724447-8769-4fe4-8559-8eb9763064b4',	36),
(2,	'Yes! Today the whole page has changed, amazing!',	'2025-06-14 21:31:38.636779',	'f9724447-8769-4fe4-8559-8eb9763064b4',	36),
(3,	'wow, you can comment now?',	'2025-06-14 21:54:26.753139',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85',	36),
(4,	'Ohhhhhhh!',	'2025-06-14 21:56:19.260841',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	36),
(5,	'Cool, right?',	'2025-06-14 22:00:32.089531',	'f9724447-8769-4fe4-8559-8eb9763064b4',	37),
(6,	'But there\'s still some little problems...',	'2025-06-14 22:22:51.360787',	'f9724447-8769-4fe4-8559-8eb9763064b4',	37),
(7,	'Yes you can!',	'2025-06-14 22:29:43.279887',	'f9724447-8769-4fe4-8559-8eb9763064b4',	30),
(8,	'And some more features on the way...',	'2025-06-14 22:32:48.989399',	'f9724447-8769-4fe4-8559-8eb9763064b4',	30),
(9,	'Wow looks like it\'s working now!',	'2025-06-14 22:33:17.087434',	'f9724447-8769-4fe4-8559-8eb9763064b4',	37),
(10,	'Now there\'s even more new design!',	'2025-06-14 23:50:21.781414',	'f9724447-8769-4fe4-8559-8eb9763064b4',	27),
(11,	'Oh, now I am not even the first user in the Docker Compose!',	'2025-06-15 00:29:46.897801',	'42fb79b2-0e52-4026-9929-a0db3a18d15a',	38),
(15,	'Yes ^_^',	'2025-06-21 15:54:40.189184',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85',	25),
(16,	'Haha',	'2025-06-21 15:55:10.209407',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85',	44),
(17,	'Wow that\'s great!',	'2025-06-26 23:15:37.576682',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85',	46),
(18,	'Looks great!',	'2025-06-26 23:16:52.516156',	'575c5039-8cb5-49b2-8825-25b3c3317172',	46),
(19,	'Yay',	'2025-06-26 23:20:55.731230',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	48);

DROP TABLE IF EXISTS `hashtag_post`;
CREATE TABLE `hashtag_post` (
  `post_id` bigint(20) NOT NULL,
  `hashtag_id` bigint(20) NOT NULL,
  KEY `FKldhvovb6npj6ndb6wab58d42r` (`hashtag_id`),
  KEY `FKhxtkd48remreae69yduo7kxvq` (`post_id`),
  CONSTRAINT `FKhxtkd48remreae69yduo7kxvq` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKldhvovb6npj6ndb6wab58d42r` FOREIGN KEY (`hashtag_id`) REFERENCES `hash_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `hashtag_post` (`post_id`, `hashtag_id`) VALUES
(46,	102),
(46,	103),
(46,	104),
(47,	102),
(48,	102);

DROP TABLE IF EXISTS `hash_tag`;
CREATE TABLE `hash_tag` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `hash_tag` (`id`, `name`) VALUES
(102,	'hashtag'),
(103,	'test'),
(104,	'123Go');

DROP TABLE IF EXISTS `hash_tag_seq`;
CREATE TABLE `hash_tag_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;


DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `author_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj8ntmheqdjt8hvo4td0wixygs` (`author_id`),
  CONSTRAINT `FKj8ntmheqdjt8hvo4td0wixygs` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `post` (`id`, `content`, `timestamp`, `author_id`) VALUES
(1,	'It\'s my first Post at Tweetigel!!!',	'2025-05-16 21:05:02.136322',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(2,	'Second post, let\'s see if everything work well.',	'2025-05-18 15:24:32.329397',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(3,	'My name is Yoyo!',	'2025-05-18 21:10:55.658356',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
(4,	'Hi everyone! I\'m happy to be here at TweetIgel!',	'2025-05-25 16:41:38.017977',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(5,	'Hey I like the new UI colors!',	'2025-05-25 23:11:05.578387',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(6,	'Wow now I can edit my profile page! Cool!',	'2025-05-30 14:21:11.390866',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(7,	'I heard there is something called Ollama?',	'2025-06-04 19:50:10.782835',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(8,	'Have anyone ever played with hula hoop?',	'2025-06-04 19:51:03.613942',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(9,	'Let\'s tweet as much as we can!',	'2025-06-04 19:51:35.155478',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(10,	'Can\'t wait to see some new features here! So that I can switch from X to here...',	'2025-06-04 19:52:29.863770',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(11,	'Can you be more specific? What features do you want?',	'2025-06-04 19:53:25.584751',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(12,	'Hmmm, like pagination? I don\'t want to scroll the screen all the time..',	'2025-06-04 19:54:09.277705',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(13,	'Then let\'s make it happen!',	'2025-06-04 19:54:39.355422',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(14,	'Yo! I can\'t wait!',	'2025-06-04 19:55:43.969649',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
(15,	'Spricht hier jemand Deutsch?',	'2025-06-04 19:57:34.750216',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
(16,	'Ja!',	'2025-06-04 19:57:51.985676',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(17,	'Why is this platform called TweetIgel? And why is the logo a hedgehog?',	'2025-06-04 20:01:13.736691',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(18,	'Hedgehog is cute!',	'2025-06-04 20:01:57.762413',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(19,	'Okay it\'s No.19! Who is going to post the 20th post?',	'2025-06-04 20:04:02.573207',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(20,	'Is ironic that I am the first user but I didn\'t post the first post?',	'2025-06-04 20:05:02.317032',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(21,	'Well because you didn\'t use BCrypt for your passpord, so at first you couldn\'t log in, right?',	'2025-06-04 20:05:43.810793',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(22,	'That\'s true...',	'2025-06-04 20:06:20.143849',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(23,	'I think we have posted more than 20 posts, good job!',	'2025-06-04 20:07:30.875220',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(24,	'Wonderful!',	'2025-06-04 20:08:17.396617',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(25,	'The place is getting more and more lively, I like it!',	'2025-06-04 20:10:00.635230',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(26,	'I heard the editor got updated!',	'2025-06-05 19:07:37.993461',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(27,	'I like new design!',	'2025-06-05 19:26:20.681673',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(28,	'Can I see the tick another time if I post again?',	'2025-06-05 19:27:19.689207',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(29,	'yay happy happy happy ',	'2025-06-12 08:48:13.324000',	'4cb505eb-3a95-4562-b03d-86cf08185f85'),
(30,	'I heard you can delete your post now!',	'2025-06-14 12:54:53.615531',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(32,	'Let me try the deletion too!',	'2025-06-14 13:10:54.029145',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(34,	'I heard the editor got an update!',	'2025-06-14 13:19:26.612635',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(35,	'Wow! now the post successfully display will automatically disappear after 3 seconds!',	'2025-06-14 13:19:54.465184',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(36,	'wow, I see there\'s a lot of update going on~',	'2025-06-14 13:24:49.394800',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(37,	'I heard the editor is renewed!',	'2025-06-14 19:56:11.298213',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(38,	'Wow! The first post in Docker Compose!',	'2025-06-15 00:18:56.926925',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(44,	'Great to see so many new features!',	'2025-06-21 15:54:57.757448',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(46,	'I have reworked #hashtag, let\'s #test it out, #123Go!',	'2025-06-26 21:44:20.665292',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(47,	'I love #hashtag!!!',	'2025-06-26 23:15:49.455399',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(48,	'Wow there\'s #hashtag? Amazing!',	'2025-06-26 23:16:11.697729',	'575c5039-8cb5-49b2-8825-25b3c3317172');

DROP TABLE IF EXISTS `post_hashtags`;
CREATE TABLE `post_hashtags` (
  `post_id` bigint(20) NOT NULL,
  `hashtags_id` bigint(20) NOT NULL,
  KEY `FK98q4niybsnyrgbl2xhn814hsn` (`hashtags_id`),
  KEY `FKhkuj72xlmwdt447ax2qoq8o0r` (`post_id`),
  CONSTRAINT `FK98q4niybsnyrgbl2xhn814hsn` FOREIGN KEY (`hashtags_id`) REFERENCES `hash_tag` (`id`),
  CONSTRAINT `FKhkuj72xlmwdt447ax2qoq8o0r` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;


DROP TABLE IF EXISTS `post_liked_list`;
CREATE TABLE `post_liked_list` (
  `post_id` bigint(20) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`post_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FKp2qc7oy64vmo7vjej5fes07y7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `post_liked_list_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `post_liked_list` (`post_id`, `user_id`) VALUES
(10,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(12,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(21,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(35,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(36,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(38,	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
(25,	'4cb505eb-3a95-4562-b03d-86cf08185f85'),
(20,	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(29,	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(38,	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(46,	'575c5039-8cb5-49b2-8825-25b3c3317172'),
(3,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(4,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(23,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(26,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(35,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(36,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(46,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(48,	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
(1,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(3,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(4,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(7,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(17,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(24,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(25,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(27,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(28,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(38,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(46,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(48,	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
(4,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(5,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(6,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(7,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(29,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(30,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(36,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(47,	'f9724447-8769-4fe4-8559-8eb9763064b4'),
(48,	'f9724447-8769-4fe4-8559-8eb9763064b4');

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `registered_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `hashed_password` varchar(255) DEFAULT NULL,
  `biography` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `user` (`id`, `registered_at`, `username`, `hashed_password`, `biography`, `email`, `full_name`) VALUES
('1b29b21b-492c-4a53-a0e1-ae0f1d84ecd9',	'2025-06-05 19:21:35.816098',	'newUser',	'$2a$10$Uz5FGPIo7lDpCFNIKnsS..TfkWekGuigUJqwFfNJfZJiRaC2nH/1a',	NULL,	NULL,	NULL),
('42fb79b2-0e52-4026-9929-a0db3a18d15a',	'2025-05-11 10:51:33.818128',	'firstUser',	'$2a$10$6o8nkTavEXFchwEgK7yv5eHj/AzKEu2A9u4F1U2fNYd3Y44dWluyW',	'Hey I am the first user of the application!',	'',	''),
('4cb505eb-3a95-4562-b03d-86cf08185f85',	'2025-06-12 08:47:29.658599',	'mai',	'$2a$10$T7KHRa7dF4nacwwnHC8vl.hg0uIX1sNmCi59Ja57RFGo7ZjIiYkWm',	'student',	'mai@gmail.com',	'nguyen thi tuyet mai'),
('575c5039-8cb5-49b2-8825-25b3c3317172',	'2025-05-15 23:15:56.444233',	'superStar',	'$2a$10$B7/D7JObiZSJmeT4wkeWgebn02AwyLVyXnZNd3/6y8Nj28xRfdnDS',	'You already know me ^_^',	'star@famous.de',	'Huge Star'),
('a9b2091c-4155-41a7-aa7e-446de793ad6e',	'2025-05-18 15:33:07.121406',	'Yoyo',	'$2a$10$oZN3hx7l/DLs7HfaaXpeeOlq8y70cITsPzLCivziDDZl4sjhmc/F.',	NULL,	NULL,	NULL),
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'2025-05-22 21:31:44.224863',	'Olala',	'$2a$10$cunSCiccLRHQ5EpKIirThONAxBztkfbtmYuewfgikflhCUb3ts6Hy',	NULL,	NULL,	NULL),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'2025-05-24 13:02:04.784176',	'Hoppla',	'$2a$10$RmjZuVZdLRVsIchSSzPP0.v0swDcYVGISXhm13WNbU.YrIz9POufm',	'I love hula loop!',	'hoppla@hulala.de',	'Hupu Hola'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'2025-05-16 10:53:09.792638',	'safeUser',	'$2a$10$nzPe7ZS28M9T4REwcNC8GeZfObKoIrsZ.KYTcRNXdwiwUSLI0/3iO',	'I am safe because I am the first one to use BCrypt for my password!',	'safe@a-safe-email.com',	'Very Safe');

DROP TABLE IF EXISTS `user_followed`;
CREATE TABLE `user_followed` (
  `follower_id` varchar(255) NOT NULL,
  `followed_id` varchar(255) NOT NULL,
  PRIMARY KEY (`follower_id`,`followed_id`),
  KEY `FKeauwx9oma5ek3uaqte69e0krw` (`followed_id`),
  CONSTRAINT `FKeauwx9oma5ek3uaqte69e0krw` FOREIGN KEY (`followed_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKi86v1lf8hf995tvkkg9an66qw` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO `user_followed` (`follower_id`, `followed_id`) VALUES
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'1b29b21b-492c-4a53-a0e1-ae0f1d84ecd9'),
('4cb505eb-3a95-4562-b03d-86cf08185f85',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
('575c5039-8cb5-49b2-8825-25b3c3317172',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'42fb79b2-0e52-4026-9929-a0db3a18d15a'),
('575c5039-8cb5-49b2-8825-25b3c3317172',	'4cb505eb-3a95-4562-b03d-86cf08185f85'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'4cb505eb-3a95-4562-b03d-86cf08185f85'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'4cb505eb-3a95-4562-b03d-86cf08185f85'),
('42fb79b2-0e52-4026-9929-a0db3a18d15a',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('4cb505eb-3a95-4562-b03d-86cf08185f85',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('a9b2091c-4155-41a7-aa7e-446de793ad6e',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'575c5039-8cb5-49b2-8825-25b3c3317172'),
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'a9b2091c-4155-41a7-aa7e-446de793ad6e'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'bae1350e-8db1-4aaf-ae8f-e9a497bf35f4'),
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
('f9724447-8769-4fe4-8559-8eb9763064b4',	'e48a37cc-4958-4f91-b8d9-16a3c14beb85'),
('42fb79b2-0e52-4026-9929-a0db3a18d15a',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
('575c5039-8cb5-49b2-8825-25b3c3317172',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
('a9b2091c-4155-41a7-aa7e-446de793ad6e',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
('bae1350e-8db1-4aaf-ae8f-e9a497bf35f4',	'f9724447-8769-4fe4-8559-8eb9763064b4'),
('e48a37cc-4958-4f91-b8d9-16a3c14beb85',	'f9724447-8769-4fe4-8559-8eb9763064b4');

-- 2025-07-03 20:28:01 UTC
