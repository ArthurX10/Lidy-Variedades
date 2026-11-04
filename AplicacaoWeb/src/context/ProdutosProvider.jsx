import { useCallback, useEffect, useMemo, useState } from 'react';
import { listarProdutos } from '../services/produtos';
import { ProdutosContext } from './ProdutosContextStore';

export function ProdutosProvider({ children }) {
  const [produtos, setProdutos] = useState([]);
  const [carregandoProdutos, setCarregandoProdutos] = useState(true);
  const [erroProdutos, setErroProdutos] = useState('');

  const atualizarProdutos = useCallback(async () => {
    setCarregandoProdutos(true);
    setErroProdutos('');

    try {
      setProdutos(await listarProdutos());
    } catch (error) {
      setErroProdutos(error.message || 'Não foi possível carregar os produtos.');
    } finally {
      setCarregandoProdutos(false);
    }
  }, []);

  const value = useMemo(() => ({
    produtos,
    carregandoProdutos,
    erroProdutos,
    atualizarProdutos,
  }), [produtos, carregandoProdutos, erroProdutos, atualizarProdutos]);

  useEffect(() => {
    let ativo = true;

    const carregar = async () => {
      try {
        const produtosCarregados = await listarProdutos();
        if (ativo) setProdutos(produtosCarregados);
      } catch (error) {
        if (ativo) {
          setErroProdutos(error.message || 'Não foi possível carregar os produtos.');
        }
      } finally {
        if (ativo) setCarregandoProdutos(false);
      }
    };

    void carregar();
    return () => {
      ativo = false;
    };
  }, []);

  return (
    <ProdutosContext.Provider value={value}>
      {children}
    </ProdutosContext.Provider>
  );
}
