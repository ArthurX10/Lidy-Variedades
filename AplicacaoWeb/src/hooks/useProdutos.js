import { useContext } from 'react';
import { ProdutosContext } from '../context/ProdutosContextStore';

export function useProdutos() {
  const context = useContext(ProdutosContext);
  if (!context) {
    throw new Error('useProdutos deve ser usado dentro de ProdutosProvider.');
  }
  return context;
}
