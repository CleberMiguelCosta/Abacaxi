package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.VendaDTO;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.MovimentacaoFinanceira;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.model.TipoResiduo;
import com.abacaxi.residuos.model.Venda;
import com.abacaxi.residuos.model.enums.TipoMovimentacaoFinanceira;
import com.abacaxi.residuos.repository.MovimentacaoFinanceiraRepository;
import com.abacaxi.residuos.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
    private final PoloService poloService;
    private final TipoResiduoService tipoResiduoService;
    private final EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public List<VendaDTO> listarTodos() {
        return vendaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VendaDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    /** Gerenciar vendas + atualiza estoque e receita financeira */
    @Transactional
    public VendaDTO cadastrar(VendaDTO dto) {
        Polo polo = poloService.buscarEntidade(dto.getPoloId());
        TipoResiduo tipo = tipoResiduoService.buscarEntidade(dto.getTipoResiduoId());

        estoqueService.saida(polo, tipo, dto.getQuantidadeKg());

        BigDecimal valorTotal = dto.getValorUnitario()
                .multiply(BigDecimal.valueOf(dto.getQuantidadeKg()))
                .setScale(2, RoundingMode.HALF_UP);

        Venda venda = Venda.builder()
                .tipoResiduo(tipo)
                .polo(polo)
                .quantidadeKg(dto.getQuantidadeKg())
                .valorUnitario(dto.getValorUnitario())
                .valorTotal(valorTotal)
                .dataVenda(dto.getDataVenda())
                .comprador(dto.getComprador())
                .observacoes(dto.getObservacoes())
                .build();

        venda = vendaRepository.save(venda);

        movimentacaoFinanceiraRepository.save(MovimentacaoFinanceira.builder()
                .tipo(TipoMovimentacaoFinanceira.RECEITA)
                .descricao("Venda de " + tipo.getNome() + " — " + dto.getQuantidadeKg() + " kg")
                .valor(valorTotal)
                .dataMovimentacao(dto.getDataVenda())
                .polo(polo)
                .venda(venda)
                .build());

        return toDTO(venda);
    }

    @Transactional
    public VendaDTO atualizar(Long id, VendaDTO dto) {
        Venda venda = buscarEntidade(id);
        venda.setComprador(dto.getComprador());
        venda.setObservacoes(dto.getObservacoes());
        venda.setDataVenda(dto.getDataVenda());
        if (dto.getValorUnitario() != null) {
            venda.setValorUnitario(dto.getValorUnitario());
            venda.setValorTotal(dto.getValorUnitario()
                    .multiply(BigDecimal.valueOf(venda.getQuantidadeKg()))
                    .setScale(2, RoundingMode.HALF_UP));
        }
        return toDTO(vendaRepository.save(venda));
    }

    @Transactional
    public void excluir(Long id) {
        if (!vendaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Venda não encontrada: " + id);
        }
        vendaRepository.deleteById(id);
    }

    public Venda buscarEntidade(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada: " + id));
    }

    private VendaDTO toDTO(Venda v) {
        return VendaDTO.builder()
                .id(v.getId())
                .tipoResiduoId(v.getTipoResiduo().getId())
                .tipoResiduoNome(v.getTipoResiduo().getNome())
                .poloId(v.getPolo().getId())
                .poloNome(v.getPolo().getNome())
                .quantidadeKg(v.getQuantidadeKg())
                .valorUnitario(v.getValorUnitario())
                .valorTotal(v.getValorTotal())
                .dataVenda(v.getDataVenda())
                .comprador(v.getComprador())
                .observacoes(v.getObservacoes())
                .build();
    }
}
