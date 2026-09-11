package com.doacoes.repository;

import com.doacoes.entity.Distribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistribuicaoRepository
        extends JpaRepository<Distribuicao, Long> {
}