create table if not exists characters
(
    characterName text primary key,
    accountName   text     not null,
    experience    integer  not null,
    x             integer  not null,
    y             integer  not null,
    angle         smallint not null,
    unique (accountName, characterName)
);