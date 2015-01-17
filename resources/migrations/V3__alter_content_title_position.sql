alter table content add title varchar(250);
alter table content add position integer;

update content set title = initcap(id);
update content set position = 10 where id = 'welcome';
update content set position = 20 where id = 'about';

alter table content alter title set not null;
alter table content alter position set not null;
