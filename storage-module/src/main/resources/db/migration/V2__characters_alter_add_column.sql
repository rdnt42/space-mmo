alter table characters
    add column if not exists is_online boolean not null default false;
