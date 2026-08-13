package com.example.application.service;

import com.example.application.model.Laporan;
import com.example.application.repository.LaporanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LaporanScheduler {

    private final LaporanRepository laporanRepository;
    private final LaporanService laporanService;

    public LaporanScheduler(LaporanRepository laporanRepository, LaporanService laporanService) {
        this.laporanRepository = laporanRepository;
        this.laporanService = laporanService;
    }

    // Run every hour at minute 0
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void autoConfirmLaporan() {
        // Find all reports waiting for confirmation
        List<Laporan> waitingReports = laporanRepository.findByStatusOrderByDibuatPadaDesc(Laporan.Status.MENUNGGU_KONFIRMASI);
        
        LocalDateTime now = LocalDateTime.now();
        for (Laporan laporan : waitingReports) {
            if (laporan.getMenungguKonfirmasiPada() != null) {
                // If it has been waiting for more than 2 days
                if (laporan.getMenungguKonfirmasiPada().plusDays(2).isBefore(now)) {
                    try {
                        laporanService.konfirmasiSelesaiOlehWarga(laporan.getId());
                        System.out.println("[Scheduler] Automatically confirmed laporan: " + laporan.getKodeLaporan());
                    } catch (Exception e) {
                        System.err.println("[Scheduler] Failed to auto confirm laporan: " + laporan.getKodeLaporan() + " - " + e.getMessage());
                    }
                }
            }
        }
    }
}
