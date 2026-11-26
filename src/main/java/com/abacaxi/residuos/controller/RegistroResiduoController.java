package com.abacaxi.residuos.controller;

import com.abacaxi.residuos.dto.RegistroResiduoDTO;
import com.abacaxi.residuos.dto.RendimentoDTO;
import com.abacaxi.residuos.service.RegistroResiduoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-residuo")
@RequiredArgsConstructor
public class RegistroResiduoController {

    private final RegistroResiduoService registroResiduoService;

    @GetMapping
    public ResponseEntity<List<RegistroResiduoDTO>> listar() {
        return ResponseEntity.ok(registroResiduoService.listarTodos());
    }

    /** RF15 — Alertas de peso fora da margem */
    @GetMapping("/alertas-qualidade")
    public ResponseEntity<List<RegistroResiduoDTO>> alertasQualidade() {
        return ResponseEntity.ok(registroResiduoService.listarAlertasQualidade());
    }

    /** RF09 — Rendimento e aproveitamento */
    @GetMapping("/rendimento")
    public ResponseEntity<RendimentoDTO> rendimento() {
        return ResponseEntity.ok(registroResiduoService.calcularRendimento());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroResiduoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(registroResiduoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RegistroResiduoDTO> cadastrar(@Valid @RequestBody RegistroResiduoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroResiduoService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroResiduoDTO> atualizar(
            @PathVariable Long id, @Valid @RequestBody RegistroResiduoDTO dto) {
        return ResponseEntity.ok(registroResiduoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        registroResiduoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
