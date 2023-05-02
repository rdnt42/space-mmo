create table if not exists item_types
(
    item_type_id integer primary key,
    name         text not null,
    name_ru      text not null,
    dsc_ru       text not null
)