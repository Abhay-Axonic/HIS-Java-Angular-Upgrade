export interface TaxComponentRecord {
  id: number;
  code: string;
  component: string;
  status: boolean;
}

export interface TaxComponentDetail {
  id: number;
  code: string;
  component: string;
  status: boolean;
}

export function emptyTaxComponentRecord(): TaxComponentRecord {
  return { id: 0, code: '', component: '', status: true };
}
