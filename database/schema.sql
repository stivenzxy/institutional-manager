CREATE TABLE IF NOT EXISTS personas (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nombres VARCHAR(100) NOT NULL,
        apellidos VARCHAR(100) NOT NULL,
        email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS facultades (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        decano_id INT NOT NULL,
        FOREIGN KEY (decano_id) REFERENCES personas(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS programas (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        duracion INT NOT NULL,
        registro VARCHAR(50) NOT NULL,
        facultad_id INT NOT NULL,
        FOREIGN KEY (facultad_id) REFERENCES facultades(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS profesores (
        id INT PRIMARY KEY,
        tipoContrato VARCHAR(100) NOT NULL,
        FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estudiantes (
        id INT PRIMARY KEY ,
        codigo BIGINT NOT NULL UNIQUE,
        activo BOOLEAN NOT NULL,
        promedio DECIMAL(3,2) NOT NULL,
        programa_id INT NOT NULL,
        FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE,
        FOREIGN KEY (programa_id) REFERENCES programas(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS cursos (
        id INT AUTO_INCREMENT PRIMARY KEY,
        codigo BIGINT NOT NULL UNIQUE,
        nombre VARCHAR(100) NOT NULL,
        activo BOOLEAN NOT NULL,
        programa_id INT NOT NULL,
        FOREIGN KEY (programa_id) REFERENCES programas(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS inscripciones (
        id INT AUTO_INCREMENT PRIMARY KEY,
        curso_id INT NOT NULL,
        estudiante_id INT NOT NULL,
        anio INT NOT NULL,
        periodo INT NOT NULL,
        FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE RESTRICT,
        FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS curso_profesor (
        curso_id INT NOT NULL,
        profesor_id INT NOT NULL,
        anio INT NOT NULL,
        semestre INT NOT NULL,
        PRIMARY KEY (curso_id, profesor_id),
        FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
        FOREIGN KEY (profesor_id) REFERENCES profesores(id) ON DELETE CASCADE
);

MERGE INTO personas (nombres, apellidos, email) KEY(email) VALUES
        ('Elvis', 'Perez', 'elvis@unillanos.edu.co'),
        ('Roger', 'Calderon', 'rcalderon@unillanos.edu.co'),
        ('Jesus', 'Perez', 'jesus.perez.torres@unillanos.edu.co');

MERGE INTO facultades (id, nombre, decano_id) KEY(id) VALUES
        (1, 'FCBI', 1),
        (2, 'Ciencias de la Salud', 2);

MERGE INTO programas (id, nombre, duracion, registro, facultad_id) KEY(id) VALUES
        (1, 'Ingeniería de Sistemas', 10, 'REG-FCBI-002', 1),
        (2, 'Enfermería', 9, 'REG-SALUD-001', 2);

MERGE INTO profesores (id, tipoContrato) KEY(id) VALUES (2, 'Catedrático');

MERGE INTO estudiantes (id, codigo, activo, promedio, programa_id) KEY(id) VALUES
        (3, 160004725, TRUE, 3.6, 1);