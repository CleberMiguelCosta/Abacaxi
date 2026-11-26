package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.EstoqueDTO;
import com.abacaxi.residuos.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<EstoqueDTO>> listar() {
        return ResponseEntity.ok(estoqueService.listarTodos());
    }

    /** RF14 — Alertas de estoque baixo (com corAlerta) */
    @GetMapping("/alertas")
    public ResponseEntity<List<EstoqueDTO>> alertas() {
        return ResponseEntity.ok(estoqueService.listarAlertas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }
}
