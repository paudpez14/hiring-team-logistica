import { create } from "zustand";

interface SearchTermProducts {
  productCode: string;
  productName: string;
  categoryName: string;
  addSearchTerms: (newSearchTerms: NewSearchTerms) => void;
}

export interface NewSearchTerms {
  productCode?: string;
  productName?: string;
  categoryName?: string;
}

export const useSearchTermsProducts = create<SearchTermProducts>((set) => ({
  productCode: "",
  productName: "",
  categoryName: "",
  addSearchTerms: (newSearchTerms: NewSearchTerms) => {
    set((state) => ({
      ...state,
      ...newSearchTerms,
    }));
  },
}));
