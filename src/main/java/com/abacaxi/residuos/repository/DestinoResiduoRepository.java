package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.DestinoResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinoResiduoRepository extends JpaRepository<DestinoResiduo, Long> {

    List<DestinoResiduo> findByTipoDestino(TipoDestino tipoDestino);

    @Query("SELECT COALESCE(SUM(d.quantidadeKg), 0) FROM DestinoResiduo d")
    Double somarQuantidadeReaproveitada();
}
