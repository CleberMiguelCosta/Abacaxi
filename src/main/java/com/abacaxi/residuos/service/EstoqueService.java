package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.EstoqueDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.Estoque;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.enums.NivelEstoque;
import com.abacaxi.residuos.repository.EstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    @Value("${app.estoque.nivel-baixo:50}")
    private double nivelBaixo;

    @Value("${app.estoque.nivel-critico:20}")
    private double nivelCritico;

    @Transactional(readOnly = true)
    public List<EstoqueDTO> listarTodos() {
        return estoqueRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EstoqueDTO> listarAlertas() {
        return estoqueRepository.findByNivelIn(List.of(NivelEstoque.BAIXO, NivelEstoque.CRITICO))
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public EstoqueDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional
    public void entrada(Polo polo, TipoResiduo tipo, double quantidadeKg) {
        Estoque estoque = obterOuCriar(polo, tipo);
        estoque.setQuantidadeKg(estoque.getQuantidadeKg() + quantidadeKg);
        atualizarNivel(estoque);
        estoque.setAtualizadoEm(LocalDateTime.now());
        estoqueRepository.save(estoque);
    }

    @Transactional
    public void saida(Polo polo, TipoResiduo tipo, double quantidadeKg) {
        Estoque estoque = estoqueRepository.findByPoloIdAndTipoResiduoId(polo.getId(), tipo.getId())
                .orElseThrow(() -> new NegocioException(
                        "Estoque insuficiente para polo " + polo.getNome() + " / " + tipo.getNome()));
        if (estoque.getQuantidadeKg() < quantidadeKg) {
            throw new NegocioException("Estoque insuficiente. Disponível: " + estoque.getQuantidadeKg() + " kg");
        }
        estoque.setQuantidadeKg(estoque.getQuantidadeKg() - quantidadeKg);
        atualizarNivel(estoque);
        estoque.setAtualizadoEm(LocalDateTime.now());
        estoqueRepository.save(estoque);
    }

    private Estoque obterOuCriar(Polo polo, TipoResiduo tipo) {
        return estoqueRepository.findByPoloIdAndTipoResiduoId(polo.getId(), tipo.getId())
                .orElseGet(() -> estoqueRepository.save(Estoque.builder()
                        .polo(polo)
                        .tipoResiduo(tipo)
                        .quantidadeKg(0.0)
                        .nivel(NivelEstoque.CRITICO)
                        .atualizadoEm(LocalDateTime.now())
                        .build()));
    }

    private void atualizarNivel(Estoque estoque) {
        double q = estoque.getQuantidadeKg();
        if (q <= nivelCritico) {
            estoque.setNivel(NivelEstoque.CRITICO);
        } else if (q <= nivelBaixo) {
            estoque.setNivel(NivelEstoque.BAIXO);
        } else {
            estoque.setNivel(NivelEstoque.NORMAL);
        }
    }

    public Estoque buscarEntidade(Long id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estoque não encontrado: " + id));
    }

    private EstoqueDTO toDTO(Estoque e) {
        String cor = switch (e.getNivel()) {
            case NORMAL -> "VERDE";
            case BAIXO -> "AMARELO";
            case CRITICO -> "VERMELHO";
        };
        return EstoqueDTO.builder()
                .id(e.getId())
                .tipoResiduoId(e.getTipoResiduo().getId())
                .tipoResiduoNome(e.getTipoResiduo().getNome())
                .poloId(e.getPolo().getId())
                .poloNome(e.getPolo().getNome())
                .quantidadeKg(e.getQuantidadeKg())
                .nivel(e.getNivel())
                .corAlerta(cor)
                .atualizadoEm(e.getAtualizadoEm())
                .build();
    }
}
