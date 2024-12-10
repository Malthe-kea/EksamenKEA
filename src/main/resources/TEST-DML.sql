
-- Indsæt data i Employee-tabellen (2 projektledere og 2 arbejdere)
INSERT INTO employee (email, password, role, employee_rate, max_hours)
VALUES ('ama', 'Amalie123', 'PROJECTLEADER', 50, 40),   -- Projektleder 1
       ('zuzu', 'Zuhur1234', 'PROJECTLEADER', 20, 35), -- Projektleder 2
       ('worker', 'Worker123', 'WORKER', 25, 30),       -- Arbejder 1
       ('worker2@example.com', 'Worker123', 'WORKER', 30, 25); -- Arbejder 2

-- Indsæt data i Project-tabellen
INSERT INTO project (project_name, budget, project_description, employee_id, is_archived)
VALUES ('Project Alpha', 100000, 'Alpha description', 1, FALSE), -- Aktivt projekt
       ('Project Beta', 50000, 'Beta description', 2, FALSE),    -- Aktivt projekt
       ('Project Gamma', 75000, 'Gamma description', 1, FALSE),
       ('Project Delta', 60000, 'Delta description', 2, TRUE);   -- Arkiveret projekt

-- Indsæt data i Subproject-tabellen
INSERT INTO subproject (subproject_name, subproject_description, project_id, is_archived)
VALUES ('Alpha Subproject A', 'Alpha A desc', 1, FALSE),
       ('Alpha Subproject B', 'Alpha B desc', 1, FALSE),
       ('Beta Subproject A', 'Beta A desc', 2, FALSE),
       ('Zuzu Subproject A', 'Description for Zuzu Subproject A', 3, FALSE),
       ('Zuzu Subproject B', 'Description for Zuzu Subproject B', 3, FALSE);

-- Indsæt data i Task-tabellen
INSERT INTO task (task_name, start_date, end_date, status, employee_id, actual_hours, estimated_hours, subproject_id, is_archived)
VALUES ('Task 1', '2024-11-01', '2024-11-05', 'INPROGRESS', 3, 5, 10, 1, FALSE),   -- Tildeler medarbejder med ID 3
       ('Task 2', '2024-11-02', '2024-11-06', 'NOTSTARTED', 4, 10, 20, 2, FALSE),   -- Tildeler medarbejder med ID 4
       ('Task 3', '2024-11-03', '2024-11-07', 'COMPLETE', NULL, 15, 15, 3, FALSE), -- Ingen medarbejder tildelt
       ('Task A2', '2024-11-06', '2024-11-10', 'NOTSTARTED', 4, 100, 15, 4, FALSE),
       ('Task B1', '2024-11-11', '2024-11-15', 'COMPLETE', NULL, 10, 20, 5, FALSE),
       ('Task B2', '2024-11-16', '2024-11-20', 'OVERDUE', NULL, 100, 12, 5, FALSE);