package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.InformacaoReaproveitamentoDTO;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import com.abacaxi.residuos.service.InformacaoReaproveitamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reaproveitamento")
@RequiredArgsConstructor
public class InformacaoReaproveitamentoController {

    private final InformacaoReaproveitamentoService service;

    /** RF11 — Consultar informações sobre reaproveitamento */
    @GetMapping
    public ResponseEntity<List<InformacaoReaproveitamentoDTO>> listar(
            @RequestParam(required = false) CategoriaResiduo categoria,
            @RequestParam(required = false) TipoDestino destino) {
        if (categoria != null) {
            return ResponseEntity.ok(service.listarPorCategoria(categoria));
        }
        if (destino != null) {
            return ResponseEntity.ok(service.listarPorDestino(destino));
        }
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformacaoReaproveitamentoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<InformacaoReaproveitamentoDTO> cadastrar(
            @Valid @RequestBody InformacaoReaproveitamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InformacaoReaproveitamentoDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody InformacaoReaproveitamentoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
