alter table content alter created_at set default now() at time zone 'UTC';
alter table content alter updated_at set default now() at time zone 'UTC';
