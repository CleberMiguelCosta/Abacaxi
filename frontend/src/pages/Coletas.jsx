import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import StatusBadge from '@/components/StatusBadge'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'

const STATUS = ['AGENDADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA']
const emptyForm = {
  poloId: '',
  dataColeta: '',
  horario: '08:00',
  tipoResiduoId: '',
  quantidadePrevistaKg: '',
  localColeta: '',
  observacoes: '',
}

export default function Coletas() {
  const [lista, setLista] = useState([])
  const [polos, setPolos] = useState([])
  const [tipos, setTipos] = useState([])
  const [filtroStatus, setFiltroStatus] = useState('')
  const [erro, setErro] = useState('')
  const [ok, setOk] = useState('')
  const [open, setOpen] = useState(false)
  const [editId, setEditId] = useState(null)
  const [form, setForm] = useState(emptyForm)

  const carregar = async () => {
    setLista(await endpoints.coletas(filtroStatus ? { status: filtroStatus } : {}))
  }

  useEffect(() => {
    Promise.all([endpoints.polos(), endpoints.tiposResiduo()])
      .then(([p, t]) => {
        setPolos(p)
        setTipos(t)
      })
      .catch((e) => setErro(e.message))
  }, [])

  useEffect(() => {
    carregar().catch((e) => setErro(e.message))
  }, [filtroStatus])

  const salvar = async (e) => {
    e.preventDefault()
    setErro('')
    setOk('')
    const body = {
      poloId: Number(form.poloId),
      dataColeta: form.dataColeta,
      horario: form.horario.length === 5 ? `${form.horario}:00` : form.horario,
      tipoResiduoId: Number(form.tipoResiduoId),
      quantidadePrevistaKg: Number(form.quantidadePrevistaKg),
      localColeta: form.localColeta || null,
      observacoes: form.observacoes || null,
    }
    try {
      if (editId) await endpoints.atualizarColeta(editId, body)
      else await endpoints.criarColeta(body)
      setOpen(false)
      setOk(editId ? 'Coleta atualizada.' : 'Coleta cadastrada.')
      await carregar()
    } catch (err) {
      setErro(err.message)
    }
  }

  return (
    <div>
      <PageHeader
        title="Coletas"
        description="Cadastro, consulta, edição, cancelamento e status."
        actions={
          <Button
            onClick={() => {
              setEditId(null)
              setForm(emptyForm)
              setOpen(true)
            }}
          >
            Nova coleta
          </Button>
        }
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}
      {ok && (
        <div className="mb-4 rounded-md border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-800">
          {ok}
        </div>
      )}

      <Card className="mb-4">
        <CardContent className="pt-6">
          <div className="max-w-xs space-y-2">
            <Label>Filtrar por status</Label>
            <Select value={filtroStatus} onChange={(e) => setFiltroStatus(e.target.value)}>
              <option value="">Todos</option>
              {STATUS.map((s) => (
                <option key={s} value={s}>
                  {s.replaceAll('_', ' ')}
                </option>
              ))}
            </Select>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="pt-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Polo</TableHead>
                <TableHead>Data</TableHead>
                <TableHead>Horário</TableHead>
                <TableHead>Resíduo</TableHead>
                <TableHead>Qtd (kg)</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {lista.map((c) => (
                <TableRow key={c.id}>
                  <TableCell>{c.poloNome}</TableCell>
                  <TableCell>{c.dataColeta}</TableCell>
                  <TableCell>{c.horario?.slice(0, 5)}</TableCell>
                  <TableCell>{c.tipoResiduoNome}</TableCell>
                  <TableCell>{c.quantidadePrevistaKg}</TableCell>
                  <TableCell>
                    <StatusBadge status={c.status} />
                  </TableCell>
                  <TableCell>
                    <div className="flex flex-wrap items-center gap-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                          setEditId(c.id)
                          setForm({
                            poloId: String(c.poloId),
                            dataColeta: c.dataColeta,
                            horario: c.horario?.slice(0, 5) || '08:00',
                            tipoResiduoId: String(c.tipoResiduoId),
                            quantidadePrevistaKg: String(c.quantidadePrevistaKg),
                            localColeta: c.localColeta || '',
                            observacoes: c.observacoes || '',
                          })
                          setOpen(true)
                        }}
                      >
                        Editar
                      </Button>
                      <Select
                        className="h-8 w-auto"
                        value={c.status}
                        onChange={(e) =>
                          endpoints
                            .statusColeta(c.id, e.target.value)
                            .then(carregar)
                            .catch((err) => setErro(err.message))
                        }
                      >
                        {STATUS.map((s) => (
                          <option key={s} value={s}>
                            {s.replaceAll('_', ' ')}
                          </option>
                        ))}
                      </Select>
                      {c.status !== 'CANCELADA' && c.status !== 'CONCLUIDA' && (
                        <Button
                          variant="destructive"
                          size="sm"
                          onClick={() =>
                            endpoints
                              .cancelarColeta(c.id)
                              .then(() => {
                                setOk('Coleta cancelada.')
                                return carregar()
                              })
                              .catch((err) => setErro(err.message))
                          }
                        >
                          Cancelar
                        </Button>
                      )}
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          {lista.length === 0 && (
            <p className="py-8 text-center text-sm text-muted-foreground">Nenhuma coleta encontrada.</p>
          )}
        </CardContent>
      </Card>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editId ? 'Editar coleta' : 'Nova coleta'}</DialogTitle>
          </DialogHeader>
          <form className="space-y-3" onSubmit={salvar}>
            <div className="space-y-2">
              <Label>Polo</Label>
              <Select required value={form.poloId} onChange={(e) => setForm({ ...form, poloId: e.target.value })}>
                <option value="">Selecione</option>
                {polos.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.nome}
                  </option>
                ))}
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Tipo de resíduo</Label>
              <Select
                required
                value={form.tipoResiduoId}
                onChange={(e) => setForm({ ...form, tipoResiduoId: e.target.value })}
              >
                <option value="">Selecione</option>
                {tipos.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.nome}
                  </option>
                ))}
              </Select>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-2">
                <Label>Data</Label>
                <Input
                  type="date"
                  required
                  value={form.dataColeta}
                  onChange={(e) => setForm({ ...form, dataColeta: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>Horário</Label>
                <Input
                  type="time"
                  required
                  value={form.horario}
                  onChange={(e) => setForm({ ...form, horario: e.target.value })}
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Quantidade prevista (kg)</Label>
              <Input
                type="number"
                min="0.1"
                step="0.1"
                required
                value={form.quantidadePrevistaKg}
                onChange={(e) => setForm({ ...form, quantidadePrevistaKg: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label>Local</Label>
              <Input value={form.localColeta} onChange={(e) => setForm({ ...form, localColeta: e.target.value })} />
            </div>
            <div className="space-y-2">
              <Label>Observações</Label>
              <Textarea
                rows={2}
                value={form.observacoes}
                onChange={(e) => setForm({ ...form, observacoes: e.target.value })}
              />
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setOpen(false)}>
                Fechar
              </Button>
              <Button type="submit">Salvar</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  )
}
