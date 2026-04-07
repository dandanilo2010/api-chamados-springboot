package com.example.agendamento_consulta.dto;

import com.example.agendamento_consulta.entity.ChamadoEntity;
import com.example.agendamento_consulta.enums.PrioridadeChamado;
import com.example.agendamento_consulta.enums.StatusChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 5, max = 1000, message = "A descrição deve ter entre 5 e 1000 caracteres")
    private String descricao;

    @NotNull(message = "O status é obrigatório")
    private StatusChamado status;

    @NotNull(message = "A prioridade é obrigatória")
    private PrioridadeChamado prioridade;

    private LocalDateTime dataCriacao;

    private Long usuarioId;

    private String nomeUsuario;

    public ChamadoDTO(ChamadoEntity chamado) {
        this.id = chamado.getId();
        this.titulo = chamado.getTitulo();
        this.descricao = chamado.getDescricao();
        this.status = chamado.getStatus();
        this.prioridade = chamado.getPrioridade();
        this.dataCriacao = chamado.getDataCriacao();

        if (chamado.getUsuario() != null) {
            this.usuarioId = chamado.getUsuario().getId();
            this.nomeUsuario = chamado.getUsuario().getNome();
        }
    }
}