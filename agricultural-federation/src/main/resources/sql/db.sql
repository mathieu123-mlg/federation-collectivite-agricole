drop database agricultural_federation;
create database agricultural_federation;
create user agricultural_federation_manager with password '123456';
grant connect on database agricultural_federation to agricultural_federation_manager;

grant create, usage on schema public to agricultural_federation_manager;
alter default privileges in schema public
    grant select, update, insert, delete on tables to agricultural_federation_manager;
alter default privileges in schema public
    grant usage, select, update on sequences to agricultural_federation_manager;
