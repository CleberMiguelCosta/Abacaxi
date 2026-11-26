package com.abacaxi.residuos.repository;

import com.abacaxi.residuos.model.MetaArrecadacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaArrecadacaoRepository extends JpaRepository<MetaArrecadacao, Long> {

    List<MetaArrecadacao> findByPoloId(Long poloId);
}
