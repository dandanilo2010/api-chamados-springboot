package com.example.agendamento_consulta.controller;

import com.example.agendamento_consulta.dto.ChamadoDTO;
import com.example.agendamento_consulta.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoDTO> criarChamado(@RequestBody @Valid ChamadoDTO chamado) {
        return ResponseEntity.status(201)
                .body(chamadoService.criarChamado(chamado));
    }

    @GetMapping
    public ResponseEntity<Page<ChamadoDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(chamadoService.listarChamados(pageable));
    }

    @GetMapping("/meus")
    public ResponseEntity<Page<ChamadoDTO>> listarMeusChamados(Pageable pageable) {
        return ResponseEntity.ok(chamadoService.listarMeusChamados(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadoDTO> atualizar(@PathVariable Long id,
                                                @RequestBody @Valid ChamadoDTO chamado) {
        return ResponseEntity.ok(chamadoService.atualizarChamado(id, chamado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chamadoService.deletarChamado(id);
        return ResponseEntity.noContent().build();
    }
}