package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.AtualizarStatusColetaDTO;
import com.abacaxi.residuos.dto.ColetaDTO;
import com.abacaxi.residuos.dto.EventoCalendarioDTO;
import com.abacaxi.residuos.model.enums.StatusColeta;
import com.abacaxi.residuos.service.ColetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/coletas")
@RequiredArgsConstructor
public class ColetaController {

    private final ColetaService coletaService;

    /** RF02 — Consultar coletas com filtros */
    @GetMapping
    public ResponseEntity<List<ColetaDTO>> listar(
            @RequestParam(required = false) Long poloId,
            @RequestParam(required = false) StatusColeta status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(coletaService.listar(poloId, status, dataInicio, dataFim));
    }

    /** RF07 — Calendário de coletas (deve vir antes de /{id}) */
    @GetMapping("/calendario")
    public ResponseEntity<List<EventoCalendarioDTO>> calendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(coletaService.calendario(inicio, fim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColetaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(coletaService.buscarPorId(id));
    }

    /** RF01 — Cadastrar coleta */
    @PostMapping
    public ResponseEntity<ColetaDTO> cadastrar(@Valid @RequestBody ColetaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coletaService.cadastrar(dto));
    }

    /** RF03 — Editar coleta */
    @PutMapping("/{id}")
    public ResponseEntity<ColetaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ColetaDTO dto) {
        return ResponseEntity.ok(coletaService.atualizar(id, dto));
    }

    /** RF05 — Atualizar status */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ColetaDTO> atualizarStatus(
            @PathVariable Long id, @Valid @RequestBody AtualizarStatusColetaDTO dto) {
        return ResponseEntity.ok(coletaService.atualizarStatus(id, dto));
    }

    /** RF04 — Cancelar coleta */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ColetaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(coletaService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        coletaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
