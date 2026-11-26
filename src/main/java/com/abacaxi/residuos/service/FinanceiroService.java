package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.MovimentacaoFinanceiraDTO;
import com.abacaxi.residuos.dto.RelatorioFinanceiroDTO;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.MovimentacaoFinanceira;
import com.abacaxi.residuos.model.enums.TipoMovimentacaoFinanceira;
import com.abacaxi.residuos.repository.MovimentacaoFinanceiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceiroService {

    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
    private final PoloService poloService;

    @Transactional(readOnly = true)
    public List<MovimentacaoFinanceiraDTO> listarTodos() {
        return movimentacaoFinanceiraRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoFinanceiraDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional
    public MovimentacaoFinanceiraDTO cadastrar(MovimentacaoFinanceiraDTO dto) {
        MovimentacaoFinanceira m = MovimentacaoFinanceira.builder()
                .tipo(dto.getTipo())
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .dataMovimentacao(dto.getDataMovimentacao())
                .polo(dto.getPoloId() != null ? poloService.buscarEntidade(dto.getPoloId()) : null)
                .build();
        return toDTO(movimentacaoFinanceiraRepository.save(m));
    }

    @Transactional
    public MovimentacaoFinanceiraDTO atualizar(Long id, MovimentacaoFinanceiraDTO dto) {
        MovimentacaoFinanceira m = buscarEntidade(id);
        m.setTipo(dto.getTipo());
        m.setDescricao(dto.getDescricao());
        m.setValor(dto.getValor());
        m.setDataMovimentacao(dto.getDataMovimentacao());
        if (dto.getPoloId() != null) {
            m.setPolo(poloService.buscarEntidade(dto.getPoloId()));
        }
        return toDTO(movimentacaoFinanceiraRepository.save(m));
    }

    @Transactional
    public void excluir(Long id) {
        if (!movimentacaoFinanceiraRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Movimentação não encontrada: " + id);
        }
        movimentacaoFinanceiraRepository.deleteById(id);
    }

    /** RF13 — Relatório financeiro consolidado */
    @Transactional(readOnly = true)
    public RelatorioFinanceiroDTO relatorio(LocalDate inicio, LocalDate fim) {
        List<MovimentacaoFinanceira> lista = (inicio != null && fim != null)
                ? movimentacaoFinanceiraRepository.findByDataMovimentacaoBetween(inicio, fim)
                : movimentacaoFinanceiraRepository.findAll();

        BigDecimal custos = lista.stream()
                .filter(m -> m.getTipo() == TipoMovimentacaoFinanceira.CUSTO)
                .map(MovimentacaoFinanceira::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal receitas = lista.stream()
                .filter(m -> m.getTipo() == TipoMovimentacaoFinanceira.RECEITA)
                .map(MovimentacaoFinanceira::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RelatorioFinanceiroDTO.builder()
                .totalCustos(custos)
                .totalReceitas(receitas)
                .lucroLiquido(receitas.subtract(custos))
                .movimentacoes(lista.stream().map(this::toDTO).toList())
                .build();
    }

    public MovimentacaoFinanceira buscarEntidade(Long id) {
        return movimentacaoFinanceiraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrada: " + id));
    }

    private MovimentacaoFinanceiraDTO toDTO(MovimentacaoFinanceira m) {
        return MovimentacaoFinanceiraDTO.builder()
                .id(m.getId())
                .tipo(m.getTipo())
                .descricao(m.getDescricao())
                .valor(m.getValor())
                .dataMovimentacao(m.getDataMovimentacao())
                .poloId(m.getPolo() != null ? m.getPolo().getId() : null)
                .poloNome(m.getPolo() != null ? m.getPolo().getNome() : null)
                .vendaId(m.getVenda() != null ? m.getVenda().getId() : null)
                .build();
    }
}
