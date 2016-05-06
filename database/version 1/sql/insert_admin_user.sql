DELETE FROM User;
INSERT INTO User(name,pwd_hash,UsrGroupId) VALUES('admin', '21232f297a57a5a743894a0e4a801fc3', 1);
INSERT INTO User(name,pwd_hash,UsrGroupId) VALUES('schoolman', 'd1e6b917e2b99d7e4a94d0390b84e304', 2);

INSERT INTO tb_person(nickname,name,telephone,email,school_id,person_type_id,obs) VALUES('admin','admin','(11)1111-1111','admin',NULL,1,NULL);
INSERT INTO tb_person(nickname,name,telephone,email,school_id,person_type_id,obs) VALUES('schoolman','schoolman','(11)1111-1111','schoolman',NULL,2,NULL);