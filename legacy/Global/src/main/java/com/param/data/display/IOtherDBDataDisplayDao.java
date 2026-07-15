package com.param.data.display;

import com.param.data.dto.OtherAppDataReqDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings({"rawtypes"})
public interface IOtherDBDataDisplayDao {
	public Response encounterList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response quickSummery(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getTriageList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getHistory(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getPhysicalExamination(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getPrecriptionList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getLabOrderedsList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getDiagnosysList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getRadiologyOrdersList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getProceduresList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getreviewNotesList(OtherAppDataReqDto reqDto)throws BillingException;
	public Response getProvisionalDiagonsys(OtherAppDataReqDto reqDto)throws BillingException;
}
