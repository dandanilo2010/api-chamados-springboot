package com.example.agendamento_consulta.exception;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(Long id) {
        super("Usuário não encontrado com id: " + id);
    }

    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
