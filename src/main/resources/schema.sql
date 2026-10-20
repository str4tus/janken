CREATE TABLE users(
  /*identity:自動インクリメントする主キーを示す(値は入れない)*/
  id IDENTITY,
  name VARCHAR NOT NULL
);
CREATE TABLE matches(
  id IDENTITY,
  user1 INT NOT NULL,
  user2 INT NOT NULL,
  user1hand VARCHAR NOT NULL,
  user2hand VARCHAR NOT NULL
);
