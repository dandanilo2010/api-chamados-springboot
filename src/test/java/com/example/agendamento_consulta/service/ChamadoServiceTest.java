package com.example.agendamento_consulta.service;

import com.example.agendamento_consulta.dto.ChamadoDTO;
import com.example.agendamento_consulta.entity.ChamadoEntity;
import com.example.agendamento_consulta.entity.UsuarioEntity;
import com.example.agendamento_consulta.enums.PrioridadeChamado;
import com.example.agendamento_consulta.enums.RoleUsuario;
import com.example.agendamento_consulta.enums.StatusChamado;
import com.example.agendamento_consulta.exception.BusinessException;
import com.example.agendamento_consulta.repository.ChamadoRepository;
import com.example.agendamento_consulta.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveCriarChamadoComUsuarioAutenticado() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("danilo@email.com", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Danilo");
        usuario.setEmail("danilo@email.com");
        usuario.setSenha("123456");
        usuario.setRole(RoleUsuario.ROLE_USER);

        ChamadoDTO dto = new ChamadoDTO();
        dto.setTitulo("Erro no sistema");
        dto.setDescricao("Sistema trava ao clicar no botão salvar");
        dto.setPrioridade(PrioridadeChamado.ALTA);
        dto.setStatus(StatusChamado.FINALIZADO);

        when(usuarioRepository.findByEmail("danilo@email.com")).thenReturn(Optional.of(usuario));

        ChamadoEntity chamadoSalvo = new ChamadoEntity();
        chamadoSalvo.setId(10L);
        chamadoSalvo.setTitulo(dto.getTitulo());
        chamadoSalvo.setDescricao(dto.getDescricao());
        chamadoSalvo.setPrioridade(PrioridadeChamado.ALTA);
        chamadoSalvo.setStatus(StatusChamado.ABERTO);
        chamadoSalvo.setUsuario(usuario);

        when(chamadoRepository.save(any(ChamadoEntity.class))).thenReturn(chamadoSalvo);

        ChamadoDTO resultado = chamadoService.criarChamado(dto);

        assertNotNull(resultado);
        assertEquals("Erro no sistema", resultado.getTitulo());
        assertEquals("Sistema trava ao clicar no botão salvar", resultado.getDescricao());
        assertEquals(StatusChamado.ABERTO, resultado.getStatus());
        assertEquals(PrioridadeChamado.ALTA, resultado.getPrioridade());

        ArgumentCaptor<ChamadoEntity> captor = ArgumentCaptor.forClass(ChamadoEntity.class);
        verify(chamadoRepository).save(captor.capture());

        ChamadoEntity chamadoCapturado = captor.getValue();

        assertEquals("Erro no sistema", chamadoCapturado.getTitulo());
        assertEquals("Sistema trava ao clicar no botão salvar", chamadoCapturado.getDescricao());
        assertEquals(StatusChamado.ABERTO, chamadoCapturado.getStatus());
        assertEquals(PrioridadeChamado.ALTA, chamadoCapturado.getPrioridade());
        assertEquals(usuario, chamadoCapturado.getUsuario());

        verify(usuarioRepository).findByEmail("danilo@email.com");
        verify(chamadoRepository).save(any(ChamadoEntity.class));
    }

    @Test
    void naoDeveCriarChamadoComPrioridadeAltaEDescricaoCurta() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("danilo@email.com", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Danilo");
        usuario.setEmail("danilo@email.com");
        usuario.setRole(RoleUsuario.ROLE_USER);

        ChamadoDTO dto = new ChamadoDTO();
        dto.setTitulo("Erro crítico");
        dto.setDescricao("curta");
        dto.setPrioridade(PrioridadeChamado.ALTA);

        when(usuarioRepository.findByEmail("danilo@email.com")).thenReturn(Optional.of(usuario));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> chamadoService.criarChamado(dto)
        );

        assertEquals("Chamados com prioridade ALTA precisam de uma descrição mais detalhada", exception.getMessage());

        verify(usuarioRepository).findByEmail("danilo@email.com");
        verify(chamadoRepository, never()).save(any(ChamadoEntity.class));
    }
}