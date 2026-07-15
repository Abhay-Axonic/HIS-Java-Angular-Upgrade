package com.param.billing.ipd.service;

import org.springframework.web.multipart.MultipartFile;

import com.param.billing.global.transaction.dto.MPreauthRequestMasterDto;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.billing.ipd.dto.PreAuthorizationRequestDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.opd.encounter.dto.PatientDocReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPreAuthorizationRequestService {
	public Response getPreAuthorizationRequestList(PreAuthorizationRequestDto preAuthorizationRequestDto)throws BillingException;
	public Response saveNewPreAuthorizationRequest(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response updateAuthorizationRequest(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response getPreAuthorizationRequestListByPayeeId(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response cancelNewPreAuthorizationRequestByPreAuthorizationId(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response saveInsuranceAuthorizationDoc(int organizationId,MultipartFile inputFile,MPreauthRequestMaster reqDto)throws ApplicationException;
	public Response  updateIsSplitStatusByPreAuthorizationId(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response getPreAuthorizationRequestListByPayeeIdIPDCharges(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response getPatientDocumentNameForPreAuthorization(MPreauthRequestMasterDto patientDocReqDto)throws ApplicationException;
	Response getPreAuthorizationRequestListCount(PreAuthorizationRequestDto preAuthorizationRequestDto)
			throws BillingException;
}
