package com.abacaxi.residuos.model;

import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "informacoes_reaproveitamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacaoReaproveitamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaResiduo categoriaResiduo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoDestino tipoDestino;

    @Column(nullable = false, length = 2000)
    private String conteudoTecnico;

    @Column(length = 1000)
    private String orientacoes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
