package com.example.agendamento_consulta.exception;

public class ChamadoNotFoundException extends RuntimeException {

    public ChamadoNotFoundException(Long id) {
        super("Chamado não encontrado com id: " + id);
    }
}