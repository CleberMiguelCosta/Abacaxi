package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.TipoDestino;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinoResiduoDTO {

    private Long id;

    @NotNull(message = "Registro de resíduo é obrigatório")
    private Long registroResiduoId;

    @NotNull(message = "Tipo de destino é obrigatório")
    private TipoDestino tipoDestino;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Double quantidadeKg;

    @NotNull(message = "Data de destinação é obrigatória")
    private LocalDate dataDestinacao;

    private String destinoDetalhado;
    private String observacoes;
}
