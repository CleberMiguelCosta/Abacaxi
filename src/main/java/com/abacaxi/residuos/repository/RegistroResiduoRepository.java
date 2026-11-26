package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.RegistroResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistroResiduoRepository extends JpaRepository<RegistroResiduo, Long> {

    List<RegistroResiduo> findByPoloId(Long poloId);

    List<RegistroResiduo> findByAlertaQualidadeTrue();

    @Query("SELECT COALESCE(SUM(r.pesoKg), 0) FROM RegistroResiduo r WHERE r.polo.id = :poloId")
    Double somarPesoPorPolo(Long poloId);

    @Query("SELECT COALESCE(SUM(r.pesoKg), 0) FROM RegistroResiduo r")
    Double somarPesoTotal();

    @Query("SELECT COALESCE(SUM(r.quantidadeAbacaxiProcessadaKg), 0) FROM RegistroResiduo r")
    Double somarAbacaxiProcessado();
}
