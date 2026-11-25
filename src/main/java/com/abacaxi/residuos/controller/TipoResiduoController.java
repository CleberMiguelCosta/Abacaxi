package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.TipoResiduoDTO;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.service.TipoResiduoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-residuo")
@RequiredArgsConstructor
public class TipoResiduoController {

    private final TipoResiduoService tipoResiduoService;

    /** RF08 — Classificar tipos de resíduo */
    @GetMapping
    public ResponseEntity<List<TipoResiduoDTO>> listar(
            @RequestParam(required = false) CategoriaResiduo categoria) {
        if (categoria != null) {
            return ResponseEntity.ok(tipoResiduoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(tipoResiduoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoResiduoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(tipoResiduoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoResiduoDTO> cadastrar(@Valid @RequestBody TipoResiduoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoResiduoService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoResiduoDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody TipoResiduoDTO dto) {
        return ResponseEntity.ok(tipoResiduoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tipoResiduoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
