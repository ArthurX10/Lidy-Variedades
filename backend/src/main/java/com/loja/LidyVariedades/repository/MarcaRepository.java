package com.loja.LidyVariedades.repository;
import com.loja.LidyVariedades.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    java.util.Optional<Marca> findByNomeIgnoreCase(String nome);
}