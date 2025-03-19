CREATE TABLE public.resources (
                                  id serial4 NOT NULL,
                                  "name" varchar(150) NOT NULL,
                                  description text NULL,
                                  CONSTRAINT resources_name_key UNIQUE (name),
                                  CONSTRAINT resources_pkey PRIMARY KEY (id)
);

CREATE INDEX idx_resources_name ON public.resources ("name");

CREATE TABLE public.users (
                              id serial4 NOT NULL,
                              "name" varchar(100) NOT NULL,
                              surname varchar(100) NULL,
                              phone varchar(20) NULL,
                              email varchar(100) NULL,
                              login varchar(20) NOT NULL,
                              "password" text NULL,
                              "role" varchar(20) NOT NULL,
                              CONSTRAINT users_login_key UNIQUE (login),
                              CONSTRAINT users_phone_email_key UNIQUE (phone, email),
                              CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE INDEX idx_users_login ON public.users (login);


CREATE TABLE public.users_resources (
                                        id serial4 NOT NULL,
                                        resource_id int4 NULL,
                                        user_id int4 NULL,
                                        duration_type varchar(10) NOT NULL,
                                        duration int4 NOT NULL,
                                        date_start timestamp NOT NULL,
                                        date_end timestamp NOT NULL,
                                        CONSTRAINT users_resources_pkey PRIMARY KEY (id),
                                        CONSTRAINT users_resources_resource_id_fkey FOREIGN KEY (resource_id) REFERENCES public.resources(id) ON DELETE CASCADE,
                                        CONSTRAINT users_resources_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX idx_date_start ON public.users_resources (date_start);
CREATE INDEX idx_date_end ON public.users_resources (date_end);

CREATE INDEX idx_resource_id ON public.users_resources (resource_id);
CREATE INDEX idx_user_id ON public.users_resources (user_id);