package com.param.billing.ipd.dao;

import com.param.billing.global.transaction.dto.MPreauthRequestMasterDto;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.billing.ipd.dto.PreAuthorizationRequestDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.opd.encounter.dto.PatientDocReqDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPreAuthorizationRequest extends IGenericDao<MPreauthRequestMaster, Integer> {
	public Response getPreAuthorizationRequestList(PreAuthorizationRequestDto preAuthorizationRequestDto)throws BillingException;
	public Response saveNewPreAuthorizationRequest(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response updateAuthorizationRequest(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response getPreAuthorizationRequestListByPayeeId(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response cancelNewPreAuthorizationRequestByPreAuthorizationId(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response  updateIsSplitStatusByPreAuthorizationId(MPreauthRequestMasterDto reqDto)throws BillingException;
	Response updateConsumedAmount(Integer admissionId, Integer encounterId) throws BillingException;
	public Response getPreAuthorizationRequestListByPayeeIdIPDCharges(MPreauthRequestMasterDto reqDto)throws BillingException;
	public Response getPatientDocumentNameForPreAuthorization(MPreauthRequestMasterDto patientDocReqDto)throws ApplicationException;
	public Response getPreAuthorizationRequestListCount(PreAuthorizationRequestDto preAuthorizationRequestDto)throws BillingException;
}