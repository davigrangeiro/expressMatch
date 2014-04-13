CREATE TABLE user
(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nick varchar(50) NOT NULL unique,
  name varchar(256) not null,
  pass varchar(500) NOT NULL,
  enabled boolean NOT NULL,
  insert_date TIMESTAMP DEFAULT NOW()
);
 
CREATE TABLE authorities
(
  id int not null auto_increment primary key,
  user_id int NOT NULL, 
  authority character varying(50) NOT NULL,
  insert_date TIMESTAMP DEFAULT NOW(),
  FOREIGN KEY (user_id) REFERENCES user (id)
);

