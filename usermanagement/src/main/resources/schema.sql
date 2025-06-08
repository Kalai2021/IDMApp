CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

-- Groups table
CREATE TABLE groups (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    display_name VARCHAR(50) NOT NULL ,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

-- Roles table
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(50) NOT NULL ,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

-- User-Groups join table
CREATE TABLE user_groups (
    user_id UUID NOT NULL,
    group_id UUID NOT NULL,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
);  


-- User-Roles join table
CREATE TABLE role_members (
    role_id UUID NOT NULL,
    member_id UUID NOT NULL,
    member_type VARCHAR(10) NOT NULL CHECK (member_type IN ('USER', 'GROUP')),
    PRIMARY KEY (role_id, member_id, member_type),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
    -- Note: No direct FK to users/groups because member_id is polymorphic
);

CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- User-Organizations join table
CREATE TABLE org_members (
    entity_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    entity_type VARCHAR(10) NOT NULL CHECK (entity_type IN ('USER', 'GROUP', 'ROLE')),
    PRIMARY KEY (organization_id, entity_id, entity_type),
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

/*
-- Group-Organizations join table
CREATE TABLE group_organizations (
    group_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    PRIMARY KEY (group_id, organization_id),
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE    
);


-- User-Role-Organizations join table

CREATE TABLE user_role_organizations (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id, organization_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);
-- Group-Role-Organizations join table  
CREATE TABLE group_role_organizations (
    group_id UUID NOT NULL,
    role_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    PRIMARY KEY (group_id, role_id, organization_id),
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);
*/


