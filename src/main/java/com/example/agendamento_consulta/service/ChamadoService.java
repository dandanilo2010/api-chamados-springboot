package com.example.agendamento_consulta.service;

import com.example.agendamento_consulta.dto.ChamadoDTO;
import com.example.agendamento_consulta.entity.ChamadoEntity;
import com.example.agendamento_consulta.entity.UsuarioEntity;
import com.example.agendamento_consulta.enums.PrioridadeChamado;
import com.example.agendamento_consulta.enums.StatusChamado;
import com.example.agendamento_consulta.exception.BusinessException;
import com.example.agendamento_consulta.exception.ChamadoNotFoundException;
import com.example.agendamento_consulta.exception.UsuarioNotFoundException;
import com.example.agendamento_consulta.repository.ChamadoRepository;
import com.example.agendamento_consulta.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ChamadoDTO criarChamado(ChamadoDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = authentication.getName();

        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário autenticado não encontrado"));

        if (dto.getPrioridade() == PrioridadeChamado.ALTA && dto.getDescricao().length() < 15) {
            throw new BusinessException("Chamados com prioridade ALTA precisam de uma descrição mais detalhada");
        }

        ChamadoEntity chamado = new ChamadoEntity();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setUsuario(usuario);

        ChamadoEntity salvo = chamadoRepository.save(chamado);

        return new ChamadoDTO(salvo);
    }

    public Page<ChamadoDTO> listarChamados(Pageable pageable) {
        return chamadoRepository.findAll(pageable)
                .map(ChamadoDTO::new);
    }

    public Page<ChamadoDTO> listarMeusChamados(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = authentication.getName();

        return chamadoRepository.findByUsuarioEmail(emailUsuarioLogado, pageable)
                .map(ChamadoDTO::new);
    }

    public ChamadoDTO buscarPorId(Long id) {
        ChamadoEntity chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNotFoundException(id));

        return new ChamadoDTO(chamado);
    }

    public ChamadoDTO atualizarChamado(Long id, ChamadoDTO dto) {
        ChamadoEntity chamadoExistente = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNotFoundException(id));

        if (chamadoExistente.getStatus() == StatusChamado.FINALIZADO) {
            throw new BusinessException("Chamado finalizado não pode ser alterado");
        }

        chamadoExistente.setTitulo(dto.getTitulo());
        chamadoExistente.setDescricao(dto.getDescricao());
        chamadoExistente.setStatus(dto.getStatus());
        chamadoExistente.setPrioridade(dto.getPrioridade());

        ChamadoEntity atualizado = chamadoRepository.save(chamadoExistente);

        return new ChamadoDTO(atualizado);
    }

    public void deletarChamado(Long id) {
        ChamadoEntity chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNotFoundException(id));

        chamadoRepository.delete(chamado);
    }
}