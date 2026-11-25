import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import StatusBadge from '@/components/StatusBadge'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'

export default function Dashboard() {
  const [coletas, setColetas] = useState([])
  const [alertas, setAlertas] = useState([])
  const [rendimento, setRendimento] = useState(null)
  const [relatorio, setRelatorio] = useState(null)
  const [erro, setErro] = useState('')

  useEffect(() => {
    Promise.all([
      endpoints.coletas(),
      endpoints.alertasEstoque(),
      endpoints.rendimento(),
      endpoints.relatorio(),
    ])
      .then(([c, a, r, f]) => {
        setColetas(c.slice(0, 5))
        setAlertas(a)
        setRendimento(r)
        setRelatorio(f)
      })
      .catch((e) => setErro(e.message))
  }, [])

  const money = (v) =>
    Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

  return (
    <div>
      <PageHeader
        title="Painel"
        description="Visão geral de coletas, estoque e resultado financeiro."
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <div className="mb-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {[
          { label: 'Lucro líquido', value: money(relatorio?.lucroLiquido), to: '/relatorios' },
          {
            label: 'Resíduos gerados',
            value: rendimento ? `${rendimento.percentualResiduosGerados}%` : '—',
            to: '/residuos',
          },
          {
            label: 'Reaproveitamento',
            value: rendimento ? `${rendimento.percentualResiduosReaproveitados}%` : '—',
            to: '/reaproveitamento',
          },
          {
            label: 'Alertas de estoque',
            value: String(alertas.length),
            to: '/estoque',
            pulse: alertas.length > 0,
          },
        ].map((m) => (
          <Card key={m.label}>
            <CardHeader className="pb-2">
              <CardTitle className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                {m.label}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className={cn('font-[family-name:var(--font-display)] text-3xl font-semibold', m.pulse && 'animate-pulse')}>
                {m.value}
              </p>
              <Link to={m.to} className="mt-2 inline-block text-sm font-medium text-primary hover:underline">
                Ver detalhes
              </Link>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-4 lg:grid-cols-5">
        <Card className="lg:col-span-3">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle>Próximas coletas</CardTitle>
            <Link to="/coletas" className="text-sm font-medium text-primary hover:underline">
              Gerenciar
            </Link>
          </CardHeader>
          <CardContent>
            {coletas.length === 0 ? (
              <p className="py-8 text-center text-sm text-muted-foreground">Nenhuma coleta cadastrada.</p>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Polo</TableHead>
                    <TableHead>Data</TableHead>
                    <TableHead>Resíduo</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {coletas.map((c) => (
                    <TableRow key={c.id}>
                      <TableCell>{c.poloNome}</TableCell>
                      <TableCell>
                        {c.dataColeta} {c.horario?.slice(0, 5)}
                      </TableCell>
                      <TableCell>{c.tipoResiduoNome}</TableCell>
                      <TableCell>
                        <StatusBadge status={c.status} />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>

        <Card className="lg:col-span-2">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle>Estoque crítico</CardTitle>
            <Link to="/estoque" className="text-sm font-medium text-primary hover:underline">
              Ver todos
            </Link>
          </CardHeader>
          <CardContent className="space-y-3">
            {alertas.length === 0 ? (
              <p className="py-8 text-center text-sm text-muted-foreground">Sem alertas no momento.</p>
            ) : (
              alertas.map((a) => (
                <div
                  key={a.id}
                  className={cn(
                    'rounded-lg border px-3 py-3',
                    a.corAlerta === 'VERMELHO' && 'border-red-200 bg-red-50',
                    a.corAlerta === 'AMARELO' && 'border-amber-200 bg-amber-50',
                    a.corAlerta === 'VERDE' && 'border-emerald-200 bg-emerald-50',
                  )}
                >
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <p className="font-medium">{a.tipoResiduoNome}</p>
                      <p className="text-xs text-muted-foreground">{a.poloNome}</p>
                    </div>
                    <Badge variant={a.nivel === 'CRITICO' ? 'destructive' : 'warning'}>
                      {a.quantidadeKg} kg
                    </Badge>
                  </div>
                </div>
              ))
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
