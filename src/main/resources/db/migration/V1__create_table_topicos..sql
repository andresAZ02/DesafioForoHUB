CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL UNIQUE,
    mensaje VARCHAR(255) NOT NULL UNIQUE,
    fecha_Creacion datetime not null,
    `status` TINYINT,
    autor VARCHAR(255) NOT NULL,
    curso VARCHAR(255) NOT NULL,
    respuestas INT UNSIGNED NOT NULL,
    PRIMARY KEY (id)
);