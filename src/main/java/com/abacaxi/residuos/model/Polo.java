package com.abacaxi.residuos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "polos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Polo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 20)
    private String telefone;

    @Column(length = 120)
    private String responsavel;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
