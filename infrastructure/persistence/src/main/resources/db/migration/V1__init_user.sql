-- Create locations table
CREATE TABLE locations
(
    id         BINARY(16) PRIMARY KEY,
    region     VARCHAR(255) NOT NULL,
    sub_region VARCHAR(255) NOT NULL
);

-- Create user_desired_partners table
CREATE TABLE user_desired_partners
(
    id                     BINARY(16) PRIMARY KEY,
    birth_year_range_start INT          NOT NULL,
    birth_year_range_end   INT          NOT NULL,
    job                    VARCHAR(255) NOT NULL,
    prefer_distance        VARCHAR(50)  NOT NULL
);

-- Create users table
CREATE TABLE users
(
    id   BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create user_locations table
CREATE TABLE user_locations
(
    user_id     BINARY(16),
    location_id BINARY(16),
    PRIMARY KEY (user_id, location_id)
);

-- Create user_profiles table
CREATE TABLE user_profiles
(
    id         BINARY(16) PRIMARY KEY,
    gender     VARCHAR(50)  NOT NULL,
    birth_year INT          NOT NULL,
    company    VARCHAR(255) NOT NULL,
    job        VARCHAR(255) NOT NULL
);
