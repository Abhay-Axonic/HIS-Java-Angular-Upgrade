export interface AraOption {
  id: number;
  [key: string]: unknown;
}

export interface AraDropdowns {
  fromStore: AraOption[];
  toStore: AraOption[];
  status: AraOption[];
  indentType: AraOption[];
}

export interface AraRow {
  id: number;
  returnNo?: string;
  isChecked?: boolean;
  [key: string]: unknown;
}
