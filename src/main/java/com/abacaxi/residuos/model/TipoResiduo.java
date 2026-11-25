package com.abacaxi.residuos.model;

import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_residuo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaResiduo categoria;

    @Column(length = 500)
    private String descricao;

    /** Peso médio esperado por lote (kg) — usado na validação RF15 */
    @Column(nullable = false)
    @Builder.Default
    private Double pesoMedioEsperadoKg = 100.0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
