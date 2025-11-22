-- Drop and recreate database
DROP DATABASE IF EXISTS javaportal;
CREATE DATABASE javaportal;
USE javaportal;

-- Create Employee table first
CREATE TABLE Employee (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email_id VARCHAR(255),
    password VARCHAR(255),
    contact BIGINT,
    address VARCHAR(255),
    cabin_details VARCHAR(255),
    responsibility VARCHAR(255),
    role VARCHAR(255),
    manager_id INT,
    team_id INT,
    first_login BOOLEAN DEFAULT TRUE
);

-- Create Team table
CREATE TABLE Team (
    team_id INT PRIMARY KEY AUTO_INCREMENT,
    team_name VARCHAR(255),
    description VARCHAR(300),
    team_size INT,
    manager_id INT,
    FOREIGN KEY (manager_id) REFERENCES Employee(emp_id)
);

-- Add foreign keys to Employee
ALTER TABLE Employee
    ADD CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES Employee(emp_id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_employee_team FOREIGN KEY (team_id) REFERENCES Team(team_id) ON DELETE SET NULL;

-- Non-technical event
CREATE TABLE Non_Technical_Event (
    non_technical_event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(255),
    location VARCHAR(255),
    start_time DATETIME,
    end_time DATETIME,
    description TEXT,
    status VARCHAR(50),
    type VARCHAR(50) DEFAULT 'NON_TECHNICAL',
    event_coordinator_id INT,
    FOREIGN KEY (event_coordinator_id) REFERENCES Employee(emp_id)
);

-- Training event
CREATE TABLE Training_Event (
    training_event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(255),
    location VARCHAR(255),
    start_time DATETIME,
    end_time DATETIME,
    description TEXT,
    status VARCHAR(50),
    type VARCHAR(50) DEFAULT 'TRAINING',
    anchor_id INT,
    FOREIGN KEY (anchor_id) REFERENCES Employee(emp_id)
);

-- Notification
CREATE TABLE Notification (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    manager_id INT,
    employee_id INT,
    time_stamp DATETIME,
    message VARCHAR(100),
    status VARCHAR(6),
    FOREIGN KEY (manager_id) REFERENCES Employee(emp_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(emp_id)
);

-- Area of Interest
CREATE TABLE employee_area_of_interest (
    employee_emp_id INT,
    interest VARCHAR(255),
    FOREIGN KEY (employee_emp_id) REFERENCES Employee(emp_id)
);

-- Expertise
CREATE TABLE employee_expertise (
    employee_emp_id INT,
    expertise VARCHAR(255),
    FOREIGN KEY (employee_emp_id) REFERENCES Employee(emp_id)
);

-- Thoughts
CREATE TABLE thoughts (
    thought_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT,
    emp_name VARCHAR(40),
    message VARCHAR(250),
    shared_on DATETIME,
    FOREIGN KEY (emp_id) REFERENCES Employee(emp_id)
);

-- Query table (escaped)
CREATE TABLE `query` (
    query_id INT PRIMARY KEY AUTO_INCREMENT,
    email_id VARCHAR(30),
    message VARCHAR(250),
    sent_on DATETIME,
    status VARCHAR(6)
);

-- Persistent logins
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL
);

-- Insert Employees
INSERT INTO Employee (emp_id, name, email_id, password, contact, address, cabin_details, responsibility, role, manager_id, team_id, first_login) VALUES
(1, 'Admin', 'admin@infy.com', '$2a$10$cAEvBcAXHJ//CNv8gQMjru0.rWH6jQq8wXyGfFTr7Qffw0/F86EPC', NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, FALSE),
(2, 'Ezhilan', 'vsezhilan@gmail.com', '$2a$10$rHMpJ23lEw7vcnLkigP.De6akI56m1aA7nNiKyFZWEGoFnZCErTDe', 9972919577, 'Chennai', 'GEC 2', 'To Design Frontend', 'MANAGER', NULL, NULL, FALSE),
(3, 'Soham', 'sohamthorat24@gmail.com', '$2a$10$G9Zx/NMKi4vFfdVyqA7nNOXglvo8tyY0uDnkYwApoukpmkexQweKW', 9999999999, 'Mumbai', 'GEC 2', 'To Design Backend', 'MANAGER', NULL, NULL, FALSE),
(4, 'Partha', 'akashpartha2003@gmail.com', '$2a$10$LiUz2MBl5lLTLPZAe4GU8.rl3rXSTE3rUHVfUigoD0Rvp8HKRh.1a', 8888888888, 'Cuttac', 'GEC 2', 'To Design Database', 'MANAGER', NULL, NULL, FALSE),
(6, 'Koushik', 'nyamathabadkoushik@gmail.com', '$2a$10$vI8P/QA25u4ivhfSw.BFeuGm/p0vK2GX4JIAMjLxn4UO7dA8EVlS2', 7777777777, 'Hyderabad', 'GEC 2', 'To create non-technical events', 'EVENT_COORDINATOR', NULL, NULL, FALSE),
(7, 'Ironman', 'koushiksai8564@gmail.com', '$2a$10$.26QN7gychzSXdg7QHlRSOYcEXmhPoEABfehyziXlb4cotvQZe6H2', 6666666666, 'Hyderabad', 'GEC 2', 'To work in teams', 'TEAM_MEMBER', NULL, NULL, FALSE),
(8, 'Batman', 'serialspammer333@gmail.com', '$2a$10$eD7lELNZcf4r1bDkwKK0cuiR.ww7SuZrZNymb4k7Bj6.0sYX5svT.', 9999988888, 'Mumbai', 'GEC 2', 'To work in teams', 'TEAM_MEMBER', NULL, NULL, FALSE),
(9, 'Superman', 'retinainsight.work@gmail.com', '$2a$10$ZbJm/bSWhlIrlaS.MuAlruNjHuMUc0u0cQxy08bXbuctK4ahoLeMG', 6666677777, 'Mumbai', 'GEC 2', 'To work in teams', 'TEAM_MEMBER', NULL, NULL, FALSE),
(10, 'Kavya', 'kavyasree3002@gmail.com', '$2a$10$qvkPwduuSJErwOYcVOjDCeZuM8O1j7Q216Q02bpX79y64Zu5.B5N2', 6666655555, 'Hyderabad', 'GEC 1', 'To create training events', 'OPERATIONS_ANCHOR', NULL, NULL, FALSE),
(11, 'Hulk', 'kavyasreems2025@gmail.com', '$2a$10$tj49.ehDKZA/E0PmBcYsDOFSGXev1UAm1G7UoFtOeZJ.bCwn3zzU2', 7777744444, 'Hyderabad', 'GEC 1', 'To create training events', 'TEAM_MEMBER', NULL, NULL, FALSE);

-- Insert Teams after managers exist
INSERT INTO Team (team_id, team_name, description, team_size, manager_id) VALUES
(1, 'Team A', 'A Team', 3, 2),
(2, 'Team B', 'B Team', 3, 3),
(3, 'Team C', 'C Team', 3, 4);

-- Update Employees to assign team and manager
UPDATE Employee SET manager_id = 2, team_id = 1 WHERE emp_id IN (7, 8);
UPDATE Employee SET manager_id = 3, team_id = 2 WHERE emp_id IN (9, 10);
UPDATE Employee SET manager_id = 4, team_id = 3 WHERE emp_id IN (6, 11);

-- Training Events
INSERT INTO Training_Event (training_event_id, event_name, location, start_time, end_time, description, status, type, anchor_id) VALUES
(1, 'Thursday Typescript', 'GEC 1', '2025-06-12 14:30:00', '2025-06-12 17:30:00', 'Basics of Typescript', 'UPCOMING', 'TRAINING', 10),
(2, 'Monday MongoDB', 'GEC 1', '2025-06-16 14:00:00', '2025-06-16 20:00:00', 'Basics of MongoDB', 'UPCOMING', 'TRAINING', 10),
(3, 'Tuesday Tables', 'GEC 1',
(3, 'Tuesday Tables', 'GEC 1', '2025-06-17 14:00:00', '2025-06-17 17:00:00', 'Basics of Database Tables', 'UPCOMING', 'TRAINING', 10);

-- Non-Technical Events
INSERT INTO Non_Technical_Event (non_technical_event_id, event_name, location, start_time, end_time, description, status, type, event_coordinator_id) VALUES
(1, 'Fun Friday', 'GEC 2', '2025-06-13 15:00:00', '2025-06-13 17:00:00', 'Team games and fun activities', 'UPCOMING', 'NON_TECHNICAL', 6),
(2, 'Quiz Buzz', 'GEC 2', '2025-06-20 14:00:00', '2025-06-20 16:00:00', 'General knowledge quiz competition', 'UPCOMING', 'NON_TECHNICAL', 6);

-- Thoughts
INSERT INTO thoughts (emp_id, emp_name, message, shared_on) VALUES
(2, 'Ezhilan', 'Leadership is not a position or title, it is action and example.', '2025-06-10 09:00:00'),
(3, 'Soham', 'Backend performance is key to a smooth user experience.', '2025-06-11 11:00:00'),
(6, 'Koushik', 'Organizing events builds collaboration and creativity.', '2025-06-12 14:00:00');

-- Queries
INSERT INTO `query` (email_id, message, sent_on, status) VALUES
('user1@example.com', 'How do I reset my password?', '2025-06-10 08:30:00', 'OPEN'),
('user2@example.com', 'Can I join multiple teams?', '2025-06-11 10:15:00', 'OPEN');

-- Notification
INSERT INTO Notification (manager_id, employee_id, time_stamp, message, status) VALUES
(2, 7, '2025-06-10 09:30:00', 'You have been added to Team A.', 'UNREAD'),
(3, 9, '2025-06-11 12:00:00', 'You have been added to Team B.', 'UNREAD');

-- Area of Interest
INSERT INTO employee_area_of_interest (employee_emp_id, interest) VALUES
(7, 'Frontend Development'),
(8, 'Backend APIs'),
(9, 'Database Design');

-- Expertise
INSERT INTO employee_expertise (employee_emp_id, expertise) VALUES
(2, 'Angular'),
(3, 'Java Spring Boot'),
(4, 'MySQL');

-- Persistent Logins (example values)
INSERT INTO persistent_logins (username, series, token, last_used) VALUES
('admin@infy.com', 'abc123series', 'tokenvalue123', NOW()),
('vsezhilan@gmail.com', 'xyz456series', 'tokenvalue456', NOW());