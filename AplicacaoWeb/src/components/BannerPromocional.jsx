import { Link } from 'react-router-dom';
import './BannerPromocional.css';
import banner from '../assets/teste.avif';

function BannerPromocional({
  imagem = banner,
  titulo = 'Sua Essência, Nossa Maior Inspiração.',
  subtitulo = 'Nova coleção 2026',
}) {
  const estiloFundo = {
    backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.45), rgba(0, 0, 0, 0.45)), url(${imagem})`,
  };

  return (
    <div className="banner-container" style={estiloFundo}>
      <div className="banner-text-left">
        <p>{subtitulo}</p>
        <h1>{titulo}</h1>
        <Link to="#acervo">Explorar catálogo</Link>
      </div>
    </div>
  );
}

export default BannerPromocional;
