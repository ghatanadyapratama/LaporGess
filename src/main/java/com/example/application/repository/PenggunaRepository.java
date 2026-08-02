package com.example.application.repository;

import com.example.application.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, Integer> {
    Optional<Pengguna> findByUsername(String username);

    List<Pengguna> findByPeran(Pengguna.Peran peran);

    List<Pengguna> findByStatus(Pengguna.Status status);

    List<Pengguna> findByStatusAndPeran(Pengguna.Status status, Pengguna.Peran peran);
    List<Pengguna> findByPeranOrderByPoinDesc(Pengguna.Peran peran);
    long countByStatusAndPeran(Pengguna.Status status, Pengguna.Peran peran);
    long countByStatus(Pengguna.Status status);
}