create table if not exists "collectivity_member"
(
    id              varchar primary key,
    member_id       varchar references "member" (id),
    collectivity_id varchar references "collectivity" (id)
);

alter table collectivity_member
    add column adhesion_date timestamp without time zone default '2026-01-01';

alter  table collectivity_member rename column adhesion_date to join_date;