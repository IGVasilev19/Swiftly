import React, { useEffect, useRef, useState } from "react";

type Country = { code: string; label: string; dial: string };

const COUNTRIES: Country[] = [
  { code: "NL", label: "Netherlands", dial: "+31" },
  { code: "BE", label: "Belgium", dial: "+32" },
  { code: "DE", label: "Germany", dial: "+49" },
  { code: "FR", label: "France", dial: "+33" },
  { code: "US", label: "United States", dial: "+1" },
  { code: "GB", label: "United Kingdom", dial: "+44" },
  { code: "AU", label: "Australia", dial: "+61" },
];

type Props = {
  value?: string;
  onChange?: (value: string) => void;
  id?: string;
  defaultCountry?: string; // ISO2, e.g. "NL"
  placeholder?: string;
  className?: string;
};

export default function PhoneNumberInput({
  value = "",
  onChange,
  id = "phone-input",
  defaultCountry = "NL",
  placeholder = "6 1234 5678",
  className = "",
}: Props) {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<Country>(
    COUNTRIES.find((c) => c.code === defaultCountry) ?? COUNTRIES[0]
  );
  const [local, setLocal] = useState("" /* user-editable local part */);
  const ref = useRef<HTMLDivElement | null>(null);

  // initialize local part from incoming value (strip dial)
  useEffect(() => {
    if (!value) {
      setLocal("");
      return;
    }
    const dial = selected.dial;
    if (value.startsWith(dial)) {
      setLocal(value.slice(dial.length));
    } else {
      // try to detect any dial from list
      const match = COUNTRIES.find((c) => value.startsWith(c.dial));
      if (match) {
        setSelected(match);
        setLocal(value.slice(match.dial.length));
      } else {
        // fallback: keep full value in local
        setLocal(value);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [value]);

  // close on outside click
  useEffect(() => {
    function onDoc(e: MouseEvent) {
      if (!ref.current) return;
      if (!ref.current.contains(e.target as Node)) setOpen(false);
    }
    document.addEventListener("click", onDoc);
    return () => document.removeEventListener("click", onDoc);
  }, []);

  const setCountry = (c: Country) => {
    setSelected(c);
    // propagate combined value
    const combined = c.dial + local.replace(/\D/g, "");
    onChange?.(combined);
    setOpen(false);
  };

  const onLocalChange = (next: string) => {
    const cleaned = next.replace(/\D/g, "");
    setLocal(cleaned);
    onChange?.(selected.dial + cleaned);
  };

  return (
    <div ref={ref} className={`w-full ${className}`}>
      <div className="flex items-stretch">
        <button
          type="button"
          aria-haspopup="listbox"
          aria-expanded={open}
          onClick={() => setOpen((v) => !v)}
          className="shrink-0 inline-flex items-center px-3 border border-[#0f172a1a] rounded-l-md bg-white text-sm text-[#0F172A] focus:outline-none"
        >
          <span className="mr-2 text-sm">{selected.code}</span>
          <span className="font-medium">{selected.dial}</span>
          <svg
            className="w-3 h-3 ml-2"
            viewBox="0 0 10 6"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            aria-hidden
          >
            <path
              d="M1 1L5 5L9 1"
              stroke="currentColor"
              strokeWidth={1.5}
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>

        <input
          id={id}
          type="text"
          inputMode="tel"
          value={local}
          onChange={(e) => onLocalChange(e.target.value)}
          placeholder={placeholder}
          className="flex-1 h-10 px-3 border-t border-b border-r border-[#0f172a1a] rounded-r-md focus:outline-none text-sm text-[#0F172A]"
          aria-label="Local phone number"
        />
      </div>

      {open && (
        <ul
          role="listbox"
          aria-activedescendant={selected.code}
          className="mt-1 max-h-40 overflow-auto rounded-md border border-[#e5e7eb] bg-white shadow-sm z-20"
        >
          {COUNTRIES.map((c) => (
            <li key={c.code}>
              <button
                type="button"
                onClick={() => setCountry(c)}
                className="w-full text-left px-3 py-2 hover:bg-gray-50 text-sm text-[#0F172A]"
              >
                <span className="inline-flex items-center gap-2">
                  <span className="font-medium">{c.label}</span>
                  <span className="text-zinc-500">{c.dial}</span>
                </span>
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
