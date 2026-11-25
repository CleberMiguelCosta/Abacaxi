import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'
import { Badge } from '@/components/ui/badge'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'

export default function Residuos() {
  const [registros, setRegistros] = useState([])
  const [alertas, setAlertas] = useState([])
  const [rendimento, setRendimento] = useState(null)
  const [destinos, setDestinos] = useState([])
  const [polos, setPolos] = useState([])
  const [tipos, setTipos] = useState([])
  const [coletas, setColetas] = useState([])
  const [erro, setErro] = useState('')
  const [modalReg, setModalReg] = useState(false)
  const [modalDest, setModalDest] = useState(false)
  const [formReg, setFormReg] = useState({
    coletaId: '',
    tipoResiduoId: '',
    poloId: '',
    pesoKg: '',
    quantidadeAbacaxiProcessadaKg: '',
  })
  const [formDest, setFormDest] = useState({
    registroResiduoId: '',
    tipoDestino: 'COMPOSTAGEM',
    quantidadeKg: '',
    dataDestinacao: new Date().toISOString().slice(0, 10),
    destinoDetalhado: '',
  })

  const carregar = async () => {
    const [r, a, ren, d, p, t, c] = await Promise.all([
      endpoints.registros(),
      endpoints.alertasQualidade(),
      endpoints.rendimento(),
      endpoints.destinos(),
      endpoints.polos(),
      endpoints.tiposResiduo(),
      endpoints.coletas(),
    ])
    setRegistros(r)
    setAlertas(a)
    setRendimento(ren)
    setDestinos(d)
    setPolos(p)
    setTipos(t)
    setColetas(c)
  }

  useEffect(() => {
    carregar().catch((e) => setErro(e.message))
  }, [])

  return (
    <div>
      <PageHeader
        title="Resíduos"
        description="Classificar, pesar, destino e rendimento."
        actions={
          <>
            <Button variant="outline" onClick={() => setModalDest(true)}>
              Registrar destino
            </Button>
            <Button onClick={() => setModalReg(true)}>Registrar peso</Button>
          </>
        }
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      {rendimento && (
        <div className="mb-4 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {[
            ['Processado', `${rendimento.totalAbacaxiProcessadoKg} kg`],
            ['% gerados', `${rendimento.percentualResiduosGerados}%`],
            ['% reaproveitados', `${rendimento.percentualResiduosReaproveitados}%`],
            ['Alertas qualidade', String(alertas.length)],
          ].map(([label, value]) => (
            <Card key={label}>
              <CardHeader className="pb-2">
                <CardTitle className="text-xs uppercase tracking-wide text-muted-foreground">{label}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="font-[family-name:var(--font-display)] text-2xl font-semibold">{value}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {alertas.length > 0 && (
        <Card className="mb-4 border-red-200">
          <CardHeader>
            <CardTitle>Alertas de peso</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            {alertas.map((a) => (
              <div key={a.id} className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm">
                <strong>{a.tipoResiduoNome}</strong> — {a.mensagemAlerta}
              </div>
            ))}
          </CardContent>
        </Card>
      )}

      <Card className="mb-4">
        <CardHeader>
          <CardTitle>Registros</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Data</TableHead>
                <TableHead>Polo</TableHead>
                <TableHead>Tipo</TableHead>
                <TableHead>Peso</TableHead>
                <TableHead>Processado</TableHead>
                <TableHead>Qualidade</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {registros.map((r) => (
                <TableRow key={r.id}>
                  <TableCell>{r.dataRegistro?.replace('T', ' ').slice(0, 16)}</TableCell>
                  <TableCell>{r.poloNome}</TableCell>
                  <TableCell>{r.tipoResiduoNome}</TableCell>
                  <TableCell>{r.pesoKg} kg</TableCell>
                  <TableCell>{r.quantidadeAbacaxiProcessadaKg} kg</TableCell>
                  <TableCell>
                    <Badge variant={r.alertaQualidade ? 'destructive' : 'success'}>
                      {r.alertaQualidade ? 'Fora da margem' : 'OK'}
                    </Badge>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Destinos</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Registro</TableHead>
                <TableHead>Tipo destino</TableHead>
                <TableHead>Qtd</TableHead>
                <TableHead>Data</TableHead>
                <TableHead>Detalhe</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {destinos.map((d) => (
                <TableRow key={d.id}>
                  <TableCell>#{d.registroResiduoId}</TableCell>
                  <TableCell>{d.tipoDestino.replaceAll('_', ' ')}</TableCell>
                  <TableCell>{d.quantidadeKg} kg</TableCell>
                  <TableCell>{d.dataDestinacao}</TableCell>
                  <TableCell>{d.destinoDetalhado || '—'}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          {destinos.length === 0 && (
            <p className="py-6 text-center text-sm text-muted-foreground">Nenhum destino registrado.</p>
          )}
        </CardContent>
      </Card>

      <Dialog open={modalReg} onOpenChange={setModalReg}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Registrar resíduo</DialogTitle>
          </DialogHeader>
          <form
            className="space-y-3"
            onSubmit={async (e) => {
              e.preventDefault()
              try {
                await endpoints.criarRegistro({
                  coletaId: formReg.coletaId ? Number(formReg.coletaId) : null,
                  tipoResiduoId: Number(formReg.tipoResiduoId),
                  poloId: Number(formReg.poloId),
                  pesoKg: Number(formReg.pesoKg),
                  quantidadeAbacaxiProcessadaKg: Number(formReg.quantidadeAbacaxiProcessadaKg),
                })
                setModalReg(false)
                await carregar()
              } catch (err) {
                setErro(err.message)
              }
            }}
          >
            <div className="space-y-2">
              <Label>Coleta (opcional)</Label>
              <Select value={formReg.coletaId} onChange={(e) => setFormReg({ ...formReg, coletaId: e.target.value })}>
                <option value="">—</option>
                {coletas.map((c) => (
                  <option key={c.id} value={c.id}>
                    #{c.id} {c.poloNome} — {c.dataColeta}
                  </option>
                ))}
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Polo</Label>
              <Select required value={formReg.poloId} onChange={(e) => setFormReg({ ...formReg, poloId: e.target.value })}>
                <option value="">Selecione</option>
                {polos.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.nome}
                  </option>
                ))}
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Tipo</Label>
              <Select
                required
                value={formReg.tipoResiduoId}
                onChange={(e) => setFormReg({ ...formReg, tipoResiduoId: e.target.value })}
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
                <Label>Peso (kg)</Label>
                <Input
                  type="number"
                  step="0.1"
                  required
                  value={formReg.pesoKg}
                  onChange={(e) => setFormReg({ ...formReg, pesoKg: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>Abacaxi processado (kg)</Label>
                <Input
                  type="number"
                  step="0.1"
                  required
                  value={formReg.quantidadeAbacaxiProcessadaKg}
                  onChange={(e) =>
                    setFormReg({ ...formReg, quantidadeAbacaxiProcessadaKg: e.target.value })
                  }
                />
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setModalReg(false)}>
                Fechar
              </Button>
              <Button type="submit">Salvar</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      <Dialog open={modalDest} onOpenChange={setModalDest}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Registrar destino</DialogTitle>
          </DialogHeader>
          <form
            className="space-y-3"
            onSubmit={async (e) => {
              e.preventDefault()
              try {
                await endpoints.criarDestino({
                  registroResiduoId: Number(formDest.registroResiduoId),
                  tipoDestino: formDest.tipoDestino,
                  quantidadeKg: Number(formDest.quantidadeKg),
                  dataDestinacao: formDest.dataDestinacao,
                  destinoDetalhado: formDest.destinoDetalhado || null,
                })
                setModalDest(false)
                await carregar()
              } catch (err) {
                setErro(err.message)
              }
            }}
          >
            <div className="space-y-2">
              <Label>Registro</Label>
              <Select
                required
                value={formDest.registroResiduoId}
                onChange={(e) => setFormDest({ ...formDest, registroResiduoId: e.target.value })}
              >
                <option value="">Selecione</option>
                {registros.map((r) => (
                  <option key={r.id} value={r.id}>
                    #{r.id} {r.tipoResiduoNome} — {r.pesoKg} kg
                  </option>
                ))}
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Tipo de destino</Label>
              <Select
                value={formDest.tipoDestino}
                onChange={(e) => setFormDest({ ...formDest, tipoDestino: e.target.value })}
              >
                {['COMPOSTAGEM', 'FABRICACAO_RACAO', 'APROVEITAMENTO_FIBRAS', 'BIOGAS', 'OUTROS'].map((t) => (
                  <option key={t} value={t}>
                    {t.replaceAll('_', ' ')}
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
                  value={formDest.quantidadeKg}
                  onChange={(e) => setFormDest({ ...formDest, quantidadeKg: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>Data</Label>
                <Input
                  type="date"
                  required
                  value={formDest.dataDestinacao}
                  onChange={(e) => setFormDest({ ...formDest, dataDestinacao: e.target.value })}
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Detalhe</Label>
              <Input
                value={formDest.destinoDetalhado}
                onChange={(e) => setFormDest({ ...formDest, destinoDetalhado: e.target.value })}
              />
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setModalDest(false)}>
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
