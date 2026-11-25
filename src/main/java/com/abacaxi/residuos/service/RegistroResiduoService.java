package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.RegistroResiduoDTO;
import com.abacaxi.residuos.dto.RendimentoDTO;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.Coleta;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.model.RegistroResiduo;
import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.enums.StatusColeta;
import com.abacaxi.residuos.repository.DestinoResiduoRepository;
import com.abacaxi.residuos.repository.RegistroResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistroResiduoService {

    private final RegistroResiduoRepository registroResiduoRepository;
    private final DestinoResiduoRepository destinoResiduoRepository;
    private final PoloService poloService;
    private final TipoResiduoService tipoResiduoService;
    private final ColetaService coletaService;
    private final EstoqueService estoqueService;

    @Value("${app.qualidade.margem-peso-percentual:15}")
    private double margemPesoPercentual;

    @Transactional(readOnly = true)
    public List<RegistroResiduoDTO> listarTodos() {
        return registroResiduoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<RegistroResiduoDTO> listarAlertasQualidade() {
        return registroResiduoRepository.findByAlertaQualidadeTrue().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public RegistroResiduoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    /** RF08 + RF15 — Classificar, pesar e validar peso */
    @Transactional
    public RegistroResiduoDTO cadastrar(RegistroResiduoDTO dto) {
        TipoResiduo tipo = tipoResiduoService.buscarEntidade(dto.getTipoResiduoId());
        Polo polo = poloService.buscarEntidade(dto.getPoloId());

        Coleta coleta = null;
        if (dto.getColetaId() != null) {
            coleta = coletaService.buscarEntidade(dto.getColetaId());
            if (coleta.getStatus() == StatusColeta.AGENDADA) {
                coleta.setStatus(StatusColeta.EM_ANDAMENTO);
            }
        }

        String mensagemAlerta = null;
        boolean alerta = false;
        double esperado = tipo.getPesoMedioEsperadoKg();
        double desvio = Math.abs(dto.getPesoKg() - esperado) / esperado * 100.0;
        if (desvio > margemPesoPercentual) {
            alerta = true;
            mensagemAlerta = String.format(
                    "Peso %.2f kg fora da margem aceitável (±%.0f%%). Esperado: %.2f kg (desvio: %.1f%%)",
                    dto.getPesoKg(), margemPesoPercentual, esperado, desvio);
        }

        RegistroResiduo registro = RegistroResiduo.builder()
                .coleta(coleta)
                .tipoResiduo(tipo)
                .polo(polo)
                .pesoKg(dto.getPesoKg())
                .quantidadeAbacaxiProcessadaKg(dto.getQuantidadeAbacaxiProcessadaKg())
                .dataRegistro(LocalDateTime.now())
                .alertaQualidade(alerta)
                .mensagemAlerta(mensagemAlerta)
                .observacoes(dto.getObservacoes())
                .build();

        registro = registroResiduoRepository.save(registro);
        estoqueService.entrada(polo, tipo, dto.getPesoKg());

        if (coleta != null) {
            coleta.setStatus(StatusColeta.CONCLUIDA);
        }

        return toDTO(registro);
    }

    @Transactional
    public RegistroResiduoDTO atualizar(Long id, RegistroResiduoDTO dto) {
        RegistroResiduo registro = buscarEntidade(id);
        registro.setTipoResiduo(tipoResiduoService.buscarEntidade(dto.getTipoResiduoId()));
        registro.setPolo(poloService.buscarEntidade(dto.getPoloId()));
        registro.setPesoKg(dto.getPesoKg());
        registro.setQuantidadeAbacaxiProcessadaKg(dto.getQuantidadeAbacaxiProcessadaKg());
        registro.setObservacoes(dto.getObservacoes());
        return toDTO(registroResiduoRepository.save(registro));
    }

    @Transactional
    public void excluir(Long id) {
        if (!registroResiduoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Registro de resíduo não encontrado: " + id);
        }
        registroResiduoRepository.deleteById(id);
    }

    /** RF09 — Calcular rendimento e aproveitamento */
    @Transactional(readOnly = true)
    public RendimentoDTO calcularRendimento() {
        double totalProcessado = nvl(registroResiduoRepository.somarAbacaxiProcessado());
        double totalGerados = nvl(registroResiduoRepository.somarPesoTotal());
        double totalReaproveitados = nvl(destinoResiduoRepository.somarQuantidadeReaproveitada());

        double pctGerados = totalProcessado > 0 ? (totalGerados / totalProcessado) * 100.0 : 0.0;
        double pctReaproveitados = totalGerados > 0 ? (totalReaproveitados / totalGerados) * 100.0 : 0.0;

        Map<String, Double> porCategoria = new HashMap<>();
        List<RegistroResiduo> todos = registroResiduoRepository.findAll();
        Map<String, Double> somaCat = new HashMap<>();
        for (RegistroResiduo r : todos) {
            String cat = r.getTipoResiduo().getCategoria().name();
            somaCat.merge(cat, r.getPesoKg(), Double::sum);
        }
        for (Map.Entry<String, Double> e : somaCat.entrySet()) {
            porCategoria.put(e.getKey(),
                    totalProcessado > 0 ? (e.getValue() / totalProcessado) * 100.0 : 0.0);
        }

        return RendimentoDTO.builder()
                .totalAbacaxiProcessadoKg(round(totalProcessado))
                .totalResiduosGeradosKg(round(totalGerados))
                .totalResiduosReaproveitadosKg(round(totalReaproveitados))
                .percentualResiduosGerados(round(pctGerados))
                .percentualResiduosReaproveitados(round(pctReaproveitados))
                .percentualPorCategoria(porCategoria)
                .build();
    }

    public RegistroResiduo buscarEntidade(Long id) {
        return registroResiduoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de resíduo não encontrado: " + id));
    }

    private double nvl(Double v) {
        return v == null ? 0.0 : v;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private RegistroResiduoDTO toDTO(RegistroResiduo r) {
        return RegistroResiduoDTO.builder()
                .id(r.getId())
                .coletaId(r.getColeta() != null ? r.getColeta().getId() : null)
                .tipoResiduoId(r.getTipoResiduo().getId())
                .tipoResiduoNome(r.getTipoResiduo().getNome())
                .poloId(r.getPolo().getId())
                .poloNome(r.getPolo().getNome())
                .pesoKg(r.getPesoKg())
                .quantidadeAbacaxiProcessadaKg(r.getQuantidadeAbacaxiProcessadaKg())
                .dataRegistro(r.getDataRegistro())
                .alertaQualidade(r.getAlertaQualidade())
                .mensagemAlerta(r.getMensagemAlerta())
                .observacoes(r.getObservacoes())
                .build();
    }
}
