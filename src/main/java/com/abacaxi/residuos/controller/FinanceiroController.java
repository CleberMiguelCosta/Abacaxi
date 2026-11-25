package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.MovimentacaoFinanceiraDTO;
import com.abacaxi.residuos.dto.RelatorioFinanceiroDTO;
import com.abacaxi.residuos.service.FinanceiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
@RequiredArgsConstructor
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    @GetMapping("/movimentacoes")
    public ResponseEntity<List<MovimentacaoFinanceiraDTO>> listar() {
        return ResponseEntity.ok(financeiroService.listarTodos());
    }

    /** RF13 / RNF02 — Relatório financeiro consolidado */
    @GetMapping("/relatorio")
    public ResponseEntity<RelatorioFinanceiroDTO> relatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(financeiroService.relatorio(inicio, fim));
    }

    @GetMapping("/movimentacoes/{id}")
    public ResponseEntity<MovimentacaoFinanceiraDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(financeiroService.buscarPorId(id));
    }

    @PostMapping("/movimentacoes")
    public ResponseEntity<MovimentacaoFinanceiraDTO> cadastrar(
            @Valid @RequestBody MovimentacaoFinanceiraDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.cadastrar(dto));
    }

    @PutMapping("/movimentacoes/{id}")
    public ResponseEntity<MovimentacaoFinanceiraDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody MovimentacaoFinanceiraDTO dto) {
        return ResponseEntity.ok(financeiroService.atualizar(id, dto));
    }

    @DeleteMapping("/movimentacoes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        financeiroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
