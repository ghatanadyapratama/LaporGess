package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(name = "kata_sandi", length = 255)
    private String kataSandi;

    @Column(name = "nama_lengkap", length = 100)
    private String namaLengkap;

    @Column(length = 100)
    private String email;

    @Column(length = 16, unique = true)
    private String nik;

    @Column(length = 15)
    private String telepon;

    @Column(columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "nomor_rumah", length = 20)
    private String nomorRumah;

    @Column(name = "rt_rw", length = 20)
    private String rtRw;

    @Column(length = 50)
    private String kecamatan;

    @Convert(converter = JenisKelaminConverter.class)
    @Column(name = "Jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "foto_profil", length = 255)
    private String fotoProfil;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Peran peran = Peran.WARGA;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.PENDING;

    @Column
    private Integer poin = 0;

    @Column(name = "total_laporan")
    private Integer totalLaporan = 0;

    @Column(name = "total_selesai")
    private Integer totalSelesai = 0;

    @Column(name = "terakhir_masuk")
    private LocalDateTime terakhirMasuk;

    @Column(name = "dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime dibuatPada;

    @Column(name = "diperbarui_pada", insertable = false, updatable = false)
    private LocalDateTime diperbaruiPada;

    // Enums
    public enum JenisKelamin {
        LAKI_LAKI,
        PEREMPUAN
    }

    @Converter
    public static class JenisKelaminConverter implements AttributeConverter<JenisKelamin, String> {
        @Override
        public String convertToDatabaseColumn(JenisKelamin attribute) {
            if (attribute == null) return null;
            return attribute == JenisKelamin.LAKI_LAKI ? "LAKI-LAKI" : "PEREMPUAN";
        }

        @Override
        public JenisKelamin convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return "LAKI-LAKI".equalsIgnoreCase(dbData) ? JenisKelamin.LAKI_LAKI : JenisKelamin.PEREMPUAN;
        }
    }

    public enum Peran {
        ADMIN, PETUGAS_LAPANGAN, WARGA
    }

    public enum Status {
        PENDING, AKTIF, NONAKTIF, DIBLOKIR
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getKataSandi() { return kataSandi; }
    public void setKataSandi(String kataSandi) { this.kataSandi = kataSandi; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNomorRumah() { return nomorRumah; }
    public void setNomorRumah(String nomorRumah) { this.nomorRumah = nomorRumah; }

    public String getRtRw() { return rtRw; }
    public void setRtRw(String rtRw) { this.rtRw = rtRw; }

    public String getKecamatan() { return kecamatan; }
    public void setKecamatan(String kecamatan) { this.kecamatan = kecamatan; }

    public JenisKelamin getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(JenisKelamin jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getFotoProfil() { return fotoProfil; }
    public void setFotoProfil(String fotoProfil) { this.fotoProfil = fotoProfil; }

    public Peran getPeran() { return peran; }
    public void setPeran(Peran peran) { this.peran = peran; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Integer getPoin() { return poin; }
    public void setPoin(Integer poin) { this.poin = poin; }

    public Integer getTotalLaporan() { return totalLaporan; }
    public void setTotalLaporan(Integer totalLaporan) { this.totalLaporan = totalLaporan; }

    public Integer getTotalSelesai() { return totalSelesai; }
    public void setTotalSelesai(Integer totalSelesai) { this.totalSelesai = totalSelesai; }

    public LocalDateTime getTerakhirMasuk() { return terakhirMasuk; }
    public void setTerakhirMasuk(LocalDateTime terakhirMasuk) { this.terakhirMasuk = terakhirMasuk; }

    public LocalDateTime getDibuatPada() { return dibuatPada; }
    public LocalDateTime getDiperbaruiPada() { return diperbaruiPada; }
}
