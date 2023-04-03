create table if not exists characters
(
    character_name text primary key,
    account_name   text     not null,
    experience     integer  not null,
    x              integer  not null,
    y              integer  not null,
    angle          smallint not null,
    unique (character_name, account_name)
);