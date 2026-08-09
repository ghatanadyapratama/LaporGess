package com.example.application.repository;

import com.example.application.model.RiwayatPoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiwayatPoinRepository extends JpaRepository<RiwayatPoin, Integer> {
}
