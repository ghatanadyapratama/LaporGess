package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "riwayat_poin")
public class RiwayatPoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @Column
    private Integer poin;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JenisRiwayat jenis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laporan_id")
    private Laporan laporan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hadiah_id")
    private Hadiah hadiah;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "timestamp_dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDibuatPada;

    public enum JenisRiwayat {
        DIBERIKAN, DITUKAR
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Pengguna getPengguna() { return pengguna; }
    public void setPengguna(Pengguna pengguna) { this.pengguna = pengguna; }
    public Integer getPoin() { return poin; }
    public void setPoin(Integer poin) { this.poin = poin; }
    public JenisRiwayat getJenis() { return jenis; }
    public void setJenis(JenisRiwayat jenis) { this.jenis = jenis; }
    public Laporan getLaporan() { return laporan; }
    public void setLaporan(Laporan laporan) { this.laporan = laporan; }
    public Hadiah getHadiah() { return hadiah; }
    public void setHadiah(Hadiah hadiah) { this.hadiah = hadiah; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public LocalDateTime getTimestampDibuatPada() { return timestampDibuatPada; }
}
