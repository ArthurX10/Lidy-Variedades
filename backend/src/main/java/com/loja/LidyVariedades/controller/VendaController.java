package com.loja.LidyVariedades.controller;

import com.loja.LidyVariedades.model.Cliente;
import com.loja.LidyVariedades.model.ItemVenda;
import com.loja.LidyVariedades.model.ItemVendaRequest;
import com.loja.LidyVariedades.model.Produto;
import com.loja.LidyVariedades.model.Venda;
import com.loja.LidyVariedades.model.VendaRequest;
import com.loja.LidyVariedades.repository.ClienteRepository;
import com.loja.LidyVariedades.repository.ItemVendaRepository;
import com.loja.LidyVariedades.repository.ProdutoRepository;
import com.loja.LidyVariedades.repository.VendaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(value = "/vendas", produces = "application/json")
public class VendaController {
    
    @Autowired private VendaRepository vendaRepository;
    @Autowired private ItemVendaRepository itemVendaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ClienteRepository clienteRepository;


    @GetMapping 
    public List<Venda> listarTodos(){
        return vendaRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id){
        return vendaRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping 
    @Transactional
    public ResponseEntity<Venda> criar(@Valid @RequestBody VendaRequest request ){
        Venda venda = new Venda();
        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElse(null);
            venda.setCliente(cliente);
        }
        venda.setFormaPagamento(request.getFormaPagamento());

        BigDecimal total = BigDecimal.ZERO;
        Venda vendaSalva = vendaRepository.save(venda);

        for(ItemVendaRequest itemRequest : request.getItens()){
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemRequest.getProdutoId()));

                if(produto.getQuantidadeEstoque() < itemRequest.getQuantidade()){
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
                }

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(vendaSalva);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(itemRequest.getQuantidade());
            BigDecimal precoUnitario = produto.getPrecoPromocional();
            if (precoUnitario == null || precoUnitario.compareTo(produto.getPrecoVenda()) >= 0) {
                precoUnitario = produto.getPrecoVenda();
            }
            itemVenda.setPrecoUnitario(precoUnitario);
            itemVendaRepository.save(itemVenda);

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemRequest.getQuantidade());
            produtoRepository.save(produto);

            total = total.add(precoUnitario.multiply(BigDecimal.valueOf(itemRequest.getQuantidade())));
        }
            vendaSalva.setTotalVenda(total);
            vendaRepository.save(vendaSalva);

            return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if(!vendaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        vendaRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }
}
