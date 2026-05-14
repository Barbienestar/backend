INSERT INTO Role (id, name) VALUES (1, 'User');

INSERT INTO Suburb (id, name, zip_code) VALUES (1, 'Test Suburb', '12345');

INSERT INTO Users (id, name, last_name_1, last_name_2, age, email, provider_uuid, active, id_role, id_suburb, created_at, updated_at)
VALUES (1, 'John', 'Pork', NULL, 30, 'john@test.com', 'test-uuid-123', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Street (id, name) VALUES (1, 'Test Street');

INSERT INTO Status (id, name) VALUES (1, 'Pendiente');

INSERT INTO Medicine (id, generic_name, dosage_form, strength, presentation, created_at, updated_at)
VALUES (1, 'Paracetamol', 'Tablet', '500mg', 'Box', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Hospital (id, name, maps_url, id_street, created_at, updated_at)
VALUES (1, 'Hospital name', 'https://maps.test.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
