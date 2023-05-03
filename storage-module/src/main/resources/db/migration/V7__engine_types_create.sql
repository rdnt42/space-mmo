create table if not exists engine_types
(
    engine_type_id integer primary key,
    cost           integer,
    speed          smallint not null,
    jump           smallint not null,
    name_ru        text,
    dsc_ru         text
)