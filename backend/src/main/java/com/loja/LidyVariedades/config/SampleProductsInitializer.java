package com.loja.LidyVariedades.config;

import java.math.BigDecimal;
import java.util.List;

import com.loja.LidyVariedades.model.Categoria;
import com.loja.LidyVariedades.model.Marca;
import com.loja.LidyVariedades.model.Produto;
import com.loja.LidyVariedades.repository.CategoriaRepository;
import com.loja.LidyVariedades.repository.MarcaRepository;
import com.loja.LidyVariedades.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.seed.sample-products", havingValue = "true")
public class SampleProductsInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SampleProductsInitializer.class);

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    public SampleProductsInitializer(
        ProdutoRepository produtoRepository,
        CategoriaRepository categoriaRepository,
        MarcaRepository marcaRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (produtoRepository.count() > 0) {
            logger.info("Carga de produtos de exemplo ignorada: já existem produtos cadastrados.");
            return;
        }

        Categoria perfumes = obterCategoria("Perfumes");
        Categoria cosmeticos = obterCategoria("Cosméticos");
        Categoria lingeries = obterCategoria("Lingeries");

        Marca aromaReal = obterMarca("Aroma Real");
        Marca florDeLuna = obterMarca("Flor de Luna");
        Marca bellaForma = obterMarca("Bella Forma");

        produtoRepository.saveAll(List.of(
            criarProduto(
                "Perfume Brisa Dourada",
                "Fragrância floral com notas cítricas, ideal para o dia a dia.",
                "EXEMPLO-PERF-001",
                aromaReal,
                perfumes,
                "72.00",
                "119.90",
                "99.90",
                12,
                3
            ),
            criarProduto(
                "Hidratante Flor de Algodão",
                "Hidratante corporal de rápida absorção e toque macio.",
                "EXEMPLO-COSM-001",
                florDeLuna,
                cosmeticos,
                "24.00",
                "49.90",
                null,
                18,
                4
            ),
            criarProduto(
                "Conjunto Conforto Rosa",
                "Conjunto de lingerie confortável para uso diário.",
                "EXEMPLO-LING-001",
                bellaForma,
                lingeries,
                "38.00",
                "79.90",
                null,
                8,
                2
            )
        ));

        logger.info("Produtos de exemplo adicionados ao banco para teste.");
    }

    private Categoria obterCategoria(String nome) {
        return categoriaRepository.findByNomeIgnoreCase(nome)
            .orElseGet(() -> {
                Categoria categoria = new Categoria();
                categoria.setNome(nome);
                return categoriaRepository.save(categoria);
            });
    }

    private Marca obterMarca(String nome) {
        return marcaRepository.findByNomeIgnoreCase(nome)
            .orElseGet(() -> {
                Marca marca = new Marca();
                marca.setNome(nome);
                return marcaRepository.save(marca);
            });
    }

    private Produto criarProduto(
        String nome,
        String descricao,
        String codigo,
        Marca marca,
        Categoria categoria,
        String precoCusto,
        String precoVenda,
        String precoPromocional,
        int quantidadeEstoque,
        int estoqueMinimo
    ) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setCodigoReferencia(codigo);
        produto.setMarca(marca);
        produto.setCategoria(categoria);
        produto.setPrecoCusto(new BigDecimal(precoCusto));
        produto.setPrecoVenda(new BigDecimal(precoVenda));
        produto.setPrecoPromocional(
            precoPromocional == null ? null : new BigDecimal(precoPromocional)
        );
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produto.setEstoqueMinimo(estoqueMinimo);
        return produto;
    }
}
