import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'

const money = (v) => Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

export default function Vendas() {
  const [lista, setLista] = useState([])
  const [polos, setPolos] = useState([])
  const [tipos, setTipos] = useState([])
  const [erro, setErro] = useState('')
  const [ok, setOk] = useState('')
  const [open, setOpen] = useState(false)
  const [form, setForm] = useState({
    tipoResiduoId: '',
    poloId: '',
    quantidadeKg: '',
    valorUnitario: '',
    dataVenda: new Date().toISOString().slice(0, 10),
    comprador: '',
  })

  const carregar = () =>
    Promise.all([endpoints.vendas(), endpoints.polos(), endpoints.tiposResiduo()])
      .then(([v, p, t]) => {
        setLista(v)
        setPolos(p)
        setTipos(t)
      })
      .catch((e) => setErro(e.message))

  useEffect(() => {
    carregar()
  }, [])

  return (
    <div>
      <PageHeader
        title="Vendas"
        description="Área restrita — requer autenticação."
        actions={<Button onClick={() => setOpen(true)}>Nova venda</Button>}
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

      <Card>
        <CardContent className="pt-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Data</TableHead>
                <TableHead>Polo</TableHead>
                <TableHead>Resíduo</TableHead>
                <TableHead>Qtd</TableHead>
                <TableHead>Unitário</TableHead>
                <TableHead>Total</TableHead>
                <TableHead>Comprador</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {lista.map((v) => (
                <TableRow key={v.id}>
                  <TableCell>{v.dataVenda}</TableCell>
                  <TableCell>{v.poloNome}</TableCell>
                  <TableCell>{v.tipoResiduoNome}</TableCell>
                  <TableCell>{v.quantidadeKg} kg</TableCell>
                  <TableCell>{money(v.valorUnitario)}</TableCell>
                  <TableCell>{money(v.valorTotal)}</TableCell>
                  <TableCell>{v.comprador || '—'}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          {lista.length === 0 && (
            <p className="py-8 text-center text-sm text-muted-foreground">Nenhuma venda registrada.</p>
          )}
        </CardContent>
      </Card>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Nova venda</DialogTitle>
          </DialogHeader>
          <form
            className="space-y-3"
            onSubmit={async (e) => {
              e.preventDefault()
              setErro('')
              setOk('')
              try {
                await endpoints.criarVenda({
                  tipoResiduoId: Number(form.tipoResiduoId),
                  poloId: Number(form.poloId),
                  quantidadeKg: Number(form.quantidadeKg),
                  valorUnitario: Number(form.valorUnitario),
                  dataVenda: form.dataVenda,
                  comprador: form.comprador || null,
                })
                setOpen(false)
                setOk('Venda registrada e estoque atualizado.')
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
              <Label>Resíduo</Label>
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
                <Label>Quantidade (kg)</Label>
                <Input
                  type="number"
                  step="0.1"
                  required
                  value={form.quantidadeKg}
                  onChange={(e) => setForm({ ...form, quantidadeKg: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>Valor unitário (R$)</Label>
                <Input
                  type="number"
                  step="0.01"
                  required
                  value={form.valorUnitario}
                  onChange={(e) => setForm({ ...form, valorUnitario: e.target.value })}
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Data</Label>
              <Input
                type="date"
                required
                value={form.dataVenda}
                onChange={(e) => setForm({ ...form, dataVenda: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label>Comprador</Label>
              <Input value={form.comprador} onChange={(e) => setForm({ ...form, comprador: e.target.value })} />
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setOpen(false)}>
                Fechar
              </Button>
              <Button type="submit">Registrar</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  )
}
