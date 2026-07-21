export interface LiaOption {
  id: number;
  [key: string]: unknown;
}

export interface LiaDropdowns {
  fromStore: LiaOption[];
  toStore: LiaOption[];
  status: LiaOption[];
  indentType: LiaOption[];
}

export interface LiaRow {
  id: number;
  issueDate?: string;
  issueNo?: string;
  fromStoreName?: string;
  toStoreName?: string;
  priorityName?: string;
  remark?: string;
  status?: string;
  isChecked?: boolean;
}
