create table if not exists characters
(
    character_name text primary key,
    account_name   text             not null,
    experience     integer          not null,
    x              double precision not null,
    y              double precision not null,
    angle          smallint         not null,
    unique (character_name, account_name)
);