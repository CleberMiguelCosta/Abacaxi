package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.MetaArrecadacaoDTO;
import com.abacaxi.residuos.service.MetaArrecadacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
public class MetaArrecadacaoController {

    private final MetaArrecadacaoService metaArrecadacaoService;

    /** RF12 — Metas de arrecadação */
    @GetMapping
    public ResponseEntity<List<MetaArrecadacaoDTO>> listar(
            @RequestParam(required = false) Long poloId) {
        if (poloId != null) {
            return ResponseEntity.ok(metaArrecadacaoService.listarPorPolo(poloId));
        }
        return ResponseEntity.ok(metaArrecadacaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaArrecadacaoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(metaArrecadacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MetaArrecadacaoDTO> cadastrar(@Valid @RequestBody MetaArrecadacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metaArrecadacaoService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaArrecadacaoDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody MetaArrecadacaoDTO dto) {
        return ResponseEntity.ok(metaArrecadacaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        metaArrecadacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
