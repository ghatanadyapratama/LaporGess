package com.example.application.repository;

import com.example.application.model.KomentarLaporan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KomentarLaporanRepository extends JpaRepository<KomentarLaporan, Integer> {
}
