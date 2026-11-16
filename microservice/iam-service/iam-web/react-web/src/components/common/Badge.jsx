import React from 'react';

export const Badge = ({ children, variant = "default", size = "md", className = "" }) => {
  const variantStyles = {
    default: "bg-slate-800 text-slate-300 border-slate-700",
    primary: "bg-indigo-500/15 text-indigo-400 border-indigo-500/30",
    success: "bg-emerald-500/15 text-emerald-400 border-emerald-500/30",
    warning: "bg-amber-500/15 text-amber-400 border-amber-500/30",
    danger: "bg-rose-500/15 text-rose-400 border-rose-500/30",
    info: "bg-sky-500/15 text-sky-400 border-sky-500/30",
    purple: "bg-purple-500/15 text-purple-400 border-purple-500/30",
  };

  const sizeStyles = {
    sm: "px-2 py-0.5 text-xs font-medium",
    md: "px-2.5 py-1 text-xs font-semibold",
    lg: "px-3 py-1.5 text-sm font-semibold",
  };

  return (
    <span className={`inline-flex items-center gap-1.5 rounded-full border ${variantStyles[variant] || variantStyles.default} ${sizeStyles[size]} ${className}`}>
      {children}
    </span>
  );
};
