create table comment
(
    id          bigint auto_increment
        primary key,
    blog_id     bigint       not null,
    create_user varchar(20)  not null,
    user_avatar varchar(300) null,
    create_time datetime     not null,
    content     longtext     not null,
    reply       longtext     null
);

create index comment_blog_id_index
    on comment (blog_id);

create table m_blog
(
    id          bigint auto_increment
        primary key,
    user_id     bigint            not null,
    title       varchar(255)      not null,
    description varchar(255)      not null,
    content     longtext          null,
    created     datetime          not null on update CURRENT_TIMESTAMP,
    status      tinyint default 1 not null
);

create index m_blog_id_index
    on m_blog (id);

create table m_user
(
    id         bigint auto_increment
        primary key,
    username   varchar(64)          null,
    avatar     varchar(255)         null,
    email      varchar(64)          null,
    password   varchar(64)          null,
    status     int                  not null,
    created    datetime             null,
    last_login datetime             null,
    is_admin   tinyint(1) default 0 not null
)
    charset = utf8;

create index UK_USERNAME
    on m_user (username);

