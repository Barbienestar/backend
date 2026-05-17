INSERT INTO States (id, name) VALUES (1, 'Test State');

INSERT INTO Cities (id, name, id_state) VALUES (1, 'Test City', 1);

INSERT INTO Roles (id, name) VALUES (1, 'User');
INSERT INTO Roles (id, name) VALUES (2, 'citizen');
INSERT INTO Roles (id, name) VALUES (3, 'admin');

INSERT INTO Suburbs (id, name, zip_code, id_city) VALUES (1, 'Test Suburb', '12345', 1);

INSERT INTO Users (id, name, last_name_1, last_name_2, age, email, provider_uuid, active, id_role, id_suburb, created_at, updated_at)
VALUES (1, 'John', 'Pork', NULL, 30, 'john@test.com', 'test-uuid-123', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Users (id, name, last_name_1, email, provider_uuid, active, id_role, id_suburb, created_at, updated_at)
VALUES (2, 'Citizen', 'User', 'citizen@test.com', 'citizen-token', true, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Users (id, name, last_name_1, email, provider_uuid, active, id_role, id_suburb, created_at, updated_at)
VALUES (3, 'Admin', 'User', 'admin@test.com', 'admin-token', true, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Streets (id, name, id_suburb) VALUES (1, 'Test Street', 1);

INSERT INTO Statuses (id, name) VALUES (1, 'Pendiente');
INSERT INTO Statuses (id, name) VALUES (2, 'reviewing');

INSERT INTO Medicines (id, generic_name, dosage_form, strength, presentation, created_at, updated_at)
VALUES (1, 'Paracetamol', 'Tablet', '500mg', 'Box', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Hospitals (id, name, maps_url, id_street, created_at, updated_at)
VALUES (1, 'Hospital name', 'https://maps.test.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
