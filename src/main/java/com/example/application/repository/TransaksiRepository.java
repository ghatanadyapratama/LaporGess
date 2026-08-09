package com.example.application.repository;

import com.example.application.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Integer> {
}
