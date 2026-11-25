package com.abacaxi.residuos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroResiduoDTO {

    private Long id;

    private Long coletaId;

    @NotNull(message = "Tipo de resíduo é obrigatório")
    private Long tipoResiduoId;
    private String tipoResiduoNome;

    @NotNull(message = "Polo é obrigatório")
    private Long poloId;
    private String poloNome;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser positivo")
    private Double pesoKg;

    @NotNull(message = "Quantidade de abacaxi processada é obrigatória")
    @Positive(message = "Quantidade processada deve ser positiva")
    private Double quantidadeAbacaxiProcessadaKg;

    private LocalDateTime dataRegistro;
    private Boolean alertaQualidade;
    private String mensagemAlerta;
    private String observacoes;
}
