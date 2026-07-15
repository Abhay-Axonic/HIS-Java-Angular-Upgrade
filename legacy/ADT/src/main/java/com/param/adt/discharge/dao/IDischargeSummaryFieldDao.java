package com.param.adt.discharge.dao;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.global.common.Response;
@SuppressWarnings({"rawtypes"})

public interface IDischargeSummaryFieldDao{
	
	public Response saveDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response updateDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response updateDischargeSummaryFieldStatus(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getDischargeSummaryFieldById(Integer dischargeFieldId);
	
	public Response getDischargeSummaryFieldMasterList(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getDischargeSummaryFieldCount(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getActiveDischargeSummaryField(Integer organisationId);

	public Response getActiveDischargeSummaryFieldMapper(Integer organisationId);
	
	public Response getDischargeSummaryFieldByName(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getDischargeSummaryFieldByNameNotId(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response getDischargeSumColumnDetails(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response updateDischSumColumnsToPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response getDischSumColumnsDetailsForPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response getDischSumColumnsList(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response updateDischSumColumnsRequired(DischargeSummaryDto dischargeSummaryDto);
}
