package com.loja.LidyVariedades.repository;
import com.loja.LidyVariedades.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVendaId(Long vendaId);
}