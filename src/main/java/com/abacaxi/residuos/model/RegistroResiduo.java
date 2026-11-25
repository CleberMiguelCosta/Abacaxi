package com.abacaxi.residuos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registros_residuo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleta_id")
    private Coleta coleta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduo tipoResiduo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "polo_id", nullable = false)
    private Polo polo;

    /** Peso real do lote em kg */
    @Column(nullable = false)
    private Double pesoKg;

    /** Quantidade total de abacaxi processada (base do rendimento RF09) */
    @Column(nullable = false)
    private Double quantidadeAbacaxiProcessadaKg;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataRegistro = LocalDateTime.now();

    /** true quando o peso está fora da margem aceitável (RF15) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean alertaQualidade = false;

    @Column(length = 300)
    private String mensagemAlerta;

    @Column(length = 500)
    private String observacoes;
}
