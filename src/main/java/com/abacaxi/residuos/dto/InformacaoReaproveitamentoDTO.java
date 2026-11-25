package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacaoReaproveitamentoDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotNull(message = "Categoria é obrigatória")
    private CategoriaResiduo categoriaResiduo;

    @NotNull(message = "Tipo de destino é obrigatório")
    private TipoDestino tipoDestino;

    @NotBlank(message = "Conteúdo técnico é obrigatório")
    private String conteudoTecnico;

    private String orientacoes;
    private Boolean ativo;
}
