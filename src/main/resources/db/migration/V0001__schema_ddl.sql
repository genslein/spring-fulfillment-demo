-- DROP SCHEMA public;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;

-- public.customers definition

-- Drop table

-- DROP TABLE public.customers;

CREATE TABLE public.customers (
  id uuid NOT NULL,
  address_one varchar(255) NOT NULL,
  address_two varchar(255) NULL,
  city varchar(255) NOT NULL,
  created_at date NULL,
  email varchar(255) NOT NULL,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  state varchar(255) NOT NULL,
  updated_at date NULL,
  zip_code int4 NOT NULL,
  CONSTRAINT customers_pkey PRIMARY KEY (id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
   id uuid NOT NULL,
   created_at date NULL,
   customer_id uuid NULL,
   items jsonb NULL,
   updated_at date NULL,
   CONSTRAINT orders_pkey PRIMARY KEY (id),
   CONSTRAINT fk_customer_id_order FOREIGN KEY (customer_id) REFERENCES public.customers(id)
);
