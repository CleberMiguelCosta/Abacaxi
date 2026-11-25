export default function PineappleLogo({ className = 'h-9 w-9', title = 'ResíduosAbacaxi' }) {
  return (
    <svg
      viewBox="0 0 64 64"
      className={className}
      role="img"
      aria-label={title}
      xmlns="http://www.w3.org/2000/svg"
    >
      <title>{title}</title>
      {/* Crown / leaves */}
      <path
        d="M32 4c2.2 5.5 6.2 8.8 11.5 10.2-5.4 1.5-9.4 5.4-11.5 10.8-2.1-5.4-6.1-9.3-11.5-10.8C26 12.8 29.8 9.5 32 4z"
        fill="#2d9b6a"
      />
      <path
        d="M24 10c1.4 3.6 3.8 5.8 7.2 6.8-1.8 1.2-3.2 2.8-4 4.8-2.2-1.8-3.6-4.2-4.2-7.2.3-1.5.6-2.9 1-4.4z"
        fill="#1a6b4a"
      />
      <path
        d="M40 10c-1.4 3.6-3.8 5.8-7.2 6.8 1.8 1.2 3.2 2.8 4 4.8 2.2-1.8 3.6-4.2 4.2-7.2-.3-1.5-.6-2.9-1-4.4z"
        fill="#1a6b4a"
      />
      {/* Body */}
      <ellipse cx="32" cy="40" rx="16" ry="18" fill="#e8b923" />
      <ellipse cx="32" cy="40" rx="14.5" ry="16.5" fill="#f0c84a" />
      {/* Diamond pattern */}
      <path
        d="M32 24l4 5-4 5-4-5 4-5zm0 10l4 5-4 5-4-5 4-5zm0 10l4 5-4 5-4-5 4-5zM24 29l4 5-4 5-4-5 4-5zm0 10l4 5-4 5-4-5 4-5zM40 29l4 5-4 5-4-5 4-5zm0 10l4 5-4 5-4-5 4-5z"
        fill="none"
        stroke="#c99212"
        strokeWidth="1.2"
        strokeLinejoin="round"
      />
      {/* Soft highlight */}
      <ellipse cx="26" cy="34" rx="4" ry="6" fill="#ffe9a0" opacity="0.45" />
    </svg>
  )
}
