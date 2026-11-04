import Card from './Card'
import './Destaques.css'

const produtosVazios = [];

const Destaques = ({ produtos = produtosVazios, carregando = false }) => {
  return (
    <section className="destaques-section">
        <div className="destaques-header">
            <h2>Destaques da semana</h2>
            <a href="#acervo" className="ver-produtos">Ver todos os produtos</a>
        </div>
        <div className="destaques-grid">
            {carregando ? (
              <p>Carregando produtos...</p>
            ) : produtos.length === 0 ? (
              <p>Nenhum produto cadastrado ainda.</p>
            ) : produtos.slice(0, 4).map((produto) => (
              <Card 
                key={produto.id}
                id={produto.id}
                imagem={produto.imagem}
                nome={produto.nome}
                descricao={produto.descricao}
                precoAntigo={produto.precoAntigo}
                precoAtual={produto.precoAtual}
              />
            ))}
        </div>
    </section>
  )
}

export default Destaques