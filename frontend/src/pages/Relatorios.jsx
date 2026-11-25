import { useEffect, useState } from 'react'
import { endpoints } from '@/api/client'
import PageHeader from '@/components/PageHeader'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'

const money = (v) => Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

export default function Relatorios() {
  const [inicio, setInicio] = useState('')
  const [fim, setFim] = useState('')
  const [dados, setDados] = useState(null)
  const [erro, setErro] = useState('')

  const carregar = async () => {
    setErro('')
    try {
      setDados(await endpoints.relatorio(inicio || undefined, fim || undefined))
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  const custos = Number(dados?.totalCustos || 0)
  const receitas = Number(dados?.totalReceitas || 0)
  const max = Math.max(custos, receitas, 1)

  return (
    <div>
      <PageHeader title="Relatórios financeiros" description="Custos, receitas e lucro líquido." />

      {erro && (
        <div className="mb-4 rounded-md border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
          {erro}
        </div>
      )}

      <Card className="mb-4">
        <CardContent className="flex flex-wrap items-end gap-4 pt-6">
          <div className="space-y-2">
            <Label>Início</Label>
            <Input type="date" value={inicio} onChange={(e) => setInicio(e.target.value)} />
          </div>
          <div className="space-y-2">
            <Label>Fim</Label>
            <Input type="date" value={fim} onChange={(e) => setFim(e.target.value)} />
          </div>
          <Button onClick={carregar}>Atualizar</Button>
        </CardContent>
      </Card>

      {dados && (
        <>
          <div className="mb-4 grid gap-4 md:grid-cols-3">
            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-xs uppercase tracking-wide text-muted-foreground">Custos</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="font-[family-name:var(--font-display)] text-2xl font-semibold text-red-700">
                  {money(custos)}
                </p>
                <div className="mt-3 h-2 overflow-hidden rounded bg-muted">
                  <div className="h-full rounded bg-red-500 transition-all" style={{ width: `${(custos / max) * 100}%` }} />
                </div>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-xs uppercase tracking-wide text-muted-foreground">Receitas</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="font-[family-name:var(--font-display)] text-2xl font-semibold text-emerald-700">
                  {money(receitas)}
                </p>
                <div className="mt-3 h-2 overflow-hidden rounded bg-muted">
                  <div
                    className="h-full rounded bg-emerald-500 transition-all"
                    style={{ width: `${(receitas / max) * 100}%` }}
                  />
                </div>
              </CardContent>
            </Card>
            <Card className="bg-secondary/40">
              <CardHeader className="pb-2">
                <CardTitle className="text-xs uppercase tracking-wide text-muted-foreground">
                  Lucro líquido
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="font-[family-name:var(--font-display)] text-2xl font-semibold">
                  {money(dados.lucroLiquido)}
                </p>
              </CardContent>
            </Card>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Movimentações</CardTitle>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Data</TableHead>
                    <TableHead>Tipo</TableHead>
                    <TableHead>Descrição</TableHead>
                    <TableHead>Polo</TableHead>
                    <TableHead>Valor</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {(dados.movimentacoes || []).map((m) => (
                    <TableRow key={m.id}>
                      <TableCell>{m.dataMovimentacao}</TableCell>
                      <TableCell>{m.tipo}</TableCell>
                      <TableCell>{m.descricao}</TableCell>
                      <TableCell>{m.poloNome || '—'}</TableCell>
                      <TableCell>{money(m.valor)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  )
}
