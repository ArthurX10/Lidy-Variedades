import imagemPadrao from '../assets/download.jpg';
import { getCatalogProducts, getProducts, getProductById } from './api';
import { precoEfetivo, produtoEstaEmPromocao } from './catalogo';

const moeda = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
});

export function formatarPreco(value) {
  return moeda.format(Number(value) || 0);
}

export function normalizarProduto(produto) {
  const precoVenda = Number(produto.precoVenda) || 0;
  const precoPromocional = produto.precoPromocional == null
    ? null
    : Number(produto.precoPromocional);
  const emPromocao = produtoEstaEmPromocao(produto);
  const valorEfetivo = precoEfetivo(produto);

  return {
    ...produto,
    categoriaId: produto.categoria?.id ?? '',
    categoria: produto.categoria?.nome ?? '',
    marcaId: produto.marca?.id ?? '',
    marca: produto.marca?.nome ?? '',
    precoVenda,
    precoPromocional,
    precoAntigo: emPromocao ? formatarPreco(precoVenda) : '',
    precoAtual: formatarPreco(valorEfetivo),
    imagem: produto.imagemUrl || imagemPadrao,
  };
}

export async function listarProdutos() {
  const produtos = await getCatalogProducts();
  if (!Array.isArray(produtos)) {
    throw new Error('A API retornou uma lista de produtos inválida.');
  }
  return produtos.map(normalizarProduto);
}

export async function listarProdutosAdmin() {
  const produtos = await getProducts();
  if (!Array.isArray(produtos)) {
    throw new Error('A API retornou uma lista de produtos inválida.');
  }
  return produtos.map(normalizarProduto);
}

export async function obterProdutoAdmin(id) {
  return normalizarProduto(await getProductById(id));
}
