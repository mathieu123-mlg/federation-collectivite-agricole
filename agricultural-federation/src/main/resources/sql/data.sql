-- =========================
-- 1. INSERTION DES COLLECTIVITÉS (coopératives agricoles)
-- =========================
INSERT INTO collectivity (name, location, specialty, creation_date, federation_approval)
VALUES ('Coopérative Agricole du Sud', 'Dakar, Sénégal', 'Maraîchage', '2015-03-15', TRUE),
       ('Union des Producteurs de Riz', 'Saint-Louis, Sénégal', 'Riziculture', '2018-06-20', TRUE),
       ('Coopérative Laitière de Kaolack', 'Kaolack, Sénégal', 'Élevage laitier', '2010-11-10', TRUE),
       ('Groupement des Maraîchers', 'Thiès, Sénégal', 'Maraîchage bio', '2020-01-05', FALSE),
       ('Coopérative Fruitière du Nord', 'Louga, Sénégal', 'Arboriculture', '2019-09-25', TRUE),
       ('Association des Éleveurs de Ziguinchor', 'Ziguinchor, Sénégal', 'Élevage', '2012-04-18', TRUE);

-- =========================
-- 2. INSERTION DES MEMBRES
-- =========================
INSERT INTO member (first_name, last_name, birth_date, gender, address, profession, phone_number, email, adhesion_date)
VALUES ('Amadou', 'Diop', '1985-03-15', 'MALE', 'Dakar, Parcelles Assainies', 'Agriculteur', '771234567',
        'amadou.diop@email.com', '2020-01-10'),
       ('Fatou', 'Sow', '1990-07-22', 'FEMALE', 'Saint-Louis, Nord', 'Productrice', '772345678', 'fatou.sow@email.com',
        '2020-01-15'),
       ('Mamadou', 'Fall', '1982-11-05', 'MALE', 'Kaolack, Médina', 'Éleveur', '773456789', 'mamadou.fall@email.com',
        '2019-03-20'),
       ('Aissatou', 'Ndiaye', '1995-09-30', 'FEMALE', 'Thiès, Escale', 'Maraîchère', '774567890',
        'aissatou.ndiaye@email.com', '2021-06-01'),
       ('Ousmane', 'Touré', '1988-12-12', 'MALE', 'Louga, Centre', 'Arboriculteur', '775678901',
        'ousmane.toure@email.com', '2019-11-15'),
       ('Mariama', 'Ba', '1992-04-18', 'FEMALE', 'Ziguinchor, Boudody', 'Éleveuse', '776789012', 'mariama.ba@email.com',
        '2020-08-20'),
       ('Ibrahima', 'Sarr', '1987-06-25', 'MALE', 'Dakar, Grand Yoff', 'Technicien agricole', '777890123',
        'ibrahima.sarr@email.com', '2021-01-05'),
       ('Ndeye', 'Gueye', '1993-10-14', 'FEMALE', 'Saint-Louis, Sor', 'Productrice', '778901234',
        'ndeye.gueye@email.com', '2021-02-10'),
       ('Cheikh', 'Diagne', '1984-08-08', 'MALE', 'Kaolack, Léona', 'Éleveur', '779012345', 'cheikh.diagne@email.com',
        '2018-07-15'),
       ('Aminata', 'Ly', '1991-12-01', 'FEMALE', 'Thiès, Khombole', 'Maraîchère', '770123456', 'aminata.ly@email.com',
        '2022-01-20'),
       ('Moussa', 'Diallo', '1994-03-25', 'MALE', 'Tambacounda, Centre', 'Agriculteur', '781234567',
        'moussa.diallo@email.com', '2023-02-01'),
       ('Awa', 'Seck', '1989-11-12', 'FEMALE', 'Fatick, Dioffior', 'Productrice', '782345678', 'awa.seck@email.com',
        '2023-03-15');

-- =========================
-- 3. INSERTION DES AFFILIATIONS (member_collectivity)
-- Note: Les IDs sont générés automatiquement (1,2,3...)
-- =========================
INSERT INTO member_collectivity (member_id, collectivity_id, join_date, leave_date)
VALUES (1, 1, '2020-01-10', NULL),
       (2, 2, '2020-01-15', NULL),
       (3, 3, '2019-03-20', NULL),
       (4, 4, '2021-06-01', NULL),
       (5, 5, '2019-11-15', NULL),
       (6, 6, '2020-08-20', NULL),
       (7, 1, '2021-01-05', NULL),
       (8, 2, '2021-02-10', NULL),
       (9, 3, '2018-07-15', NULL),
       (10, 4, '2022-01-20', NULL),
       (1, 4, '2022-02-01', NULL),  -- Amadou rejoint une autre coopérative
       (2, 1, '2023-01-10', NULL),  -- Fatou rejoint le sud aussi
       (11, 5, '2023-02-01', NULL), -- Moussa à la coopérative fruitière
       (12, 2, '2023-03-15', NULL);
-- Awa à la riziculture

-- =========================
-- 4. INSERTION DES MANDATS
-- =========================
INSERT INTO mandate (collectivity_id, start_date, end_date)
VALUES (1, '2023-01-01', '2025-12-31'),
       (2, '2023-01-01', '2025-12-31'),
       (3, '2022-01-01', '2024-12-31'),
       (4, '2023-06-01', '2025-05-31'),
       (5, '2023-01-01', '2025-12-31'),
       (6, '2022-07-01', '2024-06-30');

-- =========================
-- 5. INSERTION DES RÔLES DES MEMBRES
-- =========================
INSERT INTO member_role (member_id, mandate_id, role)
VALUES
-- Mandat 1 (Collectivité du Sud)
(1, 1, 'PRESIDENT'),
(7, 1, 'SECRETARY'),
(2, 1, 'TREASURER'),

-- Mandat 2 (Collectivité Riziculture)
(2, 2, 'PRESIDENT'),
(8, 2, 'VICE_PRESIDENT'),

-- Mandat 3 (Collectivité Laitière)
(3, 3, 'PRESIDENT'),
(9, 3, 'TREASURER'),

-- Mandat 4 (Collectivité Maraîchers)
(4, 4, 'PRESIDENT'),
(10, 4, 'SECRETARY'),

-- Mandat 5 (Collectivité Fruitière)
(5, 5, 'PRESIDENT'),

-- Mandat 6 (Collectivité Éleveurs Ziguinchor)
(6, 6, 'PRESIDENT');

-- =========================
-- 6. INSERTION DES PARRAINAGES
-- =========================
INSERT INTO referee (candidate_id, referee_id, collectivity_id, relationship)
VALUES (1, 7, 1, 'Collègue de travail'),
       (2, 8, 2, 'Voisin'),
       (3, 9, 3, 'Frère'),
       (4, 10, 4, 'Amie'),
       (5, 1, 5, 'Mentor'),
       (6, 2, 6, 'Parent éloigné'),
       (11, 5, 5, 'Cousin'),
       (12, 8, 2, 'Collaboratrice');

-- =========================
-- 7. INSERTION DES PAIEMENTS
-- =========================
INSERT INTO payment (member_id, amount, type, payment_method, payment_date)
VALUES
-- Paiements d'Amadou (ID 1)
(1, 25000, 'REGISTRATION', 'CASH', '2023-01-15 10:30:00'),
(1, 12000, 'COTISATION', 'MOBILE_MONEY', '2023-06-20 14:45:00'),
(1, 12000, 'COTISATION', 'BANK_TRANSFER', '2024-01-10 09:15:00'),
(1, 15000, 'COTISATION', 'CASH', '2024-06-15 11:30:00'),

-- Paiements de Fatou (ID 2)
(2, 25000, 'REGISTRATION', 'MOBILE_MONEY', '2023-01-20 11:00:00'),
(2, 15000, 'COTISATION', 'CASH', '2023-07-15 16:20:00'),
(2, 15000, 'COTISATION', 'BANK_TRANSFER', '2024-02-10 14:00:00'),

-- Paiements de Mamadou (ID 3)
(3, 30000, 'REGISTRATION', 'CASH', '2022-04-01 08:00:00'),
(3, 12000, 'COTISATION', 'BANK_TRANSFER', '2023-05-10 13:30:00'),
(3, 12000, 'COTISATION', 'MOBILE_MONEY', '2024-02-15 10:00:00'),

-- Paiements d'Aissatou (ID 4)
(4, 25000, 'REGISTRATION', 'MOBILE_MONEY', '2021-07-10 12:00:00'),
(4, 10000, 'COTISATION', 'CASH', '2023-08-20 15:45:00'),

-- Paiements d'Ousmane (ID 5)
(5, 25000, 'REGISTRATION', 'BANK_TRANSFER', '2022-01-05 09:00:00'),
(5, 15000, 'COTISATION', 'MOBILE_MONEY', '2023-11-25 11:30:00'),

-- Paiements de Mariama (ID 6)
(6, 25000, 'REGISTRATION', 'CASH', '2020-09-01 14:00:00'),
(6, 12000, 'COTISATION', 'MOBILE_MONEY', '2023-10-10 09:45:00'),

-- Paiements d'Ibrahima (ID 7)
(7, 25000, 'REGISTRATION', 'BANK_TRANSFER', '2021-01-10 10:00:00'),
(7, 12000, 'COTISATION', 'CASH', '2023-09-05 13:15:00'),

-- Paiements de Ndeye (ID 8)
(8, 25000, 'REGISTRATION', 'MOBILE_MONEY', '2021-02-15 09:30:00'),
(8, 15000, 'COTISATION', 'BANK_TRANSFER', '2023-12-01 16:00:00'),

-- Paiements de Cheikh (ID 9)
(9, 30000, 'REGISTRATION', 'CASH', '2018-08-01 11:00:00'),
(9, 12000, 'COTISATION', 'MOBILE_MONEY', '2023-04-20 10:30:00'),

-- Paiements d'Aminata (ID 10)
(10, 25000, 'REGISTRATION', 'BANK_TRANSFER', '2022-01-25 14:20:00'),
(10, 10000, 'COTISATION', 'CASH', '2023-11-15 09:45:00'),

-- Nouveaux membres
(11, 25000, 'REGISTRATION', 'MOBILE_MONEY', '2023-02-05 11:00:00'),
(12, 25000, 'REGISTRATION', 'CASH', '2023-03-20 15:30:00');

-- =========================
-- 8. INSERTION DES COMPTES COLLECTIVITÉS
-- =========================
INSERT INTO collectivity_account (collectivity_id, type, balance, created_at)
VALUES (1, 'BANK', 250000, '2023-01-01 00:00:00'),
       (1, 'CASH', 125000, '2023-01-01 00:00:00'),
       (1, 'MOBILE_MONEY', 80000, '2024-01-01 00:00:00'),
       (2, 'MOBILE_MONEY', 180000, '2023-01-01 00:00:00'),
       (2, 'BANK', 100000, '2023-06-01 00:00:00'),
       (3, 'BANK', 320000, '2022-01-01 00:00:00'),
       (3, 'CASH', 95000, '2022-01-01 00:00:00'),
       (4, 'CASH', 45000, '2023-06-01 00:00:00'),
       (5, 'BANK', 95000, '2023-01-01 00:00:00'),
       (5, 'MOBILE_MONEY', 60000, '2023-07-01 00:00:00'),
       (6, 'CASH', 150000, '2022-07-01 00:00:00');

-- =========================
-- 9. INSERTION DES ACTIVITÉS
-- =========================
INSERT INTO activity (collectivity_id, type, activity_date, mandatory)
VALUES (1, 'GENERAL_MEETING', '2023-03-15', TRUE),
       (1, 'TRAINING', '2023-06-10', FALSE),
       (1, 'EXCEPTIONAL', '2023-09-20', TRUE),
       (1, 'GENERAL_MEETING', '2024-03-20', TRUE),
       (2, 'GENERAL_MEETING', '2023-04-05', TRUE),
       (2, 'TRAINING', '2023-08-15', FALSE),
       (3, 'TRAINING', '2023-07-25', FALSE),
       (3, 'GENERAL_MEETING', '2024-02-10', TRUE),
       (4, 'GENERAL_MEETING', '2023-10-01', TRUE),
       (4, 'EXCEPTIONAL', '2024-01-15', TRUE),
       (5, 'TRAINING', '2023-05-18', FALSE),
       (5, 'GENERAL_MEETING', '2024-03-05', TRUE),
       (6, 'EXCEPTIONAL', '2023-11-30', TRUE),
       (6, 'GENERAL_MEETING', '2024-01-20', TRUE);

-- =========================
-- 10. INSERTION DES PRÉSENCES
-- =========================
INSERT INTO attendance (activity_id, member_id, present, justified)
VALUES
-- Activité 1: Assemblée Générale Sud (2023-03-15)
(1, 1, TRUE, FALSE),
(1, 7, TRUE, FALSE),
(1, 2, FALSE, TRUE),
(1, 11, TRUE, FALSE),

-- Activité 2: Formation Sud (2023-06-10)
(2, 1, TRUE, FALSE),
(2, 7, FALSE, FALSE),
(2, 2, TRUE, FALSE),
(2, 11, TRUE, FALSE),

-- Activité 3: Événement Exceptionnel Sud (2023-09-20)
(3, 1, TRUE, FALSE),
(3, 7, TRUE, FALSE),
(3, 2, TRUE, FALSE),

-- Activité 4: Assemblée Générale Riziculture (2023-04-05)
(5, 2, TRUE, FALSE),
(5, 8, TRUE, FALSE),
(5, 12, FALSE, TRUE),

-- Activité 5: Formation Riziculture (2023-08-15)
(6, 2, TRUE, FALSE),
(6, 8, FALSE, FALSE),

-- Activité 6: Formation Élevage laitier (2023-07-25)
(7, 3, TRUE, FALSE),
(7, 9, FALSE, TRUE),

-- Activité 7: Assemblée Maraîchers (2023-10-01)
(9, 4, TRUE, FALSE),
(9, 10, TRUE, FALSE),
(9, 1, TRUE, FALSE),

-- Activité 8: Formation Fruitière (2023-05-18)
(11, 5, TRUE, FALSE),
(11, 11, FALSE, FALSE),

-- Activité 9: Événement Exceptionnel Ziguinchor (2023-11-30)
(13, 6, TRUE, FALSE),
(13, 2, FALSE, FALSE),

-- Activité 10: Assemblée Générale Sud 2024 (2024-03-20)
(4, 1, TRUE, FALSE),
(4, 7, TRUE, FALSE),
(4, 2, TRUE, FALSE),
(4, 11, FALSE, TRUE);