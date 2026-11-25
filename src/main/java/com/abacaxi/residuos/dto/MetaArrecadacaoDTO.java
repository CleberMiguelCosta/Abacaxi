package com.abacaxi.residuos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaArrecadacaoDTO {

    private Long id;

    @NotNull(message = "Polo é obrigatório")
    private Long poloId;
    private String poloNome;

    @NotNull(message = "Meta em kg é obrigatória")
    @Positive(message = "Meta deve ser positiva")
    private Double metaKg;

    @NotNull(message = "Data início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data fim é obrigatória")
    private LocalDate dataFim;

    private String descricao;

    /** Percentual de atingimento (calculado) */
    private Double percentualAtingimento;
    private Double quantidadeArrecadadaKg;
}
