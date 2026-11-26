package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.VendaDTO;
import com.abacaxi.residuos.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<VendaDTO>> listar() {
        return ResponseEntity.ok(vendaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    /** RNF03 — exige autenticação (token/API key) */
    @PostMapping
    public ResponseEntity<VendaDTO> cadastrar(@Valid @RequestBody VendaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody VendaDTO dto) {
        return ResponseEntity.ok(vendaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
