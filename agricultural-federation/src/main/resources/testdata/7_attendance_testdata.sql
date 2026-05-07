-- ============================================================
-- PRÉSENCES — act-col1-ag (Assemblée générale col-1)
-- Membres col-1 : C1-M1..C1-M8
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col1-ag-C1M1', 'act-col1-ag', 'C1-M1', 'ATTENDED'),
    ('att-col1-ag-C1M2', 'act-col1-ag', 'C1-M2', 'ATTENDED'),
    ('att-col1-ag-C1M3', 'act-col1-ag', 'C1-M3', 'ATTENDED'),
    ('att-col1-ag-C1M4', 'act-col1-ag', 'C1-M4', 'ATTENDED'),
    ('att-col1-ag-C1M5', 'act-col1-ag', 'C1-M5', 'ATTENDED'),
    ('att-col1-ag-C1M6', 'act-col1-ag', 'C1-M6', 'MISSING'),
    ('att-col1-ag-C1M7', 'act-col1-ag', 'C1-M7', 'ATTENDED'),
    ('att-col1-ag-C1M8', 'act-col1-ag', 'C1-M8', 'MISSING');

-- ============================================================
-- PRÉSENCES — act-col1-ex (Formation riziculture col-1)
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col1-ex-C1M1', 'act-col1-ex', 'C1-M1', 'ATTENDED'),
    ('att-col1-ex-C1M2', 'act-col1-ex', 'C1-M2', 'ATTENDED'),
    ('att-col1-ex-C1M3', 'act-col1-ex', 'C1-M3', 'MISSING'),
    ('att-col1-ex-C1M4', 'act-col1-ex', 'C1-M4', 'ATTENDED'),
    ('att-col1-ex-C1M5', 'act-col1-ex', 'C1-M5', 'ATTENDED'),
    ('att-col1-ex-C1M6', 'act-col1-ex', 'C1-M6', 'ATTENDED'),
    ('att-col1-ex-C1M7', 'act-col1-ex', 'C1-M7', 'MISSING'),
    ('att-col1-ex-C1M8', 'act-col1-ex', 'C1-M8', 'ATTENDED');

-- ============================================================
-- PRÉSENCES — act-col2-ag (Assemblée générale col-2)
-- Membres col-2 : C1-M1..C1-M8 (mêmes membres que col-1)
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col2-ag-C1M1', 'act-col2-ag', 'C1-M1', 'ATTENDED'),
    ('att-col2-ag-C1M2', 'act-col2-ag', 'C1-M2', 'MISSING'),
    ('att-col2-ag-C1M3', 'act-col2-ag', 'C1-M3', 'ATTENDED'),
    ('att-col2-ag-C1M4', 'act-col2-ag', 'C1-M4', 'ATTENDED'),
    ('att-col2-ag-C1M5', 'act-col2-ag', 'C1-M5', 'ATTENDED'),
    ('att-col2-ag-C1M6', 'act-col2-ag', 'C1-M6', 'ATTENDED'),
    ('att-col2-ag-C1M7', 'act-col2-ag', 'C1-M7', 'MISSING'),
    ('att-col2-ag-C1M8', 'act-col2-ag', 'C1-M8', 'ATTENDED');

-- ============================================================
-- PRÉSENCES — act-col2-ex (Visite étangs col-2)
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col2-ex-C1M1', 'act-col2-ex', 'C1-M1', 'ATTENDED'),
    ('att-col2-ex-C1M2', 'act-col2-ex', 'C1-M2', 'ATTENDED'),
    ('att-col2-ex-C1M3', 'act-col2-ex', 'C1-M3', 'ATTENDED'),
    ('att-col2-ex-C1M4', 'act-col2-ex', 'C1-M4', 'MISSING'),
    ('att-col2-ex-C1M5', 'act-col2-ex', 'C1-M5', 'ATTENDED'),
    ('att-col2-ex-C1M6', 'act-col2-ex', 'C1-M6', 'MISSING'),
    ('att-col2-ex-C1M7', 'act-col2-ex', 'C1-M7', 'ATTENDED'),
    ('att-col2-ex-C1M8', 'act-col2-ex', 'C1-M8', 'ATTENDED');

-- ============================================================
-- PRÉSENCES — act-col3-ag (Assemblée générale col-3)
-- Membres col-3 : C3-M1..C3-M8
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col3-ag-C3M1', 'act-col3-ag', 'C3-M1', 'ATTENDED'),
    ('att-col3-ag-C3M2', 'act-col3-ag', 'C3-M2', 'ATTENDED'),
    ('att-col3-ag-C3M3', 'act-col3-ag', 'C3-M3', 'ATTENDED'),
    ('att-col3-ag-C3M4', 'act-col3-ag', 'C3-M4', 'MISSING'),
    ('att-col3-ag-C3M5', 'act-col3-ag', 'C3-M5', 'ATTENDED'),
    ('att-col3-ag-C3M6', 'act-col3-ag', 'C3-M6', 'ATTENDED'),
    ('att-col3-ag-C3M7', 'act-col3-ag', 'C3-M7', 'MISSING'),
    ('att-col3-ag-C3M8', 'act-col3-ag', 'C3-M8', 'ATTENDED');

-- ============================================================
-- PRÉSENCES — act-col3-ex (Atelier miel col-3)
-- + C1-M1 venu d'une autre collectivité (présence externe)
-- ============================================================
INSERT INTO "attendance" (id, activity_id, member_id, attendance_status)
VALUES
    ('att-col3-ex-C3M1', 'act-col3-ex', 'C3-M1', 'ATTENDED'),
    ('att-col3-ex-C3M2', 'act-col3-ex', 'C3-M2', 'ATTENDED'),
    ('att-col3-ex-C3M3', 'act-col3-ex', 'C3-M3', 'MISSING'),
    ('att-col3-ex-C3M4', 'act-col3-ex', 'C3-M4', 'ATTENDED'),
    ('att-col3-ex-C3M5', 'act-col3-ex', 'C3-M5', 'ATTENDED'),
    ('att-col3-ex-C3M6', 'act-col3-ex', 'C3-M6', 'MISSING'),
    ('att-col3-ex-C3M7', 'act-col3-ex', 'C3-M7', 'ATTENDED'),
    ('att-col3-ex-C3M8', 'act-col3-ex', 'C3-M8', 'ATTENDED'),
    -- C1-M1 venu depuis col-1 (membre externe, ATTENDED uniquement)
    ('att-col3-ex-C1M1', 'act-col3-ex', 'C1-M1', 'ATTENDED');
