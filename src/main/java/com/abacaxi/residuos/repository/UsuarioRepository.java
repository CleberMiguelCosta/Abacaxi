package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.Usuario;
import com.abacaxi.residuos.model.enums.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenAcesso(String tokenAcesso);

    List<Usuario> findByPerfil(PerfilUsuario perfil);

    boolean existsByEmailIgnoreCase(String email);
}
