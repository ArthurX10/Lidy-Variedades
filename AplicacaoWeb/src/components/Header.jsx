import Logo from './Logo';
import './Header.css';
import { FaBagShopping, FaBars, FaMagnifyingGlass, FaWhatsapp, FaXmark } from 'react-icons/fa6';
import { IconField } from 'primereact/iconfield';
import { InputIcon } from 'primereact/inputicon';
import { InputText } from 'primereact/inputtext';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useCart } from '../hooks/useCart';
import { useStoreSettings } from '../hooks/useStoreSettings';
const itensMenu = [
  { nome: 'Cosméticos', path: '/cosmeticos' },
  { nome: 'Perfumes', path: '/perfumes' },
  { nome: 'Lingeries', path: '/lingeries' },
  { nome: 'Promoções', path: '/promocoes' },
];

const Header = () => {
  const [consulta, setConsulta] = useState('');
  const [menuAberto, setMenuAberto] = useState(false);
  const navigate = useNavigate();
  const { itemCount } = useCart();
  const { settings } = useStoreSettings();
  const telefone = (settings?.whatsappNumber || '').replace(/\D/g, '');

  const fecharMenu = () => setMenuAberto(false);

  const handleSearch = (event) => {
    event.preventDefault();
    navigate(`/busca?q=${encodeURIComponent(consulta.trim())}`);
  };

  return (
    <>
      <header className="header-content">
        <div className="header-left">
          <button
            type="button"
            className="menu-toggle"
            aria-label={menuAberto ? 'Fechar menu' : 'Abrir menu'}
            aria-expanded={menuAberto}
            aria-controls="header-navigation"
            onClick={() => setMenuAberto((aberto) => !aberto)}
          >
            {menuAberto ? <FaXmark aria-hidden="true" /> : <FaBars aria-hidden="true" />}
          </button>

          <Link className="header-logo" to="/" aria-label="Lidy Variedades - página inicial">
            <Logo />
          </Link>
          <nav
            id="header-navigation"
            className={`nav-links ${menuAberto ? 'aberto' : ''}`}
            aria-label="Navegação principal"
          >
            <ul className="menu">
              {itensMenu.map((item) => (
                <li key={item.path}>
                  <NavLink to={item.path} onClick={fecharMenu}>
                    {item.nome}
                  </NavLink>
                </li>
              ))}
            </ul>
          </nav>
        </div>

        <div className="header-right">
          <form className="header-search" onSubmit={handleSearch} role="search">
            <IconField iconPosition="left" className="search-field">
              <InputIcon className="pi pi-search search-icon" />
              <InputText
                aria-label="Buscar produtos"
                placeholder="Buscar Produtos..."
                className="search-input"
                value={consulta}
                onChange={(event) => setConsulta(event.target.value)}
              />
            </IconField>
            <button type="submit" className="header-search-submit" aria-label="Buscar">
              <FaMagnifyingGlass aria-hidden="true" />
            </button>
          </form>

          <Link className="header-cart-link" to="/carrinho" aria-label={`Carrinho, ${itemCount} itens`}>
            <FaBagShopping aria-hidden="true" />
            <span className="header-cart-label">Carrinho</span>
            <span className="header-cart-count">{itemCount}</span>
          </Link>
          <a
            className="header-whatsapp"
            href={`https://wa.me/${telefone}`}
            target="_blank"
            rel="noreferrer"
            aria-label="Fale pelo WhatsApp"
          >
            <FaWhatsapp size={25} color="#25D366" className="whatsapp-btn" />
          </a>
        </div>
      </header>
      {settings?.bannerEnabled && settings?.bannerText && (
        <div className="store-announcement" role="status">{settings.bannerText}</div>
      )}
    </>
  );
};

export default Header;
