package com.abacaxi.residuos.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioFinanceiroDTO {

    private BigDecimal totalCustos;
    private BigDecimal totalReceitas;
    private BigDecimal lucroLiquido;
    private List<MovimentacaoFinanceiraDTO> movimentacoes;
}
