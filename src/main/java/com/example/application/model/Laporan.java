package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "laporan")
public class Laporan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "kode_laporan", length = 50)
    private String kodeLaporan;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id", nullable = false)
    private Pengguna warga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petugas_id", nullable = true)
    private Pengguna petugas;

    @Column(length = 50, nullable = false)
    private String kategori;

    @Column(length = 150, nullable = false)
    private String judul;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(length = 255)
    private String lokasi;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "foto_bukti_url", length = 500)
    private String fotoBuktiUrl;

    @Column(length = 500)
    private String catatan;

    @Column(name = "catatan_tolak", length = 500)
    private String catatanTolak;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "dibuat_pada")
    private LocalDateTime dibuatPada = LocalDateTime.now();

    @Column(name = "diselesaikan_pada")
    private LocalDateTime diselesaikanPada;

    public enum Status {
        PENDING,    // Baru dikirim, menunggu admin
        DIPROSES,   // Admin sudah assign ke petugas
        SELESAI,    // Petugas sudah selesaikan
        DITOLAK     // Admin menolak laporan
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getKodeLaporan() { return kodeLaporan; }
    public void setKodeLaporan(String kodeLaporan) { this.kodeLaporan = kodeLaporan; }


    public Pengguna getWarga() { return warga; }
    public void setWarga(Pengguna warga) { this.warga = warga; }

    public Pengguna getPetugas() { return petugas; }
    public void setPetugas(Pengguna petugas) { this.petugas = petugas; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getFotoBuktiUrl() { return fotoBuktiUrl; }
    public void setFotoBuktiUrl(String fotoBuktiUrl) { this.fotoBuktiUrl = fotoBuktiUrl; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public String getCatatanTolak() { return catatanTolak; }
    public void setCatatanTolak(String catatanTolak) { this.catatanTolak = catatanTolak; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getDibuatPada() { return dibuatPada; }
    public void setDibuatPada(LocalDateTime dibuatPada) { this.dibuatPada = dibuatPada; }

    public LocalDateTime getDiselesaikanPada() { return diselesaikanPada; }
    public void setDiselesaikanPada(LocalDateTime diselesaikanPada) { this.diselesaikanPada = diselesaikanPada; }
}
