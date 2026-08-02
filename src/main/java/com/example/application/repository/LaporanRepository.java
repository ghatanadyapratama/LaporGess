package com.example.application.repository;

import com.example.application.model.Laporan;
import com.example.application.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaporanRepository extends JpaRepository<Laporan, Integer> {

    List<Laporan> findByWargaOrderByDibuatPadaDesc(Pengguna warga);

    List<Laporan> findByPetugasOrderByDibuatPadaDesc(Pengguna petugas);

    List<Laporan> findByStatusOrderByDibuatPadaDesc(Laporan.Status status);

    List<Laporan> findAllByOrderByDibuatPadaDesc();

    long countByStatus(Laporan.Status status);

    long countByWarga(Pengguna warga);

    List<Laporan> findByPetugasAndStatus(Pengguna petugas, Laporan.Status status);
}
