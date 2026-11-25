package com.abacaxi.residuos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoloDTO {

    private Long id;

    @NotBlank(message = "Nome do polo é obrigatório")
    @Size(max = 120)
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200)
    private String endereco;

    @Size(max = 100)
    private String cidade;

    @Size(max = 2)
    private String estado;

    @Size(max = 20)
    private String telefone;

    @Size(max = 120)
    private String responsavel;

    private Boolean ativo;
}
