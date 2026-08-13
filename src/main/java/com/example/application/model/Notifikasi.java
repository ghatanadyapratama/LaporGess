package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifikasi")
public class Notifikasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id", nullable = false)
    private Pengguna pengguna;

    @Column(nullable = false)
    private String pesan;

    @Column(nullable = false)
    private String tipe; // INFO, SUCCESS, WARNING

    @Column(name = "dibuat_pada", nullable = false)
    private LocalDateTime dibuatPada;

    @Column(name = "laporan_id")
    private Integer laporanId;

    @Column(nullable = false)
    private boolean dibaca;

    public Notifikasi() {
    }

    public Notifikasi(Pengguna pengguna, String pesan, String tipe, LocalDateTime dibuatPada, boolean dibaca, Integer laporanId) {
        this.pengguna = pengguna;
        this.pesan = pesan;
        this.tipe = tipe;
        this.dibuatPada = dibuatPada;
        this.dibaca = dibaca;
        this.laporanId = laporanId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public LocalDateTime getDibuatPada() {
        return dibuatPada;
    }

    public void setDibuatPada(LocalDateTime dibuatPada) {
        this.dibuatPada = dibuatPada;
    }

    public boolean isDibaca() {
        return dibaca;
    }

    public void setDibaca(boolean dibaca) {
        this.dibaca = dibaca;
    }

    public Integer getLaporanId() {
        return laporanId;
    }

    public void setLaporanId(Integer laporanId) {
        this.laporanId = laporanId;
    }
}
