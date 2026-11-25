package com.abacaxi.residuos.model;

import com.abacaxi.residuos.model.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PerfilUsuario perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polo_id")
    private Polo polo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    /** Token/chave de acesso para autenticação (RNF03) */
    @Column(unique = true, length = 64)
    private String tokenAcesso;
}
