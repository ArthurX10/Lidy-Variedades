package com.loja.LidyVariedades.controller;
import com.loja.LidyVariedades.model.Produto;
import com.loja.LidyVariedades.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/produtos", produces = "application/json")
public class ProdutoController{
    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarTodos(){
        return produtoRepository.findAll();
    }
    

    @GetMapping("/buscar")
    public List<Produto> buscarNome(@RequestParam String nome){
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id){
        return produtoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto){
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto dados){
        return produtoRepository.findById(id)
            .map(existente -> {
                existente.setNome(dados.getNome());
                existente.setMarca(dados.getMarca());
                existente.setCategoria(dados.getCategoria());
                existente.setCodigoReferencia(dados.getCodigoReferencia());
                existente.setPrecoCusto(dados.getPrecoCusto());
                existente.setPrecoVenda(dados.getPrecoVenda());
                existente.setQuantidadeEstoque(dados.getQuantidadeEstoque());
                existente.setEstoqueMinimo(dados.getEstoqueMinimo());
                return ResponseEntity.ok(produtoRepository.save(existente));
            })
            .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id){
            if(!produtoRepository.existsById(id)){
                return ResponseEntity.notFound().build();
            }
            produtoRepository.deleteById(id);
            
            return ResponseEntity.noContent().build();
        }
}