import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { createProduct, getBrands, getCategories, updateProduct } from '../services/api';
import { obterProdutoAdmin } from '../services/produtos';
import { useProdutos } from '../hooks/useProdutos';
import './PaginaProdutosAdmin.css';

const formularioVazio = {
  nome: '',
  descricao: '',
  imagemUrl: '',
  codigoReferencia: '',
  marcaId: '',
  categoriaId: '',
  precoCusto: '',
  precoVenda: '',
  precoPromocional: '',
  quantidadeEstoque: '0',
  estoqueMinimo: '0',
};

export default function FormularioProdutoAdmin() {
  const { id } = useParams();
  const editando = Boolean(id);
  const [produto, setProduto] = useState(null);
  const [carregandoProduto, setCarregandoProduto] = useState(Boolean(id));
  const [erroCarregamento, setErroCarregamento] = useState('');

  useEffect(() => {
    if (!id) return undefined;
    let ativo = true;

    obterProdutoAdmin(id)
      .then((produtoApi) => {
        if (ativo) setProduto(produtoApi);
      })
      .catch((error) => {
        if (ativo) setErroCarregamento(error.message || 'Não foi possível carregar o produto.');
      })
      .finally(() => {
        if (ativo) setCarregandoProduto(false);
      });

    return () => {
      ativo = false;
    };
  }, [id]);

  if (editando && carregandoProduto) {
    return <p className="admin-access-message">Carregando produto...</p>;
  }

  if (editando && !produto) {
    return (
      <div className="admin-page">
        <Header />
        <main className="admin-product-form-container">
          <p className="admin-products-error" role="alert">
            {erroCarregamento || 'Produto não encontrado.'}
          </p>
          <Link to="/admin/produtos">Voltar para produtos</Link>
        </main>
        <Footer />
      </div>
    );
  }

  return <FormularioProduto key={id || 'novo'} id={id} editando={editando} produto={produto} />;
}

function formularioDoProduto(produto) {
  if (!produto) return formularioVazio;

  return {
    nome: produto.nome || '',
    descricao: produto.descricao || '',
    imagemUrl: produto.imagemUrl || '',
    codigoReferencia: produto.codigoReferencia || '',
    marcaId: produto.marcaId ? String(produto.marcaId) : '',
    categoriaId: produto.categoriaId ? String(produto.categoriaId) : '',
    precoCusto: String(produto.precoCusto ?? ''),
    precoVenda: String(produto.precoVenda ?? ''),
    precoPromocional: produto.precoPromocional == null ? '' : String(produto.precoPromocional),
    quantidadeEstoque: String(produto.quantidadeEstoque ?? 0),
    estoqueMinimo: String(produto.estoqueMinimo ?? 0),
  };
}

function FormularioProduto({ id, editando, produto }) {
  const navigate = useNavigate();
  const { atualizarProdutos } = useProdutos();
  const [formulario, setFormulario] = useState(() => formularioDoProduto(produto));
  const [categorias, setCategorias] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [carregandoOpcoes, setCarregandoOpcoes] = useState(true);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState('');

  useEffect(() => {
    let ativo = true;

    Promise.all([getCategories(), getBrands()])
      .then(([categoriasApi, marcasApi]) => {
        if (!ativo) return;
        setCategorias(categoriasApi);
        setMarcas(marcasApi);
      })
      .catch((error) => {
        if (ativo) setErro(error.message || 'Não foi possível carregar marcas e categorias.');
      })
      .finally(() => {
        if (ativo) setCarregandoOpcoes(false);
      });

    return () => {
      ativo = false;
    };
  }, []);

  const alterarCampo = (event) => {
    const { name, value } = event.target;
    setFormulario((atual) => ({ ...atual, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErro('');
    setSalvando(true);

    const payload = {
      nome: formulario.nome.trim(),
      descricao: formulario.descricao.trim(),
      imagemUrl: formulario.imagemUrl.trim() || null,
      codigoReferencia: formulario.codigoReferencia.trim() || null,
      marca: formulario.marcaId ? { id: Number(formulario.marcaId) } : null,
      categoria: formulario.categoriaId ? { id: Number(formulario.categoriaId) } : null,
      precoCusto: Number(formulario.precoCusto),
      precoVenda: Number(formulario.precoVenda),
      precoPromocional: formulario.precoPromocional ? Number(formulario.precoPromocional) : null,
      quantidadeEstoque: Number(formulario.quantidadeEstoque),
      estoqueMinimo: Number(formulario.estoqueMinimo),
    };

    try {
      if (editando) {
        await updateProduct(id, payload);
      } else {
        await createProduct(payload);
      }
      await atualizarProdutos();
      navigate('/admin/produtos');
    } catch (error) {
      setErro(error.message || 'Não foi possível salvar o produto.');
    } finally {
      setSalvando(false);
    }
  };

  return (
    <div className="admin-page">
      <Header />
      <main className="admin-product-form-container">
        <div className="admin-product-form-heading">
          <Link to="/admin/produtos">← Voltar para produtos</Link>
          <h1>{editando ? 'Editar produto' : 'Cadastrar produto'}</h1>
          <p>As informações salvas também serão usadas na vitrine da loja.</p>
        </div>

        <form className="admin-product-form" onSubmit={handleSubmit}>
          <div className="admin-product-form-grid">
            <label>
              Nome do produto
              <input name="nome" value={formulario.nome} onChange={alterarCampo} required />
            </label>
            <label>
              Código de referência
              <input name="codigoReferencia" value={formulario.codigoReferencia} onChange={alterarCampo} />
            </label>
            <label>
              Categoria
              <select
                name="categoriaId"
                value={formulario.categoriaId}
                onChange={alterarCampo}
                disabled={carregandoOpcoes}
              >
                <option value="">Selecione uma categoria</option>
                {categorias.map((categoria) => (
                  <option value={categoria.id} key={categoria.id}>{categoria.nome}</option>
                ))}
              </select>
            </label>
            <label>
              Marca
              <select
                name="marcaId"
                value={formulario.marcaId}
                onChange={alterarCampo}
                disabled={carregandoOpcoes}
              >
                <option value="">Selecione uma marca</option>
                {marcas.map((marca) => (
                  <option value={marca.id} key={marca.id}>{marca.nome}</option>
                ))}
              </select>
            </label>
            <label className="admin-product-form-full">
              Descrição
              <textarea
                name="descricao"
                value={formulario.descricao}
                onChange={alterarCampo}
                rows="4"
                required
              />
            </label>
            <label className="admin-product-form-full">
              URL da imagem
              <input
                name="imagemUrl"
                type="url"
                value={formulario.imagemUrl}
                onChange={alterarCampo}
                placeholder="https://..."
              />
            </label>
            <label>
              Preço de custo (R$)
              <input
                name="precoCusto"
                type="number"
                min="0"
                step="0.01"
                value={formulario.precoCusto}
                onChange={alterarCampo}
                required
              />
            </label>
            <label>
              Preço de venda (R$)
              <input
                name="precoVenda"
                type="number"
                min="0"
                step="0.01"
                value={formulario.precoVenda}
                onChange={alterarCampo}
                required
              />
            </label>
            <label>
              Preço promocional (R$)
              <input
                name="precoPromocional"
                type="number"
                min="0"
                step="0.01"
                value={formulario.precoPromocional}
                onChange={alterarCampo}
              />
            </label>
            <label>
              Quantidade em estoque
              <input
                name="quantidadeEstoque"
                type="number"
                min="0"
                step="1"
                value={formulario.quantidadeEstoque}
                onChange={alterarCampo}
                required
              />
            </label>
            <label>
              Estoque mínimo
              <input
                name="estoqueMinimo"
                type="number"
                min="0"
                step="1"
                value={formulario.estoqueMinimo}
                onChange={alterarCampo}
                required
              />
            </label>
          </div>

          {erro && <p className="admin-products-error" role="alert">{erro}</p>}
          <div className="admin-product-form-actions">
            <Link className="admin-secondary-button" to="/admin/produtos">Cancelar</Link>
            <button className="admin-primary-button" type="submit" disabled={salvando || carregandoOpcoes}>
              {salvando ? 'Salvando...' : editando ? 'Salvar alterações' : 'Cadastrar produto'}
            </button>
          </div>
        </form>
      </main>
      <Footer />
    </div>
  );
}
