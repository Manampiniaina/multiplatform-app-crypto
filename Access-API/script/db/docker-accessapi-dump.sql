--
-- PostgreSQL database dump
--

-- Dumped from database version 15.10 (Debian 15.10-1.pgdg120+1)
-- Dumped by pg_dump version 15.10 (Debian 15.10-1.pgdg120+1)

-- Started on 2025-02-10 05:13:29 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 216 (class 1259 OID 16388)
-- Name: s_compte; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_compte
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16415)
-- Name: compte; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.compte (
    id character varying(14) DEFAULT ('CMT'::text || lpad((nextval('public.s_compte'::regclass))::text, 9, '0'::text)) NOT NULL,
    d_nb_tentative smallint DEFAULT 0,
    d_date_debloquage timestamp without time zone,
    d_pin_actuel character varying(6),
    d_date_expiration_pin timestamp without time zone,
    id_utilisateur character varying(14) NOT NULL
);


--
-- TOC entry 214 (class 1259 OID 16386)
-- Name: s_genre; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_genre
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 220 (class 1259 OID 16392)
-- Name: genre; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.genre (
    id character varying(14) DEFAULT ('GR'::text || lpad((nextval('public.s_genre'::regclass))::text, 2, '0'::text)) NOT NULL,
    libelle character varying(50) NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 16389)
-- Name: s_pin; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_pin
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 225 (class 1259 OID 16452)
-- Name: pin; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pin (
    id character varying(14) DEFAULT ('PIN'::text || lpad((nextval('public.s_pin'::regclass))::text, 9, '0'::text)) NOT NULL,
    pin character varying(6) NOT NULL,
    date_expiration timestamp without time zone NOT NULL,
    idcompte character varying(14) NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 16390)
-- Name: s_tentative; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_tentative
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 219 (class 1259 OID 16391)
-- Name: s_token; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_token
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 215 (class 1259 OID 16387)
-- Name: s_utilisateur; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.s_utilisateur
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 223 (class 1259 OID 16429)
-- Name: tentative; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tentative (
    id character varying(14) DEFAULT ('TTV'::text || lpad((nextval('public.s_tentative'::regclass))::text, 9, '0'::text)) NOT NULL,
    date_tentative timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idcompte character varying(14) NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 16441)
-- Name: token_compte; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.token_compte (
    id character varying(14) DEFAULT ('TKN'::text || lpad((nextval('public.s_token'::regclass))::text, 9, '0'::text)) NOT NULL,
    date_expiration timestamp without time zone NOT NULL,
    valeur character varying(200) NOT NULL,
    idcompte character varying(14) NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 16400)
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.utilisateur (
    id character varying(14) DEFAULT ('USR'::text || lpad((nextval('public.s_utilisateur'::regclass))::text, 9, '0'::text)) NOT NULL,
    mail character varying(50) NOT NULL,
    mdp character varying(200) NOT NULL,
    salt character varying(250) NOT NULL,
    nom character varying(100) NOT NULL,
    prenom character varying(30),
    date_naissance date NOT NULL,
    idgenre character varying(14) NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 16463)
-- Name: v_compteutilisateur; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.v_compteutilisateur AS
 SELECT u.mail,
    u.mdp,
    u.salt,
    c.id,
    c.d_nb_tentative,
    c.d_date_debloquage,
    c.d_pin_actuel,
    c.d_date_expiration_pin,
    c.id_utilisateur
   FROM (public.compte c
     JOIN public.utilisateur u ON (((c.id_utilisateur)::text = (u.id)::text)));


--
-- TOC entry 3410 (class 0 OID 16415)
-- Dependencies: 222
-- Data for Name: compte; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.compte (id, d_nb_tentative, d_date_debloquage, d_pin_actuel, d_date_expiration_pin, id_utilisateur) FROM stdin;
CMT000000006	0	\N	\N	\N	USR000000006
CMT000000007	0	\N	\N	\N	USR000000007
CMT000000008	0	\N	\N	\N	USR000000008
CMT000000009	0	\N	\N	\N	USR000000009
CMT000000010	0	\N	\N	\N	USR000000010
CMT000000002	0	\N	1153	2025-02-10 04:36:07	USR000000002
CMT000000003	0	\N	5402	2025-02-10 04:40:10	USR000000003
CMT000000004	0	\N	9374	2025-02-10 04:44:29	USR000000004
CMT000000005	0	\N	6803	2025-02-10 04:45:39	USR000000005
CMT000000001	0	\N	7124	2025-02-10 05:10:57	USR000000001
\.


--
-- TOC entry 3408 (class 0 OID 16392)
-- Dependencies: 220
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.genre (id, libelle) FROM stdin;
GR01	homme
GR02	femme
\.


--
-- TOC entry 3413 (class 0 OID 16452)
-- Dependencies: 225
-- Data for Name: pin; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.pin (id, pin, date_expiration, idcompte) FROM stdin;
PIN000000001	3142	2025-02-10 04:07:32	CMT000000001
PIN000000002	6069	2025-02-10 04:09:14	CMT000000002
PIN000000003	4837	2025-02-10 04:09:53	CMT000000003
PIN000000004	7372	2025-02-10 04:10:38	CMT000000004
PIN000000005	7235	2025-02-10 04:11:15	CMT000000005
PIN000000006	2248	2025-02-10 04:12:07	CMT000000001
PIN000000007	9478	2025-02-10 04:27:16	CMT000000001
PIN000000008	0567	2025-02-10 04:31:53	CMT000000001
PIN000000009	1153	2025-02-10 04:36:07	CMT000000002
PIN000000010	5402	2025-02-10 04:40:10	CMT000000003
PIN000000011	9374	2025-02-10 04:44:29	CMT000000004
PIN000000012	6803	2025-02-10 04:45:39	CMT000000005
PIN000000013	4126	2025-02-10 04:47:12	CMT000000001
PIN000000014	4543	2025-02-10 05:02:52	CMT000000001
PIN000000015	7124	2025-02-10 05:10:57	CMT000000001
\.


--
-- TOC entry 3411 (class 0 OID 16429)
-- Dependencies: 223
-- Data for Name: tentative; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tentative (id, date_tentative, idcompte) FROM stdin;
TTV000000001	2025-02-10 04:05:45	CMT000000001
\.


--
-- TOC entry 3412 (class 0 OID 16441)
-- Dependencies: 224
-- Data for Name: token_compte; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.token_compte (id, date_expiration, valeur, idcompte) FROM stdin;
TKN000000001	2025-02-11 04:06:27	2d159ee27dd72221f5c8e3d293574056	CMT000000001
TKN000000002	2025-02-11 04:07:25	4348f547510b8c8ff6ace7649552c319	CMT000000002
TKN000000003	2025-02-11 04:08:05	51f09f41ea2246cd522eff34d348eb7e	CMT000000003
TKN000000004	2025-02-11 04:08:48	a2ae6bcdef161900fb39fd2dd02bc1e9	CMT000000004
TKN000000005	2025-02-11 04:09:25	16a9b47bfc6c942c8b25bd757b11a01b	CMT000000005
TKN000000006	2025-02-11 04:10:16	6fe26bf4d01bbb121071cc6f0f126e3b	CMT000000001
TKN000000007	2025-02-11 04:25:29	c87b87723229fd53b4a69b8fd8ff0dab	CMT000000001
TKN000000008	2025-02-11 04:30:08	985b9ca1cacba6427df210f30d8c6f23	CMT000000001
TKN000000009	2025-02-11 04:34:16	fe01ae1141959bf6d34c7f0d70cbef4a	CMT000000002
TKN000000010	2025-02-11 04:38:23	fde70f7c87c02e4881a0d955ec94615e	CMT000000003
TKN000000011	2025-02-11 04:42:41	3e1f94cfd176fc2ff070d9ca881a713a	CMT000000004
TKN000000012	2025-02-11 04:43:52	10e4bde7433e011c9f7f657bd4baacbb	CMT000000005
TKN000000013	2025-02-11 04:45:22	10a3f4378c9e772bf0883438434d9102	CMT000000001
TKN000000014	2025-02-11 05:01:19	68dbda58a68bd113e8466b92d6870da7	CMT000000001
TKN000000015	2025-02-11 05:09:49	23ab6cd384007453bc1f9bb27e612c41	CMT000000001
\.


--
-- TOC entry 3409 (class 0 OID 16400)
-- Dependencies: 221
-- Data for Name: utilisateur; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.utilisateur (id, mail, mdp, salt, nom, prenom, date_naissance, idgenre) FROM stdin;
USR000000001	leadupuis@gmail.com	$2y$10$s9o0v1Ys/CxGlEWWISGwF.jDkKOLUuBct67lxXep7PF9FdssWkg8m	z[JoYD@iTMLp8ujA	DUPUIS	Léa	1992-03-15	GR02
USR000000002	lucmartin@hotmail.com	$2y$10$JcSMVATcJLGJ/DlMZZ1JTeGHoENv3q9WgstX1ISTyuEdd4NOmy0Qe	Qg6&Who3p@P.QU!@	MARTIN	Luc	1988-07-02	GR01
USR000000003	sophiebernard@gmail.com	$2y$10$l5iPtdRA2GHB0qj3cWHFd.Z8fNiQ7HybLmEKoHKTV8/wnx17HYsrq	11bu).J9qrs&_q4A	BERNARD	Sophie	1995-11-29	GR02
USR000000004	marcrobert@gmail.com	$2y$10$DGt8ah0dfMCw05xpaXvNGuFzCd.Fm/i5OuVIEKYC7aEf/Z92yW3Re	+FS1,XCLImI+hhdF	ROBERT	Marc	1990-06-18	GR01
USR000000005	emiliedurand@ymail.com	$2y$10$6QbqNUUqYF40MIz1e3myE.P0Qd2BPGxqI95bYbtWToVRb0uoJiEWa	SDG-/[xT]JbXkTG;	DURAND	Émilie	1987-09-05	GR02
USR000000006	pierreleclerc@ymail.com	$2y$10$WplpueoFyc92yOmYjQQNpOtcUJgXPJEzc416nU5FmiLxOufphvX2i	o[<Blw1vCTK$.lRO	LECLERC	Pierre	1993-04-22	GR01
USR000000007	claireleroy@ymail.com	$2y$10$gD8uzYIPwcLgnGTCY.UstuExHwxeJo5HdkpxAKFhRZkaRkHXRQ.Ny	?YI87c_n<taCv9=y	LEROY	Claire	1989-12-10	GR02
USR000000008	nicolasandre@gmail.com	$2y$10$SJ22IqTwm/h6j7bJComB7uHBtZRoJH5XDotSNXomeOW29gs63KFRS	laYSrPRkg<c?=gz+	ANDRÉ	Nicolas	1991-02-08	GR01
USR000000009	alicemoreau@hotmail.com	$2y$10$QVIowif9/U4SavIGPqVEEOmAF8q1ShcnfpCDnfdgQ1oWEkzh6lb26	*zNtccUMX4YcLMzg	MOREAU	Alice	1994-08-25	GR02
USR000000010	antoinesimon@ymail.com	$2y$10$oQYHOUi1N7U2XFu1ioMu6.rrUegZbgHlhm8hlp96tvRTZBAvkuCuC	x&7pwEyKZfAvW:<&	SIMON	Antoine	1986-01-14	GR01
\.


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 216
-- Name: s_compte; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_compte', 10, true);


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 214
-- Name: s_genre; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_genre', 2, true);


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 217
-- Name: s_pin; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_pin', 15, true);


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 218
-- Name: s_tentative; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_tentative', 1, true);


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 219
-- Name: s_token; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_token', 15, true);


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 215
-- Name: s_utilisateur; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.s_utilisateur', 10, true);


--
-- TOC entry 3245 (class 2606 OID 16423)
-- Name: compte compte_id_utilisateur_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compte
    ADD CONSTRAINT compte_id_utilisateur_key UNIQUE (id_utilisateur);


--
-- TOC entry 3247 (class 2606 OID 16421)
-- Name: compte compte_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compte
    ADD CONSTRAINT compte_pkey PRIMARY KEY (id);


--
-- TOC entry 3237 (class 2606 OID 16399)
-- Name: genre genre_libelle_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_libelle_key UNIQUE (libelle);


--
-- TOC entry 3239 (class 2606 OID 16397)
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (id);


--
-- TOC entry 3253 (class 2606 OID 16457)
-- Name: pin pin_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pin
    ADD CONSTRAINT pin_pkey PRIMARY KEY (id);


--
-- TOC entry 3249 (class 2606 OID 16435)
-- Name: tentative tentative_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tentative
    ADD CONSTRAINT tentative_pkey PRIMARY KEY (id);


--
-- TOC entry 3251 (class 2606 OID 16446)
-- Name: token_compte token_compte_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.token_compte
    ADD CONSTRAINT token_compte_pkey PRIMARY KEY (id);


--
-- TOC entry 3241 (class 2606 OID 16409)
-- Name: utilisateur utilisateur_mail_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_mail_key UNIQUE (mail);


--
-- TOC entry 3243 (class 2606 OID 16407)
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id);


--
-- TOC entry 3255 (class 2606 OID 16424)
-- Name: compte compte_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compte
    ADD CONSTRAINT compte_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id);


--
-- TOC entry 3258 (class 2606 OID 16458)
-- Name: pin pin_idcompte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pin
    ADD CONSTRAINT pin_idcompte_fkey FOREIGN KEY (idcompte) REFERENCES public.compte(id);


--
-- TOC entry 3256 (class 2606 OID 16436)
-- Name: tentative tentative_idcompte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tentative
    ADD CONSTRAINT tentative_idcompte_fkey FOREIGN KEY (idcompte) REFERENCES public.compte(id);


--
-- TOC entry 3257 (class 2606 OID 16447)
-- Name: token_compte token_compte_idcompte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.token_compte
    ADD CONSTRAINT token_compte_idcompte_fkey FOREIGN KEY (idcompte) REFERENCES public.compte(id);


--
-- TOC entry 3254 (class 2606 OID 16410)
-- Name: utilisateur utilisateur_idgenre_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_idgenre_fkey FOREIGN KEY (idgenre) REFERENCES public.genre(id);


-- Completed on 2025-02-10 05:13:30 UTC

--
-- PostgreSQL database dump complete
--

