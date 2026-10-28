CREATE TABLE users(
  /*identity:自動インクリメントする主キーを示す(値は入れない)*/
  id IDENTITY,
  name VARCHAR NOT NULL
);
CREATE TABLE matchinfo(
  id IDENTITY,
  user1 INT NOT NULL,
  user2 INT NOT NULL,
  user1Hand VARCHAR NOT NULL,
  isActive BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE matches(
  id IDENTITY,
  user1 INT NOT NULL,
  user2 INT NOT NULL,
  user1hand VARCHAR NOT NULL,
  user2hand VARCHAR NOT NULL,
  /*DEFAULTで初期値をFALSEに設定*/
  isActive BOOLEAN NOT NULL DEFAULT FALSE
);
