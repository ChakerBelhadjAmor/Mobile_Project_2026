-- Seed fixture for the Supervision des Livraisons project.
-- Loaded after schema generation thanks to spring.jpa.defer-datasource-initialization=true.

-- Postes / Positions
INSERT INTO postes (codeposte, libelle, indice) VALUES ('P01', 'CONTROLEUR', 100) ON CONFLICT DO NOTHING;
INSERT INTO postes (codeposte, libelle, indice) VALUES ('P02', 'LIVREUR',    50)  ON CONFLICT DO NOTHING;

-- Personnel (login / mot_p in clear for ease of evaluation)
INSERT INTO personnel (idpers, nompers, prenompers, adrpers, villepers, telpers, d_embauche, login, mot_p, codeposte)
VALUES (1, 'Ben Ali',  'Sami',  '12 Av. Habib',  'Tunis',    '20100100', '2022-01-10', 'ctrl1', 'pass', 'P01')
ON CONFLICT DO NOTHING;
INSERT INTO personnel (idpers, nompers, prenompers, adrpers, villepers, telpers, d_embauche, login, mot_p, codeposte)
VALUES (2, 'Trabelsi', 'Karim', '5 Rue Mongi',   'Sousse',   '20200200', '2023-03-15', 'liv1',  'pass', 'P02')
ON CONFLICT DO NOTHING;
INSERT INTO personnel (idpers, nompers, prenompers, adrpers, villepers, telpers, d_embauche, login, mot_p, codeposte)
VALUES (3, 'Bouzidi',  'Amine', '8 Rue de la Liberté', 'Sfax', '20300300', '2024-06-01', 'liv2',  'pass', 'P02')
ON CONFLICT DO NOTHING;

-- Clients
INSERT INTO clients (noclt, nomclt, prenomclt, adrclt, villeclt, code_postal, telclt, adrmail)
VALUES (101, 'Zouari', 'Leila',  '14 Av. Bourguiba',    'Tunis',  '1000', '98100100', 'leila@mail.com')
ON CONFLICT DO NOTHING;
INSERT INTO clients (noclt, nomclt, prenomclt, adrclt, villeclt, code_postal, telclt, adrmail)
VALUES (102, 'Saidi',  'Mehdi',  '22 Rue Charles',      'Ariana', '2080', '98200200', 'mehdi@mail.com')
ON CONFLICT DO NOTHING;
INSERT INTO clients (noclt, nomclt, prenomclt, adrclt, villeclt, code_postal, telclt, adrmail)
VALUES (103, 'Kefi',   'Nadia',  '3 Lotissement Nour',  'Sousse', '4000', '98300300', 'nadia@mail.com')
ON CONFLICT DO NOTHING;

-- Articles
INSERT INTO articles (refart, designation,             prix_a, prix_v, codetva, categorie, qtestk) VALUES ('A001', 'Riz 5kg',            8.0,  12.0, 'TVA19', 'Epicerie',  120) ON CONFLICT DO NOTHING;
INSERT INTO articles (refart, designation,             prix_a, prix_v, codetva, categorie, qtestk) VALUES ('A002', 'Huile olive 1L',     15.0, 22.0, 'TVA19', 'Epicerie',  80)  ON CONFLICT DO NOTHING;
INSERT INTO articles (refart, designation,             prix_a, prix_v, codetva, categorie, qtestk) VALUES ('A003', 'Pâtes 500g',         1.0,  2.0,  'TVA07', 'Epicerie',  300) ON CONFLICT DO NOTHING;
INSERT INTO articles (refart, designation,             prix_a, prix_v, codetva, categorie, qtestk) VALUES ('A004', 'Eau minérale 6x1.5L', 2.5, 4.0,  'TVA07', 'Boisson',   200) ON CONFLICT DO NOTHING;

-- Commandes (dates: today + yesterday for dashboard realism)
INSERT INTO commandes (nocde, noclt, datecde, etatcde) VALUES (1001, 101, CURRENT_DATE,        'CONFIRMEE') ON CONFLICT DO NOTHING;
INSERT INTO commandes (nocde, noclt, datecde, etatcde) VALUES (1002, 102, CURRENT_DATE,        'CONFIRMEE') ON CONFLICT DO NOTHING;
INSERT INTO commandes (nocde, noclt, datecde, etatcde) VALUES (1003, 103, CURRENT_DATE,        'CONFIRMEE') ON CONFLICT DO NOTHING;
INSERT INTO commandes (nocde, noclt, datecde, etatcde) VALUES (1004, 101, CURRENT_DATE - 1,    'CONFIRMEE') ON CONFLICT DO NOTHING;
INSERT INTO commandes (nocde, noclt, datecde, etatcde) VALUES (1005, 103, CURRENT_DATE - 1,    'CONFIRMEE') ON CONFLICT DO NOTHING;

-- Order lines
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1001, 'A001', 2) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1001, 'A003', 4) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1002, 'A002', 1) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1002, 'A004', 3) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1003, 'A001', 1) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1003, 'A002', 2) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1004, 'A003', 10) ON CONFLICT DO NOTHING;
INSERT INTO lig_cdes (nocde, refart, qtecde) VALUES (1005, 'A004', 5) ON CONFLICT DO NOTHING;

-- Deliveries
INSERT INTO livraison_com (nocde, dateliv, livreur, modepay, etatliv, remarque) VALUES (1001, CURRENT_DATE,     2, 'ESPECES', 'EN_COURS',   NULL) ON CONFLICT DO NOTHING;
INSERT INTO livraison_com (nocde, dateliv, livreur, modepay, etatliv, remarque) VALUES (1002, CURRENT_DATE,     3, 'CB',      'EN_ATTENTE', NULL) ON CONFLICT DO NOTHING;
INSERT INTO livraison_com (nocde, dateliv, livreur, modepay, etatliv, remarque) VALUES (1003, CURRENT_DATE,     2, 'ESPECES', 'EN_ATTENTE', NULL) ON CONFLICT DO NOTHING;
INSERT INTO livraison_com (nocde, dateliv, livreur, modepay, etatliv, remarque) VALUES (1004, CURRENT_DATE - 1, 2, 'CB',      'LIVREE',     NULL) ON CONFLICT DO NOTHING;
INSERT INTO livraison_com (nocde, dateliv, livreur, modepay, etatliv, remarque) VALUES (1005, CURRENT_DATE - 1, 3, 'ESPECES', 'NON_LIVREE', 'Client absent') ON CONFLICT DO NOTHING;
