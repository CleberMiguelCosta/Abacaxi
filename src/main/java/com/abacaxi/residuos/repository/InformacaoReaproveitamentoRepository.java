package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.InformacaoReaproveitamento;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformacaoReaproveitamentoRepository extends JpaRepository<InformacaoReaproveitamento, Long> {

    List<InformacaoReaproveitamento> findByAtivoTrue();

    List<InformacaoReaproveitamento> findByCategoriaResiduoAndAtivoTrue(CategoriaResiduo categoria);

    List<InformacaoReaproveitamento> findByTipoDestinoAndAtivoTrue(TipoDestino tipoDestino);
}
