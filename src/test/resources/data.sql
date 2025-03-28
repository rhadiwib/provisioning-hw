-- test data
INSERT INTO device (mac_address, model, username, password, override_fragment) VALUES ('ab-cd-ef-ad-be-cf', 'DESK', 'john', 'doe', null);

INSERT INTO device (mac_address, model, username, password, override_fragment) VALUES ('c1-a2-e3-f4-d5-b6', 'CONFERENCE', 'sofia', 'red', null);

INSERT INTO device (mac_address, model, username, password, override_fragment) VALUES ('a1-b2-c3-d4-e5-f6', 'DESK', 'walter', 'white', STRINGDECODE('domain=sip.anotherdomain.com\nport=5161\ntimeout=10'));

INSERT INTO device (mac_address, model, username, password, override_fragment) VALUES ('1b-2c-3a-4a-5f-6f', 'CONFERENCE', 'eric', 'blue', '{"domain":"sip.anotherdomain.com","port":"5161","timeout":10}');