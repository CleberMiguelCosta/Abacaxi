package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    private PerfilUsuario perfil;

    private Long poloId;
    private String poloNome;
    private Boolean ativo;
    private String tokenAcesso;
}
