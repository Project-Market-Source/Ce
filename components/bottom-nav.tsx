"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Home, Search, Grid3X3, Bookmark } from "lucide-react";

const navItems = [
  { href: "/", icon: Home, label: "Home" },
  { href: "/search", icon: Search, label: "Search" },
  { href: "/categories", icon: Grid3X3, label: "Categories" },
  { href: "/watchlist", icon: Bookmark, label: "Watchlist" },
];

export function BottomNav() {
  const pathname = usePathname();
  
  return (
    <nav className="fixed bottom-0 left-0 right-0 h-20 bg-[var(--background)]/95 backdrop-blur-lg border-t border-white/5 z-50">
      <div className="flex items-center justify-around h-full max-w-md mx-auto px-4">
        {navItems.map(({ href, icon: Icon, label }) => {
          const isActive = pathname === href;
          return (
            <Link
              key={href}
              href={href}
              className={`flex flex-col items-center gap-1 px-4 py-2 rounded-xl transition-colors ${
                isActive
                  ? "text-[var(--primary)]"
                  : "text-[var(--muted-foreground)] hover:text-white"
              }`}
            >
              <Icon
                className={`w-6 h-6 ${isActive ? "fill-[var(--primary)]/20" : ""}`}
              />
              <span className="text-[10px] font-medium">{label}</span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
