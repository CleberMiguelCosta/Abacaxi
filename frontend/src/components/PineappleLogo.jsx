export default function PineappleLogo({ className = 'h-12 w-12', title = 'ResíduosAbacaxi' }) {
  return (
    <svg
      viewBox="0 0 72 72"
      className={className}
      role="img"
      aria-label={title}
      xmlns="http://www.w3.org/2000/svg"
    >
      <title>{title}</title>
      {/* Crown — outer leaves */}
      <path
        d="M36 6c1.8 4.2 4.6 7.2 8.4 8.8-3.6 1.4-6.4 4-8.4 7.6-2-3.6-4.8-6.2-8.4-7.6C31.4 13.2 34.2 10.2 36 6z"
        fill="#2f9f6d"
      />
      <path
        d="M24 14c1.6 4 4.2 6.6 7.8 7.8-2.2 1.6-4 3.6-5 6-2.6-2.2-4.4-5.2-5.2-8.8.4-1.8.8-3.4 2.4-5z"
        fill="#1b6f4c"
      />
      <path
        d="M48 14c-1.6 4-4.2 6.6-7.8 7.8 2.2 1.6 4 3.6 5 6 2.6-2.2 4.4-5.2 5.2-8.8-.4-1.8-.8-3.4-2.4-5z"
        fill="#1b6f4c"
      />
      {/* Crown — inner leaf tips */}
      <path
        d="M30 8c1 3.2 2.6 5.2 5 6.2-1.2 1.2-2.2 2.6-2.8 4.2-1.8-1.4-3-3.4-3.6-5.8L30 8z"
        fill="#248a5c"
      />
      <path
        d="M42 8c-1 3.2-2.6 5.2-5 6.2 1.2 1.2 2.2 2.6 2.8 4.2 1.8-1.4 3-3.4 3.6-5.8L42 8z"
        fill="#248a5c"
      />
      {/* Body */}
      <ellipse cx="36" cy="44" rx="18" ry="20" fill="#d9a51a" />
      <ellipse cx="36" cy="44" rx="16.5" ry="18.5" fill="#f0c84a" />
      {/* Diamond pattern */}
      <g
        fill="none"
        stroke="#c48e10"
        strokeWidth="1.35"
        strokeLinejoin="round"
      >
        <path d="M36 26l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M36 39l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M36 52l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M26 32.5l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M26 45.5l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M46 32.5l5 6.5-5 6.5-5-6.5 5-6.5z" />
        <path d="M46 45.5l5 6.5-5 6.5-5-6.5 5-6.5z" />
      </g>
      {/* Soft highlight */}
      <ellipse cx="29" cy="37" rx="5" ry="7.5" fill="#ffe9a0" opacity="0.5" />
    </svg>
  )
}
