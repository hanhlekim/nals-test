create table status
(
    id          int auto_increment PRIMARY KEY,
    description varchar(250) not null
);


create table work
(
    id         bigint auto_increment PRIMARY KEY,
    work_name  varchar(500) not null,
    start_date date         null,
    end_date   date         null,
    status     int          not null
);


