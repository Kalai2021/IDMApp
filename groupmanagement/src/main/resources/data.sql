INSERT INTO groups (id, name, display_name, description, created_at, updated_at)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'admin', 'Administrators', 'System administrators group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('22222222-2222-2222-2222-222222222222', 'users', 'Regular Users', 'Regular users group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING; 