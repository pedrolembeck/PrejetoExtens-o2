package com.doacoes.repository;

import com.doacoes.entity.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    @Query("""
            SELECT d
            FROM Doacao d
            WHERE LOWER(d.nomeDoador) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.descricaoItem) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(d.unidade) LIKE LOWER(CONCAT('%', :termo, '%'))
            """)
    List<Doacao> pesquisar(@Param("termo") String termo);
}