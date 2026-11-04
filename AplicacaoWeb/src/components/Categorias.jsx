import { Link } from 'react-router-dom';
import './Categorias.css';
import imgCosmeticos from '../assets/download.jpg';

const categoriasData = [
  {
    id: 1,
    nome: 'Cosméticos',
    subtitulo: 'Cuidados e beleza',
    imagem: imgCosmeticos,
    path: '/cosmeticos',
  },
  {
    id: 2,
    nome: 'Perfumes',
    subtitulo: 'Aromas que encantam',
    imagem: imgCosmeticos,
    path: '/perfumes',
  },
  {
    id: 3,
    nome: 'Lingeries',
    subtitulo: 'Conforto e estilo',
    imagem: imgCosmeticos,
    path: '/lingeries',
  },
];

function Categorias() {
  return (
    <section className="categoria-section">
      <div className="categorias-header">
        <h2>Categorias Exclusivas</h2>
        <p>Selecionadas para cada momento</p>
      </div>
      <div className="categorias-grid">
        {categoriasData.map((categoria) => (
          <Link
            key={categoria.id}
            to={categoria.path}
            className="categoria-card"
          >
            <div className="categoria-img-container">
              <img src={categoria.imagem} alt={categoria.nome} />
            </div>
            <h3>{categoria.nome}</h3>
            <span>{categoria.subtitulo}</span>
          </Link>
        ))}
      </div>
    </section>
  );
}

export default Categorias;