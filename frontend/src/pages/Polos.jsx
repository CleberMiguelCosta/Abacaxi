import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Badge } from '@/components/ui/badge'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'

const empty = {
  nome: '',
  endereco: '',
  cidade: '',
  estado: '',
  telefone: '',
  responsavel: '',
  ativo: true,
}

export default function Polos() {
  const [lista, setLista] = useState([])
  const [erro, setErro] = useState('')
  const [open, setOpen] = useState(false)
  const [editId, setEditId] = useState(null)
  const [form, setForm] = useState(empty)

  const carregar = () => endpoints.polos().then(setLista).catch((e) => setErro(e.message))

  useEffect(() => {
    carregar()
  }, [])

  const salvar = async (e) => {
    e.preventDefault()
    try {
      if (editId) await endpoints.atualizarPolo(editId, form)
      else await endpoints.criarPolo(form)
      setOpen(false)
      await carregar()
    } catch (err) {
      setErro(err.message)
    }
  }

  return (
    <div>
      <PageHeader
        title="Polos"
        description="Polos participantes e dados cadastrais."
        actions={
          <Button
            onClick={() => {
              setEditId(null)
              setForm(empty)
              setOpen(true)
            }}
          >
            Novo polo
          </Button>
        }
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <Card>
        <CardContent className="pt-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nome</TableHead>
                <TableHead>Cidade</TableHead>
                <TableHead>Responsável</TableHead>
                <TableHead>Telefone</TableHead>
                <TableHead>Status</TableHead>
                <TableHead />
              </TableRow>
            </TableHeader>
            <TableBody>
              {lista.map((p) => (
                <TableRow key={p.id}>
                  <TableCell>
                    <div className="font-medium">{p.nome}</div>
                    <div className="text-xs text-muted-foreground">{p.endereco}</div>
                  </TableCell>
                  <TableCell>
                    {p.cidade}/{p.estado}
                  </TableCell>
                  <TableCell>{p.responsavel}</TableCell>
                  <TableCell>{p.telefone}</TableCell>
                  <TableCell>
                    <Badge variant={p.ativo ? 'success' : 'secondary'}>
                      {p.ativo ? 'Ativo' : 'Inativo'}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        setEditId(p.id)
                        setForm({ ...p })
                        setOpen(true)
                      }}
                    >
                      Editar
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editId ? 'Editar polo' : 'Novo polo'}</DialogTitle>
          </DialogHeader>
          <form className="space-y-3" onSubmit={salvar}>
            {['nome', 'endereco', 'cidade', 'estado', 'telefone', 'responsavel'].map((k) => (
              <div className="space-y-2" key={k}>
                <Label className="capitalize">{k}</Label>
                <Input
                  required={k === 'nome' || k === 'endereco'}
                  maxLength={k === 'estado' ? 2 : undefined}
                  value={form[k] || ''}
                  onChange={(e) => setForm({ ...form, [k]: e.target.value })}
                />
              </div>
            ))}
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
