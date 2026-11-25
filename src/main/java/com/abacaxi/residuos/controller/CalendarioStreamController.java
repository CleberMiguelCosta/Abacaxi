package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.EventoCalendarioDTO;
import com.abacaxi.residuos.service.ColetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * RNF05 — Atualizações do calendário em tempo real via Server-Sent Events.
 */
@RestController
@RequestMapping("/api/calendario")
@RequiredArgsConstructor
public class CalendarioStreamController {

    private final ColetaService coletaService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        SseEmitter emitter = new SseEmitter(0L);

        var future = scheduler.scheduleAtFixedRate(() -> {
            try {
                List<EventoCalendarioDTO> eventos = coletaService.calendario(inicio, fim);
                emitter.send(SseEmitter.event().name("calendario").data(eventos));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 3, TimeUnit.SECONDS);

        emitter.onCompletion(() -> future.cancel(true));
        emitter.onTimeout(() -> {
            future.cancel(true);
            emitter.complete();
        });

        return emitter;
    }
}
