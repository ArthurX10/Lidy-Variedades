import { useMemo } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import Card from '../components/Card';
import { useProdutos } from '../hooks/useProdutos';
import { normalizarTexto } from '../services/catalogo';
import './BuscaProdutos.css';

export default function BuscaProdutos() {
  const [searchParams] = useSearchParams();
  const consulta = searchParams.get('q')?.trim() || '';
  const termo = normalizarTexto(consulta);
  const { produtos, carregandoProdutos, erroProdutos } = useProdutos();
  const resultados = useMemo(() => (
    termo
      ? produtos.filter((produto) => (
        [produto.nome, produto.descricao, produto.marca, produto.categoria, produto.codigoReferencia]
          .some((valor) => normalizarTexto(valor || '').includes(termo))
      ))
      : []
  ), [produtos, termo]);

  return (
    <>
      <Header />
      <main className="busca-page">
        <p className="busca-breadcrumb"><Link to="/">Home</Link> / Busca</p>
        <h1>Resultados da busca</h1>
        <p className="busca-consulta">
          {consulta ? `Busca por “${consulta}”` : 'Digite um termo no campo de busca.'}
        </p>
        {erroProdutos && <p className="busca-erro" role="alert">{erroProdutos}</p>}
        {carregandoProdutos ? (
          <p>Carregando produtos...</p>
        ) : resultados.length ? (
          <div className="busca-grid">
            {resultados.map((produto) => (
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
        ) : (
          <p className="busca-vazia">Nenhum produto encontrado para essa busca.</p>
        )}
      </main>
      <Footer />
    </>
  );
}
