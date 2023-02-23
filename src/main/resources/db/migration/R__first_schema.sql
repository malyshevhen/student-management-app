CREATE TABLE groups
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE courses
(
    id          BIGSERIAL PRIMARY KEY,
    name        TEXT NOT NULL UNIQUE,
    description TEXT NOT NULL
);
CREATE TABLE students
(
    id BIGSERIAL PRIMARY KEY,
    group_id   BIGINT REFERENCES groups (id),
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL
);
CREATE TABLE students_courses
(
    student_id BIGINT REFERENCES students (id),
    course_id  BIGINT REFERENCES courses (id),
    PRIMARY KEY (student_id, course_id)
);
