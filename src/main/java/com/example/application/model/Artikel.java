package com.example.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "artikel")
public class Artikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String judul;

    @Column(length = 200)
    private String slug;

    @Column(columnDefinition = "LONGTEXT")
    private String konten;

    @Column(name = "konten_ringkas", columnDefinition = "TEXT")
    private String kontenRingkas;

    @Column(length = 50)
    private String kategori;

    @Column(length = 255)
    private String tags;

    @Column(name = "url_gambar", length = 255)
    private String urlGambar;

    @Column(name = "jumlah_dilihat")
    private Integer jumlahDilihat = 0;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "penulis_id")
    private Pengguna penulis;

    @Column(name = "datetime_published_at")
    private LocalDateTime datetimePublishedAt;

    @Column(name = "timestamp_dibuat_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDibuatPada;

    @Column(name = "timestamp_diperbarui_pada", insertable = false, updatable = false)
    private LocalDateTime timestampDiperbaruiPada;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getKonten() { return konten; }
    public void setKonten(String konten) { this.konten = konten; }
    public String getKontenRingkas() { return kontenRingkas; }
    public void setKontenRingkas(String kontenRingkas) { this.kontenRingkas = kontenRingkas; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getUrlGambar() { return urlGambar; }
    public void setUrlGambar(String urlGambar) { this.urlGambar = urlGambar; }
    public Integer getJumlahDilihat() { return jumlahDilihat; }
    public void setJumlahDilihat(Integer jumlahDilihat) { this.jumlahDilihat = jumlahDilihat; }
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    public Pengguna getPenulis() { return penulis; }
    public void setPenulis(Pengguna penulis) { this.penulis = penulis; }
    public LocalDateTime getDatetimePublishedAt() { return datetimePublishedAt; }
    public void setDatetimePublishedAt(LocalDateTime datetimePublishedAt) { this.datetimePublishedAt = datetimePublishedAt; }
    public LocalDateTime getTimestampDibuatPada() { return timestampDibuatPada; }
    public LocalDateTime getTimestampDiperbaruiPada() { return timestampDiperbaruiPada; }
}
