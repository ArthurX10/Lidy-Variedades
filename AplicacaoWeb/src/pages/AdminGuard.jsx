import { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { getAdminSession } from '../services/api';

export default function AdminGuard({ children }) {
  const [estado, setEstado] = useState('carregando');
  const [erro, setErro] = useState('');

  useEffect(() => {
    let ativo = true;

    getAdminSession()
      .then(() => {
        if (ativo) setEstado('autenticado');
      })
      .catch((error) => {
        if (!ativo) return;
        if (error.status === 401) {
          setEstado('nao-autenticado');
          return;
        }
        setErro(error.message || 'Não foi possível verificar o acesso administrativo.');
        setEstado('erro');
      });

    return () => {
      ativo = false;
    };
  }, []);

  if (estado === 'carregando') {
    return <p className="admin-access-message">Verificando acesso...</p>;
  }
  if (estado === 'nao-autenticado') {
    return <Navigate to="/PaginaAdmin" replace />;
  }
  if (estado === 'erro') {
    return <p className="admin-access-error" role="alert">{erro}</p>;
  }
  return children;
}
