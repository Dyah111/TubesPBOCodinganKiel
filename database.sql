CREATE TABLE dokters (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    spesialis VARCHAR(100) NOT NULL,
    kapasitas INT(11) NOT NULL
);


CREATE TABLE jadwal_dokter (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dokter_id VARCHAR(50) NOT NULL,
    jadwal VARCHAR(100) NOT NULL,
    jadwal_mulai INT(11),
    jadwal_selesai INT(11),
    FOREIGN KEY (dokter_id) REFERENCES dokters(id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(10) NOT NULL
);

create table reservations (
reservation_id int auto_increment primary key,
dokter_id varchar(50) not null,
pasien_id INT(11),
status varchar(100),
nomor_antrian INT,
foreign key (dokter_id) references dokters(id)
foreign key (pasien_id) references users(id)
) engine=InnoDB;