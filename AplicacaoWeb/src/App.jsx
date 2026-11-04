import { BrowserRouter, Navigate, Route, Routes, useParams } from 'react-router-dom';
import './App.css';
import './index.css';
import AdminGuard from './pages/AdminGuard';
import FormularioProdutoAdmin from './pages/FormularioProdutoAdmin';
import Home from './pages/Home';
import PaginaAdmin from './pages/PaginaAdmin';
import PaginaCategoria from './pages/PaginaCategoria';
import PaginaProduto from './pages/PaginaProduto';
import PaginaProdutosAdmin from './pages/PaginaProdutosAdmin';
import BuscaProdutos from './pages/BuscaProdutos';
import Carrinho from './pages/Carrinho';
import PaginaLocalizacao from './pages/PaginaLocalizacao';
import { ProdutosProvider } from './context/ProdutosProvider';
import { useProdutos } from './hooks/useProdutos';
import { CartProvider } from './context/CartProvider';
import { StoreSettingsProvider } from './context/StoreSettingsProvider';
import { useStoreSettings } from './hooks/useStoreSettings';
import { produtoEstaEmPromocao, produtoPertenceCategoria } from './services/catalogo';
import ConfigAdmin from './pages/ConfigAdmin';

function PaginaProdutoRota() {
  const { id } = useParams();
  const { produtos, carregandoProdutos, erroProdutos } = useProdutos();
  const produto = produtos.find((item) => String(item.id) === id);

  if (carregandoProdutos) return <p>Carregando produto...</p>;
  if (erroProdutos) return <p role="alert">Não foi possível carregar o produto: {erroProdutos}</p>;
  if (!produto) return <p>Produto não encontrado.</p>;

  return <PaginaProduto produto={produto} produtos={produtos} />;
}

function RotasAplicacao() {
  const { produtos, carregandoProdutos, erroProdutos } = useProdutos();
  const { error: erroConfiguracoes } = useStoreSettings();
  const produtosPorCategoria = (categoria) =>
    produtos.filter((produto) => produtoPertenceCategoria(produto, categoria));
  const produtosPromocionais = produtos.filter(produtoEstaEmPromocao);

  return (
    <>
      {(erroProdutos || erroConfiguracoes) && (
        <div className="api-status-errors">
          {erroProdutos && (
            <p className="api-status-error" role="alert">
              Não foi possível carregar os produtos da loja: {erroProdutos}
            </p>
          )}
          {erroConfiguracoes && (
            <p className="api-status-error" role="alert">
              Não foi possível carregar as configurações da loja: {erroConfiguracoes}
            </p>
          )}
        </div>
      )}
      <Routes>
        <Route
          path="/"
          element={<Home produtos={produtos} carregando={carregandoProdutos} />}
        />
        <Route
          path="/perfumes"
          element={
            <PaginaCategoria
              titulo="Perfumes"
              subtitulo="Encontre a fragrância ideal para você"
              produtos={produtosPorCategoria('Perfumes')}
              carregando={carregandoProdutos}
            />
          }
        />
        <Route
          path="/cosmeticos"
          element={
            <PaginaCategoria
              titulo="Cosméticos"
              subtitulo="Produtos de beleza para realçar todos os dias"
              produtos={produtosPorCategoria('Cosméticos')}
              carregando={carregandoProdutos}
            />
          }
        />
        <Route
          path="/lingeries"
          element={
            <PaginaCategoria
              titulo="Lingeries"
              subtitulo="Conforto e elegância para o seu dia"
              produtos={produtosPorCategoria('Lingeries')}
              carregando={carregandoProdutos}
            />
          }
        />
        <Route
          path="/promocoes"
          element={
            <PaginaCategoria
              titulo="Promoções"
              subtitulo="Aproveite as melhores ofertas da semana"
              produtos={produtosPromocionais}
              carregando={carregandoProdutos}
            />
          }
        />
        <Route path="/produtos/:id" element={<PaginaProdutoRota />} />
        <Route path="/busca" element={<BuscaProdutos />} />
        <Route path="/carrinho" element={<Carrinho />} />
        <Route path="/localizacao" element={<PaginaLocalizacao />} />
        <Route path="/PaginaAdmin" element={<PaginaAdmin />} />
        <Route path="/admin/login" element={<PaginaAdmin />} />
        <Route path="/admin" element={<Navigate to="/PaginaAdmin" replace />} />
        <Route
          path="/admin/produtos"
          element={
            <AdminGuard>
              <PaginaProdutosAdmin />
            </AdminGuard>
          }
        />
        <Route
          path="/admin/produtos/novo"
          element={
            <AdminGuard>
              <FormularioProdutoAdmin />
            </AdminGuard>
          }
        />
        <Route
          path="/admin/produtos/:id/editar"
          element={
            <AdminGuard>
              <FormularioProdutoAdmin />
            </AdminGuard>
          }
        />
        <Route path="/admin/configuracoes" element={<AdminGuard><ConfigAdmin /></AdminGuard>} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <StoreSettingsProvider>
        <CartProvider>
          <ProdutosProvider>
            <RotasAplicacao />
          </ProdutosProvider>
        </CartProvider>
      </StoreSettingsProvider>
    </BrowserRouter>
  );
}

export default App;
