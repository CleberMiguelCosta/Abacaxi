package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Long> {

    List<TipoResiduo> findByAtivoTrue();

    List<TipoResiduo> findByCategoria(CategoriaResiduo categoria);

    boolean existsByNomeIgnoreCase(String nome);
}
