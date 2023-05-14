create table if not exists hull_types
(
    hull_type_id integer primary key,
    cost         integer,
    hp           smallint not null,
    evasion      smallint not null,
    armor        smallint not null,
    name_ru      text,
    dsc_ru       text
)