
--CREATE TABLE IF NOT EXISTS PLAYER TABLE  (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  username VARCHAR(80) NOT NULL
--);
--
--
--CREATE TABLE IF NOT EXISTS PLAYER_TRANSACTION (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  balance DECIMAL(8,2) DEFAULT 0.00,
--  status VARCHAR(20),
--  player_id INT NOT NULL FOREIGN KEY(id) REFERENCES PLAYER(id)
--);