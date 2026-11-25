package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.Coleta;
import com.abacaxi.residuos.model.enums.StatusColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {

    List<Coleta> findByStatus(StatusColeta status);

    List<Coleta> findByPoloId(Long poloId);

    List<Coleta> findByDataColetaBetween(LocalDate inicio, LocalDate fim);

    @Query("""
            SELECT c FROM Coleta c
            WHERE (:poloId IS NULL OR c.polo.id = :poloId)
              AND (:status IS NULL OR c.status = :status)
              AND (:dataInicio IS NULL OR c.dataColeta >= :dataInicio)
              AND (:dataFim IS NULL OR c.dataColeta <= :dataFim)
            ORDER BY c.dataColeta, c.horario
            """)
    List<Coleta> filtrar(
            @Param("poloId") Long poloId,
            @Param("status") StatusColeta status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
