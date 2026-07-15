package com.param.adt.discharge.dao;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDischargeSummaryTabularTemplateDataDao {

	public Response saveDischargeSummTabularTemplateData(DischargeSummaryTabularTemplateDataDto dischargeSummaryTabularTemplateDataDto)throws ApplicationException;

	public Response inactiveDischargeSummaryTabularData(DischargeSummaryDto dischargeSummaryDto)throws ApplicationException;

}
