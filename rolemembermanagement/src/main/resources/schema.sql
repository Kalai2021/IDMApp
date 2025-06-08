CREATE TABLE IF NOT EXISTS role_members (
    id UUID PRIMARY KEY,
    role_id UUID NOT NULL,
    entity_id UUID NOT NULL,
    type VARCHAR(10) NOT NULL,
    UNIQUE(role_id, entity_id)
); 