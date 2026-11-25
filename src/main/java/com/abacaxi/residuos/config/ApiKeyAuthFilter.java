package com.abacaxi.residuos.config;

import com.abacaxi.residuos.model.Usuario;
import com.abacaxi.residuos.model.enums.PerfilUsuario;
import com.abacaxi.residuos.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Autenticação por token/chave de acesso (RNF03 / RNF04).
 * Header: Authorization: Bearer &lt;token&gt;  ou  X-API-KEY: &lt;chave&gt;
 */
@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    @Value("${app.security.api-key}")
    private String apiKeyGlobal;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = extrairToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (apiKeyGlobal.equals(token)) {
                var auth = new UsernamePasswordAuthenticationToken(
                        "api-key", null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"),
                                new SimpleGrantedAuthority("ROLE_RESPONSAVEL_VENDAS"),
                                new SimpleGrantedAuthority("ROLE_GESTOR")
                        ));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                Optional<Usuario> usuario = usuarioRepository.findByTokenAcesso(token);
                if (usuario.isPresent() && Boolean.TRUE.equals(usuario.get().getAtivo())) {
                    PerfilUsuario perfil = usuario.get().getPerfil();
                    var auth = new UsernamePasswordAuthenticationToken(
                            usuario.get().getEmail(), null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name())));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7).trim();
        }
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null && !apiKey.isBlank()) {
            return apiKey.trim();
        }
        return null;
    }
}
