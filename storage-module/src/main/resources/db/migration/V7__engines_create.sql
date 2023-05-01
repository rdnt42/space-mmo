create table if not exists engines
(
    equipment_id bigint primary key references equipments,
    speed        smallint not null,
    cost         integer  not null
)