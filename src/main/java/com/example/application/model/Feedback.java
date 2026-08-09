package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laporan_id")
    private Laporan laporan;

    @Column
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String ulasan;

    @Column(name = "timestamp_dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDibuatPada;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Pengguna getPengguna() { return pengguna; }
    public void setPengguna(Pengguna pengguna) { this.pengguna = pengguna; }
    public Laporan getLaporan() { return laporan; }
    public void setLaporan(Laporan laporan) { this.laporan = laporan; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getUlasan() { return ulasan; }
    public void setUlasan(String ulasan) { this.ulasan = ulasan; }
    public LocalDateTime getTimestampDibuatPada() { return timestampDibuatPada; }
}
