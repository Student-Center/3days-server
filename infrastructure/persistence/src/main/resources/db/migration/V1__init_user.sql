CREATE TABLE companies
(
    id   BINARY(16)   NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_companies PRIMARY KEY (id)
);

CREATE TABLE jobs
(
    id         BINARY(16)   NOT NULL,
    occupation VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_jobs PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id           BINARY(16)   NOT NULL,
    region       VARCHAR(255) NOT NULL,
    sub_region   VARCHAR(255) NOT NULL,
    user_profile BINARY(16)   NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE user_desired_partner_job_occupations
(
    user_desired_partner_id BINARY(16)   NOT NULL,
    job_occupations         VARCHAR(255) NULL
);

CREATE TABLE user_desired_partners
(
    id                     BINARY(16)   NOT NULL,
    birth_year_range_start INT,
    birth_year_range_end   INT,
    prefer_distance        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_desired_partners PRIMARY KEY (id)
);

CREATE TABLE user_profiles
(
    id             BINARY(16)   NOT NULL,
    gender         VARCHAR(255) NULL,
    birth_year     INT          NULL,
    company        BINARY(16)   NULL,
    job_occupation VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_profiles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                 BINARY(16)   NOT NULL,
    name               VARCHAR(255) NOT NULL,
    phone_number       VARCHAR(255) NOT NULL,
    profile_id         BINARY(16)   NULL,
    desired_partner_id BINARY(16)   NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_phone_number UNIQUE (phone_number)
);
