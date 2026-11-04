package com.loja.LidyVariedades.controller;
import com.loja.LidyVariedades.model.Produto;
import com.loja.LidyVariedades.repository.ProdutoRepository;
import com.loja.LidyVariedades.repository.CategoriaRepository;
import com.loja.LidyVariedades.repository.MarcaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(value = "/produtos", produces = "application/json")
public class ProdutoController{
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

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
        prepararProduto(produto);
        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto dados){
        return produtoRepository.findById(id)
            .map(existente -> {
                existente.setNome(dados.getNome());
                existente.setDescricao(dados.getDescricao());
                existente.setImagemUrl(dados.getImagemUrl());
                existente.setCodigoReferencia(dados.getCodigoReferencia());
                existente.setPrecoCusto(dados.getPrecoCusto());
                existente.setPrecoVenda(dados.getPrecoVenda());
                existente.setPrecoPromocional(dados.getPrecoPromocional());
                existente.setQuantidadeEstoque(dados.getQuantidadeEstoque());
                existente.setEstoqueMinimo(dados.getEstoqueMinimo());
                prepararProduto(dados);
                existente.setMarca(dados.getMarca());
                existente.setCategoria(dados.getCategoria());
                return ResponseEntity.ok(produtoRepository.save(existente));
            })
            .orElse(ResponseEntity.notFound().build());
        }

        private void prepararProduto(Produto produto) {
            if (produto.getPrecoPromocional() != null
                && produto.getPrecoPromocional().compareTo(produto.getPrecoVenda()) >= 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O preco promocional precisa ser menor que o preco de venda."
                );
            }

            if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
                produto.setCategoria(categoriaRepository.findById(produto.getCategoria().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria invalida.")));
            } else {
                produto.setCategoria(null);
            }

            if (produto.getMarca() != null && produto.getMarca().getId() != null) {
                produto.setMarca(marcaRepository.findById(produto.getMarca().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca invalida.")));
            } else {
                produto.setMarca(null);
            }
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