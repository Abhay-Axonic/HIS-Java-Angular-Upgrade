package com.param.adt.discharge.service;

import javax.transaction.Transactional;

import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.global.common.Response;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryTabularTemplateDataService {

	public Response saveDischargeSummTabularTemplateData(DischargeSummaryTabularTemplateDataDto dischargeSummaryTabularTemplateDataDto);

}
