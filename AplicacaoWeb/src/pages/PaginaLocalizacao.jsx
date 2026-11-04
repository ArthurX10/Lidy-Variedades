import Header from '../components/Header';
import Footer from '../components/Footer';
import { FaLocationDot } from 'react-icons/fa6';
import './PaginaLocalizacao.css';

const endereco = 'Sítio Cruz das Almas, Zona Rural, Guaraciaba do Norte, CE';
const linkMapa = `https://maps.app.goo.gl/TpgfMDYRBNLkDVuW9`;


export default function PaginaLocalizacao() {
  return (
    <>
      <Header />
      <main className="localizacao-page">
        <div className="localizacao-card">
          <FaLocationDot aria-hidden="true" />
          <h1>Onde estamos</h1>
          <p>{endereco}</p>
          <p>Atendimento de segunda a sexta, das 08h às 18h, e aos sábados, das 08h às 13h.</p>
          <a href={linkMapa} target="_blank" rel="noreferrer">
            Abrir endereço no Google Maps
          </a>
        </div>
      </main>
      <Footer />
    </>
  );
}
