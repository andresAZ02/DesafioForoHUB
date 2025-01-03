CREATE TABLE usuarios (
    id bigint not null auto_increment,
    login varchar(100) not null UNIQUE,
    contrasena varchar(300) not null,
    primary key(id)

);

