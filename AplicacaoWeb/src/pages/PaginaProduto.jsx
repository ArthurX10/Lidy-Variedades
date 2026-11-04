import { useState } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { FaRegCreditCard, FaWhatsapp } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import './PaginaProduto.css';
import { useCart } from '../hooks/useCart';
import { useStoreSettings } from '../hooks/useStoreSettings';
import { precoEfetivo } from '../services/catalogo';

const produtosVazios = [];

const PaginaProduto = ({
  produto,
  produtos = produtosVazios,
  parcelas = 3,
}) => {
  const { addToCart, items } = useCart();
  const { settings } = useStoreSettings();
  const [adicionado, setAdicionado] = useState(false);
  const estoqueDisponivel = Number.isInteger(produto.quantidadeEstoque)
    ? produto.quantidadeEstoque
    : null;
  const quantidadeNoCarrinho = items.find((item) => item.id === produto.id)?.quantity ?? 0;
  const estoqueEsgotado = estoqueDisponivel === 0;
  const estoqueDesconhecido = estoqueDisponivel === null;
  const limiteNoCarrinho = estoqueDisponivel !== null
    && quantidadeNoCarrinho >= estoqueDisponivel;
  const precoNumerico = precoEfetivo(produto);
  const parcelaFormatada = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(precoNumerico / parcelas);
  const produtosRelacionados = produtos
    .filter((item) => item.id !== produto.id)
    .slice(0, 4);
  const mensagemWhatsApp = encodeURIComponent(
    `Olá! Tenho interesse no produto ${produto.nome}.`,
  );
  const telefone = (settings?.whatsappNumber || '').replace(/\D/g, '');

  const handleAddToCart = () => {
    if (estoqueDesconhecido || estoqueEsgotado || limiteNoCarrinho) return;
    addToCart(produto);
    setAdicionado(true);
  };

  return (
    <>
      <Header />

      <main className="pagina-produto">
        <div className="produto-voltar">
          <Link to="/">‹ Voltar para o Catálogo</Link>
        </div>

        <section className="produto-detalhe" aria-labelledby="produto-nome">
          <div className="produto-imagem-principal">
            <img src={produto.imagem} alt={produto.nome} />
          </div>

          <div className="produto-detalhes">
            <div className="produto-identificacao">
              <p className="produto-categoria">{produto.categoria}</p>
              <h1 id="produto-nome">{produto.nome}</h1>
              <div className="produto-precos">
                <span className="produto-preco-atual">{produto.precoAtual}</span>
                {produto.precoAntigo && (
                  <span className="produto-preco-antigo">{produto.precoAntigo}</span>
                )}
              </div>
            </div>

            <p
              className={`produto-disponibilidade${estoqueEsgotado ? ' esgotado' : ''}`}
              role="status"
            >
              {estoqueDesconhecido
                ? 'Disponibilidade não informada'
                : estoqueEsgotado
                  ? 'Produto esgotado'
                  : `Disponível: ${estoqueDisponivel} ${estoqueDisponivel === 1 ? 'unidade' : 'unidades'}`}
            </p>

            <div className="produto-parcelamento">
              <div className="produto-parcelamento-titulo">
                <FaRegCreditCard aria-hidden="true" />
                <strong>Parcelamento</strong>
              </div>
              <p>
                Ou até {parcelas}x de <strong>{parcelaFormatada}</strong> sem juros no cartão.
              </p>
            </div>

            <p className="produto-descricao">{produto.descricao}</p>

            <div className="produto-acoes">
              <a
                className="produto-whatsapp"
                href={`https://wa.me/${telefone}?text=${mensagemWhatsApp}`}
                target="_blank"
                rel="noreferrer"
              >
                <FaWhatsapp aria-hidden="true" />
                Solicitar via WhatsApp
              </a>
              <button
                className="produto-adicionar-carrinho"
                type="button"
                onClick={handleAddToCart}
                disabled={estoqueDesconhecido || estoqueEsgotado || limiteNoCarrinho}
              >
                {estoqueEsgotado
                  ? 'Esgotado'
                  : estoqueDesconhecido
                    ? 'Estoque indisponível'
                    : limiteNoCarrinho
                      ? 'Limite disponível atingido'
                      : adicionado
                        ? 'Adicionar mais uma unidade'
                        : 'Adicionar ao carrinho'}
              </button>
            </div>
            {adicionado && (
              <p className="produto-carrinho-feedback" role="status">
                Produto adicionado ao carrinho. <Link to="/carrinho">Ver carrinho</Link>
              </p>
            )}
          </div>
        </section>

        {produtosRelacionados.length > 0 && (
          <section className="produtos-relacionados" aria-labelledby="relacionados-titulo">
            <h2 id="relacionados-titulo">Quem viu este, também gostou</h2>
            <div className="produtos-relacionados-lista">
              {produtosRelacionados.map((item) => (
                <article className="produto-relacionado-card" key={item.id}>
                  <Link
                    className="produto-relacionado-imagem"
                    to={`/produtos/${item.id}`}
                    aria-label={`Ver ${item.nome}`}
                  >
                    <img src={item.imagem} alt={item.nome} />
                  </Link>
                  <div className="produto-relacionado-info">
                    <h3>{item.nome}</h3>
                    <p>{item.descricao}</p>
                    {item.precoAntigo && (
                      <span className="relacionado-preco-antigo">{item.precoAntigo}</span>
                    )}
                    <span className="relacionado-preco-atual">{item.precoAtual}</span>
                    <Link
                      className="produto-relacionado-comprar"
                      to={`/produtos/${item.id}`}
                    >
                      Comprar Produto
                    </Link>
                  </div>
                </article>
              ))}
            </div>
          </section>
        )}
      </main>

      <Footer />
    </>
  );
};

export default PaginaProduto;
