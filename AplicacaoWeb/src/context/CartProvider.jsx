import { useCallback, useMemo, useState } from 'react';
import { CartContext } from './CartStore';
import { precoEfetivo } from '../services/catalogo';

export function CartProvider({ children }) {
  const [items, setItems] = useState([]);

  const addToCart = useCallback((product) => {
    setItems((currentItems) => {
      const existing = currentItems.find((item) => item.id === product.id);
      const stock = Number.isInteger(product.quantidadeEstoque)
        ? product.quantidadeEstoque
        : null;
      if (stock !== null && (stock === 0 || (existing && existing.quantity >= stock))) {
        return currentItems;
      }
      if (existing) {
        return currentItems.map((item) => (
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        ));
      }
      return [...currentItems, { ...product, quantity: 1 }];
    });
  }, []);

  const updateQuantity = useCallback((id, quantity) => {
    setItems((currentItems) => currentItems.flatMap((item) => {
      if (item.id !== id) return [item];
      if (quantity <= 0) return [];

      const stock = Number.isInteger(item.quantidadeEstoque)
        ? item.quantidadeEstoque
        : null;
      const updatedQuantity = Math.floor(quantity);
      if (updatedQuantity < 1) return [];

      return [{
        ...item,
        quantity: stock === null ? updatedQuantity : Math.min(updatedQuantity, stock),
      }];
    }));
  }, []);

  const removeFromCart = useCallback((id) => {
    setItems((currentItems) => currentItems.filter((item) => item.id !== id));
  }, []);

  const value = useMemo(() => ({
    items,
    itemCount: items.reduce((total, item) => total + item.quantity, 0),
    total: items.reduce(
      (sum, item) => sum + precoEfetivo(item) * item.quantity,
      0,
    ),
    addToCart,
    updateQuantity,
    removeFromCart,
  }), [items, addToCart, updateQuantity, removeFromCart]);

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}
