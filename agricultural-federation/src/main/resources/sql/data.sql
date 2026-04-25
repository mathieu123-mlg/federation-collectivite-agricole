insert into collectivity (id, number, name, location, speciality)
values ('col-1', 1, 'Mpanorina', 'Ambatondrazaka', 'Riziculture'),
       ('col-2', 2, 'Dobo Voalohany', 'Ambatondrazaka', 'Pisciculture'),
       ('col-3', 3, 'Tantely Mamy', 'Brickaville', 'Apiculture');

insert into member (id, first_name, last_name, birthdate, gender, address, profession, phone_number, email)
values ('M1', 'Nom membre 1', 'Prénom membre 1', '01/02/1980', 'M', 'Lot II V M Ambato', 'Riziculteur', '0341234567','member.1@fed-agri.mg'),
       ('M2', 'Nom membre 2', 'Prénom membre 2', '05/03/1982', 'M', 'Lot II F Ambato', 'Agriculteur', '0321234567','member.2@fed-agri.mg'),
       ('M3', 'Nom membre 3', 'Prénom membre 3', '10/03/1992', 'M', 'Lot II J Ambato', 'Collecteur', '0331234567','member.3@fed-agri.mg'),
       ('M4', 'Nom membre 4', 'Prénom membre 4', '22/05/1988', 'F', 'Lot AK 50 Ambato', 'Distributeur', '0381234567','member.4@fed-agri.mg'),
       ('M5', 'Nom membre 5', 'Prénom membre 5', '21/08/1999', 'M', 'Lot UV 80 Ambato', 'Riziculteur', '0373434567','member.5@fed-agri.mg'),
       ('M6', 'Nom membre 6', 'Prénom membre 6', '22/08/1998', 'F', 'Lot UV 6 Ambato', 'Riziculteur', '0372234567','member.6@fed-agri.mg'),
       ('M7', 'Nom membre 7', 'Prénom membre 7', '31/01/1998', 'M', 'Lot UV 7 Ambato', 'Riziculteur', '0374234567','member.7@fed-agri.mg'),
       ('M8', 'Nom membre 8', 'Prénom membre 8', '20/08/1975', 'M', 'Lot UV 8 Ambato', 'Riziculteur', '0370234567','member.8@fed-agri.mg'),

       ('M9', 'Nom membre 9', 'Prénom membre 9', '02/01/1988', 'M', 'Lot 33 J Antsirabe', 'Apiculteur', '0341234567','member.9@fed-agri.mg'),
       ('M10', 'Nom membre 10', 'Prénom membre 10', '05/03/1982', 'M', 'Lot 2 J Antsirabe', 'Agriculteur', '0321234567', 'member.10@fed-agri.mg'),
       ('M11', 'Nom membre 11', 'Prénom membre 11', '12/03/1992', 'M', 'Lot 8 KM Antsirabe', 'Collecteur', '0331234567', 'member.11@fed-agri.mg'),
       ('M12', 'Nom membre 12', 'Prénom membre 12', '10/05/1988', 'F', 'Lot AK 50 Antsirabe', 'Distributeur', '0381234567', 'member.12@fed-agri.mg'),
       ('M13', 'Nom membre 13', 'Prénom membre 13', '11/08/1999', 'M', 'Lot UV 80 Antsirabe', 'Apiculteur', '0373434567', 'member.13@fed-agri.mg'),
       ('M14', 'Nom membre 14', 'Prénom membre 14', '09/08/1998', 'F', 'Lot UV 6 Antsirabe', 'Apiculteur', '0372234567', 'member.14@fed-agri.mg'),
       ('M15', 'Nom membre 15', 'Prénom membre 15', '13/01/1998', 'M', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374234567', 'member.15@fed-agri.mg'),
       ('M16', 'Nom membre 16', 'Prénom membre 16', '02/08/1975', 'M', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370234567', 'member.16@fed-agri.mg');

insert into member_collectivity (id, collectivity_id, member_id, occupation)
values ('C1-M1', 'col-1', 'M1', 'PRESIDENT'),
       ('C1-M2', 'col-1', 'M2', 'VICE_PRESIDENT'),
       ('C1-M3', 'col-1', 'M3', 'SECRETARY'),
       ('C1-M4', 'col-1', 'M4', 'TREASURER'),
       ('C1-M5', 'col-1', 'M5', 'CONFIRMED'),
       ('C1-M6', 'col-1', 'M6', 'CONFIRMED'),
       ('C1-M7', 'col-1', 'M7', 'CONFIRMED'),
       ('C1-M8', 'col-1', 'M8', 'CONFIRMED'),

       ('C2-M1', 'col-2', 'M1', 'CONFIRMED'),
       ('C2-M2', 'col-2', 'M2', 'CONFIRMED'),
       ('C2-M3', 'col-2', 'M3', 'CONFIRMED'),
       ('C2-M4', 'col-2', 'M4', 'CONFIRMED'),
       ('C2-M5', 'col-2', 'M5', 'PRESIDENT'),
       ('C2-M6', 'col-2', 'M6', 'VICE_PRESIDENT'),
       ('C2-M7', 'col-2', 'M7', 'SECRETARY'),
       ('C2-M8', 'col-2', 'M8', 'TREASURER'),

       ('C3-M1', 'col-3', 'M9', 'PRESIDENT'),
       ('C3-M2', 'col-3', 'M10', 'VICE_PRESIDENT'),
       ('C3-M3', 'col-3', 'M11', 'SECRETARY'),
       ('C3-M4', 'col-3', 'M12', 'TREASURER'),
       ('C3-M5', 'col-3', 'M13', 'CONFIRMED'),
       ('C3-M6', 'col-3', 'M14', 'CONFIRMED'),
       ('C3-M7', 'col-3', 'M15', 'CONFIRMED'),
       ('C3-M8', 'col-3', 'M16', 'CONFIRMED');

insert into member_referrals (collectivity_id, member_col_id, referrer_col_id)
values ('col-1', 'C1-M3', 'C1-M1'), ('col-1', 'C1-M3', 'C1-M2'),
       ('col-1', 'C1-M4', 'C1-M1'), ('col-1', 'C1-M4', 'C1-M2'),
       ('col-1', 'C1-M5', 'C1-M1'), ('col-1', 'C1-M5', 'C1-M2'),
       ('col-1', 'C1-M6', 'C1-M1'), ('col-1', 'C1-M6', 'C1-M2'),
       ('col-1', 'C1-M7', 'C1-M1'), ('col-1', 'C1-M7', 'C1-M2'),
       ('col-1', 'C1-M8', 'C1-M6'), ('col-1', 'C1-M8', 'C1-M7'),

       ('col-2', 'C2-M3', 'C2-M1'), ('col-2', 'C2-M3', 'C2-M2'),
       ('col-2', 'C2-M4', 'C2-M1'), ('col-2', 'C2-M4', 'C2-M2'),
       ('col-2', 'C2-M5', 'C2-M1'), ('col-2', 'C2-M5', 'C2-M2'),
       ('col-2', 'C2-M6', 'C2-M1'), ('col-2', 'C2-M6', 'C2-M2'),
       ('col-2', 'C2-M7', 'C2-M1'), ('col-2', 'C2-M7', 'C2-M2'),
       ('col-2', 'C2-M8', 'C2-M6'), ('col-2', 'C2-M8', 'C2-M7'),

       ('col-3', 'C3-M1', 'C1-M1'), ('col-3', 'C3-M1', 'C1-M2'),
       ('col-3', 'C3-M2', 'C1-M1'), ('col-3', 'C3-M2', 'C1-M2'),
       ('col-3', 'C3-M3', 'C3-M1'), ('col-3', 'C3-M3', 'C3-M2'),
       ('col-3', 'C3-M4', 'C3-M1'), ('col-3', 'C3-M4', 'C3-M2'),
       ('col-3', 'C3-M5', 'C3-M1'), ('col-3', 'C3-M5', 'C3-M2'),
       ('col-3', 'C3-M6', 'C3-M1'), ('col-3', 'C3-M6', 'C3-M2'),
       ('col-3', 'C3-M7', 'C3-M1'), ('col-3', 'C3-M7', 'C3-M2'),
       ('col-3', 'C3-M8', 'C3-M1'), ('col-3', 'C3-M8', 'C3-M2');

insert into membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
values ('cot-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '01/01/2026', 100_000, 'col-1'),
       ('cot-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '01/01/2026', 100_000, 'col-2'),
       ('cot-3', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '01/01/2026', 50_000, 'col-3');

insert into account_collectivity (collectivity_id, id, account_type, amount, titular, account_number)
values ('col-1', 'C1-A-CASH', 'CASH', 0, null, null),
       ('col-1', 'C1-A-MOBILE-1', 'ORANGE_MONEY', 0, 'Mpanorina', 0370489612),

       ('col-2', 'C2-A-CASH', 'CASH', 0, null, null),
       ('col-2', 'C2-A-MOBILE-1', 'ORANGE_MONEY', 0, 'Dobo Voalohany', 0320489612),

       ('col-3', 'C3-A-CASH', 'CASH', 0, null, null);

insert into payment (collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
values ('col-1', 'C1-M1', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M2', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M3', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M4', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M5', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M6', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M7', 60_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M8', 90_000, 'C1-A-CASH', 'CASH', '01/01/2026'),

       ('col-2', 'C2-M1', 60_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M2', 90_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M3', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M4', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M5', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M6', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M7', 40_000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '01/01/2026'),
       ('col-2', 'C2-M8', 60_000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '01/01/2026');

insert into transaction (collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
values ('col-1', 'C1-M1', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M2', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M3', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M4', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M5', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M6', 100_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M7', 60_000, 'C1-A-CASH', 'CASH', '01/01/2026'),
       ('col-1', 'C1-M8', 90_000, 'C1-A-CASH', 'CASH', '01/01/2026'),

       ('col-2', 'C2-M1', 60_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M2', 90_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M3', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M4', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M5', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M6', 100_000, 'C2-A-CASH', 'CASH', '01/01/2026'),
       ('col-2', 'C2-M7', 40_000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '01/01/2026'),
       ('col-2', 'C2-M8', 60_000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '01/01/2026');
