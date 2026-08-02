package com.example.application.service;

import com.example.application.model.JadwalShift;
import com.example.application.model.Pengguna;
import com.example.application.repository.JadwalShiftRepository;
import com.example.application.repository.PenggunaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class JadwalShiftService {

    private final JadwalShiftRepository jadwalShiftRepository;
    private final PenggunaRepository penggunaRepository;

    public JadwalShiftService(JadwalShiftRepository jadwalShiftRepository, PenggunaRepository penggunaRepository) {
        this.jadwalShiftRepository = jadwalShiftRepository;
        this.penggunaRepository = penggunaRepository;
    }

    @Transactional(readOnly = true)
    public List<JadwalShift> getJadwalByRentang(LocalDate start, LocalDate end) {
        List<JadwalShift> list = jadwalShiftRepository.findByTanggalBetweenOrderByJamMulaiAsc(start, end);
        list.forEach(j -> { if (j.getPetugas() != null) j.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional(readOnly = true)
    public List<JadwalShift> getJadwalTanggal(LocalDate date) {
        List<JadwalShift> list = jadwalShiftRepository.findByTanggal(date);
        list.forEach(j -> { if (j.getPetugas() != null) j.getPetugas().getNamaLengkap(); });
        return list;
    }

    @Transactional
    public List<JadwalShift> saveAll(List<JadwalShift> jadwalList) {
        return jadwalShiftRepository.saveAll(jadwalList);
    }

    @Transactional
    public void deleteAllByRentang(LocalDate start, LocalDate end) {
        List<JadwalShift> toDelete = jadwalShiftRepository.findByTanggalBetweenOrderByJamMulaiAsc(start, end);
        jadwalShiftRepository.deleteAll(toDelete);
    }

    @Transactional
    public List<JadwalShift> generateJadwalOtomatis(LocalDate start, LocalDate end) {
        // 1. Delete existing for this range
        deleteAllByRentang(start, end);

        // 2. Fetch all active Petugas
        List<Pengguna> allPetugas = penggunaRepository.findByStatusAndPeran(Pengguna.Status.AKTIF, Pengguna.Peran.PETUGAS_LAPANGAN);
        if (allPetugas.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. Define the standard slots
        // 06:00-08:00, 08:00-10:00, 10:00-12:00, 13:00-15:00, 15:00-17:00
        LocalTime[][] slots = {
            {LocalTime.of(6, 0), LocalTime.of(8, 0)},
            {LocalTime.of(8, 0), LocalTime.of(10, 0)},
            {LocalTime.of(10, 0), LocalTime.of(12, 0)},
            {LocalTime.of(13, 0), LocalTime.of(15, 0)},
            {LocalTime.of(15, 0), LocalTime.of(17, 0)}
        };

        String[] zones = {"Zona A", "Zona B", "Zona C", "Zona D", "Zona E"};

        List<JadwalShift> newSchedule = new ArrayList<>();
        
        LocalDate current = start;
        while (!current.isAfter(end)) {
            List<Pengguna> dailyOfficers = new ArrayList<>(allPetugas);
            Collections.shuffle(dailyOfficers);
            int officerIndex = 0;

            for (LocalTime[] slot : slots) {
                for (String zone : zones) {
                    Pengguna selectedPetugas = dailyOfficers.get(officerIndex % dailyOfficers.size());
                    
                    JadwalShift js = new JadwalShift();
                    js.setTanggal(current);
                    js.setJamMulai(slot[0]);
                    js.setJamSelesai(slot[1]);
                    js.setZona(zone);
                    js.setPetugas(selectedPetugas);
                    js.setJenisShift(determineJenis(slot[0]));
                    js.setKeterangan("Otomatis");
                    
                    newSchedule.add(js);
                    officerIndex++;
                }
            }
            current = current.plusDays(1);
        }

        return jadwalShiftRepository.saveAll(newSchedule);
    }

    private JadwalShift.JenisShift determineJenis(LocalTime start) {
        if (start.isBefore(LocalTime.of(12, 0))) {
            return JadwalShift.JenisShift.PAGI;
        } else if (start.isBefore(LocalTime.of(18, 0))) {
            return JadwalShift.JenisShift.SIANG;
        }
        return JadwalShift.JenisShift.MALAM;
    }
}
