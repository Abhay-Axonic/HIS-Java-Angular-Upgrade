/** Row shape returned by getTabularTemplateMasterList / getTabularTemplateMasterById
 * (TabularTemplateMasterDto.java, via TabularTemplateMasterController). */
export interface TabularTemplateRow {
  tabularTemplateId: number;
  tabularTemplateCode: string;
  tabularTemplateDesc: string;
  status: 'A' | 'I';
}

/** Form/edit-state shape -- mirrors $scope.common (ctrl:43-50, 227-228). */
export interface TabularTemplateFormState {
  tabularTemplateId: number | null;
  tabularTemplateCode: string;
  tabularTemplateDesc: string;
}

export function emptyTabularTemplateForm(): TabularTemplateFormState {
  return {
    tabularTemplateId: null,
    tabularTemplateCode: '',
    tabularTemplateDesc: '',
  };
}
