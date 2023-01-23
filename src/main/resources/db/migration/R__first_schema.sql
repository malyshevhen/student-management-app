CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(255) NOT NULL UNIQUE,
    course_description VARCHAR(255) NOT NULL
);
CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INT REFERENCES groups (group_id),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
);
CREATE TABLE students_courses
(
    student_id INT REFERENCES students (student_id),
    course_id  INT REFERENCES courses (course_id),
    PRIMARY KEY (student_id, course_id)
);
