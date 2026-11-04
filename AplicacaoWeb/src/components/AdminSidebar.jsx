import { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { FaGear, FaHouse, FaTag } from 'react-icons/fa6';
import { logout } from '../services/api';
import './AdminSidebar.css';

export default function AdminSidebar() {
  const [erro, setErro] = useState('');
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/PaginaAdmin', { replace: true });
    } catch (error) {
      setErro(error.message || 'Não foi possível encerrar a sessão.');
    }
  };

  return (
    <aside className="admin-sidebar" aria-label="Navegação administrativa">
      <div className="admin-sidebar-brand">
        <span>Lidy Variedades</span>
        <small>Painel Admin</small>
      </div>
      <nav className="admin-sidebar-nav">
        <NavLink
          className={({ isActive }) => `admin-sidebar-link${isActive ? ' active' : ''}`}
          to="/admin/produtos"
        >
          <FaTag aria-hidden="true" />
          <span>Produtos</span>
        </NavLink>
        <NavLink
          className={({ isActive }) => `admin-sidebar-link${isActive ? ' active' : ''}`}
          to="/admin/configuracoes"
        >
          <FaGear aria-hidden="true" />
          <span>Configurações</span>
        </NavLink>
        <NavLink className="admin-sidebar-link" to="/">
          <FaHouse aria-hidden="true" />
          <span>Ver Loja</span>
        </NavLink>
      </nav>
      {erro && <p className="admin-sidebar-error" role="alert">{erro}</p>}
      <button type="button" className="admin-sidebar-logout" onClick={handleLogout}>
        Encerrar sessão
      </button>
    </aside>
  );
}
