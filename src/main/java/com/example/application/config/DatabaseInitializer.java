package com.example.application.config;

import com.example.application.model.Pengguna;
import com.example.application.repository.PenggunaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final PenggunaRepository penggunaRepository;

    public DatabaseInitializer(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create active user 'budi' if not exists
        if (penggunaRepository.findByUsername("budi").isEmpty()) {
            Pengguna budi = new Pengguna();
            budi.setUsername("budi");
            budi.setKataSandi("budi"); // plaintext password matching authentication setup
            budi.setNamaLengkap("Budi");
            budi.setEmail("budi@laporgess.com");
            budi.setTelepon("081234567890");
            budi.setAlamat("Jl. Sudirman, RT 01/02");
            budi.setNomorRumah("12");
            budi.setRtRw("01/02");
            budi.setKecamatan("Kecamatan Contoh");
            budi.setPeran(Pengguna.Peran.WARGA);
            budi.setStatus(Pengguna.Status.AKTIF); // Already active (verified)
            budi.setPoin(1250); // Points showing on dashboard image
            budi.setTotalLaporan(16);
            budi.setTotalSelesai(14); // Completed reports showing on dashboard image
            budi.setJenisKelamin(Pengguna.JenisKelamin.LAKI_LAKI);

            penggunaRepository.save(budi);
            System.out.println("====================================================");
            System.out.println("  Default User Created: username='budi', password='budi' (STATUS: AKTIF)");
            System.out.println("====================================================");
        }
    }
}
