DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;
CREATE TABLE students (
    student_id SERIAL,
    group_id INT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (student_id)
);
CREATE TABLE groups (
    group_id SERIAL,
    group_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (group_id)
);
CREATE TABLE courses (
    course_id SERIAL,
    course_name VARCHAR(255) NOT NULL,
    course_description VARCHAR(255) NOT NULL,
    PRIMARY KEY (course_id)
);
CREATE TABLE students_courses (
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);
