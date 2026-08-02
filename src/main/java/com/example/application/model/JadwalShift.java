package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "jadwal_shift")
public class JadwalShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petugas_id", nullable = false)
    private Pengguna petugas;

    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @Column(name = "jam_mulai")
    private LocalTime jamMulai;

    @Column(name = "jam_selesai")
    private LocalTime jamSelesai;

    @Column(length = 50)
    private String zona;

    @Column(length = 100)
    private String keterangan;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_shift", length = 20)
    private JenisShift jenisShift = JenisShift.PAGI;

    public enum JenisShift {
        PAGI, SIANG, MALAM
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Pengguna getPetugas() { return petugas; }
    public void setPetugas(Pengguna petugas) { this.petugas = petugas; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public LocalTime getJamMulai() { return jamMulai; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }

    public LocalTime getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(LocalTime jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public JenisShift getJenisShift() { return jenisShift; }
    public void setJenisShift(JenisShift jenisShift) { this.jenisShift = jenisShift; }
}
