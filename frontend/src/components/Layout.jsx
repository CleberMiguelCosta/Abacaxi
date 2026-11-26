import { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import {
  LayoutDashboard,
  Truck,
  CalendarDays,
  MapPin,
  Recycle,
  Package,
  ShoppingCart,
  BarChart3,
  Target,
  BookOpen,
  LogOut,
  Menu,
  X,
} from 'lucide-react'
import { useAuth } from '@/context/AuthContext'
import PineappleLogo from '@/components/PineappleLogo'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Separator } from '@/components/ui/separator'
import { cn } from '@/lib/utils'

const LINKS = [
  { to: '/', label: 'Painel', icon: LayoutDashboard, end: true },
  { to: '/coletas', label: 'Coletas', icon: Truck },
  { to: '/calendario', label: 'Calendário', icon: CalendarDays },
  { to: '/polos', label: 'Polos', icon: MapPin },
  { to: '/residuos', label: 'Resíduos', icon: Recycle },
  { to: '/estoque', label: 'Estoque', icon: Package },
  { to: '/vendas', label: 'Vendas', icon: ShoppingCart },
  { to: '/relatorios', label: 'Relatórios', icon: BarChart3 },
  { to: '/metas', label: 'Metas', icon: Target },
  { to: '/reaproveitamento', label: 'Reuso', icon: BookOpen },
]

function NavItems({ onNavigate }) {
  return (
    <nav className="flex flex-col gap-1 px-3 py-2">
      {LINKS.map(({ to, label, icon: Icon, end }) => (
        <NavLink
          key={to}
          to={to}
          end={end}
          onClick={onNavigate}
          className={({ isActive }) =>
            cn(
              'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
              isActive
                ? 'bg-sidebar-accent text-sidebar-primary'
                : 'text-sidebar-foreground/75 hover:bg-sidebar-accent/70 hover:text-sidebar-foreground',
            )
          }
        >
          <Icon className="h-4 w-4 shrink-0" />
          {label}
        </NavLink>
      ))}
    </nav>
  )
}

export default function Layout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)

  const sair = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="flex min-h-screen bg-background">
      {/* Desktop sidebar */}
      <aside className="sticky top-0 hidden h-screen w-64 shrink-0 flex-col bg-sidebar text-sidebar-foreground md:flex">
        <div className="flex flex-col items-center gap-3 px-4 py-7 text-center">
          <PineappleLogo className="h-24 w-24 shrink-0" />
          <div className="min-w-0">
            <p className="font-[family-name:var(--font-display)] text-2xl font-bold leading-none tracking-tight">
              ResíduosAbacaxi
            </p>
            <p className="mt-1.5 text-xs uppercase tracking-[0.14em] text-sidebar-foreground/60">
              Gestão de resíduos
            </p>
          </div>
        </div>
        <Separator className="bg-sidebar-border" />
        <ScrollArea className="flex-1">
          <NavItems />
        </ScrollArea>
        <div className="border-t border-sidebar-border p-4">
          <p className="truncate text-sm font-medium">{user?.nome}</p>
          <p className="truncate text-xs capitalize text-sidebar-foreground/60">
            {user?.perfil?.replaceAll('_', ' ').toLowerCase()}
          </p>
          <Button
            variant="ghost"
            size="sm"
            className="mt-3 w-full justify-start text-sidebar-foreground/80 hover:bg-sidebar-accent hover:text-sidebar-foreground"
            onClick={sair}
          >
            <LogOut className="h-4 w-4" />
            Sair
          </Button>
        </div>
      </aside>

      {/* Mobile drawer */}
      {open && (
        <div className="fixed inset-0 z-40 md:hidden">
          <button
            type="button"
            className="absolute inset-0 bg-black/40"
            aria-label="Fechar menu"
            onClick={() => setOpen(false)}
          />
          <aside className="relative z-50 flex h-full w-72 flex-col bg-sidebar text-sidebar-foreground shadow-xl">
            <div className="flex items-center justify-between px-4 py-5">
              <div className="flex items-center gap-3">
                <PineappleLogo className="h-16 w-16 shrink-0" />
                <div>
                  <p className="font-[family-name:var(--font-display)] text-xl font-bold leading-tight">
                    ResíduosAbacaxi
                  </p>
                  <p className="text-[11px] uppercase tracking-wider text-sidebar-foreground/60">
                    Gestão de resíduos
                  </p>
                </div>
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="text-sidebar-foreground"
                onClick={() => setOpen(false)}
              >
                <X className="h-5 w-5" />
              </Button>
            </div>
            <Separator className="bg-sidebar-border" />
            <ScrollArea className="flex-1">
              <NavItems onNavigate={() => setOpen(false)} />
            </ScrollArea>
          </aside>
        </div>
      )}

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="sticky top-0 z-30 flex items-center gap-3 border-b bg-card/90 px-4 py-3 backdrop-blur md:px-6">
          <Button
            variant="outline"
            size="icon"
            className="md:hidden"
            onClick={() => setOpen(true)}
            aria-label="Abrir menu"
          >
            <Menu className="h-5 w-5" />
          </Button>
          <div className="flex items-center gap-3 md:hidden">
            <PineappleLogo className="h-12 w-12 shrink-0" />
            <span className="font-[family-name:var(--font-display)] text-lg font-bold">
              ResíduosAbacaxi
            </span>
          </div>
          <div className="ml-auto hidden text-sm text-muted-foreground sm:block">
            Coleta · classificação · reaproveitamento
          </div>
        </header>

        <main className="flex-1 px-4 py-6 md:px-8">
          <div className="mx-auto w-full max-w-6xl">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  )
}
