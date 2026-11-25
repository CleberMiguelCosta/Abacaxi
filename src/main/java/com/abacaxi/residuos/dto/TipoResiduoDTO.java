package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoResiduoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Categoria é obrigatória")
    private CategoriaResiduo categoria;

    private String descricao;

    @NotNull
    @Positive(message = "Peso médio esperado deve ser positivo")
    private Double pesoMedioEsperadoKg;

    private Boolean ativo;
}
