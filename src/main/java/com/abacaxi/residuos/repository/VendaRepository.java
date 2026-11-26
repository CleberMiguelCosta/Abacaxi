package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByPoloId(Long poloId);

    List<Venda> findByDataVendaBetween(LocalDate inicio, LocalDate fim);
}
