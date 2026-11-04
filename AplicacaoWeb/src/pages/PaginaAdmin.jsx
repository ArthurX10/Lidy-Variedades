import { useState } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';
import './PaginaAdmin.css';

const PaginaAdmin = () => {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [enviando, setEnviando] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setErro('');
        setEnviando(true);

        try {
            await login(email, senha);
            navigate('/admin/produtos', { replace: true });
        } catch (error) {
            setErro(error.message || 'Não foi possível entrar.');
        } finally {
            setEnviando(false);
        }
    };

  return (
    <div className='admin-page'>
        <Header/>
        <main className='admin-container'>
        <form className="admin-card" onSubmit={handleSubmit}>
            <div className='admin-card-header'>
                <h2>Painel Administrativo</h2>
                <p>Acesso restrito ao administrador</p>
            </div>

            <div className='admin-card-fields'>

            <label htmlFor="email">Email</label>
            <input 
            id= 'email'
            type="email"
            autoComplete="username"
            required
            value={email}
            onChange = {(e)=> setEmail(e.target.value)}
            />

            <label htmlFor="password">Senha</label>
            <input
            id='password'
            type="password"
            autoComplete="current-password"
            required
            value={senha}
            onChange={(e)=>setSenha(e.target.value)}
            />

            {erro && <p className="admin-form-error" role="alert">{erro}</p>}
            <button type='submit' disabled={enviando}>
                {enviando ? 'Entrando...' : 'Entrar'}
            </button>
            </div>

        </form>
        </main>


        <Footer/>
        
    </div>
    
  )
}

export default PaginaAdmin