export interface AiaOption {
  id: number;
  [key: string]: unknown;
}

export interface AiaDropdowns {
  fromStore: AiaOption[];
  toStore: AiaOption[];
  indentType: AiaOption[];
  status: AiaOption[];
  priority: AiaOption[];
}

export interface AiaRow {
  id: number;
  issueDate?: string;
  issueNo?: string;
  toStore?: string;
  fromStore?: string;
  type?: string;
  priority?: string;
  remark?: string;
  status?: string;
}
