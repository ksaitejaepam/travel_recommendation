-- Script to insert two users into the users table only if they don't already exist

-- Insert the user with role 'USER'
INSERT INTO users (id, first_name, last_name, email, password, gender, country, city, role)
SELECT 1, 'John', 'Doe', 'user@epam.com', '$2a$12$0dUqsdZ6XcOJQqTduGo7QuSiE.8dgUsjqs71fCJMzeIbXU5HXw63q', 'Male', 'USA', 'New York', 'User'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'user@epam.com'
);

-- Insert the user with role 'ADMIN'
INSERT INTO users (id, first_name, last_name, email, password, gender, country, city, role)
SELECT 3, 'Jane', 'Smith', 'admin@epam.com', '$2a$12$hIArTwcUrcLf5LcdMO1buuydrd1lslCk1U6A.gIOc7zjhmrKvmedy', 'Female', 'USA', 'Los Angeles', 'Admin'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@epam.com'
);
