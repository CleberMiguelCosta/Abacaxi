package com.abacaxi.residuos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaDTO {

    private Long id;

    @NotNull(message = "Tipo de resíduo é obrigatório")
    private Long tipoResiduoId;
    private String tipoResiduoNome;

    @NotNull(message = "Polo é obrigatório")
    private Long poloId;
    private String poloNome;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Double quantidadeKg;

    @NotNull(message = "Valor unitário é obrigatório")
    @Positive(message = "Valor unitário deve ser positivo")
    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    @NotNull(message = "Data da venda é obrigatória")
    private LocalDate dataVenda;

    private String comprador;
    private String observacoes;
}
