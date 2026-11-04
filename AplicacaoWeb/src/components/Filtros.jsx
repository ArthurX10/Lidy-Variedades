import { useState } from 'react';
import './Filtros.css';

const filtrosPadrao = [
  {
    id: 'genero',
    titulo: 'Gênero',
    opcoes: ['Masculino', 'Feminino', 'Unissex'],
  },
  {
    id: 'tipo',
    titulo: 'Tipo',
    opcoes: ['Importado', 'Nacional'],
  },
  {
    id: 'preco',
    titulo: 'Faixa de Preço',
    opcoes: [
      'Até R$ 100',
      'R$ 100 a R$ 200',
      'R$ 200 a R$ 500',
      'Acima de R$ 500',
    ],
  },
  {
    id: 'marca',
    titulo: 'Marca',
    opcoes: ['O Boticário', 'Natura', 'Eudora', 'Avon', 'Jequiti'],
    temVerMais: true,
  },
];

const Filtros = ({ grupos = filtrosPadrao, onFilterChange }) => {
  const [selecionados, setSelecionados] = useState({});
  const [expandido, setExpandido] = useState({});

  const handleCheckboxChange = (grupoId, opcao) => {
    setSelecionados((prev) => {
      const grupoAtuais = prev[grupoId] || [];
      const novos = grupoAtuais.includes(opcao)
        ? grupoAtuais.filter((item) => item !== opcao)
        : [...grupoAtuais, opcao];

      const novoEstado = { ...prev, [grupoId]: novos };
      if (onFilterChange) onFilterChange(novoEstado);
      return novoEstado;
    });
  };

  const toggleExpandir = (grupoId) => {
    setExpandido((prev) => ({
      ...prev,
      [grupoId]: !prev[grupoId],
    }));
  };

  const limparFiltros = () => {
    setSelecionados({});
    if (onFilterChange) onFilterChange({});
  };

  const temFiltroAtivo = Object.values(selecionados).some(
    (arr) => Array.isArray(arr) && arr.length > 0
  );

  return (
    <aside className="filtros-container">
      <div className="filtros-header">
        <h3 className="filtros-titulo-principal">Filtrar por</h3>
        {temFiltroAtivo && (
          <button
            type="button"
            className="filtro-limpar-btn"
            onClick={limparFiltros}
          >
            Limpar
          </button>
        )}
      </div>

      <div className="filtro-divisor-topo"></div>

      {grupos.map((grupo) => {
        const itensVisiveis =
          grupo.temVerMais && !expandido[grupo.id]
            ? grupo.opcoes.slice(0, 3)
            : grupo.opcoes;

        return (
          <div key={grupo.id} className="filtro-secao">
            <h4 className="filtro-secao-titulo">{grupo.titulo}</h4>

            <div className="filtro-opcoes">
              {itensVisiveis.map((opcao) => {
                const valor = typeof opcao === 'string' ? opcao : opcao.value;
                const label = typeof opcao === 'string' ? opcao : opcao.label;
                const isChecked = (selecionados[grupo.id] || []).includes(valor);

                return (
                  <label key={valor} className="filtro-opcao-label">
                    <input
                      type="checkbox"
                      className="filtro-checkbox"
                      checked={isChecked}
                      onChange={() => handleCheckboxChange(grupo.id, valor)}
                    />
                    <span className="filtro-custom-checkbox"></span>
                    <span className="filtro-opcao-texto">{label}</span>
                  </label>
                );
              })}
            </div>

            {grupo.temVerMais && grupo.opcoes.length > 3 && (
              <button
                type="button"
                className="filtro-ver-mais"
                onClick={() => toggleExpandir(grupo.id)}
              >
                {expandido[grupo.id] ? 'Ver menos' : 'Ver mais'}
                <span className="filtro-seta">
                  {expandido[grupo.id] ? ' ▲' : ' ▼'}
                </span>
              </button>
            )}

            <div className="filtro-divisor"></div>
          </div>
        );
      })}
    </aside>
  );
};

export default Filtros;
