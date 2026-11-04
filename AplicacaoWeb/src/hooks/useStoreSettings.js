import { useContext } from 'react';
import { StoreSettingsContext } from '../context/StoreSettingsStore';

export function useStoreSettings() {
  const context = useContext(StoreSettingsContext);
  if (!context) {
    throw new Error('useStoreSettings deve ser usado dentro de StoreSettingsProvider.');
  }
  return context;
}
