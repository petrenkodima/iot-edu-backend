CREATE TABLE course_task
(
    id         BIGSERIAL           NOT NULL,
    tasks_id   int8                NOT NULL,
    courses_id int8                NOT NULL,
    is_show    bool DEFAULT 'true' NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE courses
(
    id          BIGSERIAL    NOT NULL,
    name        varchar(255) NOT NULL,
    description varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE group_course
(
    id         BIGSERIAL           NOT NULL,
    groups_id  int8                NOT NULL,
    courses_id int8                NOT NULL,
    is_show    bool DEFAULT 'true' NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT group_course_constraint
        UNIQUE (groups_id, courses_id)
);

CREATE TABLE groups
(
    id   BIGSERIAL    NOT NULL,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE stands
(
    id          BIGSERIAL    NOT NULL,
    name        varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    port_name   varchar(10)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE student_group
(
    id         BIGSERIAL NOT NULL,
    student_id int8      NOT NULL,
    groups_id  int8      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT student_group_constraint
        UNIQUE (student_id, groups_id)
);

CREATE TABLE students
(
    id       BIGSERIAL NOT NULL,
    users_id int8      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE students_tasks
(
    id          BIGSERIAL    NOT NULL,
    grade       int4,
    tasks_id    int8         NOT NULL,
    students_id int8         NOT NULL,
    teachers_id int8,
    file_path   varchar(255) NOT NULL,
    created_at  timestamp    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE task_group
(
    id              BIGSERIAL NOT NULL,
    expiration_date timestamp NOT NULL,
    tasks_id        int8      NOT NULL,
    groups_id       int8      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tasks
(
    id          BIGSERIAL    NOT NULL,
    name        varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    stands_id   int8         NOT NULL,
    duration    int8         NOT NULL DEFAULT 10,
    PRIMARY KEY (id)
);

CREATE TABLE teacher_course
(
    id         BIGSERIAL NOT NULL,
    teacher_id int8      NOT NULL,
    courses_id int8      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE teachers
(
    id       BIGSERIAL NOT NULL,
    users_id int8      NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE teacher_course
    ADD CONSTRAINT fk_teacher_course_courses FOREIGN KEY (courses_id) REFERENCES courses (id);
ALTER TABLE group_course
    ADD CONSTRAINT fk_group_course_groups FOREIGN KEY (groups_id) REFERENCES groups (id);
ALTER TABLE group_course
    ADD CONSTRAINT fk_group_course_courses FOREIGN KEY (courses_id) REFERENCES courses (id);
ALTER TABLE course_task
    ADD CONSTRAINT fk_course_task_tasks FOREIGN KEY (tasks_id) REFERENCES tasks (id);
ALTER TABLE course_task
    ADD CONSTRAINT fk_course_task_courses FOREIGN KEY (courses_id) REFERENCES courses (id);
ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_stands FOREIGN KEY (stands_id) REFERENCES stands (id);
ALTER TABLE student_group
    ADD CONSTRAINT fk_student_group_groups FOREIGN KEY (groups_id) REFERENCES groups (id);
ALTER TABLE student_group
    ADD CONSTRAINT fk_student_group_students FOREIGN KEY (student_id) REFERENCES students (id);
ALTER TABLE students
    ADD CONSTRAINT fk_students_users FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE teachers
    ADD CONSTRAINT fk_teachers_users FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE teacher_course
    ADD CONSTRAINT fk_teacher_course_teachers FOREIGN KEY (teacher_id) REFERENCES teachers (id);
ALTER TABLE students_tasks
    ADD CONSTRAINT fk_students_tasks_tasks FOREIGN KEY (tasks_id) REFERENCES tasks (id);
ALTER TABLE students_tasks
    ADD CONSTRAINT fk_students_tasks_students FOREIGN KEY (students_id) REFERENCES students (id);
ALTER TABLE students_tasks
    ADD CONSTRAINT fk_students_tasks_teachers FOREIGN KEY (teachers_id) REFERENCES teachers (id);
ALTER TABLE task_group
    ADD CONSTRAINT fk_task_group_tasks FOREIGN KEY (tasks_id) REFERENCES tasks (id);
ALTER TABLE task_group
    ADD CONSTRAINT fk_task_group_groups FOREIGN KEY (groups_id) REFERENCES groups (id);
ALTER TABLE courses
    ADD CONSTRAINT courses_name_unique UNIQUE (name);
ALTER TABLE stands
    ADD CONSTRAINT stands_name_unique UNIQUE (name);
ALTER TABLE task_group
    ADD CONSTRAINT task_group_expiration_date_check CHECK (expiration_date > NOW());
ALTER TABLE students_tasks
    ADD CONSTRAINT students_tasks_grade_check CHECK (grade BETWEEN 0 AND 100);
ALTER TABLE task_group
    ADD CONSTRAINT task_group_unique_key UNIQUE (tasks_id, groups_id, expiration_date);