package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.StatusColeta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColetaDTO {

    private Long id;

    @NotNull(message = "Polo é obrigatório")
    private Long poloId;
    private String poloNome;

    @NotNull(message = "Data da coleta é obrigatória")
    private LocalDate dataColeta;

    @NotNull(message = "Horário é obrigatório")
    private LocalTime horario;

    @NotNull(message = "Tipo de resíduo é obrigatório")
    private Long tipoResiduoId;
    private String tipoResiduoNome;

    @NotNull(message = "Quantidade prevista é obrigatória")
    @Positive(message = "Quantidade prevista deve ser positiva")
    private Double quantidadePrevistaKg;

    private StatusColeta status;
    private String localColeta;
    private String observacoes;
}
