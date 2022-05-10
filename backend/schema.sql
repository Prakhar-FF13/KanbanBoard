DROP TABLE IF EXISTS users;

CREATE TABLE users (
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS workspaces;

CREATE TABLE workspaces (
  wid INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(255) UNIQUE NOT NULL,
  members INTEGER DEFAULT 1,
  createdBy VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS workspaceusers;

CREATE TABLE workspaceusers (
  wid INTEGER,
  username VARCHAR(255),
  FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE,
  FOREIGN KEY (wid) REFERENCES workspaces (wid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS workspaceTasks;

CREATE TABLE workspaceTasks (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  wid INTEGER,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  assignee VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  priority VARCHAR(255) NOT NULL,
  FOREIGN KEY (wid) REFERENCES workspaces (wid) ON DELETE CASCADE
);
DROP TABLE IF EXISTS workspacecollaborators;

CREATE TABLE workspacecollaborators(

    wid INTEGER,
    leader varchar(255),
    username_collaborators varchar(255)
);