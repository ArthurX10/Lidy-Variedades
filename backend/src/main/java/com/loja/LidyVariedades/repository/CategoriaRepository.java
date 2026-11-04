package com.loja.LidyVariedades.repository;
import com.loja.LidyVariedades.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    java.util.Optional<Categoria> findByNomeIgnoreCase(String nome);
}
