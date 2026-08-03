-- ============================================================
-- Schema SQL untuk database db_laporgess
-- MySQL 5.x compatible
-- Jalankan script ini di MySQL sebelum menjalankan aplikasi
-- ============================================================

CREATE DATABASE IF NOT EXISTS db_laporgess
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_laporgess;

-- ============================================================
-- Tabel: pengguna
-- ============================================================
CREATE TABLE IF NOT EXISTS pengguna (
    Id              INT             NOT NULL AUTO_INCREMENT,
    username        VARCHAR(50),
    kata_sandi      VARCHAR(255),
    nama_lengkap    VARCHAR(100),
    email           VARCHAR(100),
    nik             VARCHAR(16)     UNIQUE,
    telepon         VARCHAR(15),
    alamat          TEXT,
    nomor_rumah     VARCHAR(20),
    rt_rw           VARCHAR(20),
    kecamatan       VARCHAR(50),
    Jenis_kelamin   VARCHAR(20),
    tanggal_lahir   DATE,
    foto_profil     VARCHAR(255),
    peran           VARCHAR(20)     DEFAULT 'WARGA',
    status          VARCHAR(20)     DEFAULT 'PENDING',
    poin            INT             DEFAULT 0,
    total_laporan   INT             DEFAULT 0,
    total_selesai   INT             DEFAULT 0,
    terakhir_masuk  DATETIME,
    keahlian        VARCHAR(100),
    dibuat_pada     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    diperbarui_pada DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Tabel: laporan
-- ============================================================
CREATE TABLE IF NOT EXISTS laporan (
    id                  INT             NOT NULL AUTO_INCREMENT,
    kode_laporan        VARCHAR(50),
    pengguna_id         INT             NOT NULL,
    petugas_id          INT,
    kategori            VARCHAR(50)     NOT NULL,
    judul               VARCHAR(150)    NOT NULL,
    deskripsi           TEXT,
    lokasi              VARCHAR(255),
    foto_url            VARCHAR(500),
    foto_bukti_url      VARCHAR(500),
    catatan             VARCHAR(500),
    catatan_tolak       VARCHAR(500),
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    dibuat_pada         DATETIME,
    diselesaikan_pada   DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_laporan_warga   FOREIGN KEY (pengguna_id) REFERENCES pengguna(Id),
    CONSTRAINT fk_laporan_petugas FOREIGN KEY (petugas_id)  REFERENCES pengguna(Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Tabel: notifikasi
-- ============================================================
CREATE TABLE IF NOT EXISTS notifikasi (
    id          INT             NOT NULL AUTO_INCREMENT,
    pengguna_id INT             NOT NULL,
    pesan       VARCHAR(255)    NOT NULL,
    tipe        VARCHAR(50)     NOT NULL,
    dibuat_pada DATETIME        NOT NULL,
    dibaca      TINYINT(1)      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_notifikasi_pengguna FOREIGN KEY (pengguna_id) REFERENCES pengguna(Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Tabel: jadwal_shift
-- ============================================================
CREATE TABLE IF NOT EXISTS jadwal_shift (
    id          INT         NOT NULL AUTO_INCREMENT,
    petugas_id  INT         NOT NULL,
    tanggal     DATE        NOT NULL,
    jam_mulai   TIME,
    jam_selesai TIME,
    zona        VARCHAR(50),
    keterangan  VARCHAR(100),
    jenis_shift VARCHAR(20) DEFAULT 'PAGI',
    PRIMARY KEY (id),
    CONSTRAINT fk_jadwal_petugas FOREIGN KEY (petugas_id) REFERENCES pengguna(Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Tabel: hadiah
-- ============================================================
CREATE TABLE IF NOT EXISTS hadiah (
    id          INT             NOT NULL AUTO_INCREMENT,
    nama        VARCHAR(255)    NOT NULL,
    deskripsi   TEXT,
    harga_poin  INT             NOT NULL,
    stok        INT             NOT NULL,
    foto_url    VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
