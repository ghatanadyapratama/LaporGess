package com.example.application.repository;

import com.example.application.model.RiwayatStatusLaporan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiwayatStatusLaporanRepository extends JpaRepository<RiwayatStatusLaporan, Integer> {
}
