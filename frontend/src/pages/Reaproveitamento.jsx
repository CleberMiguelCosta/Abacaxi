import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'
import { Badge } from '@/components/ui/badge'

export default function Reaproveitamento() {
  const [lista, setLista] = useState([])
  const [filtro, setFiltro] = useState('')
  const [erro, setErro] = useState('')

  useEffect(() => {
    endpoints.reaproveitamento().then(setLista).catch((e) => setErro(e.message))
  }, [])

  const filtrada = filtro ? lista.filter((i) => i.categoriaResiduo === filtro) : lista

  return (
    <div>
      <PageHeader
        title="Reaproveitamento"
        description="Consulta técnica e orientações de destinação."
        actions={
          <div className="w-48 space-y-2">
            <Label htmlFor="cat">Categoria</Label>
            <Select id="cat" value={filtro} onChange={(e) => setFiltro(e.target.value)}>
              <option value="">Todas</option>
              {['CASCA', 'BAGACO', 'COROA', 'FOLHAS', 'OUTROS'].map((c) => (
                <option key={c} value={c}>
                  {c}
                </option>
              ))}
            </Select>
          </div>
        }
      />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {filtrada.map((i) => (
          <Card key={i.id}>
            <CardHeader>
              <Badge variant="secondary" className="mb-2 w-fit">
                {i.categoriaResiduo} · {i.tipoDestino.replaceAll('_', ' ')}
              </Badge>
              <CardTitle className="text-lg">{i.titulo}</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <p className="text-sm leading-relaxed text-muted-foreground">{i.conteudoTecnico}</p>
              {i.orientacoes && (
                <div className="rounded-lg border bg-secondary/40 p-3">
                  <p className="mb-1 text-xs font-semibold uppercase tracking-wide">Orientações</p>
                  <p className="text-sm">{i.orientacoes}</p>
                </div>
              )}
            </CardContent>
          </Card>
        ))}
      </div>

      {filtrada.length === 0 && (
        <p className="py-10 text-center text-sm text-muted-foreground">Nenhuma informação encontrada.</p>
      )}
    </div>
  )
}
