package com.example.agendamento_consulta.service;

import com.example.agendamento_consulta.dto.UsuarioDTO;
import com.example.agendamento_consulta.entity.UsuarioEntity;
import com.example.agendamento_consulta.enums.RoleUsuario;
import com.example.agendamento_consulta.exception.UsuarioNotFoundException;
import com.example.agendamento_consulta.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO criarUsuario(UsuarioDTO dto) {

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setRole(RoleUsuario.ROLE_USER);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioDTO(usuarioSalvo);
    }

    public Page<UsuarioDTO> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(UsuarioDTO::new);
    }


    public UsuarioDTO buscarPorId(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        return new UsuarioDTO(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO dto) {
        UsuarioEntity usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setSenha(passwordEncoder.encode(dto.getSenha()));

        UsuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return new UsuarioDTO(usuarioAtualizado);
    }

    public void deletarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        usuarioRepository.delete(usuario);
    }
}