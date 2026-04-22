\c agricultural_federation
create type gender_enum AS ENUM ('MALE', 'FEMALE');
create type role_enum AS ENUM ('PRESIDENT','VICE_PRESIDENT','TREASURER','SECRETARY','SENIOR','JUNIOR');
create type payment_type AS ENUM ('REGISTRATION', 'COTISATION');
create type payment_method AS ENUM ('CASH', 'BANK_TRANSFER', 'MOBILE_MONEY');
create type account_type_enum AS ENUM ('CASH', 'BANK', 'MOBILE_MONEY');
create type activity_type_enum AS ENUM ('GENERAL_MEETING', 'TRAINING', 'EXCEPTIONAL');

create table if not exists collectivity
(
    id                  serial PRIMARY KEY,
    location            VARCHAR(100) NOT NULL,
    specialty           VARCHAR(100) NOT NULL,
    creation_datetime   DATE         NOT NULL,
    federation_approval BOOLEAN      NOT NULL
);

create table collectivity_identifier
(
    numero int unique          not null,
    name   varchar(100) unique not null
);

create table if not exists member
(
    id            serial PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    birth_date    DATE         NOT NULL,
    gender        gender_enum  NOT NULL,
    address       TEXT,
    profession    VARCHAR(100),
    phone_number  VARCHAR(20),
    email         VARCHAR(100) UNIQUE,
    adhesion_date DATE         NOT NULL
);

-- =========================
-- TABLE: member_collectivity
-- (historique des affiliations)
-- =========================

create table if not exists member_collectivity
(
    id              serial PRIMARY KEY,
    member_id       serial NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    collectivity_id serial NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    join_date       DATE   NOT NULL,
    leave_date      DATE
);

-- =========================
-- TABLE: mandate
-- =========================

create table if not exists mandate
(
    id              serial PRIMARY KEY,
    collectivity_id serial NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    start_date      DATE   NOT NULL,
    end_date        DATE   NOT NULL
);

-- =========================
-- TABLE: member_role
-- =========================
create table if not exists member_role
(
    id         serial PRIMARY KEY,
    member_id  serial    NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    mandate_id serial    NOT NULL REFERENCES mandate (id) ON DELETE CASCADE,
    role       role_enum NOT NULL,

    -- Un seul rôle unique (président etc.) par mandat
    CONSTRAINT unique_role_per_mandate UNIQUE (mandate_id, role, member_id)
);

-- =========================
-- TABLE: referee (parrainage)
-- =========================

create table if not exists referee
(
    id              serial PRIMARY KEY,
    candidate_id    serial       NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    referee_id      serial       NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    collectivity_id serial REFERENCES collectivity (id),
    relationship    VARCHAR(100) NOT NULL
);

-- =========================
-- TABLE: payment
-- =========================

create table if not exists payment
(
    id             serial PRIMARY KEY,
    member_id      serial         NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    amount         DECIMAL(12, 2) NOT NULL,
    type           payment_type   NOT NULL,
    payment_method payment_method NOT NULL,
    payment_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: collectivity_account
-- =========================

create table if not exists collectivity_account
(
    id              serial PRIMARY KEY,
    collectivity_id serial            NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    type            account_type_enum NOT NULL,
    balance         DECIMAL(14, 2) DEFAULT 0,
    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: activity
-- =========================

create table if not exists activity
(
    id              serial PRIMARY KEY,
    collectivity_id serial             NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    type            activity_type_enum NOT NULL,
    activity_date   DATE               NOT NULL,
    mandatory       BOOLEAN            NOT NULL
);

-- =========================
-- TABLE: attendance
-- =========================

create table if not exists attendance
(
    id          serial PRIMARY KEY,
    activity_id serial  NOT NULL REFERENCES activity (id) ON DELETE CASCADE,
    member_id   serial  NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    present     BOOLEAN NOT NULL,
    justified   BOOLEAN DEFAULT FALSE,

    UNIQUE (activity_id, member_id)
);

-- =========================
-- INDEX (performance)
-- =========================

CREATE INDEX idx_member_collectivity_member ON member_collectivity (member_id);
CREATE INDEX idx_member_collectivity_collectivity ON member_collectivity (collectivity_id);
CREATE INDEX idx_payment_member ON payment (member_id);
CREATE INDEX idx_attendance_activity ON attendance (activity_id);
R
    (100)
    NOT NULL,
    specialty           VARCHAR(100)        NOT NULL,
    creation_datetime       DATE                NOT NULL,
    federation_approval BOOLEAN             NOT NULL
);

create table if not exists member
(
    id            serial PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    birth_date    DATE         NOT NULL,
    gender        gender_enum  NOT NULL,
    address       TEXT,
    profession    VARCHAR(100),
    phone_number  VARCHAR(20),
    email         VARCHAR(100) UNIQUE,
    adhesion_date DATE         NOT NULL
);

-- =========================
-- TABLE: member_collectivity
-- (historique des affiliations)
-- =========================

create table if not exists member_collectivity
(
    id              serial PRIMARY KEY,
    member_id       serial NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    collectivity_id serial NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    join_date       DATE   NOT NULL,
    leave_date      DATE
);

-- =========================
-- TABLE: mandate
-- =========================

create table if not exists mandate
(
    id              serial PRIMARY KEY,
    collectivity_id serial NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    start_date      DATE   NOT NULL,
    end_date        DATE   NOT NULL
);

-- =========================
-- TABLE: member_role
-- =========================
create table if not exists member_role
(
    id         serial PRIMARY KEY,
    member_id  serial    NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    mandate_id serial    NOT NULL REFERENCES mandate (id) ON DELETE CASCADE,
    role       role_enum NOT NULL,

    -- Un seul rôle unique (président etc.) par mandat
    CONSTRAINT unique_role_per_mandate UNIQUE (mandate_id, role, member_id)
);

-- =========================
-- TABLE: referee (parrainage)
-- =========================

create table if not exists referee
(
    id              serial PRIMARY KEY,
    candidate_id    serial       NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    referee_id      serial       NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    collectivity_id serial REFERENCES collectivity (id),
    relationship    VARCHAR(100) NOT NULL
);

-- =========================
-- TABLE: payment
-- =========================

create table if not exists payment
(
    id             serial PRIMARY KEY,
    member_id      serial         NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    amount         DECIMAL(12, 2) NOT NULL,
    type           payment_type   NOT NULL,
    payment_method payment_method NOT NULL,
    payment_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: collectivity_account
-- =========================

create table if not exists collectivity_account
(
    id              serial PRIMARY KEY,
    collectivity_id serial            NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    type            account_type_enum NOT NULL,
    balance         DECIMAL(14, 2) DEFAULT 0,
    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: activity
-- =========================

create table if not exists activity
(
    id              serial PRIMARY KEY,
    collectivity_id serial             NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    type            activity_type_enum NOT NULL,
    activity_date   DATE               NOT NULL,
    mandatory       BOOLEAN            NOT NULL
);

-- =========================
-- TABLE: attendance
-- =========================

create table if not exists attendance
(
    id          serial PRIMARY KEY,
    activity_id serial  NOT NULL REFERENCES activity (id) ON DELETE CASCADE,
    member_id   serial  NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    present     BOOLEAN NOT NULL,
    justified   BOOLEAN DEFAULT FALSE,

    UNIQUE (activity_id, member_id)
);

-- =========================
-- INDEX (performance)
-- =========================

CREATE INDEX idx_member_collectivity_member ON member_collectivity (member_id);
CREATE INDEX idx_member_collectivity_collectivity ON member_collectivity (collectivity_id);
CREATE INDEX idx_payment_member ON payment (member_id);
CREATE INDEX idx_attendance_activity ON attendance (activity_id);
