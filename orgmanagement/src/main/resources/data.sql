INSERT INTO orgs (id, name, description, created_at, updated_at)
VALUES 
    ('a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1', 'Test Org', 'Test organization', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('b2b2b2b2-b2b2-b2b2-b2b2-b2b2b2b2b2b2', 'Demo Org', 'Demo organization', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING; 