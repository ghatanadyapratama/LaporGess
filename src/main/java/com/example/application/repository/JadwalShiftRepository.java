package com.example.application.repository;

import com.example.application.model.JadwalShift;
import com.example.application.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JadwalShiftRepository extends JpaRepository<JadwalShift, Integer> {

    List<JadwalShift> findByPetugasOrderByTanggalAscJamMulaiAsc(Pengguna petugas);

    List<JadwalShift> findByTanggalBetweenOrderByTanggalAscJamMulaiAsc(LocalDate from, LocalDate to);

    List<JadwalShift> findByPetugasAndTanggalBetweenOrderByTanggalAscJamMulaiAsc(Pengguna petugas, LocalDate from, LocalDate to);

    List<JadwalShift> findByTanggal(LocalDate tanggal);

    List<JadwalShift> findByPetugasAndTanggal(Pengguna petugas, LocalDate tanggal);

    List<JadwalShift> findByTanggalBetweenOrderByJamMulaiAsc(LocalDate start, LocalDate end);
}
