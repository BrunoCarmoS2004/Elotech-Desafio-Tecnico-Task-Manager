CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   name VARCHAR(100) NOT NULL,
   email VARCHAR(150) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   role VARCHAR(20) NOT NULL,
   entity_status VARCHAR(30) NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      name VARCHAR(100) UNIQUE NOT NULL,
      description TEXT,
      creator_id UUID NOT NULL,
      entity_status VARCHAR(30) NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_projects_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE project_members (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     project_id UUID NOT NULL,
     user_id UUID NOT NULL,
     user_project_status VARCHAR(30) NOT NULL,
     PRIMARY KEY (project_id, user_id),
     CONSTRAINT fk_project_members_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
     CONSTRAINT fk_project_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tasks (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   title VARCHAR(150) NOT NULL,
   description TEXT,
   status VARCHAR(30) NOT NULL,
   priority VARCHAR(30) NOT NULL,
   deadline TIMESTAMP NOT NULL,
   responsible_id UUID,
   project_id UUID NOT NULL,
   entity_status VARCHAR(30) NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_tasks_responsible FOREIGN KEY (responsible_id) REFERENCES users(id) ON DELETE SET NULL,
   CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE tasks_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    task_id UUID NOT NULL,
    user_id UUID NOT NULL,
    altered_field VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    altered_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    entity_status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_logs_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);