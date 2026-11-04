import './Card.css';
import { Link } from 'react-router-dom';

const Card = ({ imagem, nome, descricao, precoAntigo, precoAtual, id }) => {
  return (
    <article className="produto-card">
      <Link className="produto-img-container" to={`/produtos/${id}`} aria-label={`Ver ${nome}`}>
        <img src={imagem} alt={nome} />
      </Link>

      <div className="produto-info">
        <h3 className="produto-nome">{nome}</h3>
        <p className="produto-descricao">{descricao}</p>

        {precoAntigo && <span className="produto-preco-antigo">{precoAntigo}</span>}
        <span className="produto-preco-novo">{precoAtual}</span>

        <Link className="produto-btn-comprar" to={`/produtos/${id}`}>
          Ver produto
        </Link>
      </div>
    </article>
  );
};

export default Card;