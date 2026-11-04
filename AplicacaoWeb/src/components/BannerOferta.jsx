import { Link } from 'react-router-dom';
import './BannerOferta.css';

const BannerOferta = () => {
  return (
    <section className="banner-oferta-container">
      <div className="banner-oferta-content">
        <div className="banner-oferta-texto">
          <h2>Beleza que cabe no seu bolso</h2>
          <p>Confira nossa seleção de produtos com descontos especiais de até 40%.</p>
        </div>

      <Link className="banner-oferta-btn" to="/promocoes">
          Aproveitar Ofertas
      </Link>
      </div>
    </section>
  );
};

export default BannerOferta;
