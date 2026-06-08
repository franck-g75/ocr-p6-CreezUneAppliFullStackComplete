INSERT INTO TOPIC(id, title, content) VALUES (1, 'Java', 'les bases du langage sont très lointaines...');
INSERT INTO TOPIC(id, title, content) VALUES (2, 'Angular', 'les bases du langage sont plus proches.');
INSERT INTO TOPIC(id, title, content) VALUES (3, 'PHP', 'PHP ? bof...');
INSERT INTO TOPIC(id, title, content) VALUES (4, 'SQL', 'Quelles sont les différences entre DDL et DML ?');
INSERT INTO TOPIC(id, title, content) VALUES (5, 'SAP', 'SAP et la migration S4Hana');

INSERT INTO USERS(id,username,email,pwd) VALUES (1, 'fguindeuil','franck.guindeuil@coucou.fr','motdepasse');
INSERT INTO USERS(id,username,email,pwd) VALUES (2, 'lili','lili@coucou.fr','passedemot');
INSERT INTO USERS(id,username,email,pwd) VALUES (3, 'delcour','delcour@coucou.fr','passedemot');

INSERT INTO POST(id, title, content, topic_id, users_id, created_at) VALUES(1,"JAVA c'est de la balle", "Java, mais c'est super génial !", 1, 1, '2026-06-05 10:49:54');
INSERT INTO POST(id, title, content, topic_id, users_id, created_at) VALUES(2,"ANGULAR c'est de la balle", "Angular est super génial aussi", 1, 1, '2026-06-05 11:10:15');

INSERT INTO COMMENT(id, content, post_id, users_id, created_at) VALUES(1, "c'est vrai", 1, 2, '2026-06-05 10:55:45');
INSERT INTO COMMENT(id, content, post_id, users_id, created_at) VALUES(2, "c'est meme pas vrai", 2, 2, '2026-06-05 12:25:56');
INSERT INTO COMMENT(id, content, post_id, users_id, created_at) VALUES(3, "c'est celui qui le dit qui y est", 2, 1, '2026-06-05 12:26:12');

INSERT INTO TOPIC_USERS(topic_id, users_id) VALUES (1,1);
INSERT INTO TOPIC_USERS(topic_id, users_id) VALUES (2,1);
INSERT INTO TOPIC_USERS(topic_id, users_id) VALUES (3,1);
INSERT INTO TOPIC_USERS(topic_id, users_id) VALUES (5,2);
INSERT INTO TOPIC_USERS(topic_id, users_id) VALUES (2,2);
