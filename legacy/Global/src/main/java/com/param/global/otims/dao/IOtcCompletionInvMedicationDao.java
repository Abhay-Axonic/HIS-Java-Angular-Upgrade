package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.otims.model.OtcCompletionInvMedication;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOtcCompletionInvMedicationDao extends IGenericDao<OtcCompletionInvMedication, Integer>{

	public Response saveInvMedications(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto) throws ApplicationException;

	public Response updateIssueStatus(OtcCompletionInvMedicationDto meddto) throws ApplicationException;

	public Response getOtMedications(int proReqId)throws ApplicationException;

}
