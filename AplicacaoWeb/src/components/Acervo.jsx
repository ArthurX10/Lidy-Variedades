import { useState } from 'react';
import Card from './Card';
import './Acervo.css';
import { produtoEstaEmPromocao, produtoPertenceCategoria } from '../services/catalogo';

const categoriasFiltro = ['Todos', 'Cosméticos', 'Perfumes', 'Lingeries', 'Promoções'];
const produtosVazios = [];

const Acervo = ({ produtos = produtosVazios, carregando = false }) => {
  const [categoriaAtiva, setCategoriaAtiva] = useState('Todos');

  const produtosFiltrados =
    categoriaAtiva === 'Todos'
      ? produtos
      : categoriaAtiva === 'Promoções'
        ? produtos.filter(produtoEstaEmPromocao)
        : produtos.filter((produto) => produtoPertenceCategoria(produto, categoriaAtiva));

  return (
    <section className="acervo-section" id="acervo">
      <div className="acervo-header">
        <span className="acervo-tag">Nosso Acervo</span>
        <h2>Encontre o seu estilo</h2>
        <div className="acervo-linha-destaque"></div>
      </div>

      <div className="acervo-filtros">
        {categoriasFiltro.map((cat) => (
          <button
            key={cat}
            type="button"
            className={`filtro-pill ${categoriaAtiva === cat ? 'ativo' : ''}`}
            aria-pressed={categoriaAtiva === cat}
            onClick={() => setCategoriaAtiva(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      <div className="acervo-grid">
        {carregando ? (
          <p>Carregando produtos...</p>
        ) : produtosFiltrados.length === 0 ? (
          <p>Nenhum produto encontrado nesta categoria.</p>
        ) : produtosFiltrados.map((produto) => (
          <Card
            key={produto.id}
            id={produto.id}
            imagem={produto.imagem}
            nome={produto.nome}
            descricao={produto.descricao}
            precoAntigo={produto.precoAntigo}
            precoAtual={produto.precoAtual}
          />
        ))}
      </div>
    </section>
  );
};

export default Acervo;