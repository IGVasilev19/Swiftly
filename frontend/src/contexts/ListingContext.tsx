/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, type ReactNode } from "react";

interface ListingContextType {
  selectedListingId: number | null;
  setSelectedListingId: (id: number | null) => void;
}

const ListingContext = createContext<ListingContextType | null>(null);
interface ListingProviderProps {
  children: ReactNode;
}

export const ListingProvider = ({ children }: ListingProviderProps) => {
  const [selectedListingId, setSelectedListingId] = useState<number | null>(
    null
  );

  return (
    <ListingContext.Provider
      value={{ selectedListingId, setSelectedListingId }}
    >
      {children}
    </ListingContext.Provider>
  );
};

export const useListingContext = () => {
  const context = useContext(ListingContext);
  if (!context) {
    throw new Error("useListingContext must be used within a ListingProvider");
  }
  return context;
};
