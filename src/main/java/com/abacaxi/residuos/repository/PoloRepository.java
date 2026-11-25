package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.Polo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoloRepository extends JpaRepository<Polo, Long> {

    List<Polo> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}
