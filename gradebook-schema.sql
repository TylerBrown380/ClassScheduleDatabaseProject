DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS grade CASCADE;
DROP TABLE IF EXISTS class CASCADE;
DROP TABLE IF EXISTS students_classes CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS items CASCADE;

CREATE TABLE student(
    student_id SERIAL PRIMARY KEY,
    name VARCHAR(500),
    email VARCHAR(500),
    username VARCHAR(500)
);

CREATE TABLE grade(
    grade_id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    grade_val FLOAT,

    FOREIGN KEY (student_id) REFERENCES student(student_id)
);
CREATE INDEX student_id_idx ON student(student_id);

CREATE TABLE class(
    class_id SERIAL PRIMARY KEY,
    description VARCHAR(500),
    course_num INTEGER,
    section_num INTEGER,
    term VARCHAR(500)
);

CREATE TABLE students_classes(
    class_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,

    FOREIGN KEY (class_id) REFERENCES class(class_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id),

    PRIMARY KEY (class_id, student_id)
);
CREATE INDEX class_id_idx ON class(class_id);

CREATE TABLE categories(
    categories_id SERIAL PRIMARY KEY,
    class_id INTEGER NOT NULL,
    weight FLOAT,
    name VARCHAR(500),

    FOREIGN KEY (class_id) REFERENCES class(class_id)
);
CREATE INDEX class_id_ix ON class(class_id);

CREATE TABLE items(
    item_id SERIAL PRIMARY KEY,
    categories_id INTEGER NOT NULL,
    description VARCHAR(500),
    point_val FLOAT,
    name VARCHAR(500),

    FOREIGN KEY (categories_id) REFERENCES categories(categories_id)
);
CREATE INDEX categories_id_idx ON categories(categories_id);


-- JUST TESTING THINGS HERE --

-- Testing add category
INSERT INTO categories(class_id, name, weight) VALUES('test1', 20);