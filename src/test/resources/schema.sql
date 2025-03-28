DROP TABLE IF EXISTS device;

CREATE TABLE device (
  mac_address VARCHAR(255) PRIMARY KEY,
  model VARCHAR(255) NOT NULL,
  username VARCHAR(255),
  password VARCHAR(255),
  override_fragment CLOB
);