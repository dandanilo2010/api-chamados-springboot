package com.example.agendamento_consulta.repository;

import com.example.agendamento_consulta.entity.ChamadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<ChamadoEntity, Long> {

    Page<ChamadoEntity> findByUsuarioEmail(String email, Pageable pageable);}
