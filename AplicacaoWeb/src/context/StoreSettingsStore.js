import { createContext } from 'react';

export const defaultStoreSettings = {
  whatsappNumber: '5588981137253',
  bannerEnabled: false,
  bannerText: '',
  instagramProfile: '@_lidy.variedades',
  instagramUrl: 'https://www.instagram.com/_lidy.variedades/',
};

export const StoreSettingsContext = createContext(null);
