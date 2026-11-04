import { useMemo, useState } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import './PaginaCategoria.css';
import Filtros from '../components/Filtros';
import Card from '../components/Card';
import { Dropdown } from 'primereact/dropdown';
import { formatarPreco } from '../services/produtos';
import { precoEfetivo } from '../services/catalogo';

const opcoes = [
  { label: 'Relevância', value: 'relevancia' },
  { label: 'Menor Preço', value: 'menor_preco' },
  { label: 'Maior Preço', value: 'maior_preco' },
];
const produtosVazios = [];

const PaginaCategoria = ({ titulo, subtitulo, produtos = produtosVazios, carregando = false }) => {
  const [ordem, setOrdem] = useState('relevancia');
  const [filtrosAtivos, setFiltrosAtivos] = useState({});
  const marcas = [...new Set(produtos.map((produto) => produto.marca).filter(Boolean))].sort();
  const gruposFiltro = [
    ...(marcas.length ? [{ id: 'marca', titulo: 'Marca', opcoes: marcas }] : []),
    {
      id: 'preco',
      titulo: 'Faixa de preço',
      opcoes: [
        { label: `Até ${formatarPreco(100)}`, value: 'ate-100' },
        { label: `${formatarPreco(100)} a ${formatarPreco(200)}`, value: '100-200' },
        { label: `${formatarPreco(200)} a ${formatarPreco(500)}`, value: '200-500' },
        { label: `Acima de ${formatarPreco(500)}`, value: 'acima-500' },
      ],
    },
  ];
  const produtosVisiveis = useMemo(() => {
    const filtrados = produtos.filter((produto) => {
      const marcasSelecionadas = filtrosAtivos.marca || [];
      const faixasSelecionadas = filtrosAtivos.preco || [];
      const preco = precoEfetivo(produto);
      const correspondeMarca = marcasSelecionadas.length === 0
        || marcasSelecionadas.includes(produto.marca);
      const correspondePreco = faixasSelecionadas.length === 0
        || faixasSelecionadas.some((faixa) => {
          if (faixa === 'ate-100') return preco <= 100;
          if (faixa === '100-200') return preco > 100 && preco <= 200;
          if (faixa === '200-500') return preco > 200 && preco <= 500;
          return preco > 500;
        });
      return correspondeMarca && correspondePreco;
    });

    return filtrados.sort((a, b) => {
      if (ordem === 'menor_preco') return precoEfetivo(a) - precoEfetivo(b);
      if (ordem === 'maior_preco') return precoEfetivo(b) - precoEfetivo(a);
      return 0;
    });
  }, [produtos, filtrosAtivos, ordem]);

  return (
    <>
      <Header />
      <main>
        <div className="perfumes-page">
          <div className='information-page'>
            <p>Home {'>'} <strong>{titulo}</strong></p>
          </div>

          <div className="perfumes-titulos">
            <div>
              <h2>{titulo}</h2>
              <p>{subtitulo}</p>
            </div>

            <label className="perfumes-ordenar">
              <p>Ordenar por: </p>
              <Dropdown
                value={ordem}
                options={opcoes}
                onChange={(e) => setOrdem(e.value)}
                aria-label="Ordenar produtos"
              />
            </label>
          </div>

          <div className='perfumes-conteudo'>
            <Filtros grupos={gruposFiltro} onFilterChange={setFiltrosAtivos} />

            <div className="perfumes-card">
              {carregando ? (
                <p>Carregando produtos...</p>
              ) : produtosVisiveis.length === 0 ? (
                <p>{produtos.length ? 'Nenhum produto corresponde aos filtros.' : 'Nenhum produto encontrado.'}</p>
              ) : (
                produtosVisiveis.map((produto) => (
                  <Card
                    key={produto.id}
                    id={produto.id}
                    imagem={produto.imagem}
                    nome={produto.nome}
                    descricao={produto.descricao}
                    precoAntigo={produto.precoAntigo}
                    precoAtual={produto.precoAtual}
                  />
                ))
              )}

            </div>
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
};

export default PaginaCategoria;
