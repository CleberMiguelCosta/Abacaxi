package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.PoloDTO;
import com.abacaxi.residuos.service.PoloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polos")
@RequiredArgsConstructor
public class PoloController {

    private final PoloService poloService;

    /** RF06 — Consultar polos participantes */
    @GetMapping
    public ResponseEntity<List<PoloDTO>> listar(@RequestParam(required = false) Boolean apenasAtivos) {
        if (Boolean.TRUE.equals(apenasAtivos)) {
            return ResponseEntity.ok(poloService.listarAtivos());
        }
        return ResponseEntity.ok(poloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoloDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(poloService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PoloDTO> cadastrar(@Valid @RequestBody PoloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poloService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoloDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PoloDTO dto) {
        return ResponseEntity.ok(poloService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        poloService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
