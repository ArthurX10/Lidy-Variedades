import { Link } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { useCart } from '../hooks/useCart';
import { useStoreSettings } from '../hooks/useStoreSettings';
import { formatarPreco } from '../services/produtos';
import { precoEfetivo } from '../services/catalogo';
import './Carrinho.css';

export default function Carrinho() {
  const { items, total, updateQuantity, removeFromCart } = useCart();
  const { settings } = useStoreSettings();
  const mensagem = encodeURIComponent([
    'Olá! Gostaria de fazer um pedido:',
    ...items.map((item) => (
      `- ${item.nome} (${item.quantity}x): ${formatarPreco(
        precoEfetivo(item) * item.quantity,
      )}`
    )),
    `Total: ${formatarPreco(total)}`,
  ].join('\n'));
  const telefone = (settings?.whatsappNumber || '').replace(/\D/g, '');

  return (
    <>
      <Header />
      <main className="carrinho-page">
        <h1>Meu carrinho</h1>
        {items.length === 0 ? (
          <section className="carrinho-vazio">
            <p>Seu carrinho está vazio.</p>
            <Link to="/">Continuar comprando</Link>
          </section>
        ) : (
          <>
            <div className="carrinho-lista">
              {items.map((item) => (
                <article className="carrinho-item" key={item.id}>
                  <img src={item.imagem} alt="" />
                  <div className="carrinho-item-info">
                    <h2>{item.nome}</h2>
                    <p>{formatarPreco(precoEfetivo(item))} cada</p>
                    <div className="carrinho-quantidade">
                      <button
                        type="button"
                        aria-label={`Diminuir quantidade de ${item.nome}`}
                        onClick={() => updateQuantity(item.id, item.quantity - 1)}
                      >
                        −
                      </button>
                      <span>{item.quantity}</span>
                      <button
                        type="button"
                        aria-label={`Aumentar quantidade de ${item.nome}`}
                        onClick={() => updateQuantity(item.id, item.quantity + 1)}
                        disabled={Number.isInteger(item.quantidadeEstoque)
                          && item.quantity >= item.quantidadeEstoque}
                      >
                        +
                      </button>
                    </div>
                    {Number.isInteger(item.quantidadeEstoque) && (
                      <p className="carrinho-estoque">
                        {item.quantidadeEstoque} {item.quantidadeEstoque === 1 ? 'unidade' : 'unidades'} disponíveis
                      </p>
                    )}
                  </div>
                  <strong>{formatarPreco(precoEfetivo(item) * item.quantity)}</strong>
                  <button
                    className="carrinho-remover"
                    type="button"
                    onClick={() => removeFromCart(item.id)}
                  >
                    Remover
                  </button>
                </article>
              ))}
            </div>
            <div className="carrinho-total">
              <strong>Total: {formatarPreco(total)}</strong>
              <a
                href={`https://wa.me/${telefone}?text=${mensagem}`}
                target="_blank"
                rel="noreferrer"
              >
                Finalizar pedido pelo WhatsApp
              </a>
            </div>
          </>
        )}
      </main>
      <Footer />
    </>
  );
}
