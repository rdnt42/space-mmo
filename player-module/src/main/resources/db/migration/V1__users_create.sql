create table if not exists users
(
    user_id           bigserial primary key,
    username          text      not null unique,
    password          text      not null,
    email             text      not null,
    is_activate       boolean   not null default false,
    created_at        timestamp not null,
    updated_at        timestamp not null,
    last_logged_in_at timestamp not null
);

create index on users (username, password);