import { useCallback, useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  FaBox,
  FaCheck,
  FaClockRotateLeft,
  FaFilter,
  FaMagnifyingGlass,
  FaPenToSquare,
  FaPlus,
  FaSliders,
  FaTrashCan,
  FaTriangleExclamation
} from 'react-icons/fa6';
import Header from '../components/Header';
import Footer from '../components/Footer';
import AdminSidebar from '../components/AdminSidebar';
import { deleteProduct } from '../services/api';
import { formatarPreco, listarProdutosAdmin } from '../services/produtos';
import './PaginaProdutosAdmin.css';

export default function PaginaProdutosAdmin() {
  const [produtos, setProdutos] = useState([]);
  const [carregandoProdutos, setCarregandoProdutos] = useState(true);
  const [erroProdutos, setErroProdutos] = useState('');
  const [busca, setBusca] = useState('');
  const [filtroEstoque, setFiltroEstoque] = useState('todos');
  const [mostrarFiltros, setMostrarFiltros] = useState(false);
  const [erroAcao, setErroAcao] = useState('');
  const [excluindoId, setExcluindoId] = useState(null);

  const atualizarProdutos = useCallback(async () => {
    setCarregandoProdutos(true);
    setErroProdutos('');

    try {
      setProdutos(await listarProdutosAdmin());
    } catch (error) {
      setErroProdutos(error.message || 'Não foi possível carregar os produtos.');
    } finally {
      setCarregandoProdutos(false);
    }
  }, []);

  useEffect(() => {
    let ativo = true;

    listarProdutosAdmin()
      .then((itens) => {
        if (ativo) setProdutos(itens);
      })
      .catch((error) => {
        if (ativo) setErroProdutos(error.message || 'Não foi possível carregar os produtos.');
      })
      .finally(() => {
        if (ativo) setCarregandoProdutos(false);
      });

    return () => {
      ativo = false;
    };
  }, []);

  const produtosFiltrados = useMemo(() => {
    const termo = busca.trim().toLocaleLowerCase('pt-BR');
    return produtos.filter((produto) => {
      const correspondeBusca = !termo
        || [produto.nome, produto.codigoReferencia, produto.marca, produto.categoria]
          .some((valor) => valor?.toLocaleLowerCase('pt-BR').includes(termo));
      const estoque = Number(produto.quantidadeEstoque) || 0;
      const minimo = Number(produto.estoqueMinimo) || 0;
      const correspondeFiltro = filtroEstoque === 'todos'
        || (filtroEstoque === 'ativo' && estoque > minimo)
        || (filtroEstoque === 'baixo' && estoque > 0 && estoque <= minimo)
        || (filtroEstoque === 'inativo' && estoque === 0);

      return correspondeBusca && correspondeFiltro;
    });
  }, [busca, filtroEstoque, produtos]);

  const resumoEstoque = useMemo(() => produtos.reduce((resumo, produto) => {
    const estoque = Number(produto.quantidadeEstoque) || 0;
    const minimo = Number(produto.estoqueMinimo) || 0;
    resumo.total += 1;
    if (estoque === 0) resumo.inativos += 1;
    else if (estoque <= minimo) resumo.baixo += 1;
    else resumo.ativos += 1;
    return resumo;
  }, { total: 0, ativos: 0, inativos: 0, baixo: 0 }), [produtos]);

  const handleDelete = async (produto) => {
    if (!window.confirm(`Excluir o produto "${produto.nome}"?`)) return;
    setErroAcao('');
    setExcluindoId(produto.id);

    try {
      await deleteProduct(produto.id);
      await atualizarProdutos();
    } catch (error) {
      setErroAcao(error.message || 'Não foi possível excluir o produto.');
    } finally {
      setExcluindoId(null);
    }
  };

  return (
    <div className="admin-page">
      <Header />
      <main className="admin-dashboard">
        <AdminSidebar />

        <section className="admin-products-container">
          <div className="admin-products-heading">
            <div>
              <h1>Gerenciamento de Produtos</h1>
              <p>Organize seu catálogo e destaques</p>
            </div>
          </div>

          <section className="admin-products-summary" aria-label="Resumo do estoque">
            <article className="admin-summary-card">
              <FaSliders className="summary-icon summary-icon-total" aria-hidden="true" />
              <div><span>Total de Produtos</span><strong>{resumoEstoque.total}</strong></div>
            </article>
            <article className="admin-summary-card">
              <FaCheck className="summary-icon summary-icon-active" aria-hidden="true" />
              <div><span>Ativos</span><strong>{resumoEstoque.ativos}</strong></div>
            </article>
            <article className="admin-summary-card">
              <FaTriangleExclamation className="summary-icon summary-icon-inactive" aria-hidden="true" />
              <div><span>Inativos</span><strong>{resumoEstoque.inativos}</strong></div>
            </article>
            <article className="admin-summary-card">
              <FaClockRotateLeft className="summary-icon summary-icon-low" aria-hidden="true" />
              <div><span>Estoque baixo</span><strong>{resumoEstoque.baixo}</strong></div>
            </article>
          </section>

          <section className="admin-products-panel" aria-label="Lista de produtos">
            <div className="admin-products-toolbar">
              <label className="admin-products-search">
                <FaMagnifyingGlass aria-hidden="true" />
                <span className="visually-hidden">Buscar por nome ou categoria</span>
                <input
                  type="search"
                  placeholder="Buscar por Nome ou Categoria..."
                  value={busca}
                  onChange={(event) => setBusca(event.target.value)}
                />
              </label>
              <button
                type="button"
                className={`admin-filter-button${mostrarFiltros ? ' selected' : ''}`}
                onClick={() => setMostrarFiltros((mostrar) => !mostrar)}
                aria-expanded={mostrarFiltros}
              >
                <FaFilter aria-hidden="true" />
                Filtros
              </button>
            </div>

            {mostrarFiltros && (
              <label className="admin-stock-filter">
                Filtrar por status
                <select value={filtroEstoque} onChange={(event) => setFiltroEstoque(event.target.value)}>
                  <option value="todos">Todos os produtos</option>
                  <option value="ativo">Ativos</option>
                  <option value="baixo">Estoque baixo</option>
                  <option value="inativo">Inativos</option>
                </select>
              </label>
            )}

            {(erroProdutos || erroAcao) && (
              <p className="admin-products-error" role="alert">{erroAcao || erroProdutos}</p>
            )}

            {carregandoProdutos ? (
              <p className="admin-products-message">Carregando produtos...</p>
            ) : produtosFiltrados.length === 0 ? (
              <div className="admin-products-empty">
                <h2>{busca || filtroEstoque !== 'todos' ? 'Nenhum resultado' : 'Ainda não há produtos cadastrados'}</h2>
                <p>
                  {busca || filtroEstoque !== 'todos'
                    ? 'Tente alterar a busca ou os filtros.'
                    : 'Cadastre o primeiro produto para que ele também apareça na vitrine.'}
                </p>
              </div>
            ) : (
              <div className="admin-products-table-wrap">
                <table className="admin-products-table">
                  <thead>
                    <tr>
                      <th>Produto</th>
                      <th>Categoria</th>
                      <th>Preço</th>
                      <th>Status</th>
                      <th>Estoque</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    {produtosFiltrados.map((produto) => {
                      const estoque = Number(produto.quantidadeEstoque) || 0;
                      const minimo = Number(produto.estoqueMinimo) || 0;
                      const status = estoque === 0
                        ? { label: 'Inativo', className: 'inactive' }
                        : estoque <= minimo
                          ? { label: 'Estoque baixo', className: 'low' }
                          : { label: 'Ativo', className: 'active' };

                      return (
                        <tr key={produto.id}>
                          <td>
                            <div className="admin-product-cell">
                              <img src={produto.imagem} alt="" className="admin-product-image" />
                              <div>
                                <div className="admin-product-name">{produto.nome}</div>
                                <div className="admin-product-code">
                                  {produto.codigoReferencia || 'Sem código de referência'}
                                </div>
                              </div>
                            </div>
                          </td>
                          <td><span className="admin-category-tag">{produto.categoria || 'Sem categoria'}</span></td>
                          <td>{formatarPreco(produto.precoPromocional ?? produto.precoVenda)}</td>
                          <td>
                            <span className={`admin-stock-status ${status.className}`}>
                              <i aria-hidden="true" />{status.label}
                            </span>
                          </td>
                          <td>{estoque}</td>
                          <td>
                            <div className="admin-product-row-actions">
                              <Link
                                to={`/admin/produtos/${produto.id}/editar`}
                                aria-label={`Editar ${produto.nome}`}
                                title="Editar"
                              >
                                <FaPenToSquare aria-hidden="true" />
                              </Link>
                              <button
                                type="button"
                                onClick={() => handleDelete(produto)}
                                disabled={excluindoId === produto.id}
                                aria-label={`Excluir ${produto.nome}`}
                                title={excluindoId === produto.id ? 'Excluindo...' : 'Excluir'}
                              >
                                <FaTrashCan aria-hidden="true" />
                              </button>
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </section>

          <div className="admin-products-bottom">
            <span><FaBox aria-hidden="true" /> {produtosFiltrados.length} produto(s) exibido(s)</span>
            <Link className="admin-primary-button" to="/admin/produtos/novo">
              <FaPlus aria-hidden="true" />
              Adicionar Produto
            </Link>
          </div>
        </section>
      </main>
      <Footer />
    </div>
  );
}
