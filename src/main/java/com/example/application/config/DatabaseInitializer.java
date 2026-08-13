package com.example.application.config;

import com.example.application.model.*;
import com.example.application.repository.*;
import com.example.application.service.LaporanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final PenggunaRepository penggunaRepository;
    private final LaporanService laporanService;
    private final JadwalShiftRepository jadwalShiftRepository;
    private final NotifikasiRepository notifikasiRepository;
    private final HadiahRepository hadiahRepository;
    private final LaporanRepository laporanRepository;

    public DatabaseInitializer(PenggunaRepository penggunaRepository,
                               LaporanService laporanService,
                               JadwalShiftRepository jadwalShiftRepository,
                               NotifikasiRepository notifikasiRepository,
                               HadiahRepository hadiahRepository,
                               LaporanRepository laporanRepository) {
        this.penggunaRepository = penggunaRepository;
        this.laporanService = laporanService;
        this.jadwalShiftRepository = jadwalShiftRepository;
        this.notifikasiRepository = notifikasiRepository;
        this.hadiahRepository = hadiahRepository;
        this.laporanRepository = laporanRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // ─── WARGA: budi ───────────────────────────────────────────────────
        if (penggunaRepository.findByUsername("budi").isEmpty()) {
            Pengguna budi = new Pengguna();
            budi.setUsername("budi");
            budi.setKataSandi("budi");
            budi.setNamaLengkap("Budi Santoso");
            budi.setEmail("budi@laporgess.com");
            budi.setTelepon("081234567890");
            budi.setAlamat("Jl. Sudirman, RT 01/02");
            budi.setNomorRumah("12");
            budi.setRtRw("01/02");
            budi.setKecamatan("Kecamatan Pusat");
            budi.setPeran(Pengguna.Peran.WARGA);
            budi.setStatus(Pengguna.Status.AKTIF);
            budi.setPoin(1250);
            budi.setTotalLaporan(3);
            budi.setTotalSelesai(2);
            budi.setJenisKelamin(Pengguna.JenisKelamin.LAKI_LAKI);
            penggunaRepository.save(budi);
            System.out.println("  [SEED] Warga 'budi' dibuat. password=budi");
        }

        // ─── WARGA PENDING: rina ───────────────────────────────────────────
        if (penggunaRepository.findByUsername("rina").isEmpty()) {
            Pengguna rina = new Pengguna();
            rina.setUsername("rina");
            rina.setKataSandi("rina123");
            rina.setNamaLengkap("Rina Wijaya");
            rina.setEmail("rina@laporgess.com");
            rina.setNik("3271012345678901");
            rina.setTelepon("081299887766");
            rina.setAlamat("Jl. Melati No 5");
            rina.setNomorRumah("5");
            rina.setRtRw("02/01");
            rina.setKecamatan("Perumahan Indah");
            rina.setPeran(Pengguna.Peran.WARGA);
            rina.setStatus(Pengguna.Status.PENDING);
            rina.setPoin(0);
            rina.setTotalLaporan(0);
            rina.setTotalSelesai(0);
            rina.setJenisKelamin(Pengguna.JenisKelamin.PEREMPUAN);
            penggunaRepository.save(rina);
            System.out.println("  [SEED] Warga PENDING 'rina' dibuat.");
        }

        // ─── WARGA PENDING: budig ──────────────────────────────────────────
        if (penggunaRepository.findByUsername("budig").isEmpty()) {
            Pengguna budig = new Pengguna();
            budig.setUsername("budig");
            budig.setKataSandi("budig123");
            budig.setNamaLengkap("Budi Gunawan");
            budig.setEmail("budig@laporgess.com");
            budig.setNik("3271019876543210");
            budig.setTelepon("081234000000");
            budig.setAlamat("Jl. Mawar No 12");
            budig.setNomorRumah("12");
            budig.setRtRw("01/02");
            budig.setKecamatan("Kecamatan Barat");
            budig.setPeran(Pengguna.Peran.WARGA);
            budig.setStatus(Pengguna.Status.PENDING);
            budig.setPoin(0);
            budig.setTotalLaporan(0);
            budig.setTotalSelesai(0);
            budig.setJenisKelamin(Pengguna.JenisKelamin.LAKI_LAKI);
            penggunaRepository.save(budig);
            System.out.println("  [SEED] Warga PENDING 'budig' dibuat.");
        }

        // ─── ADMIN ─────────────────────────────────────────────────────────
        if (penggunaRepository.findByUsername("admin").isEmpty()) {
            Pengguna admin = new Pengguna();
            admin.setUsername("admin");
            admin.setKataSandi("admin123");
            admin.setNamaLengkap("Administrator");
            admin.setEmail("admin@laporgess.com");
            admin.setTelepon("081200000000");
            admin.setAlamat("Kantor Lapor Gess");
            admin.setNomorRumah("1");
            admin.setRtRw("00/00");
            admin.setKecamatan("Pusat");
            admin.setPeran(Pengguna.Peran.ADMIN);
            admin.setStatus(Pengguna.Status.AKTIF);
            admin.setPoin(0);
            admin.setTotalLaporan(0);
            admin.setTotalSelesai(0);
            admin.setJenisKelamin(Pengguna.JenisKelamin.LAKI_LAKI);
            penggunaRepository.save(admin);
            System.out.println("  [SEED] Admin 'admin' dibuat. password=admin123");
        }

        // ─── PETUGAS ───────────────────────────────────────────────────────
        if (penggunaRepository.findByUsername("petugas").isEmpty()) {
            Pengguna petugas = new Pengguna();
            petugas.setUsername("petugas");
            petugas.setKataSandi("petugas123");
            petugas.setNamaLengkap("Agus Pratama");
            petugas.setEmail("petugas@laporgess.com");
            petugas.setTelepon("081299999999");
            petugas.setAlamat("Pos Lapangan Distrik Pusat");
            petugas.setNomorRumah("1");
            petugas.setRtRw("00/00");
            petugas.setKecamatan("Distrik Pusat");
            petugas.setPeran(Pengguna.Peran.PETUGAS_LAPANGAN);
            petugas.setStatus(Pengguna.Status.AKTIF);
            petugas.setPoin(0);
            petugas.setTotalLaporan(0);
            petugas.setTotalSelesai(45);
            petugas.setJenisKelamin(Pengguna.JenisKelamin.LAKI_LAKI);
            penggunaRepository.save(petugas);
            System.out.println("  [SEED] Petugas 'petugas' dibuat. password=petugas123");
        }

        // ─── LAPORAN SEED DATA ─────────────────────────────────────────────
        if (laporanRepository.count() == 0) {
            Pengguna budi = penggunaRepository.findByUsername("budi").orElse(null);
            Pengguna petugas = penggunaRepository.findByUsername("petugas").orElse(null);

            if (budi != null && petugas != null) {
                // Laporan 1: PENDING
                Laporan l1 = new Laporan();
                l1.setKodeLaporan("LAP-001");
                l1.setWarga(budi);
                l1.setKategori("Sampah Liar");
                l1.setJudul("Tumpukan Sampah di Pinggir Jalan");
                l1.setDeskripsi("Terdapat tumpukan sampah besar di pinggir Jl. Merdeka yang sudah menimbulkan bau tidak sedap dan mengganggu warga sekitar.");
                l1.setLokasi("Jl. Merdeka, RT 03/02");
                l1.setFotoUrl("icons/sampah_liar.png");
                l1.setStatus(Laporan.Status.PENDING);
                l1.setDibuatPada(LocalDateTime.now().minusDays(2));
                laporanRepository.save(l1);

                // Laporan 2: DIPROSES (assigned to petugas)
                Laporan l2 = new Laporan();
                l2.setKodeLaporan("LAP-002");
                l2.setWarga(budi);
                l2.setPetugas(petugas);
                l2.setKategori("Pohon Tumbang");
                l2.setJudul("Pohon Tumbang Memblokir Jalan");
                l2.setDeskripsi("Sebuah pohon besar tumbang akibat angin kencang dan menghalangi sebagian badan jalan utama. Perlu penanganan segera.");
                l2.setLokasi("Jl. Sudirman, RT 02/01");
                l2.setFotoUrl("icons/pohon_tumbang.png");
                l2.setStatus(Laporan.Status.DIPROSES);
                l2.setDibuatPada(LocalDateTime.now().minusDays(5));
                laporanRepository.save(l2);

                // Laporan 3: SELESAI
                Laporan l3 = new Laporan();
                l3.setKodeLaporan("LAP-003");
                l3.setWarga(budi);
                l3.setPetugas(petugas);
                l3.setKategori("Lampu Jalan");
                l3.setJudul("Lampu Jalan Mati");
                l3.setDeskripsi("Lampu jalan di depan gang RT 03 sudah mati sejak 2 minggu lalu, menyebabkan area menjadi gelap gulita di malam hari.");
                l3.setLokasi("Jl. Merdeka, RT 03/02");
                l3.setFotoUrl("icons/lampu_jalan.png");
                l3.setStatus(Laporan.Status.SELESAI);
                l3.setCatatan("Pohon yang tumbang telah berhasil dipotong dan dibersihkan dari jalan utama. Arus lalu lintas sudah kembali normal.");
                l3.setDibuatPada(LocalDateTime.now().minusDays(15));
                l3.setDiselesaikanPada(LocalDateTime.now().minusDays(12));
                laporanRepository.save(l3);

                System.out.println("  [SEED] 3 laporan contoh dibuat.");
            }
        }

        // ─── JADWAL SHIFT SEED DATA ────────────────────────────────────────
        if (jadwalShiftRepository.count() == 0) {
            Pengguna petugas = penggunaRepository.findByUsername("petugas").orElse(null);
            if (petugas != null) {
                LocalDate today = LocalDate.now();
                // Create shifts for current week (Mon-Sat)
                String[][] shiftData = {
                    {"PAGI",  "06:00", "12:00", "Zona A - Jl. Merdeka"},
                    {"SIANG", "12:00", "18:00", "Zona B - Jl. Sudirman"},
                    {"PAGI",  "06:00", "12:00", "Zona A - Jl. Merdeka"},
                    {"MALAM", "18:00", "00:00", "Zona C - Jl. Diponegoro"},
                    {"PAGI",  "06:00", "12:00", "Zona A - Jl. Merdeka"},
                    {"SIANG", "12:00", "18:00", "Zona D - Alun-Alun Kota"},
                };

                for (int i = 0; i < 6; i++) {
                    JadwalShift shift = new JadwalShift();
                    shift.setPetugas(petugas);
                    // Adjust to Monday of current week + i days
                    LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
                    shift.setTanggal(monday.plusDays(i));
                    shift.setJenisShift(JadwalShift.JenisShift.valueOf(shiftData[i][0]));
                    shift.setJamMulai(LocalTime.parse(shiftData[i][1]));
                    shift.setJamSelesai(LocalTime.parse(shiftData[i][2]));
                    shift.setZona(shiftData[i][3]);
                    shift.setKeterangan("Patroli rutin");
                    jadwalShiftRepository.save(shift);
                }
                System.out.println("  [SEED] 6 jadwal shift minggu ini dibuat.");
            }
        }

        // ==== 4. Buat Hadiah Awal ====
        if (hadiahRepository.count() == 0) {
            hadiahRepository.save(new Hadiah("Voucher Belanja Rp 50.000", "Potongan belanja di minimarket terdekat.", 500, 20, "icons/hadiah1.png"));
            hadiahRepository.save(new Hadiah("T-Shirt LaporGess Eksklusif", "Kaos katun nyaman edisi terbatas LaporGess.", 1200, 10, "icons/hadiah2.png"));
            hadiahRepository.save(new Hadiah("Botol Minum Tumbler", "Tumbler stainless steel tahan panas dan dingin.", 800, 15, "icons/hadiah3.png"));
            hadiahRepository.save(new Hadiah("Saldo E-Wallet Rp 25.000", "Saldo Gopay, OVO, atau Dana.", 300, 50, "icons/hadiah4.png"));
            System.out.println("  [SEED] 4 hadiah contoh dibuat.");
        }

        // ==== 5. Buat Notifikasi Awal ====
        Pengguna budi = penggunaRepository.findByUsername("budi").orElse(null);
        if (notifikasiRepository.count() == 0 && budi != null) {
            notifikasiRepository.save(new Notifikasi(budi, "Selamat datang di LaporGess! Mari mulai melapor.", "INFO", LocalDateTime.now().minusDays(1), false, null));
            notifikasiRepository.save(new Notifikasi(budi, "Laporan Anda 'Pohon Tumbang' sedang diproses.", "WARNING", LocalDateTime.now().minusHours(2), false, null));
            System.out.println("  [SEED] 2 notifikasi contoh dibuat.");
        }

        System.out.println("====================================================");
        System.out.println("  LaporGess seeded successfully!");
        System.out.println("  Login: budi/budi (WARGA) | admin/admin123 (ADMIN)");
        System.out.println("         petugas/petugas123 (PETUGAS)");
        System.out.println("====================================================");
    }
}
