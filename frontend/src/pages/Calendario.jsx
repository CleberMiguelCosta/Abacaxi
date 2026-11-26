import { useEffect, useMemo, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import StatusBadge from '@/components/StatusBadge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { cn } from '@/lib/utils'

function monthBounds(date) {
  const y = date.getFullYear()
  const m = date.getMonth()
  const inicio = new Date(y, m, 1)
  const fim = new Date(y, m + 1, 0)
  const iso = (d) => d.toISOString().slice(0, 10)
  return { inicio: iso(inicio), fim: iso(fim), y, m }
}

export default function Calendario() {
  const [cursor, setCursor] = useState(() => new Date())
  const [eventos, setEventos] = useState([])
  const [selecionado, setSelecionado] = useState(null)
  const [erro, setErro] = useState('')
  const [live, setLive] = useState(false)

  const { inicio, fim, y, m } = useMemo(() => monthBounds(cursor), [cursor])
  const mesLabel = cursor.toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' })
  const diasNoMes = new Date(y, m + 1, 0).getDate()
  const primeiroDiaSemana = new Date(y, m, 1).getDay()

  useEffect(() => {
    endpoints.calendario(inicio, fim).then(setEventos).catch((e) => setErro(e.message))
  }, [inicio, fim])

  useEffect(() => {
    const es = new EventSource(`/api/calendario/stream?inicio=${inicio}&fim=${fim}`)
    es.addEventListener('calendario', (ev) => {
      try {
        setEventos(JSON.parse(ev.data))
        setLive(true)
      } catch {
        /* ignore */
      }
    })
    es.onerror = () => {
      setLive(false)
      es.close()
    }
    return () => es.close()
  }, [inicio, fim])

  const porDia = eventos.reduce((acc, e) => {
    acc[e.data] = acc[e.data] || []
    acc[e.data].push(e)
    return acc
  }, {})

  const cells = []
  for (let i = 0; i < primeiroDiaSemana; i += 1) cells.push(null)
  for (let d = 1; d <= diasNoMes; d += 1) cells.push(d)

  return (
    <div>
      <PageHeader
        title="Calendário"
        description="Coletas e entregas do mês."
        actions={
          <div className="flex items-center gap-2">
            {live && <Badge variant="success">Ao vivo</Badge>}
            <Button variant="outline" size="sm" onClick={() => setCursor(new Date(y, m - 1, 1))}>
              ←
            </Button>
            <Button variant="outline" size="sm" onClick={() => setCursor(new Date())}>
              Hoje
            </Button>
            <Button variant="outline" size="sm" onClick={() => setCursor(new Date(y, m + 1, 1))}>
              →
            </Button>
          </div>
        }
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle className="capitalize">{mesLabel}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-7 gap-2">
            {['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'].map((d) => (
              <div key={d} className="px-1 py-1 text-center text-xs font-semibold uppercase text-muted-foreground">
                {d}
              </div>
            ))}
            {cells.map((d, idx) => {
              if (!d) return <div key={`e-${idx}`} />
              const iso = `${y}-${String(m + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`
              const dayEvents = porDia[iso] || []
              return (
                <button
                  type="button"
                  key={iso}
                  onClick={() => setSelecionado(iso)}
                  className={cn(
                    'min-h-20 rounded-lg border bg-card p-2 text-left transition hover:border-primary',
                    dayEvents.length > 0 && 'bg-secondary/40',
                    selecionado === iso && 'border-primary ring-2 ring-ring/40',
                  )}
                >
                  <span className="text-sm font-semibold">{d}</span>
                  <div className="mt-1 hidden space-y-1 sm:block">
                    {dayEvents.slice(0, 2).map((ev) => (
                      <div
                        key={ev.coletaId}
                        className="truncate rounded bg-background/80 px-1 py-0.5 text-[10px] text-muted-foreground"
                      >
                        {ev.horario?.slice(0, 5)} {ev.poloNome}
                      </div>
                    ))}
                  </div>
                </button>
              )
            })}
          </div>
        </CardContent>
      </Card>

      {selecionado && (
        <Card className="mt-4">
          <CardHeader>
            <CardTitle>Detalhes — {selecionado}</CardTitle>
          </CardHeader>
          <CardContent>
            {(porDia[selecionado] || []).length === 0 ? (
              <p className="py-6 text-center text-sm text-muted-foreground">Sem coletas neste dia.</p>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Horário</TableHead>
                    <TableHead>Polo</TableHead>
                    <TableHead>Local</TableHead>
                    <TableHead>Resíduo</TableHead>
                    <TableHead>Qtd</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {(porDia[selecionado] || []).map((e) => (
                    <TableRow key={e.coletaId}>
                      <TableCell>{e.horario?.slice(0, 5)}</TableCell>
                      <TableCell>{e.poloNome}</TableCell>
                      <TableCell>{e.local}</TableCell>
                      <TableCell>{e.tipoResiduo}</TableCell>
                      <TableCell>{e.quantidadeKg} kg</TableCell>
                      <TableCell>
                        <StatusBadge status={e.status} />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  )
}
