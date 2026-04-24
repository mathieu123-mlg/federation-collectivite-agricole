create table if not exists collectivity
(
    id             varchar primary key,
    number         int          not null,
    name           varchar(100) not null,
    locality       varchar(100),
    specialisation varchar(100) not null
);

create type gender as enum ('M', 'F');
create type occupation as enum ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'CONFIRMED', 'JUNIOR');

create table if not exists member
(
    id           varchar primary key,
    first_name   varchar(100) not null,
    last_name    varchar(100) not null,
    birthdate    date         not null,
    gender       gender       not null default 'M',
    address      varchar      not null,
    profession   varchar(100) not null,
    phone_number varchar(30)  not null,
    email        varchar(40)  not null
);

create table member_collectivity
(
    id              varchar primary key,
    collectivity_id varchar references collectivity (id),
    member_id       varchar references member (id),
    occupation      occupation not null default 'JUNIOR'
);

create table member_referrals
(
    id              serial primary key,
    collectivity_id varchar references collectivity (id),
    member_col_id   varchar references member_collectivity (id),
    referrer_col_id varchar references member_collectivity (id) check ( member_col_id != referrer_col_id )
);

create type status AS ENUM ('ACTIVE', 'INACTIVE');
create type frequency AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

create table if not exists membership_fee
(
    id              varchar primary key,
    label           varchar not null,
    status          status  not null                    default 'INACTIVE',
    frequency       frequency                           default 'MONTHLY',
    eligibility     date    not null                    default current_date,
    amount          numeric(10, 2) check ( amount > 0 ) default 0,
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