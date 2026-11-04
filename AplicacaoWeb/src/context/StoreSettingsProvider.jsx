import { useCallback, useEffect, useMemo, useState } from 'react';
import { getStoreSettings, updateStoreSettings } from '../services/api';
import { defaultStoreSettings, StoreSettingsContext } from './StoreSettingsStore';

export function StoreSettingsProvider({ children }) {
  const [settings, setSettings] = useState(defaultStoreSettings);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let active = true;

    getStoreSettings()
      .then((currentSettings) => {
        if (!active) return;
        setSettings({ ...defaultStoreSettings, ...currentSettings });
        setError('');
      })
      .catch((requestError) => {
        if (active) {
          setError(requestError.message || 'Não foi possível carregar as configurações da loja.');
        }
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const refreshSettings = useCallback(async () => {
    try {
      const currentSettings = await getStoreSettings();
      setSettings({ ...defaultStoreSettings, ...currentSettings });
      setError('');
      setLoading(false);
    } catch (requestError) {
      setError(requestError.message || 'Não foi possível carregar as configurações da loja.');
      setLoading(false);
    }
  }, []);

  const saveSettings = useCallback(async (nextSettings) => {
    setError('');
    try {
      const savedSettings = await updateStoreSettings(nextSettings);
      setSettings({ ...defaultStoreSettings, ...savedSettings });
      return savedSettings;
    } catch (requestError) {
      setError(requestError.message || 'Não foi possível salvar as configurações da loja.');
      throw requestError;
    }
  }, []);

  const value = useMemo(() => ({
    settings,
    loading,
    error,
    refreshSettings,
    saveSettings,
  }), [settings, loading, error, refreshSettings, saveSettings]);

  return (
    <StoreSettingsContext.Provider value={value}>
      {children}
    </StoreSettingsContext.Provider>
  );
}
