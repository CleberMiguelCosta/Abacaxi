package com.abacaxi.residuos.config;

import com.abacaxi.residuos.model.*;
import com.abacaxi.residuos.model.enums.*;
import com.abacaxi.residuos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PoloRepository poloRepository;
    private final TipoResiduoRepository tipoResiduoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ColetaRepository coletaRepository;
    private final InformacaoReaproveitamentoRepository infoRepository;
    private final MovimentacaoFinanceiraRepository financeiroRepository;
    private final MetaArrecadacaoRepository metaRepository;
    private final EstoqueRepository estoqueRepository;

    @Override
    public void run(String... args) {
        if (poloRepository.count() > 0) {
            return;
        }

        Polo polo1 = poloRepository.save(Polo.builder()
                .nome("Polo Frutal")
                .endereco("Rodovia BR-153, Km 12")
                .cidade("Frutal")
                .estado("MG")
                .telefone("(34) 99999-0001")
                .responsavel("Maria Silva")
                .ativo(true)
                .build());

        Polo polo2 = poloRepository.save(Polo.builder()
                .nome("Polo Ituverava")
                .endereco("Av. dos Agricultores, 450")
                .cidade("Ituverava")
                .estado("SP")
                .telefone("(16) 98888-0002")
                .responsavel("João Santos")
                .ativo(true)
                .build());

        TipoResiduo casca = tipoResiduoRepository.save(TipoResiduo.builder()
                .nome("Casca de Abacaxi")
                .categoria(CategoriaResiduo.CASCA)
                .descricao("Casca resultante do processamento")
                .pesoMedioEsperadoKg(80.0)
                .ativo(true)
                .build());

        TipoResiduo bagaco = tipoResiduoRepository.save(TipoResiduo.builder()
                .nome("Bagaço de Abacaxi")
                .categoria(CategoriaResiduo.BAGACO)
                .descricao("Bagaço após extração do suco")
                .pesoMedioEsperadoKg(120.0)
                .ativo(true)
                .build());

        TipoResiduo coroa = tipoResiduoRepository.save(TipoResiduo.builder()
                .nome("Coroa de Abacaxi")
                .categoria(CategoriaResiduo.COROA)
                .descricao("Coroa removida no beneficiamento")
                .pesoMedioEsperadoKg(40.0)
                .ativo(true)
                .build());

        tipoResiduoRepository.save(TipoResiduo.builder()
                .nome("Folhas de Abacaxi")
                .categoria(CategoriaResiduo.FOLHAS)
                .descricao("Folhas da planta e da coroa")
                .pesoMedioEsperadoKg(60.0)
                .ativo(true)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Admin Sistema")
                .email("admin@abacaxi.com")
                .senha("admin123")
                .perfil(PerfilUsuario.ADMINISTRADOR)
                .ativo(true)
                .tokenAcesso("token-admin-001")
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Maria Silva")
                .email("maria@polo.com")
                .senha("polo123")
                .perfil(PerfilUsuario.RESPONSAVEL_POLO)
                .polo(polo1)
                .ativo(true)
                .tokenAcesso("token-polo-001")
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Carlos Operador")
                .email("operador@abacaxi.com")
                .senha("op123")
                .perfil(PerfilUsuario.OPERADOR_COLETA)
                .polo(polo1)
                .ativo(true)
                .tokenAcesso("token-operador-001")
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Ana Vendas")
                .email("vendas@abacaxi.com")
                .senha("vendas123")
                .perfil(PerfilUsuario.RESPONSAVEL_VENDAS)
                .ativo(true)
                .tokenAcesso("token-vendas-001")
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Pedro Gestor")
                .email("gestor@abacaxi.com")
                .senha("gestor123")
                .perfil(PerfilUsuario.GESTOR)
                .ativo(true)
                .tokenAcesso(UUID.randomUUID().toString().replace("-", ""))
                .build());

        coletaRepository.save(Coleta.builder()
                .polo(polo1)
                .dataColeta(LocalDate.now().plusDays(1))
                .horario(LocalTime.of(8, 0))
                .tipoResiduo(casca)
                .quantidadePrevistaKg(100.0)
                .status(StatusColeta.AGENDADA)
                .localColeta(polo1.getEndereco())
                .observacoes("Coleta matinal")
                .build());

        coletaRepository.save(Coleta.builder()
                .polo(polo2)
                .dataColeta(LocalDate.now().plusDays(2))
                .horario(LocalTime.of(14, 30))
                .tipoResiduo(bagaco)
                .quantidadePrevistaKg(150.0)
                .status(StatusColeta.AGENDADA)
                .localColeta(polo2.getEndereco())
                .build());

        metaRepository.save(MetaArrecadacao.builder()
                .polo(polo1)
                .metaKg(5000.0)
                .dataInicio(LocalDate.now().withDayOfMonth(1))
                .dataFim(LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1))
                .descricao("Meta mensal Polo Frutal")
                .build());

        estoqueRepository.save(Estoque.builder()
                .polo(polo1)
                .tipoResiduo(casca)
                .quantidadeKg(15.0)
                .nivel(NivelEstoque.CRITICO)
                .build());

        estoqueRepository.save(Estoque.builder()
                .polo(polo1)
                .tipoResiduo(bagaco)
                .quantidadeKg(45.0)
                .nivel(NivelEstoque.BAIXO)
                .build());

        estoqueRepository.save(Estoque.builder()
                .polo(polo2)
                .tipoResiduo(coroa)
                .quantidadeKg(200.0)
                .nivel(NivelEstoque.NORMAL)
                .build());

        infoRepository.save(InformacaoReaproveitamento.builder()
                .titulo("Compostagem de casca de abacaxi")
                .categoriaResiduo(CategoriaResiduo.CASCA)
                .tipoDestino(TipoDestino.COMPOSTAGEM)
                .conteudoTecnico("A casca do abacaxi é rica em fibra e nutrientes. "
                        + "Pode ser compostada em pilhas aeróbicas com relação C/N adequada.")
                .orientacoes("Triturar a casca, misturar com material seco e revolver a cada 7 dias.")
                .ativo(true)
                .build());

        infoRepository.save(InformacaoReaproveitamento.builder()
                .titulo("Bagaço para ração animal")
                .categoriaResiduo(CategoriaResiduo.BAGACO)
                .tipoDestino(TipoDestino.FABRICACAO_RACAO)
                .conteudoTecnico("O bagaço pode ser desidratado e incorporado à ração de ruminantes "
                        + "como fonte de fibra digestível.")
                .orientacoes("Secar até 12% de umidade e moer antes da mistura.")
                .ativo(true)
                .build());

        infoRepository.save(InformacaoReaproveitamento.builder()
                .titulo("Fibras da folha para têxteis")
                .categoriaResiduo(CategoriaResiduo.FOLHAS)
                .tipoDestino(TipoDestino.APROVEITAMENTO_FIBRAS)
                .conteudoTecnico("As folhas (especialmente da coroa) possuem fibras longas "
                        + "utilizadas em artesanato e tecidos sustentáveis (Piña cloth).")
                .orientacoes("Extrair fibras por maceração e secar à sombra.")
                .ativo(true)
                .build());

        financeiroRepository.save(MovimentacaoFinanceira.builder()
                .tipo(TipoMovimentacaoFinanceira.CUSTO)
                .descricao("Combustível para coleta")
                .valor(new BigDecimal("350.00"))
                .dataMovimentacao(LocalDate.now().minusDays(5))
                .polo(polo1)
                .build());

        financeiroRepository.save(MovimentacaoFinanceira.builder()
                .tipo(TipoMovimentacaoFinanceira.RECEITA)
                .descricao("Venda inicial de casca para compostagem")
                .valor(new BigDecimal("800.00"))
                .dataMovimentacao(LocalDate.now().minusDays(2))
                .polo(polo1)
                .build());
    }
}
