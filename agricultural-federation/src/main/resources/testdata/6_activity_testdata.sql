-- ============================================================
-- ACTIVITÉS — COLLECTIVITÉ 1  (col-1 · Mpanorina)
-- ============================================================
INSERT INTO "activity" (id, collectivity_id, label, activity_type, week_ordinal, day_of_week, executive_date, occupations_concerned)
VALUES
    ('act-col1-ag', 'col-1', 'Assemblée générale mensuelle', 'MEETING', 2, 'SU', null, 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR,JUNIOR'),
    ('act-col1-fo', 'col-1', 'Formation obligatoire juniors', 'TRAINING', 4, 'SA', null, 'JUNIOR'),
    ('act-col1-ex', 'col-1', 'Formation sur la riziculture moderne', 'TRAINING', null, null, '2026-05-10', 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR,JUNIOR');

-- ============================================================
-- ACTIVITÉS — COLLECTIVITÉ 2  (col-2 · Dobo voalohany)
-- ============================================================
INSERT INTO "activity" (id, collectivity_id, label, activity_type, week_ordinal, day_of_week, executive_date, occupations_concerned)
VALUES
    ('act-col2-ag', 'col-2', 'Assemblée générale mensuelle', 'MEETING', 2, 'SU', null, 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR,JUNIOR'),
    ('act-col2-fo', 'col-2', 'Formation obligatoire juniors', 'TRAINING', 4, 'SA', null, 'JUNIOR'),
    ('act-col2-ex', 'col-2', 'Visite des étangs piscicoles', 'OTHER', null, null, '2026-05-03', 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR');

-- ============================================================
-- ACTIVITÉS — COLLECTIVITÉ 3  (col-3 · Tantely mamy)
-- ============================================================
INSERT INTO "activity" (id, collectivity_id, label, activity_type, week_ordinal, day_of_week, executive_date, occupations_concerned)
VALUES
    ('act-col3-ag', 'col-3', 'Assemblée générale mensuelle', 'MEETING', 2, 'SU', null, 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR,JUNIOR'),
    ('act-col3-fo', 'col-3', 'Formation obligatoire juniors', 'TRAINING', 4, 'SA', null, 'JUNIOR'),
    ('act-col3-ex', 'col-3', 'Atelier sur la production de miel', 'TRAINING', null, null, '2026-05-06', 'PRESIDENT,VICE_PRESIDENT,SECRETARY,TREASURER,SENIOR,JUNIOR');
