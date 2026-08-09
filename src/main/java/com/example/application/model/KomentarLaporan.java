package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "komentar_laporan")
public class KomentarLaporan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laporan_id")
    private Laporan laporan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @Column(columnDefinition = "TEXT")
    private String komentar;

    @Column(name = "timestamp_dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDibuatPada;

    @Column(name = "timestamp_diperbarui_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDiperbaruiPada;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Laporan getLaporan() { return laporan; }
    public void setLaporan(Laporan laporan) { this.laporan = laporan; }
    public Pengguna getPengguna() { return pengguna; }
    public void setPengguna(Pengguna pengguna) { this.pengguna = pengguna; }
    public String getKomentar() { return komentar; }
    public void setKomentar(String komentar) { this.komentar = komentar; }
    public LocalDateTime getTimestampDibuatPada() { return timestampDibuatPada; }
    public LocalDateTime getTimestampDiperbaruiPada() { return timestampDiperbaruiPada; }
}
