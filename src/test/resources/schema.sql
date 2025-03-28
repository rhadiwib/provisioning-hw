DROP TABLE IF EXISTS device;

CREATE TABLE device (
  mac_address VARCHAR(255) PRIMARY KEY,
  model VARCHAR(255) NOT NULL,
  username VARCHAR(255),
  password VARCHAR(255),
  override_fragment CLOB
);

-- test data
INSERT INTO device (mac_address, model, username, password, override_fragment)
VALUES ('aa-bb-cc-dd-ee-ff', 'DESK', 'john', 'doe', null);

INSERT INTO device (mac_address, model, username, password, override_fragment)
VALUES ('f1-e2-d3-c4-b5-a6', 'CONFERENCE', 'sofia', 'red', null);

INSERT INTO device (mac_address, model, username, password, override_fragment)
VALUES ('a1-b2-c3-d4-e5-f6', 'DESK', 'walter', 'white', 'domain=sip.test.com
port=5161
timeout=10');

INSERT INTO device (mac_address, model, username, password, override_fragment)
VALUES ('1a-2b-3c-4d-5e-6f', 'CONFERENCE', 'eric', 'blue', '{"domain":"sip.test.com","port":"5161","timeout":10}');