package com.abacaxi.residuos.model;

import com.abacaxi.residuos.model.enums.TipoDestino;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "destinos_residuo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinoResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registro_residuo_id", nullable = false)
    private RegistroResiduo registroResiduo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoDestino tipoDestino;

    @Column(nullable = false)
    private Double quantidadeKg;

    @Column(nullable = false)
    private LocalDate dataDestinacao;

    @Column(length = 200)
    private String destinoDetalhado;

    @Column(length = 500)
    private String observacoes;
}
