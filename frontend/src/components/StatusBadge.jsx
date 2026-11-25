import { Badge } from '@/components/ui/badge'

const MAP = {
  AGENDADA: { variant: 'info', label: 'Agendada' },
  EM_ANDAMENTO: { variant: 'warning', label: 'Em andamento' },
  CONCLUIDA: { variant: 'success', label: 'Concluída' },
  CANCELADA: { variant: 'destructive', label: 'Cancelada' },
}

export default function StatusBadge({ status }) {
  const cfg = MAP[status] || { variant: 'outline', label: status }
  return <Badge variant={cfg.variant}>{cfg.label}</Badge>
}
