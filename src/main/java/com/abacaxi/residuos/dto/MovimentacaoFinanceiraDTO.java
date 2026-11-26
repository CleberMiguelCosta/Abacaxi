package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.TipoMovimentacaoFinanceira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoFinanceiraDTO {

    private Long id;

    @NotNull(message = "Tipo é obrigatório")
    private TipoMovimentacaoFinanceira tipo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Data é obrigatória")
    private LocalDate dataMovimentacao;

    private Long poloId;
    private String poloNome;
    private Long vendaId;
}
