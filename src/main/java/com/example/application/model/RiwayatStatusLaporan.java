package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "riwayat_status_laporan")
public class RiwayatStatusLaporan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laporan_id")
    private Laporan laporan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_lama", length = 20)
    private Laporan.Status statusLama;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_baru", length = 20)
    private Laporan.Status statusBaru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diubah_oleh")
    private Pengguna diubahOleh;

    @Column(columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "datetime_diubah_pada")
    private LocalDateTime datetimeDiubahPada;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Laporan getLaporan() { return laporan; }
    public void setLaporan(Laporan laporan) { this.laporan = laporan; }
    public Laporan.Status getStatusLama() { return statusLama; }
    public void setStatusLama(Laporan.Status statusLama) { this.statusLama = statusLama; }
    public Laporan.Status getStatusBaru() { return statusBaru; }
    public void setStatusBaru(Laporan.Status statusBaru) { this.statusBaru = statusBaru; }
    public Pengguna getDiubahOleh() { return diubahOleh; }
    public void setDiubahOleh(Pengguna diubahOleh) { this.diubahOleh = diubahOleh; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public LocalDateTime getDatetimeDiubahPada() { return datetimeDiubahPada; }
    public void setDatetimeDiubahPada(LocalDateTime datetimeDiubahPada) { this.datetimeDiubahPada = datetimeDiubahPada; }
}
