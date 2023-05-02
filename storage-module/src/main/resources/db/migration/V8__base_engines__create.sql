create table if not exists base_engines
(
    item_type_id int primary key references item_types,
    base_cost    integer,
    base_speed   smallint not null,
    base_jump    smallint not null
)