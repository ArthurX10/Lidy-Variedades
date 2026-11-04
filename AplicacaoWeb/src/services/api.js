const API_BASE_URL = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/$/, '');
let csrfToken;

export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
  }
}

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    credentials: 'include',
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
  });

  const responseText = await response.text();
  let responseBody = null;
  if (responseText) {
    try {
      responseBody = JSON.parse(responseText);
    } catch {
      responseBody = responseText;
    }
  }

  if (!response.ok) {
    const message =
      typeof responseBody === 'object' && responseBody !== null
        ? responseBody.message || responseBody.detail
        : responseBody;
    throw new ApiError(message || `Falha na API (${response.status}).`, response.status);
  }

  return responseBody;
}

async function getCsrfHeader() {
  if (!csrfToken) {
    const csrf = await request('/auth/csrf');
    if (!csrf?.token) {
      throw new Error('A API não retornou o token de segurança CSRF.');
    }
    csrfToken = csrf.token;
  }

  return { 'X-XSRF-TOKEN': csrfToken };
}

async function mutate(path, method, body) {
  const headers = await getCsrfHeader();
  const result = await request(path, {
    method,
    headers,
    ...(body === undefined ? {} : { body: JSON.stringify(body) }),
  });

  if (path === '/auth/login' || path === '/auth/logout') {
    csrfToken = undefined;
  }
  return result;
}

export function login(email, password) {
  return mutate('/auth/login', 'POST', { email, password });
}

export async function logout() {
  await mutate('/auth/logout', 'POST');
  csrfToken = undefined;
}

export function getAdminSession() {
  return request('/auth/session');
}

export function getProducts() {
  return request('/produtos');
}

export function getCatalogProducts() {
  return request('/catalogo/produtos');
}

export function getProductById(id) {
  return request(`/produtos/${id}`);
}

export function getCategories() {
  return request('/categorias');
}

export function getBrands() {
  return request('/marcas');
}

export function getStoreSettings() {
  return request('/configuracoes');
}

export function updateStoreSettings(settings) {
  return mutate('/configuracoes', 'PUT', settings);
}

export function createProduct(product) {
  return mutate('/produtos', 'POST', product);
}

export function updateProduct(id, product) {
  return mutate(`/produtos/${id}`, 'PUT', product);
}

export function deleteProduct(id) {
  return mutate(`/produtos/${id}`, 'DELETE');
}
