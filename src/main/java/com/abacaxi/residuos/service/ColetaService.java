package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.AtualizarStatusColetaDTO;
import com.abacaxi.residuos.dto.ColetaDTO;
import com.abacaxi.residuos.dto.EventoCalendarioDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.Coleta;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.enums.StatusColeta;
import com.abacaxi.residuos.repository.ColetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColetaService {

    private final ColetaRepository coletaRepository;
    private final PoloService poloService;
    private final TipoResiduoService tipoResiduoService;

    @Transactional(readOnly = true)
    public List<ColetaDTO> listar(Long poloId, StatusColeta status, LocalDate dataInicio, LocalDate dataFim) {
        return coletaRepository.filtrar(poloId, status, dataInicio, dataFim).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ColetaDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    /** RF01 — Cadastrar coleta */
    @Transactional
    public ColetaDTO cadastrar(ColetaDTO dto) {
        Polo polo = poloService.buscarEntidade(dto.getPoloId());
        TipoResiduo tipo = tipoResiduoService.buscarEntidade(dto.getTipoResiduoId());

        Coleta coleta = Coleta.builder()
                .polo(polo)
                .dataColeta(dto.getDataColeta())
                .horario(dto.getHorario())
                .tipoResiduo(tipo)
                .quantidadePrevistaKg(dto.getQuantidadePrevistaKg())
                .status(StatusColeta.AGENDADA)
                .localColeta(dto.getLocalColeta() != null ? dto.getLocalColeta() : polo.getEndereco())
                .observacoes(dto.getObservacoes())
                .build();

        return toDTO(coletaRepository.save(coleta));
    }

    /** RF03 — Editar coleta */
    @Transactional
    public ColetaDTO atualizar(Long id, ColetaDTO dto) {
        Coleta coleta = buscarEntidade(id);
        if (coleta.getStatus() == StatusColeta.CANCELADA || coleta.getStatus() == StatusColeta.CONCLUIDA) {
            throw new NegocioException("Não é possível editar coleta com status " + coleta.getStatus());
        }
        coleta.setPolo(poloService.buscarEntidade(dto.getPoloId()));
        coleta.setTipoResiduo(tipoResiduoService.buscarEntidade(dto.getTipoResiduoId()));
        coleta.setDataColeta(dto.getDataColeta());
        coleta.setHorario(dto.getHorario());
        coleta.setQuantidadePrevistaKg(dto.getQuantidadePrevistaKg());
        coleta.setLocalColeta(dto.getLocalColeta());
        coleta.setObservacoes(dto.getObservacoes());
        return toDTO(coletaRepository.save(coleta));
    }

    /** RF04 — Cancelar coleta */
    @Transactional
    public ColetaDTO cancelar(Long id) {
        Coleta coleta = buscarEntidade(id);
        if (coleta.getStatus() == StatusColeta.CONCLUIDA) {
            throw new NegocioException("Não é possível cancelar uma coleta já concluída");
        }
        if (coleta.getStatus() == StatusColeta.CANCELADA) {
            throw new NegocioException("Coleta já está cancelada");
        }
        coleta.setStatus(StatusColeta.CANCELADA);
        return toDTO(coletaRepository.save(coleta));
    }

    /** RF05 — Atualizar status */
    @Transactional
    public ColetaDTO atualizarStatus(Long id, AtualizarStatusColetaDTO dto) {
        Coleta coleta = buscarEntidade(id);
        coleta.setStatus(dto.getStatus());
        return toDTO(coletaRepository.save(coleta));
    }

    /** RF07 — Calendário de coletas */
    @Transactional(readOnly = true)
    public List<EventoCalendarioDTO> calendario(LocalDate inicio, LocalDate fim) {
        return coletaRepository.findByDataColetaBetween(inicio, fim).stream()
                .map(c -> EventoCalendarioDTO.builder()
                        .coletaId(c.getId())
                        .data(c.getDataColeta())
                        .horario(c.getHorario())
                        .poloNome(c.getPolo().getNome())
                        .local(c.getLocalColeta())
                        .quantidadeKg(c.getQuantidadePrevistaKg())
                        .status(c.getStatus())
                        .tipoResiduo(c.getTipoResiduo().getNome())
                        .build())
                .toList();
    }

    @Transactional
    public void excluir(Long id) {
        if (!coletaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Coleta não encontrada: " + id);
        }
        coletaRepository.deleteById(id);
    }

    public Coleta buscarEntidade(Long id) {
        return coletaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Coleta não encontrada: " + id));
    }

    private ColetaDTO toDTO(Coleta c) {
        return ColetaDTO.builder()
                .id(c.getId())
                .poloId(c.getPolo().getId())
                .poloNome(c.getPolo().getNome())
                .dataColeta(c.getDataColeta())
                .horario(c.getHorario())
                .tipoResiduoId(c.getTipoResiduo().getId())
                .tipoResiduoNome(c.getTipoResiduo().getNome())
                .quantidadePrevistaKg(c.getQuantidadePrevistaKg())
                .status(c.getStatus())
                .localColeta(c.getLocalColeta())
                .observacoes(c.getObservacoes())
                .build();
    }
}
