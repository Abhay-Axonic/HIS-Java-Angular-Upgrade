export interface RaaOption {
  id: number;
  [key: string]: unknown;
}

export interface RaaDropdowns {
  fromStore: RaaOption[];
  toStore: RaaOption[];
  status: RaaOption[];
}

export interface RaaRow {
  id: number;
  returnNo?: string;
  returnDate?: string;
  fromStore?: string;
  toStore?: string;
  returnBy?: string;
  remark?: string;
  status?: string;
  [key: string]: unknown;
}
