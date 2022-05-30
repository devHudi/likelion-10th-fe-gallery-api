DROP TABLE IF EXISTS IMAGE;

DROP TABLE IF EXISTS COMMENT;

CREATE TABLE IMAGE
(
    id          bigint       not null auto_increment,
    title       varchar(255) not null,
    description varchar(255) not null,
    image_url   text         not null,
    created_at  datetime     not null,
    primary key (id)
);

CREATE TABLE COMMENT
(
    id         bigint       not null auto_increment,
    author     varchar(255) not null,
    body       varchar(255) not null,
    created_at datetime     not null,
    primary key (id)
);
