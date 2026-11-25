import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'

export default function Metas() {
  const [lista, setLista] = useState([])
  const [polos, setPolos] = useState([])
  const [erro, setErro] = useState('')
  const [open, setOpen] = useState(false)
  const [form, setForm] = useState({
    poloId: '',
    metaKg: '',
    dataInicio: '',
    dataFim: '',
    descricao: '',
  })

  const carregar = () =>
    Promise.all([endpoints.metas(), endpoints.polos()])
      .then(([m, p]) => {
        setLista(m)
        setPolos(p)
      })
      .catch((e) => setErro(e.message))

  useEffect(() => {
    carregar()
  }, [])

  return (
    <div>
      <PageHeader
        title="Metas de arrecadação"
        description="Metas por polo e percentual de atingimento."
        actions={<Button onClick={() => setOpen(true)}>Nova meta</Button>}
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {lista.map((m) => (
          <Card key={m.id}>
            <CardHeader>
              <CardTitle className="text-lg">{m.poloNome}</CardTitle>
              <p className="text-xs text-muted-foreground">
                {m.dataInicio} → {m.dataFim}
              </p>
            </CardHeader>
            <CardContent>
              <div className="mb-2 flex justify-between text-sm">
                <span>
                  Meta <strong>{m.metaKg} kg</strong>
                </span>
                <span>
                  Arrecadado <strong>{m.quantidadeArrecadadaKg} kg</strong>
                </span>
              </div>
              <div className="h-2 overflow-hidden rounded bg-muted">
                <div
                  className="h-full rounded bg-primary transition-all"
                  style={{ width: `${Math.min(m.percentualAtingimento || 0, 100)}%` }}
                />
              </div>
              <p className="mt-2 text-sm font-semibold text-primary">{m.percentualAtingimento}% atingido</p>
              {m.descricao && <p className="mt-2 text-sm text-muted-foreground">{m.descricao}</p>}
            </CardContent>
          </Card>
        ))}
      </div>

      {lista.length === 0 && (
        <p className="py-10 text-center text-sm text-muted-foreground">Nenhuma meta cadastrada.</p>
      )}

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Nova meta</DialogTitle>
          </DialogHeader>
          <form
            className="space-y-3"
            onSubmit={async (e) => {
              e.preventDefault()
              try {
                await endpoints.criarMeta({
                  poloId: Number(form.poloId),
                  metaKg: Number(form.metaKg),
                  dataInicio: form.dataInicio,
                  dataFim: form.dataFim,
                  descricao: form.descricao || null,
                })
                setOpen(false)
                await carregar()
              } catch (err) {
                setErro(err.message)
              }
            }}
          >
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
              <Label>Meta (kg)</Label>
              <Input
                type="number"
                required
                value={form.metaKg}
                onChange={(e) => setForm({ ...form, metaKg: e.target.value })}
              />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-2">
                <Label>Início</Label>
                <Input
                  type="date"
                  required
                  value={form.dataInicio}
                  onChange={(e) => setForm({ ...form, dataInicio: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>Fim</Label>
                <Input
                  type="date"
                  required
                  value={form.dataFim}
                  onChange={(e) => setForm({ ...form, dataFim: e.target.value })}
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Descrição</Label>
              <Input value={form.descricao} onChange={(e) => setForm({ ...form, descricao: e.target.value })} />
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
