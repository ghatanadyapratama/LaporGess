package com.example.application.service;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PenggunaService {

    private final PenggunaRepository penggunaRepository;

    public PenggunaService(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
    }

    @Transactional
    public void registerNewUser(
            String username,
            java.time.LocalDate tanggalLahir,
            String nik,
            String alamat,
            Pengguna.JenisKelamin jenisKelamin,
            String password,
            String namaLengkap,
            String rtRw,
            String telepon,
            Pengguna.Peran peran,
            String keahlian) {

        if (penggunaRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username sudah terdaftar!");
        }

        Pengguna pengguna = new Pengguna();
        pengguna.setUsername(username);
        pengguna.setNik(nik);
        pengguna.setTanggalLahir(tanggalLahir);
        pengguna.setAlamat(alamat);
        pengguna.setJenisKelamin(jenisKelamin);
        pengguna.setKataSandi(password);
        pengguna.setPeran(peran);
        pengguna.setStatus(Pengguna.Status.PENDING);
        pengguna.setPoin(0);
        pengguna.setTotalLaporan(0);
        pengguna.setTotalSelesai(0);

        pengguna.setNamaLengkap(namaLengkap);
        pengguna.setEmail(username + "@laporgess.com");
        pengguna.setTelepon(telepon != null && !telepon.isEmpty() ? telepon : "-");
        pengguna.setNomorRumah("-");
        pengguna.setRtRw(rtRw != null && !rtRw.isEmpty() ? rtRw : "-");
        pengguna.setKecamatan("-");
        pengguna.setKeahlian(keahlian);

        penggunaRepository.save(pengguna);
    }

    @Transactional
    public Pengguna authenticateUser(String username, String password) {
        Pengguna pengguna = penggunaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username tidak ditemukan!"));

        if (!pengguna.getKataSandi().equals(password)) {
            throw new IllegalArgumentException("Kata sandi salah!");
        }

        if (pengguna.getStatus() == Pengguna.Status.PENDING) {
            throw new IllegalStateException("Akun Anda sedang menunggu verifikasi oleh administrator!");
        }

        if (pengguna.getStatus() == Pengguna.Status.NONAKTIF || pengguna.getStatus() == Pengguna.Status.DIBLOKIR) {
            throw new IllegalStateException("Akun Anda tidak aktif atau diblokir!");
        }

        return pengguna;
    }
}
