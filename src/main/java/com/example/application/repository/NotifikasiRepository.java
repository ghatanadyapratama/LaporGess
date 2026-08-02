package com.example.application.repository;

import com.example.application.model.Notifikasi;
import com.example.application.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotifikasiRepository extends JpaRepository<Notifikasi, Integer> {
    List<Notifikasi> findByPenggunaOrderByDibuatPadaDesc(Pengguna pengguna);
    List<Notifikasi> findByPenggunaAndDibacaFalse(Pengguna pengguna);
    List<Notifikasi> findByPengguna_PeranOrderByDibuatPadaDesc(Pengguna.Peran peran);
}
