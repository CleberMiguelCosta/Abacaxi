package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.TipoResiduoDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.enums.CategoriaResiduo;
import com.abacaxi.residuos.repository.TipoResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoResiduoService {

    private final TipoResiduoRepository tipoResiduoRepository;

    @Transactional(readOnly = true)
    public List<TipoResiduoDTO> listarTodos() {
        return tipoResiduoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<TipoResiduoDTO> listarPorCategoria(CategoriaResiduo categoria) {
        return tipoResiduoRepository.findByCategoria(categoria).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TipoResiduoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional
    public TipoResiduoDTO cadastrar(TipoResiduoDTO dto) {
        if (tipoResiduoRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new NegocioException("Já existe tipo de resíduo: " + dto.getNome());
        }
        TipoResiduo entity = toEntity(dto);
        entity.setId(null);
        entity.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return toDTO(tipoResiduoRepository.save(entity));
    }

    @Transactional
    public TipoResiduoDTO atualizar(Long id, TipoResiduoDTO dto) {
        TipoResiduo entity = buscarEntidade(id);
        entity.setNome(dto.getNome());
        entity.setCategoria(dto.getCategoria());
        entity.setDescricao(dto.getDescricao());
        entity.setPesoMedioEsperadoKg(dto.getPesoMedioEsperadoKg());
        if (dto.getAtivo() != null) {
            entity.setAtivo(dto.getAtivo());
        }
        return toDTO(tipoResiduoRepository.save(entity));
    }

    @Transactional
    public void excluir(Long id) {
        TipoResiduo entity = buscarEntidade(id);
        entity.setAtivo(false);
        tipoResiduoRepository.save(entity);
    }

    public TipoResiduo buscarEntidade(Long id) {
        return tipoResiduoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de resíduo não encontrado: " + id));
    }

    private TipoResiduoDTO toDTO(TipoResiduo t) {
        return TipoResiduoDTO.builder()
                .id(t.getId())
                .nome(t.getNome())
                .categoria(t.getCategoria())
                .descricao(t.getDescricao())
                .pesoMedioEsperadoKg(t.getPesoMedioEsperadoKg())
                .ativo(t.getAtivo())
                .build();
    }

    private TipoResiduo toEntity(TipoResiduoDTO dto) {
        return TipoResiduo.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .pesoMedioEsperadoKg(dto.getPesoMedioEsperadoKg())
                .ativo(dto.getAtivo())
                .build();
    }
}
