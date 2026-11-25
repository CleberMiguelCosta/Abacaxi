package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.Estoque;
import com.abacaxi.residuos.model.enums.NivelEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByPoloIdAndTipoResiduoId(Long poloId, Long tipoResiduoId);

    List<Estoque> findByNivelIn(List<NivelEstoque> niveis);

    List<Estoque> findByPoloId(Long poloId);
}
