package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.DestinoResiduoDTO;
import com.abacaxi.residuos.model.enums.TipoDestino;
import com.abacaxi.residuos.service.DestinoResiduoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
@RequiredArgsConstructor
public class DestinoResiduoController {

    private final DestinoResiduoService destinoResiduoService;

    /** RF10 — Registrar / consultar destino */
    @GetMapping
    public ResponseEntity<List<DestinoResiduoDTO>> listar(
            @RequestParam(required = false) TipoDestino tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(destinoResiduoService.listarPorTipo(tipo));
        }
        return ResponseEntity.ok(destinoResiduoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinoResiduoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(destinoResiduoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DestinoResiduoDTO> cadastrar(@Valid @RequestBody DestinoResiduoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(destinoResiduoService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinoResiduoDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody DestinoResiduoDTO dto) {
        return ResponseEntity.ok(destinoResiduoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        destinoResiduoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
