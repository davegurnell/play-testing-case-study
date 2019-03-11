-- !Ups

create table accounts (
    id bigint(20) not null auto_increment,
    balance real not null,
    primary key (id)
);

create table passwords (
    account_id bigint(20) not null auto_increment,
    password varchar(255) not null,
    primary key (account_id)
);

-- !Downs

drop table accounts;
drop table passwords;
