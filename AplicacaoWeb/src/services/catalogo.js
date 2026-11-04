const categoriasPorRota = {
  cosmeticos: ['cosmetico', 'body splash', 'hidratante', 'maquiagem', 'skincare'],
  perfumes: ['perfume', 'perfumaria', 'fragrancia'],
  lingeries: ['lingerie', 'roupa intima'],
};

export function normalizarTexto(valor = '') {
  return valor
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLocaleLowerCase('pt-BR')
    .trim();
}

export function produtoPertenceCategoria(produto, categoria) {
  const rota = normalizarTexto(categoria);
  const aliases = categoriasPorRota[rota] || [rota];
  const nomeCategoria = normalizarTexto(produto.categoria);
  return aliases.some((alias) => nomeCategoria.includes(normalizarTexto(alias)));
}

export function produtoEstaEmPromocao(produto) {
  if (!produto) return false;
  const precoVenda = Number(produto.precoVenda) || 0;
  const precoPromocional = Number(produto.precoPromocional);
  return (
    produto.precoPromocional !== null &&
    produto.precoPromocional !== undefined &&
    !Number.isNaN(precoPromocional) &&
    precoPromocional > 0 &&
    precoPromocional < precoVenda
  );
}

export function precoEfetivo(produto) {
  if (!produto) return 0;
  return produtoEstaEmPromocao(produto)
    ? Number(produto.precoPromocional)
    : Number(produto.precoVenda) || 0;
}
