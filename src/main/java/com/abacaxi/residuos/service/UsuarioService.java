package com.abacaxi.residuos.service;

import com.abacaxi.residuos.dto.LoginRequestDTO;
import com.abacaxi.residuos.dto.LoginResponseDTO;
import com.abacaxi.residuos.dto.UsuarioDTO;
import com.abacaxi.residuos.exception.NegocioException;
import com.abacaxi.residuos.exception.RecursoNaoEncontradoException;
import com.abacaxi.residuos.model.Usuario;
import com.abacaxi.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PoloService poloService;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional
    public UsuarioDTO cadastrar(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new NegocioException("E-mail já cadastrado: " + dto.getEmail());
        }
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new NegocioException("Senha é obrigatória");
        }
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .perfil(dto.getPerfil())
                .polo(dto.getPoloId() != null ? poloService.buscarEntidade(dto.getPoloId()) : null)
                .ativo(true)
                .tokenAcesso(UUID.randomUUID().toString().replace("-", ""))
                .build();
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarEntidade(id);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(dto.getSenha());
        }
        if (dto.getPoloId() != null) {
            usuario.setPolo(poloService.buscarEntidade(dto.getPoloId()));
        }
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NegocioException("Credenciais inválidas"));
        if (!usuario.getAtivo() || !usuario.getSenha().equals(dto.getSenha())) {
            throw new NegocioException("Credenciais inválidas");
        }
        return LoginResponseDTO.builder()
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .tokenAcesso(usuario.getTokenAcesso())
                .build();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorToken(String token) {
        return usuarioRepository.findByTokenAcesso(token)
                .orElseThrow(() -> new NegocioException("Token de acesso inválido"));
    }

    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    private UsuarioDTO toDTO(Usuario u) {
        return UsuarioDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .perfil(u.getPerfil())
                .poloId(u.getPolo() != null ? u.getPolo().getId() : null)
                .poloNome(u.getPolo() != null ? u.getPolo().getNome() : null)
                .ativo(u.getAtivo())
                .tokenAcesso(u.getTokenAcesso())
                .build();
    }
}
