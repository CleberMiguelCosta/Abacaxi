import { Navigate, Route, Routes } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'
import Layout from '@/components/Layout'
import Login from '@/pages/Login'
import Dashboard from '@/pages/Dashboard'
import Coletas from '@/pages/Coletas'
import Calendario from '@/pages/Calendario'
import Polos from '@/pages/Polos'
import Residuos from '@/pages/Residuos'
import Estoque from '@/pages/Estoque'
import Vendas from '@/pages/Vendas'
import Relatorios from '@/pages/Relatorios'
import Metas from '@/pages/Metas'
import Reaproveitamento from '@/pages/Reaproveitamento'

function PrivateRoute({ children }) {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  return children
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/"
        element={
          <PrivateRoute>
            <Layout />
          </PrivateRoute>
        }
      >
        <Route index element={<Dashboard />} />
        <Route path="coletas" element={<Coletas />} />
        <Route path="calendario" element={<Calendario />} />
        <Route path="polos" element={<Polos />} />
        <Route path="residuos" element={<Residuos />} />
        <Route path="estoque" element={<Estoque />} />
        <Route path="vendas" element={<Vendas />} />
        <Route path="relatorios" element={<Relatorios />} />
        <Route path="metas" element={<Metas />} />
        <Route path="reaproveitamento" element={<Reaproveitamento />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
