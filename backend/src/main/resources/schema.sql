-- Создание последовательностей, если не существует
create sequence if not exists persons_seq start with 1 increment by 1;
create sequence if not exists roles_seq start with 1 increment by 1;
create sequence if not exists tasks_seq start with 1 increment by 1;

create table if not exists persons (
    id bigint not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    userPic varchar(255),
    primary key (id)
);

create table if not exists roles (
    id bigint not null,
    name varchar(255),
    primary key (id)
);


create table if not exists persons_roles (
    person_id bigint not null,
    role_id bigint not null,
    primary key (person_id, role_id),
    foreign key (role_id) references roles,
    foreign key (person_id) references persons
);


create table if not exists tasks (
    state smallint check (state between 0 and 3),
    date_time_create timestamp(6),
    id bigint not null,
    person_id bigint,
    name varchar(255),
    primary key (id),
    foreign key (person_id) references persons
);

CREATE TABLE IF NOT EXISTS subtasks (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    is_complete BOOLEAN
);

CREATE TABLE IF NOT EXISTS tasks_subtasks (
    task_id BIGINT,
    subtask_id BIGINT,
    PRIMARY KEY (task_id, subtask_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (subtask_id) REFERENCES subtasks(id)
);

ALTER TABLE persons DROP CONSTRAINT IF EXISTS unique_email;

ALTER TABLE persons ADD CONSTRAINT unique_email UNIQUE (email);
