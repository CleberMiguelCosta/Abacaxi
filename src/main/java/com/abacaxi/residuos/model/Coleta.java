package com.abacaxi.residuos.model;

import com.abacaxi.residuos.model.enums.StatusColeta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "coletas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "polo_id", nullable = false)
    private Polo polo;

    @Column(nullable = false)
    private LocalDate dataColeta;

    @Column(nullable = false)
    private LocalTime horario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduo tipoResiduo;

    /** Quantidade prevista em kg */
    @Column(nullable = false)
    private Double quantidadePrevistaKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatusColeta status = StatusColeta.AGENDADA;

    @Column(length = 200)
    private String localColeta;

    @Column(length = 500)
    private String observacoes;
}
