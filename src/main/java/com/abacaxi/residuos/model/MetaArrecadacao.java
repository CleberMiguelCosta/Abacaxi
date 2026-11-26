package com.abacaxi.residuos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "metas_arrecadacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaArrecadacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "polo_id", nullable = false)
    private Polo polo;

    @Column(nullable = false)
    private Double metaKg;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(length = 300)
    private String descricao;
}
