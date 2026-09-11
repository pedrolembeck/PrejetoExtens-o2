package com.doacoes.repository;

import com.doacoes.entity.TipoDoacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDoacaoRepository
        extends JpaRepository<TipoDoacao, Long> {
}