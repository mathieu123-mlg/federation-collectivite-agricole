\c postgres

create database agricultural_federation_db;
create user user_manager with password '123456';
grant connect on database agricultural_federation_db to user_manager;

\c postgres agricultural_federation_db

grant create, usage on schema public to user_manager;
alter default privileges in schema public
    grant select, update, insert, delete on tables to user_manager;
alter default privileges in schema public
    grant usage, select, update on sequences to user_manager;
