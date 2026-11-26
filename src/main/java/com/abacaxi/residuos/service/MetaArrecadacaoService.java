package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.MetaArrecadacaoDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.MetaArrecadacao;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.repository.MetaArrecadacaoRepository;
import com.abacaxi.residuos.repository.RegistroResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaArrecadacaoService {

    private final MetaArrecadacaoRepository metaArrecadacaoRepository;
    private final RegistroResiduoRepository registroResiduoRepository;
    private final PoloService poloService;

    @Transactional(readOnly = true)
    public List<MetaArrecadacaoDTO> listarTodos() {
        return metaArrecadacaoRepository.findAll().stream().map(this::toDTOComPercentual).toList();
    }

    @Transactional(readOnly = true)
    public List<MetaArrecadacaoDTO> listarPorPolo(Long poloId) {
        return metaArrecadacaoRepository.findByPoloId(poloId).stream().map(this::toDTOComPercentual).toList();
    }

    @Transactional(readOnly = true)
    public MetaArrecadacaoDTO buscarPorId(Long id) {
        return toDTOComPercentual(buscarEntidade(id));
    }

    /** RF12 — Definir metas de arrecadação */
    @Transactional
    public MetaArrecadacaoDTO cadastrar(MetaArrecadacaoDTO dto) {
        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new NegocioException("Data fim deve ser posterior à data início");
        }
        Polo polo = poloService.buscarEntidade(dto.getPoloId());
        MetaArrecadacao meta = MetaArrecadacao.builder()
                .polo(polo)
                .metaKg(dto.getMetaKg())
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .descricao(dto.getDescricao())
                .build();
        return toDTOComPercentual(metaArrecadacaoRepository.save(meta));
    }

    @Transactional
    public MetaArrecadacaoDTO atualizar(Long id, MetaArrecadacaoDTO dto) {
        MetaArrecadacao meta = buscarEntidade(id);
        meta.setPolo(poloService.buscarEntidade(dto.getPoloId()));
        meta.setMetaKg(dto.getMetaKg());
        meta.setDataInicio(dto.getDataInicio());
        meta.setDataFim(dto.getDataFim());
        meta.setDescricao(dto.getDescricao());
        return toDTOComPercentual(metaArrecadacaoRepository.save(meta));
    }

    @Transactional
    public void excluir(Long id) {
        if (!metaArrecadacaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Meta não encontrada: " + id);
        }
        metaArrecadacaoRepository.deleteById(id);
    }

    public MetaArrecadacao buscarEntidade(Long id) {
        return metaArrecadacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada: " + id));
    }

    private MetaArrecadacaoDTO toDTOComPercentual(MetaArrecadacao m) {
        Double arrecadado = registroResiduoRepository.somarPesoPorPolo(m.getPolo().getId());
        if (arrecadado == null) {
            arrecadado = 0.0;
        }
        double percentual = m.getMetaKg() > 0 ? (arrecadado / m.getMetaKg()) * 100.0 : 0.0;
        return MetaArrecadacaoDTO.builder()
                .id(m.getId())
                .poloId(m.getPolo().getId())
                .poloNome(m.getPolo().getNome())
                .metaKg(m.getMetaKg())
                .dataInicio(m.getDataInicio())
                .dataFim(m.getDataFim())
                .descricao(m.getDescricao())
                .quantidadeArrecadadaKg(Math.round(arrecadado * 100.0) / 100.0)
                .percentualAtingimento(Math.round(percentual * 100.0) / 100.0)
                .build();
    }
}
