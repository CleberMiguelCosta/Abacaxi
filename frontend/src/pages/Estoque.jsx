import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'

export default function Estoque() {
  const [lista, setLista] = useState([])
  const [erro, setErro] = useState('')

  useEffect(() => {
    endpoints.estoques().then(setLista).catch((e) => setErro(e.message))
  }, [])

  return (
    <div>
      <PageHeader title="Estoque" description="Níveis e alertas visuais por cor." />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {lista.map((e) => (
          <Card
            key={e.id}
            className={cn(
              e.corAlerta === 'VERMELHO' && 'border-red-300 bg-red-50/60',
              e.corAlerta === 'AMARELO' && 'border-amber-300 bg-amber-50/60',
              e.corAlerta === 'VERDE' && 'border-emerald-200 bg-emerald-50/40',
              e.nivel !== 'NORMAL' && 'animate-pulse',
            )}
          >
            <CardHeader className="pb-2">
              <div className="flex items-center justify-between">
                <CardTitle className="text-base">{e.tipoResiduoNome}</CardTitle>
                <Badge variant={e.nivel === 'CRITICO' ? 'destructive' : e.nivel === 'BAIXO' ? 'warning' : 'success'}>
                  {e.nivel}
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-muted-foreground">{e.poloNome}</p>
              <p className="mt-2 font-[family-name:var(--font-display)] text-3xl font-semibold">
                {e.quantidadeKg} kg
              </p>
              <p className="mt-1 text-xs text-muted-foreground">Alerta {e.corAlerta}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      {lista.length === 0 && !erro && (
        <p className="py-10 text-center text-sm text-muted-foreground">Estoque vazio.</p>
      )}
    </div>
  )
}
