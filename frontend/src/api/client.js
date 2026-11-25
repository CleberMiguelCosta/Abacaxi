const API_BASE = '/api'

function getToken() {
  try {
    const raw = localStorage.getItem('abacaxi_auth')
    if (!raw) return null
    return JSON.parse(raw).tokenAcesso
  } catch {
    return null
  }
}

async function request(path, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  }
  const token = getToken()
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE}${path}`, { ...options, headers })
  if (res.status === 204) return null

  const text = await res.text()
  const data = text ? JSON.parse(text) : null

  if (!res.ok) {
    const msg = data?.mensagem || data?.message || `Erro ${res.status}`
    const detalhes = data?.detalhes?.join(', ')
    throw new Error(detalhes ? `${msg}: ${detalhes}` : msg)
  }
  return data
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: 'POST', body: JSON.stringify(body) }),
  put: (path, body) => request(path, { method: 'PUT', body: JSON.stringify(body) }),
  patch: (path, body) => request(path, { method: 'PATCH', body: JSON.stringify(body) }),
  delete: (path) => request(path, { method: 'DELETE' }),
}

export const endpoints = {
  login: (body) => api.post('/auth/login', body),
  polos: () => api.get('/polos'),
  criarPolo: (body) => api.post('/polos', body),
  atualizarPolo: (id, body) => api.put(`/polos/${id}`, body),
  excluirPolo: (id) => api.delete(`/polos/${id}`),

  coletas: (params = {}) => {
    const q = new URLSearchParams()
    Object.entries(params).forEach(([k, v]) => {
      if (v !== undefined && v !== null && v !== '') q.set(k, v)
    })
    const qs = q.toString()
    return api.get(`/coletas${qs ? `?${qs}` : ''}`)
  },
  criarColeta: (body) => api.post('/coletas', body),
  atualizarColeta: (id, body) => api.put(`/coletas/${id}`, body),
  cancelarColeta: (id) => api.patch(`/coletas/${id}/cancelar`),
  statusColeta: (id, status) => api.patch(`/coletas/${id}/status`, { status }),
  calendario: (inicio, fim) => api.get(`/coletas/calendario?inicio=${inicio}&fim=${fim}`),

  tiposResiduo: () => api.get('/tipos-residuo'),
  criarTipoResiduo: (body) => api.post('/tipos-residuo', body),

  registros: () => api.get('/registros-residuo'),
  criarRegistro: (body) => api.post('/registros-residuo', body),
  rendimento: () => api.get('/registros-residuo/rendimento'),
  alertasQualidade: () => api.get('/registros-residuo/alertas-qualidade'),

  destinos: () => api.get('/destinos'),
  criarDestino: (body) => api.post('/destinos', body),

  estoques: () => api.get('/estoques'),
  alertasEstoque: () => api.get('/estoques/alertas'),

  vendas: () => api.get('/vendas'),
  criarVenda: (body) => api.post('/vendas', body),

  relatorio: (inicio, fim) => {
    const q = new URLSearchParams()
    if (inicio) q.set('inicio', inicio)
    if (fim) q.set('fim', fim)
    const qs = q.toString()
    return api.get(`/financeiro/relatorio${qs ? `?${qs}` : ''}`)
  },

  metas: () => api.get('/metas'),
  criarMeta: (body) => api.post('/metas', body),

  reaproveitamento: () => api.get('/reaproveitamento'),
}
