create table if not exists collectivity
(
    id             varchar primary key,
    number         int          unique not null,
    name           varchar(100) unique not null,
    location       varchar(100),
    speciality varchar(100) not null
);

alter table collectivity add column updated_at timestamp;

create type gender as enum ('MALE', 'FEMALE');
create type occupation as enum ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'SENIOR', 'JUNIOR');

create table if not exists member
(
    id           varchar primary key,
    first_name   varchar(100) not null,
    last_name    varchar(100) not null,
    birthdate    date         not null,
    gender       gender       not null default 'MALE',
    address      varchar      not null,
    profession   varchar(100) not null,
    phone_number varchar(30)  not null,
    email        varchar(40)  not null,
    constraint first_name_last_name_unique unique (first_name, last_name)
);

create table member_collectivity
(
    id              varchar primary key,
    collectivity_id varchar references collectivity (id),
    member_id       varchar references member (id),
    occupation      occupation not null default 'JUNIOR',
    constraint collectivity_id_member_id unique (collectivity_id, member_id)
);

create table member_referrals
(
    id              serial primary key,
    collectivity_id varchar references collectivity (id),
    member_col_id   varchar references member_collectivity (id),
    referrer_col_id varchar references member_collectivity (id) check ( member_col_id != referrer_col_id ),
    member_relation varchar(50)
);

create type status AS ENUM ('ACTIVE', 'INACTIVE');
create type frequency AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

create table if not exists membership_fee
(
    id              varchar primary key,
    label           varchar not null,
    status          status  not null                    default 'INACTIVE',
    frequency       frequency                           default 'MONTHLY',
    eligible_from     date    not null                    default current_date,
    amount          numeric(10, 2) check ( amount >= 0 ) default 0,
    collectivity_id varchar references collectivity (id)
);

create type account_type as enum (
    'CASH',
    'AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY',
    'BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCESS_BANK', 'BAOBAB', 'SIPEM'
);

create table if not exists account_collectivity
(
    id              varchar primary key,
    collectivity_id varchar        not null references collectivity (id),
    account_type    account_type default 'CASH',
    amount          numeric(10, 2) not null check ( amount >= 0 ) default 0,
    titular         varchar check ( (account_type = 'CASH' and titular is null) or (account_type != 'CASH' and titular is not null) ),
    account_number  varchar check ( (account_type = 'CASH' and account_number is null) or (account_type != 'CASH' and account_number is not null) )
);

create type payment_mode as enum ('CASH', 'BANK_TRANSFER', 'MOBILE_MONEY');

create table if not exists payment
(
    id              serial primary key,
    collectivity_id varchar references collectivity (id)         not null,
    member_col_id   varchar references member_collectivity (id)  not null,
    amount          numeric(10, 2)                               not null check ( amount > 0 ) default 0,
    account_col_id  varchar references account_collectivity (id) not null,
    payment_mode    payment_mode                                                               default 'CASH' not null,
    created_at      timestamp without time zone                  not null                      default current_timestamp
);

create table if not exists transaction
(
    id              serial primary key,
    collectivity_id varchar references collectivity (id)         not null,
    member_col_id   varchar references member_collectivity (id)  not null,
    amount          numeric(10, 2)                               not null check ( amount > 0 ) default 0,
    account_col_id  varchar references account_collectivity (id) not null,
    payment_mode    payment_mode                                                               default 'CASH' not null,
    created_at      timestamp without time zone                  not null                      default current_timestamp
);