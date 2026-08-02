package com.example.application.service;

import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import com.example.application.repository.LaporanRepository;
import com.example.application.repository.PenggunaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LaporanService {

    private final LaporanRepository laporanRepository;
    private final PenggunaRepository penggunaRepository;

    public LaporanService(LaporanRepository laporanRepository, PenggunaRepository penggunaRepository) {
        this.laporanRepository = laporanRepository;
        this.penggunaRepository = penggunaRepository;
    }

    @Transactional
    public Laporan buatLaporan(String usernameWarga, String kategori, String judul,
                                String deskripsi, String lokasi, String fotoUrl) {
        Pengguna warga = penggunaRepository.findByUsername(usernameWarga)
                .orElseThrow(() -> new IllegalArgumentException("Warga tidak ditemukan: " + usernameWarga));

        Laporan laporan = new Laporan();
        laporan.setKodeLaporan("LAP-" + System.currentTimeMillis());
        laporan.setWarga(warga);

        laporan.setKategori(kategori);
        laporan.setJudul(judul);
        laporan.setDeskripsi(deskripsi);
        laporan.setLokasi(lokasi);
        laporan.setFotoUrl(fotoUrl);
        laporan.setStatus(Laporan.Status.PENDING);
        laporan.setDibuatPada(LocalDateTime.now());

        // Update total laporan warga
        warga.setTotalLaporan((warga.getTotalLaporan() == null ? 0 : warga.getTotalLaporan()) + 1);
        penggunaRepository.save(warga);

        return laporanRepository.save(laporan);
    }

    @Transactional
    public Laporan assignPetugas(Integer laporanId, String usernamePetugas) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new IllegalArgumentException("Laporan tidak ditemukan: " + laporanId));
        Pengguna petugas = penggunaRepository.findByUsername(usernamePetugas)
                .orElseThrow(() -> new IllegalArgumentException("Petugas tidak ditemukan: " + usernamePetugas));

        laporan.setPetugas(petugas);
        laporan.setStatus(Laporan.Status.DIPROSES);
        return laporanRepository.save(laporan);
    }

    @Transactional
    public Laporan selesaikanLaporan(Integer laporanId, String catatan, String fotoBuktiUrl) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new IllegalArgumentException("Laporan tidak ditemukan: " + laporanId));

        laporan.setStatus(Laporan.Status.SELESAI);
        laporan.setCatatan(catatan);
        laporan.setFotoBuktiUrl(fotoBuktiUrl);
        laporan.setDiselesaikanPada(LocalDateTime.now());

        // Update statistik warga
        Pengguna warga = laporan.getWarga();
        warga.setTotalSelesai((warga.getTotalSelesai() == null ? 0 : warga.getTotalSelesai()) + 1);
        warga.setPoin((warga.getPoin() == null ? 0 : warga.getPoin()) + 100); // +100 poin per laporan selesai
        penggunaRepository.save(warga);

        // Update statistik petugas
        if (laporan.getPetugas() != null) {
            Pengguna petugas = laporan.getPetugas();
            petugas.setTotalSelesai((petugas.getTotalSelesai() == null ? 0 : petugas.getTotalSelesai()) + 1);
            penggunaRepository.save(petugas);
        }

        return laporanRepository.save(laporan);
    }

    @Transactional
    public Laporan tolakLaporan(Integer laporanId, String alasan) {
        Laporan laporan = laporanRepository.findById(laporanId)
                .orElseThrow(() -> new IllegalArgumentException("Laporan tidak ditemukan: " + laporanId));

        laporan.setStatus(Laporan.Status.DITOLAK);
        laporan.setCatatanTolak(alasan);
        return laporanRepository.save(laporan);
    }

    @Transactional(readOnly = true)
    public List<Laporan> getLaporanByWarga(String usernameWarga) {
        Pengguna warga = penggunaRepository.findByUsername(usernameWarga)
                .orElseThrow(() -> new IllegalArgumentException("Warga tidak ditemukan"));
        List<Laporan> list = laporanRepository.findByWargaOrderByDibuatPadaDesc(warga);
        list.forEach(l -> { if (l.getWarga() != null) l.getWarga().getNamaLengkap(); if (l.getPetugas() != null) l.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional(readOnly = true)
    public List<Laporan> getLaporanByPetugas(String usernamePetugas) {
        Pengguna petugas = penggunaRepository.findByUsername(usernamePetugas)
                .orElseThrow(() -> new IllegalArgumentException("Petugas tidak ditemukan"));
        List<Laporan> list = laporanRepository.findByPetugasOrderByDibuatPadaDesc(petugas);
        list.forEach(l -> { if (l.getWarga() != null) l.getWarga().getNamaLengkap(); if (l.getPetugas() != null) l.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional(readOnly = true)
    public List<Laporan> getLaporanDiprosesByPetugas(String usernamePetugas) {
        Pengguna petugas = penggunaRepository.findByUsername(usernamePetugas)
                .orElseThrow(() -> new IllegalArgumentException("Petugas tidak ditemukan"));
        List<Laporan> list = laporanRepository.findByPetugasAndStatus(petugas, Laporan.Status.DIPROSES);
        list.forEach(l -> { if (l.getWarga() != null) l.getWarga().getNamaLengkap(); if (l.getPetugas() != null) l.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional(readOnly = true)
    public List<Laporan> getLaporanSelesaiByPetugas(String usernamePetugas) {
        Pengguna petugas = penggunaRepository.findByUsername(usernamePetugas)
                .orElseThrow(() -> new IllegalArgumentException("Petugas tidak ditemukan"));
        List<Laporan> list = laporanRepository.findByPetugasAndStatus(petugas, Laporan.Status.SELESAI);
        list.forEach(l -> { if (l.getWarga() != null) l.getWarga().getNamaLengkap(); if (l.getPetugas() != null) l.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional(readOnly = true)
    public List<Laporan> getAllLaporan() {
        List<Laporan> list = laporanRepository.findAllByOrderByDibuatPadaDesc();
        list.forEach(l -> {
            if (l.getWarga() != null) l.getWarga().getNamaLengkap();
            if (l.getPetugas() != null) l.getPetugas().getNamaLengkap();
        });
        return list;
    }

    @Transactional(readOnly = true)
    public List<Laporan> getLaporanByStatus(Laporan.Status status) {
        List<Laporan> list = laporanRepository.findByStatusOrderByDibuatPadaDesc(status);
        list.forEach(l -> {
            if (l.getWarga() != null) l.getWarga().getNamaLengkap();
            if (l.getPetugas() != null) l.getPetugas().getNamaLengkap();
        });
        return list;
    }

    @Transactional(readOnly = true)
    public Optional<Laporan> getById(Integer id) {
        Optional<Laporan> opt = laporanRepository.findById(id);
        opt.ifPresent(l -> {
            if (l.getWarga() != null) l.getWarga().getNamaLengkap();
            if (l.getPetugas() != null) l.getPetugas().getNamaLengkap();
        });
        return opt;
    }

    public long countByStatus(Laporan.Status status) {
        return laporanRepository.countByStatus(status);
    }
}
