package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.NivelEstoque;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueDTO {

    private Long id;
    private Long tipoResiduoId;
    private String tipoResiduoNome;
    private Long poloId;
    private String poloNome;
    private Double quantidadeKg;
    private NivelEstoque nivel;
    /** Cor sugerida para alerta visual (RF14): VERDE, AMARELO, VERMELHO */
    private String corAlerta;
    private LocalDateTime atualizadoEm;
}
