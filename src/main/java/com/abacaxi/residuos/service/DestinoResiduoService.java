package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.DestinoResiduoDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.DestinoResiduo;
import com.abacaxi.residuos.model.RegistroResiduo;
import com.abacaxi.residuos.model.enums.TipoDestino;
import com.abacaxi.residuos.repository.DestinoResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinoResiduoService {

    private final DestinoResiduoRepository destinoResiduoRepository;
    private final RegistroResiduoService registroResiduoService;

    @Transactional(readOnly = true)
    public List<DestinoResiduoDTO> listarTodos() {
        return destinoResiduoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<DestinoResiduoDTO> listarPorTipo(TipoDestino tipo) {
        return destinoResiduoRepository.findByTipoDestino(tipo).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DestinoResiduoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    /** RF10 — Registrar destino do resíduo */
    @Transactional
    public DestinoResiduoDTO cadastrar(DestinoResiduoDTO dto) {
        RegistroResiduo registro = registroResiduoService.buscarEntidade(dto.getRegistroResiduoId());
        if (dto.getQuantidadeKg() > registro.getPesoKg()) {
            throw new NegocioException(
                    "Quantidade destinadada (" + dto.getQuantidadeKg()
                            + " kg) excede o peso do registro (" + registro.getPesoKg() + " kg)");
        }

        DestinoResiduo destino = DestinoResiduo.builder()
                .registroResiduo(registro)
                .tipoDestino(dto.getTipoDestino())
                .quantidadeKg(dto.getQuantidadeKg())
                .dataDestinacao(dto.getDataDestinacao())
                .destinoDetalhado(dto.getDestinoDetalhado())
                .observacoes(dto.getObservacoes())
                .build();

        return toDTO(destinoResiduoRepository.save(destino));
    }

    @Transactional
    public DestinoResiduoDTO atualizar(Long id, DestinoResiduoDTO dto) {
        DestinoResiduo destino = buscarEntidade(id);
        destino.setRegistroResiduo(registroResiduoService.buscarEntidade(dto.getRegistroResiduoId()));
        destino.setTipoDestino(dto.getTipoDestino());
        destino.setQuantidadeKg(dto.getQuantidadeKg());
        destino.setDataDestinacao(dto.getDataDestinacao());
        destino.setDestinoDetalhado(dto.getDestinoDetalhado());
        destino.setObservacoes(dto.getObservacoes());
        return toDTO(destinoResiduoRepository.save(destino));
    }

    @Transactional
    public void excluir(Long id) {
        if (!destinoResiduoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Destino não encontrado: " + id);
        }
        destinoResiduoRepository.deleteById(id);
    }

    public DestinoResiduo buscarEntidade(Long id) {
        return destinoResiduoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Destino não encontrado: " + id));
    }

    private DestinoResiduoDTO toDTO(DestinoResiduo d) {
        return DestinoResiduoDTO.builder()
                .id(d.getId())
                .registroResiduoId(d.getRegistroResiduo().getId())
                .tipoDestino(d.getTipoDestino())
                .quantidadeKg(d.getQuantidadeKg())
                .dataDestinacao(d.getDataDestinacao())
                .destinoDetalhado(d.getDestinoDetalhado())
                .observacoes(d.getObservacoes())
                .build();
    }
}
