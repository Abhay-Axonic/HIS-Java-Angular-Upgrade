package com.param.adt.discharge.service;

import javax.transaction.Transactional;

import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.global.common.Response;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryFieldService {
	
	public Response saveDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response updateDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response updateDischargeSummaryFieldStatus(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getDischargeSummaryFieldById(Integer dischargeFieldId);
	
	public Response getDischargeSummaryFieldMasterList(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getDischargeSummaryFieldCount(DischargeSummaryFieldDto dischargeSummaryFieldDto);
	
	public Response getActiveDischargeSummaryField(Integer organisationId);
	public Response getActiveDischargeSummaryFieldMapper(Integer organisationId);

	public Response getDischargeSumColumnDetails(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response updateDischSumColumnsToPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response getDischSumColumnsDetailsForPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	public Response getDischSumColumnsList(DischargeSummaryFieldDto dischargeSummaryFieldDto);

//	public Response updateDischSumColumnsRequired(DischargeSummaryFieldDto dischargeSummaryFieldDto);

	
}
