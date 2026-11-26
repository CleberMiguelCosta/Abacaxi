package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.MovimentacaoFinanceira;
import com.abacaxi.residuos.model.enums.TipoMovimentacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long> {

    List<MovimentacaoFinanceira> findByTipo(TipoMovimentacaoFinanceira tipo);

    List<MovimentacaoFinanceira> findByDataMovimentacaoBetween(LocalDate inicio, LocalDate fim);
}
