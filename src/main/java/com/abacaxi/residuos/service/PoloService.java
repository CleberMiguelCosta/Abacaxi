package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.PoloDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.Polo;
import com.abacaxi.residuos.repository.PoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoloService {

    private final PoloRepository poloRepository;

    @Transactional(readOnly = true)
    public List<PoloDTO> listarTodos() {
        return poloRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<PoloDTO> listarAtivos() {
        return poloRepository.findByAtivoTrue().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PoloDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional
    public PoloDTO cadastrar(PoloDTO dto) {
        if (poloRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new NegocioException("Já existe um polo com o nome: " + dto.getNome());
        }
        Polo polo = toEntity(dto);
        polo.setId(null);
        polo.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return toDTO(poloRepository.save(polo));
    }

    @Transactional
    public PoloDTO atualizar(Long id, PoloDTO dto) {
        Polo polo = buscarEntidade(id);
        polo.setNome(dto.getNome());
        polo.setEndereco(dto.getEndereco());
        polo.setCidade(dto.getCidade());
        polo.setEstado(dto.getEstado());
        polo.setTelefone(dto.getTelefone());
        polo.setResponsavel(dto.getResponsavel());
        if (dto.getAtivo() != null) {
            polo.setAtivo(dto.getAtivo());
        }
        return toDTO(poloRepository.save(polo));
    }

    @Transactional
    public void excluir(Long id) {
        Polo polo = buscarEntidade(id);
        polo.setAtivo(false);
        poloRepository.save(polo);
    }

    public Polo buscarEntidade(Long id) {
        return poloRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Polo não encontrado: " + id));
    }

    private PoloDTO toDTO(Polo p) {
        return PoloDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .endereco(p.getEndereco())
                .cidade(p.getCidade())
                .estado(p.getEstado())
                .telefone(p.getTelefone())
                .responsavel(p.getResponsavel())
                .ativo(p.getAtivo())
                .build();
    }

    private Polo toEntity(PoloDTO dto) {
        return Polo.builder()
                .nome(dto.getNome())
                .endereco(dto.getEndereco())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .telefone(dto.getTelefone())
                .responsavel(dto.getResponsavel())
                .ativo(dto.getAtivo())
                .build();
    }
}
