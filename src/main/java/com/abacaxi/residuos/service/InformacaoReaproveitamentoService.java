package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.InformacaoReaproveitamentoDTO;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.InformacaoReaproveitamento;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import com.abacaxi.residuos.repository.InformacaoReaproveitamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformacaoReaproveitamentoService {

    private final InformacaoReaproveitamentoRepository repository;

    @Transactional(readOnly = true)
    public List<InformacaoReaproveitamentoDTO> listarAtivos() {
        return repository.findByAtivoTrue().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<InformacaoReaproveitamentoDTO> listarPorCategoria(CategoriaResiduo categoria) {
        return repository.findByCategoriaResiduoAndAtivoTrue(categoria).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<InformacaoReaproveitamentoDTO> listarPorDestino(TipoDestino destino) {
        return repository.findByTipoDestinoAndAtivoTrue(destino).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public InformacaoReaproveitamentoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    /** RF11 — Informações sobre reaproveitamento */
    @Transactional
    public InformacaoReaproveitamentoDTO cadastrar(InformacaoReaproveitamentoDTO dto) {
        InformacaoReaproveitamento info = InformacaoReaproveitamento.builder()
                .titulo(dto.getTitulo())
                .categoriaResiduo(dto.getCategoriaResiduo())
                .tipoDestino(dto.getTipoDestino())
                .conteudoTecnico(dto.getConteudoTecnico())
                .orientacoes(dto.getOrientacoes())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();
        return toDTO(repository.save(info));
    }

    @Transactional
    public InformacaoReaproveitamentoDTO atualizar(Long id, InformacaoReaproveitamentoDTO dto) {
        InformacaoReaproveitamento info = buscarEntidade(id);
        info.setTitulo(dto.getTitulo());
        info.setCategoriaResiduo(dto.getCategoriaResiduo());
        info.setTipoDestino(dto.getTipoDestino());
        info.setConteudoTecnico(dto.getConteudoTecnico());
        info.setOrientacoes(dto.getOrientacoes());
        if (dto.getAtivo() != null) {
            info.setAtivo(dto.getAtivo());
        }
        return toDTO(repository.save(info));
    }

    @Transactional
    public void excluir(Long id) {
        InformacaoReaproveitamento info = buscarEntidade(id);
        info.setAtivo(false);
        repository.save(info);
    }

    public InformacaoReaproveitamento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Informação não encontrada: " + id));
    }

    private InformacaoReaproveitamentoDTO toDTO(InformacaoReaproveitamento i) {
        return InformacaoReaproveitamentoDTO.builder()
                .id(i.getId())
                .titulo(i.getTitulo())
                .categoriaResiduo(i.getCategoriaResiduo())
                .tipoDestino(i.getTipoDestino())
                .conteudoTecnico(i.getConteudoTecnico())
                .orientacoes(i.getOrientacoes())
                .ativo(i.getAtivo())
                .build();
    }
}
