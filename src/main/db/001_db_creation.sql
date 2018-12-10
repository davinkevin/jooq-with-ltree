
CREATE EXTENSION IF NOT EXISTS "ltree";

CREATE TABLE IF NOT EXISTS tree(
  id SERIAL PRIMARY KEY,
  letter CHAR,
  path LTREE);
create index tree_path_idx on tree using gist (path);