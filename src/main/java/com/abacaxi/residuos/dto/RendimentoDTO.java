package com.abacaxi.residuos.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendimentoDTO {

    private Double totalAbacaxiProcessadoKg;
    private Double totalResiduosGeradosKg;
    private Double totalResiduosReaproveitadosKg;
    private Double percentualResiduosGerados;
    private Double percentualResiduosReaproveitados;
    /** Percentual por categoria (CASCA, BAGACO, FOLHAS, etc.) */
    private Map<String, Double> percentualPorCategoria;
}
