--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)
-- Dumped by pg_dump version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)

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
-- Name: haversine(double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: daniel
--

CREATE FUNCTION public.haversine(lat1 double precision, lon1 double precision, lat2 double precision, lon2 double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$
DECLARE
  R double precision;
  f1 double precision;
  f2 double precision;
  df double precision;
  da double precision;
  a double precision;
  c double precision;
BEGIN
  R := 6371e3;
  f1 := (lat1 * pi()) / 180;
  f2 := (lat2 * pi()) / 180;
  df := ((lat2 - lat1) * pi()) / 180;
  da := ((lon2 - lon1) * pi()) / 180;
  
  a := sin(df / 2)^2 + cos(f1) * cos(f2) * sin(da / 2)^2;
  c := 2 * atan2(sqrt(a), sqrt(1 - a));
  RETURN R * c / 1000;
END
$$;


ALTER FUNCTION public.haversine(lat1 double precision, lon1 double precision, lat2 double precision, lon2 double precision) OWNER TO daniel;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: event; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.event (
    appointed_time timestamp(6) with time zone NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    place_id bigint NOT NULL,
    status_id bigint NOT NULL,
    description character varying(5000) NOT NULL,
    event_type character varying(255),
    title character varying(255) NOT NULL
);


ALTER TABLE public.event OWNER TO daniel;

--
-- Name: event_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.event_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.event_seq OWNER TO daniel;

--
-- Name: opportunity; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.opportunity (
    created_on timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT opportunity_name_check CHECK (((name)::text = ANY ((ARRAY['DELETE'::character varying, 'EDIT'::character varying, 'READ_MESSAGES'::character varying, 'WRITE_MESSAGES'::character varying])::text[])))
);


ALTER TABLE public.opportunity OWNER TO daniel;

--
-- Name: opportunity_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.opportunity_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.opportunity_seq OWNER TO daniel;

--
-- Name: place; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.place (
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    created_on timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    description character varying(1000),
    title character varying(255) NOT NULL
);


ALTER TABLE public.place OWNER TO daniel;

--
-- Name: place_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.place_seq
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.place_seq OWNER TO daniel;

--
-- Name: role; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.role (
    created_on timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT role_type_check CHECK (((type)::text = ANY ((ARRAY['OWNER'::character varying, 'MEMBER'::character varying])::text[])))
);


ALTER TABLE public.role OWNER TO daniel;

--
-- Name: role_opportunity; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.role_opportunity (
    opportunity_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.role_opportunity OWNER TO daniel;

--
-- Name: status; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.status (
    created_on timestamp(6) with time zone NOT NULL,
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT status_type_check CHECK (((type)::text = ANY ((ARRAY['PENDING'::character varying, 'FAILED'::character varying, 'ACCEPTED'::character varying])::text[])))
);


ALTER TABLE public.status OWNER TO daniel;

--
-- Name: status_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.status_seq
    START WITH 1
    INCREMENT BY 10
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.status_seq OWNER TO daniel;

--
-- Name: user_event; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.user_event (
    created_on timestamp(6) with time zone NOT NULL,
    event_id bigint NOT NULL,
    id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.user_event OWNER TO daniel;

--
-- Name: user_event_role; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.user_event_role (
    role_id bigint NOT NULL,
    user_event_id bigint NOT NULL
);


ALTER TABLE public.user_event_role OWNER TO daniel;

--
-- Name: user_event_seq; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.user_event_seq
    START WITH 1
    INCREMENT BY 20
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_event_seq OWNER TO daniel;

--
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.event (appointed_time, created_on, id, place_id, status_id, description, event_type, title) FROM stdin;
2025-05-30 09:00:00+03	2025-05-28 16:31:49.576899+03	2	2	2	Хорошая кардио нагрузка, пройдём 8 км до Аксая	\N	Поход в Аксай
2025-05-31 10:00:00+03	2025-05-23 21:02:00.927777+03	1	1	2	Лёгкая кардионагрузка на свежем воздухе, пробуждающая организм и заряжающая энергией на весь день.	\N	Утренняя пробежка
\.


--
-- Data for Name: opportunity; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.opportunity (created_on, id, name) FROM stdin;
\.


--
-- Data for Name: place; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.place (latitude, longitude, created_on, id, description, title) FROM stdin;
47.20463329078848	38.94640348524399	2025-05-23 21:02:00.416459+03	1	Около памятника Петра 1	Исторический сквер
47.222207395158705	39.70916745782347	2025-05-28 16:31:47.812747+03	2	Около фонтана	Парк культуры Горького
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.role (created_on, id, type) FROM stdin;
\.


--
-- Data for Name: role_opportunity; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.role_opportunity (opportunity_id, role_id) FROM stdin;
\.


--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.status (created_on, id, type) FROM stdin;
2025-05-23 17:11:26.707582+03	1	ACCEPTED
2025-05-23 17:11:26.707609+03	2	PENDING
\.


--
-- Data for Name: user_event; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.user_event (created_on, event_id, id, user_id) FROM stdin;
2025-05-23 21:02:01.092234+03	1	1	1
2025-05-23 21:02:01.164853+03	1	2	2
2025-05-23 21:02:01.195761+03	1	3	23
2025-05-28 16:31:49.80711+03	2	22	1
2025-05-28 16:31:50.483116+03	2	23	23
\.


--
-- Data for Name: user_event_role; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.user_event_role (role_id, user_event_id) FROM stdin;
\.


--
-- Name: event_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.event_seq', 6, true);


--
-- Name: opportunity_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.opportunity_seq', 1, false);


--
-- Name: place_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.place_seq', 6, true);


--
-- Name: status_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.status_seq', 321, true);


--
-- Name: user_event_seq; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.user_event_seq', 41, true);


--
-- Name: event event_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- Name: event event_place_id_key; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT event_place_id_key UNIQUE (place_id);


--
-- Name: opportunity opportunity_name_key; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.opportunity
    ADD CONSTRAINT opportunity_name_key UNIQUE (name);


--
-- Name: opportunity opportunity_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.opportunity
    ADD CONSTRAINT opportunity_pkey PRIMARY KEY (id);


--
-- Name: place place_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.place
    ADD CONSTRAINT place_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: role role_type_key; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_type_key UNIQUE (type);


--
-- Name: status status_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_pkey PRIMARY KEY (id);


--
-- Name: status status_type_key; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_type_key UNIQUE (type);


--
-- Name: user_event user_event_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.user_event
    ADD CONSTRAINT user_event_pkey PRIMARY KEY (id);


--
-- Name: user_event_role user_event_role_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.user_event_role
    ADD CONSTRAINT user_event_role_pkey PRIMARY KEY (role_id, user_event_id);


--
-- Name: event_title_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX event_title_idx ON public.event USING btree (title);


--
-- Name: user_event_event_id_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX user_event_event_id_idx ON public.user_event USING btree (event_id);


--
-- Name: event fk1xg79nmlpg6dab286gk1uhava; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT fk1xg79nmlpg6dab286gk1uhava FOREIGN KEY (status_id) REFERENCES public.status(id);


--
-- Name: role_opportunity fk6u09xlytk32ipiish70xeiu04; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.role_opportunity
    ADD CONSTRAINT fk6u09xlytk32ipiish70xeiu04 FOREIGN KEY (opportunity_id) REFERENCES public.opportunity(id);


--
-- Name: user_event_role fk7k5jbp89s9e8r9b3i4a77463m; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.user_event_role
    ADD CONSTRAINT fk7k5jbp89s9e8r9b3i4a77463m FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: role_opportunity fkifs01lic2p3m4cvghhdu0wf0q; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.role_opportunity
    ADD CONSTRAINT fkifs01lic2p3m4cvghhdu0wf0q FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: user_event_role fkmav77ky1qpaqwvq669qwdrku6; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.user_event_role
    ADD CONSTRAINT fkmav77ky1qpaqwvq669qwdrku6 FOREIGN KEY (user_event_id) REFERENCES public.user_event(id);


--
-- Name: event fkpuvix4lexrakgdlt8si1tbtxv; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT fkpuvix4lexrakgdlt8si1tbtxv FOREIGN KEY (place_id) REFERENCES public.place(id);


--
-- Name: user_event fkspe8srtv69gubpphvrnd7wekt; Type: FK CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.user_event
    ADD CONSTRAINT fkspe8srtv69gubpphvrnd7wekt FOREIGN KEY (event_id) REFERENCES public.event(id);


--
-- PostgreSQL database dump complete
--

