package com.example.agendamento_consulta.service;

import com.example.agendamento_consulta.dto.UsuarioDTO;
import com.example.agendamento_consulta.entity.UsuarioEntity;
import com.example.agendamento_consulta.enums.RoleUsuario;
import com.example.agendamento_consulta.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCriarUsuarioComSenhaCriptografadaERoleUser() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Danilo");
        dto.setEmail("danilo@email.com");
        dto.setSenha("123456");

        when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");

        UsuarioEntity usuarioSalvo = new UsuarioEntity();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Danilo");
        usuarioSalvo.setEmail("danilo@email.com");
        usuarioSalvo.setSenha("senha-criptografada");
        usuarioSalvo.setRole(RoleUsuario.ROLE_USER);

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioSalvo);

        UsuarioDTO resultado = usuarioService.criarUsuario(dto);

        assertNotNull(resultado);
        assertEquals("Danilo", resultado.getNome());
        assertEquals("danilo@email.com", resultado.getEmail());
        assertEquals("senha-criptografada", resultado.getSenha());
        assertEquals(RoleUsuario.ROLE_USER, resultado.getRole());

        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(usuarioRepository).save(captor.capture());

        UsuarioEntity usuarioCapturado = captor.getValue();

        assertEquals("Danilo", usuarioCapturado.getNome());
        assertEquals("danilo@email.com", usuarioCapturado.getEmail());
        assertEquals("senha-criptografada", usuarioCapturado.getSenha());
        assertEquals(RoleUsuario.ROLE_USER, usuarioCapturado.getRole());
        assertNotNull(usuarioCapturado.getDataCriacao());

        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(UsuarioEntity.class));
    }
}