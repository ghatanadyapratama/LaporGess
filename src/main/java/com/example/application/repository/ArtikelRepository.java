package com.example.application.repository;

import com.example.application.model.Artikel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, Integer> {
}
