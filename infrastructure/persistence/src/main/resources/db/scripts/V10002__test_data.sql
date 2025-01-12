-- Variables for user IDs
SET @user1_id = UUID_TO_BIN('11111111-1111-1111-1111-111111111111');
SET @user2_id = UUID_TO_BIN('22222222-2222-2222-2222-222222222222');

-- Get existing company IDs
SET @company1_id = UUID_TO_BIN('00000000-0000-0000-0000-000000000012'); -- 네이버
SET @company2_id = UUID_TO_BIN('00000000-0000-0000-0000-000000000008'); -- 카카오엔터프라이즈

-- Get existing location IDs for 강남구, 서초구
SET @location1_id = UUID_TO_BIN('0191cd0e-a061-7213-b39e-51274c1e7c83'); -- 강남구
SET @location2_id = UUID_TO_BIN('0191cd0e-a061-7213-b39e-51274c1e7c84'); -- 서초구

-- Insert test user profiles
INSERT INTO user_profiles (id, gender, birth_year, company, job_occupation)
VALUES (@user1_id, 'FEMALE', 1995, @company1_id, 'IT_INFORMATION'),
       (@user2_id, 'MALE', 1993, @company2_id, 'SALES_MARKETING');

-- Insert users
INSERT INTO users (id, name, phone_number, profile_id, desired_partner_id, connection_status)
VALUES (@user1_id, '테스트유저1', '01000000001', @user1_id, @user1_id, 'ACTIVE'),
       (@user2_id, '테스트유저2', '01000000002', @user2_id, @user2_id, 'ACTIVE');

-- Insert user profile locations
INSERT INTO user_profile_locations (user_profile_id, locations_id)
VALUES (@user1_id, @location1_id),
       (@user2_id, @location2_id);

-- Insert user desired partners
INSERT INTO user_desired_partners (id, birth_year_range_start, birth_year_range_end, prefer_distance, allow_same_company)
VALUES (@user1_id, 1990, 1997, 'INCLUDE_SURROUNDING_REGIONS', false),
       (@user2_id, 1993, 1998, 'ONLY_MY_AREA', false);

-- Insert user desired partner job occupations
INSERT INTO user_desired_partner_job_occupations (user_desired_partner_id, job_occupations)
VALUES (@user1_id, 'SALES_MARKETING'),
       (@user1_id, 'FINANCE_ACCOUNTING'),
       (@user2_id, 'IT_INFORMATION'),
       (@user2_id, 'RESEARCH_DEVELOPMENT');

-- Create a chat channel between the two users
SET @channel_id = UUID_TO_BIN('33333333-3333-3333-3333-333333333333');
SET @connection_id = UUID_TO_BIN('44444444-4444-4444-4444-444444444444');

INSERT INTO channels (id, connection_id, created_at)
VALUES (@channel_id, @connection_id, NOW());

-- Add users to the chat channel
INSERT INTO chat_members (id, channel_id, joined_at)
VALUES (@user1_id, @channel_id, NOW()),
       (@user2_id, @channel_id, NOW());
