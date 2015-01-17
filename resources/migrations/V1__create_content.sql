-- content snippets in markdown
create table content (
  id              varchar(32) primary key,
  created_at      timestamp not null default now(), -- in UTC
  updated_at      timestamp not null default now(), -- in UTC
  description     varchar(250) not null default '',
  body            text not null default ''
);
