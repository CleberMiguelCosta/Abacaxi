package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.PerfilUsuario;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private Long usuarioId;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
    private String tokenAcesso;
}
