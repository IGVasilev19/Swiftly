import React, { useEffect, useRef, useMemo, useState } from "react";
import { COUNTRIES as LIB_COUNTRIES } from "@/lib/countries";
import { DIAL_MAP } from "@/lib/dialMap";

type Country = {
  code: string;
  label: string;
  dial: string;
  dialDigits: string;
  displayDial?: string;
};

const COUNTRIES: Country[] = (LIB_COUNTRIES ?? []).map((c: any) => {
  const code = (c.value ?? c.code ?? c.iso2 ?? c.alpha2 ?? "")
    .toString()
    .toUpperCase();
  const label = String(c.title ?? c.label ?? c.name ?? c.country ?? "");

  function getDialDigits(obj: any, iso: string) {
    if (obj.dialDigits) return String(obj.dialDigits).replace(/\D/g, "");
    if (obj.dial) return String(obj.dial).replace(/\D/g, "");
    if (obj.callingCode) return String(obj.callingCode).replace(/\D/g, "");
    if (
      obj.callingCodes &&
      Array.isArray(obj.callingCodes) &&
      obj.callingCodes[0]
    )
      return String(obj.callingCodes[0]).replace(/\D/g, "");
    return DIAL_MAP[iso] ?? "";
  }

  const dialDigits = getDialDigits(c, code);
  const dial = dialDigits ? `+${dialDigits}` : "";
  const displayDial = c.displayDial ?? dial;

  return {
    code: code || "",
    label: label || "",
    dial,
    dialDigits,
    displayDial,
  };
});

if (!COUNTRIES || COUNTRIES.length === 0) {
  COUNTRIES.push(
    {
      code: "NL",
      label: "Netherlands",
      dial: "+31",
      dialDigits: "31",
      displayDial: "+31",
    },
    {
      code: "US",
      label: "United States",
      dial: "+1",
      dialDigits: "1",
      displayDial: "+1",
    }
  );
}

type Props = {
  value?: string;
  onChange?: (value: string) => void;
  id?: string;
  defaultCountry?: string;
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
  const initial = COUNTRIES.find(
    (c) => c.code === (defaultCountry ?? "").toString().toUpperCase()
  ) ??
    COUNTRIES[0] ?? {
      code: "NL",
      label: "Netherlands",
      dial: "+31",
      dialDigits: "31",
      displayDial: "+31",
    };

  const [selected, setSelected] = useState<Country>(initial);
  const [local, setLocal] = useState("");
  const [query, setQuery] = useState("");
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const v = String(value ?? "").trim();
    if (!v) {
      setLocal("");
      return;
    }
    const norm = v.replace(/\s+/g, "");
    let found = COUNTRIES.find((c) => c.dial && norm.startsWith(c.dial));
    if (!found) {
      found = COUNTRIES.find(
        (c) => c.dialDigits && norm.startsWith(c.dialDigits)
      );
    }
    if (found) {
      setSelected(found);
      const prefix = norm.startsWith(found.dial)
        ? found.dial
        : norm.startsWith(found.dialDigits)
        ? found.dialDigits
        : "";
      const rest = norm.slice(prefix.length).replace(/\D/g, "");
      setLocal(rest);
      return;
    }
    setLocal(v.replace(/\D/g, ""));
  }, [value]);

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
    const combined =
      (c.dial || `+${c.dialDigits || ""}`) + local.replace(/\D/g, "");
    onChange?.(combined);
    setOpen(false);
    setQuery("");
  };

  const onLocalChange = (nextRaw: string) => {
    const hasSearchChars = /[A-Za-z+]/.test(nextRaw.trim());
    if (hasSearchChars) {
      setQuery(nextRaw);
      setOpen(true);
    } else {
      setQuery("");
    }

    const cleaned = nextRaw.replace(/\D/g, "");
    setLocal(cleaned);
    onChange?.((selected.dial || `+${selected.dialDigits || ""}`) + cleaned);
  };

  const filtered = useMemo(() => COUNTRIES, []);

  return (
    <div ref={ref} className={`w-full ${className}`}>
      <div className="flex items-stretch">
        <button
          type="button"
          aria-haspopup="listbox"
          aria-expanded={open}
          onClick={() => {
            setOpen((v) => !v);
            setQuery("");
          }}
          className="shrink-0 inline-flex items-center px-3 border border-[#0f172a1a] rounded-l-md bg-white text-sm text-[#0F172A] focus:outline-none"
        >
          <span className="mr-2 text-sm">{selected.code}</span>
          <span className="font-medium">
            {selected.displayDial ?? selected.dial}
          </span>
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
          type="tel"
          inputMode="numeric"
          pattern="[0-9]*"
          value={local}
          onChange={(e) => onLocalChange(e.target.value)}
          onKeyDown={(e) => {
            const allowed = [
              "Backspace",
              "Delete",
              "ArrowLeft",
              "ArrowRight",
              "Home",
              "End",
              "Tab",
            ];
            if (allowed.includes(e.key)) return;
            if (!/^[0-9]$/.test(e.key)) e.preventDefault();
          }}
          onPaste={(e) => {
            const paste = (
              e.clipboardData || (window as any).clipboardData
            ).getData("text");
            const cleaned = paste.replace(/\D/g, "");
            if (!cleaned) {
              e.preventDefault();
              return;
            }
            e.preventDefault();
            const newVal = (local + cleaned).replace(/\D/g, "");
            setLocal(newVal);
            onChange?.(
              (selected.dial || `+${selected.dialDigits || ""}`) + newVal
            );
          }}
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
          {filtered.map((c) => (
            <li key={c.code}>
              <button
                type="button"
                onClick={() => setCountry(c)}
                className="w-full text-left px-3 py-2 hover:bg-gray-50 text-sm text-[#0F172A] flex items-center justify-between"
              >
                <span className="inline-flex items-center gap-2">
                  <span className="font-medium">{c.label}</span>
                  <span className="text-zinc-500">
                    {c.displayDial ?? c.dial}
                  </span>
                </span>
                <span className="text-xs text-zinc-400">{c.code}</span>
              </button>
            </li>
          ))}

          {filtered.length === 0 && (
            <li className="px-3 py-2 text-sm text-zinc-500">No countries</li>
          )}
        </ul>
      )}
    </div>
  );
}
