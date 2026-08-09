package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hadiah_id")
    private Hadiah hadiah;

    @Column(name = "poin_dihabiskan")
    private Integer poinDihabiskan;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusTransaksi status;

    @Column(name = "alasan_penolakan", columnDefinition = "TEXT")
    private String alasanPenolakan;

    @Column(name = "disetujui_oleh", length = 50)
    private String disetujuiOleh;

    @Column(name = "kode_ambil", length = 50)
    private String kodeAmbil;

    @Column(name = "datetime_diambil_pada")
    private LocalDateTime datetimeDiambilPada;

    @Column(name = "datetime_dibuat_pada")
    private LocalDateTime datetimeDibuatPada;

    @Column(name = "timestamp_dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDibuatPada;

    @Column(name = "timestamp_diperbarui_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDiperbaruiPada;

    public enum StatusTransaksi {
        PENDING, DISETUJUI, DITOLAK, DIAMBIL
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Pengguna getPengguna() { return pengguna; }
    public void setPengguna(Pengguna pengguna) { this.pengguna = pengguna; }
    public Hadiah getHadiah() { return hadiah; }
    public void setHadiah(Hadiah hadiah) { this.hadiah = hadiah; }
    public Integer getPoinDihabiskan() { return poinDihabiskan; }
    public void setPoinDihabiskan(Integer poinDihabiskan) { this.poinDihabiskan = poinDihabiskan; }
    public StatusTransaksi getStatus() { return status; }
    public void setStatus(StatusTransaksi status) { this.status = status; }
    public String getAlasanPenolakan() { return alasanPenolakan; }
    public void setAlasanPenolakan(String alasanPenolakan) { this.alasanPenolakan = alasanPenolakan; }
    public String getDisetujuiOleh() { return disetujuiOleh; }
    public void setDisetujuiOleh(String disetujuiOleh) { this.disetujuiOleh = disetujuiOleh; }
    public String getKodeAmbil() { return kodeAmbil; }
    public void setKodeAmbil(String kodeAmbil) { this.kodeAmbil = kodeAmbil; }
    public LocalDateTime getDatetimeDiambilPada() { return datetimeDiambilPada; }
    public void setDatetimeDiambilPada(LocalDateTime datetimeDiambilPada) { this.datetimeDiambilPada = datetimeDiambilPada; }
    public LocalDateTime getDatetimeDibuatPada() { return datetimeDibuatPada; }
    public void setDatetimeDibuatPada(LocalDateTime datetimeDibuatPada) { this.datetimeDibuatPada = datetimeDibuatPada; }
    public LocalDateTime getTimestampDibuatPada() { return timestampDibuatPada; }
    public LocalDateTime getTimestampDiperbaruiPada() { return timestampDiperbaruiPada; }
}
