package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.StatusColeta;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtualizarStatusColetaDTO {

    @NotNull(message = "Status é obrigatório")
    private StatusColeta status;
}
