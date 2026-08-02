package com.example.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hadiah")
public class Hadiah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "harga_poin", nullable = false)
    private int hargaPoin;

    @Column(nullable = false)
    private int stok;

    @Column(name = "foto_url")
    private String fotoUrl;

    public Hadiah() {
    }

    public Hadiah(String nama, String deskripsi, int hargaPoin, int stok, String fotoUrl) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.hargaPoin = hargaPoin;
        this.stok = stok;
        this.fotoUrl = fotoUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getHargaPoin() {
        return hargaPoin;
    }

    public void setHargaPoin(int hargaPoin) {
        this.hargaPoin = hargaPoin;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
