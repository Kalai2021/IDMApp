CREATE TABLE IF NOT EXISTS org_members (
    id UUID PRIMARY KEY,
    org_id UUID NOT NULL,
    entity_id UUID NOT NULL,
    type VARCHAR(10) NOT NULL,
    UNIQUE(org_id, entity_id)
); 