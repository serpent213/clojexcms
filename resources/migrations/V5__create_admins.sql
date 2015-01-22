-- admin accounts
create table admins (
  uid             varchar(250) primary key,
  created_at      timestamp not null default now(), -- in UTC
  updated_at      timestamp not null default now(), -- in UTC
  password        varchar(100) not null default '*',
  fullname        varchar(250) not null default ''
);

insert into admins (uid, password, fullname) values (
  'admin@example.com',
  '$2a$11$P7mGHEvIyyKcv3B0BjpCqOePRJre69.z17TRQKvC.MtMvG88WtRe6', -- "admin"
  'Administrator'
);
