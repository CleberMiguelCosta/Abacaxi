package com.abacaxi.residuos.dto;

import com.abacaxi.residuos.model.enums.StatusColeta;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoCalendarioDTO {

    private Long coletaId;
    private LocalDate data;
    private LocalTime horario;
    private String poloNome;
    private String local;
    private Double quantidadeKg;
    private StatusColeta status;
    private String tipoResiduo;
}
