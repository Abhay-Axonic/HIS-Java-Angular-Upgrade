export interface RackOption {
  id?: number;
  storeId?: number;
  store?: string;
  storeName?: string;
}

export interface ShelfRow {
  shelfId?: number;
  shelfCode: string;
  shelfName: string;
}

export interface RackRow {
  id: number;
  storeId?: number;
  storeName?: string;
  rackCode: string;
  rackName: string;
  status: boolean;
  items?: ShelfRow[];
}
