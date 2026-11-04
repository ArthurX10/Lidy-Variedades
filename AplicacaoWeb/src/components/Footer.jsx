import './Footer.css';
import { FaWhatsapp, FaInstagram, FaLocationDot, FaClock } from 'react-icons/fa6';
import { IoLogoWhatsapp } from 'react-icons/io';
import { NavLink } from 'react-router-dom';
import { useStoreSettings } from '../hooks/useStoreSettings';

const itensLeft = [
  { nome: 'Perfumes', path: '/perfumes' },
  { nome: 'Cosméticos', path: '/cosmeticos' },
  { nome: 'Lingeries', path: '/lingeries' },
];

const itensRight = [
  { nome: 'Promoções', path: '/promocoes' },
  { nome: 'Onde Estamos', path: '/localizacao' },
  { nome: 'Acesso Restrito', path: '/PaginaAdmin' },
];

function formatarTelefone(numero) {
  const digitos = (numero || '').replace(/\D/g, '');
  if (digitos.length === 13) {
    return `(${digitos.slice(2, 4)}) ${digitos.slice(4, 9)}-${digitos.slice(9)}`;
  }
  if (digitos.length === 12) {
    return `(${digitos.slice(2, 4)}) ${digitos.slice(4, 8)}-${digitos.slice(8)}`;
  }
  return digitos;
}

const Footer = () => {
  const { settings } = useStoreSettings();
  const telefone = (settings?.whatsappNumber || '').replace(/\D/g, '');

  return (
    <footer className="footer-container">

      {/* Coluna 1 - Logo e redes sociais */}
      <div className="footer-col footer-brand">
        <h2 className="footer-logo">Lidy Variedades</h2>
        <p>
          Sua loja favorita de cosméticos, perfumes e lingeries
          em Guaraciaba do Norte e região. Realce sua beleza com os
          melhores produtos.
        </p>
        <div className="footer-sociais">
          <a href={`https://wa.me/${telefone}`} target="_blank" rel="noreferrer" aria-label="WhatsApp">
            <FaWhatsapp size={22} />
          </a>
          {settings?.instagramUrl && (
            <a href={settings.instagramUrl} target="_blank" rel="noreferrer" aria-label={settings.instagramProfile || 'Instagram'}>
              <FaInstagram size={22} />
            </a>
          )}
        </div>
      </div>

      {/* Coluna 2 - Contato */}
      <div className="footer-col">
        <h3 className="footer-col-titulo">Fale Conosco</h3>
        <ul className="footer-info-lista">
          <li>
            <FaLocationDot size={14} />
            <span>Sítio Cruz das Almas, Zona Rural<br />Guaraciaba do Norte – CE</span>
          </li>
          <li>
            <IoLogoWhatsapp size={14} />
            <span>{formatarTelefone(settings?.whatsappNumber)}</span>
          </li>
          <li>
            <FaClock size={14} />
            <span>Seg a Sex: 08h às 18h<br />Sábado: 08h às 13h</span>
          </li>
        </ul>
      </div>

      {/* Coluna 3 - Categorias */}
      <div className="footer-col">
        <h3 className="footer-col-titulo">Categorias</h3>
        <div className="footer-categorias">
          <ul>
            {itensLeft.map((item) => (
              <li key={item.path}>
                <NavLink to={item.path}>{item.nome}</NavLink>
              </li>
            ))}
          </ul>
          <ul>
            {itensRight.map((item) => (
              <li key={item.path}>
                <NavLink to={item.path}>{item.nome}</NavLink>
              </li>
            ))}
          </ul>
        </div>
      </div>

    </footer>
  );
};

export default Footer;
