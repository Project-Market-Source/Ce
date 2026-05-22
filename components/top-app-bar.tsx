"use client";

import Link from "next/link";
import { Search, User } from "lucide-react";

export function TopAppBar() {
  return (
    <header className="w-full px-5 py-3 flex items-center justify-between">
      {/* Logo + App Name */}
      <Link href="/" className="flex items-center gap-2.5">
        <div className="w-8 h-8 rounded-lg bg-[var(--primary)] flex items-center justify-center">
          <span className="text-white text-xs font-black italic">CM</span>
        </div>
        <span className="text-white text-xl font-bold tracking-tight">
          Cinemana
        </span>
      </Link>

      {/* Actions */}
      <div className="flex items-center gap-3">
        <Link
          href="/search"
          className="w-[38px] h-[38px] rounded-full bg-white/5 flex items-center justify-center hover:bg-white/10 transition-colors"
        >
          <Search className="w-[18px] h-[18px] text-white" />
        </Link>
        <div className="w-[38px] h-[38px] rounded-full bg-[#475569] border border-white/15 flex items-center justify-center">
          <User className="w-5 h-5 text-white/60" />
        </div>
      </div>
    </header>
  );
}
