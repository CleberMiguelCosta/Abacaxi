package com.abacaxi.residuos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> handleNaoEncontrado(
            RecursoNaoEncontradoException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroResposta> handleNegocio(
            NegocioException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidacao(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatarErro)
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), detalhes);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGeral(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI(), null);
    }

    private String formatarErro(FieldError e) {
        return e.getField() + ": " + e.getDefaultMessage();
    }

    private ResponseEntity<ErroResposta> build(
            HttpStatus status, String mensagem, String caminho, List<String> detalhes) {
        ErroResposta body = ErroResposta.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(mensagem)
                .caminho(caminho)
                .detalhes(detalhes)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
