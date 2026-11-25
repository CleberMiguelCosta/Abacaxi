package com.abacaxi.residuos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduo tipoResiduo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "polo_id", nullable = false)
    private Polo polo;

    @Column(nullable = false)
    private Double quantidadeKg;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorUnitario;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private LocalDate dataVenda;

    @Column(length = 150)
    private String comprador;

    @Column(length = 500)
    private String observacoes;
}
