create table if not exists engine_types
(
    engine_type_id smallint primary key,
    name           text     not null,
    base_cost      integer  not null,
    base_speed     smallint not null,
    base_jump      smallint not null,
    name_ru        text     not null,
    dsc_ru         text     not null
)