create table User
(
    id           bigint auto_increment
        primary key,
    user_id      varchar(20) not null,
    password     varchar(45) not null,
    nickname     varchar(12) not null,
    age          int         null,
    phone_number int         null,
    role         varchar(15) not null,
    status       varchar(15) not null,
    create_time  datetime    not null,
    update_time  datetime    null,
    constraint User_uq_nickname
        unique (nickname),
    constraint User_uq_phone_number
        unique (phone_number),
    constraint User_uq_user_id
        unique (user_id)
);