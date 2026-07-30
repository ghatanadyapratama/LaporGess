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
            String password) {

        if (penggunaRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username sudah terdaftar!");
        }

        Pengguna pengguna = new Pengguna();
        pengguna.setUsername(username);
        pengguna.setNik(nik);
        pengguna.setTanggalLahir(tanggalLahir);
        pengguna.setAlamat(alamat);
        pengguna.setJenisKelamin(jenisKelamin);
        // Note: For production, please hash the password using a password encoder (like BCrypt).
        // Since we are connecting directly to XAMPP for this project context, we store it.
        pengguna.setKataSandi(password);
        pengguna.setPeran(Pengguna.Peran.WARGA);
        pengguna.setStatus(Pengguna.Status.PENDING);
        pengguna.setPoin(0);
        pengguna.setTotalLaporan(0);
        pengguna.setTotalSelesai(0);

        // Placeholder untuk kolom NOT NULL — dilengkapi nanti di halaman Profil
        pengguna.setNamaLengkap(username);
        pengguna.setEmail(username + "@laporgess.com");
        pengguna.setTelepon("-");
        pengguna.setNomorRumah("-");
        pengguna.setRtRw("-");
        pengguna.setKecamatan("-");

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
