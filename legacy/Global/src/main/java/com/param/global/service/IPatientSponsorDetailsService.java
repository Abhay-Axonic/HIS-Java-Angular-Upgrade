package com.param.global.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.PatientSponsorForCostEstimationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientSponsorDetailsService {

	Response savePatientSponsorDetails(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList, Integer encounterId,Integer admissionId, Integer selfPayeeId);
	
	Response savePatientSponsorDetailsPharamcy(List<PatientSponsorDetailsDto> patientSponsorDetailslist, Integer encounterId,Integer admissionId,Integer selfPayeeId);

	@Transactional
	Response getSponsorDetailsByEncounterId(int encounterId)throws ApplicationException;
	
	@Transactional
	Response savePatientSponsorDetailsAndUpdatePatientSponsorUpdate (List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList, int encounterId);
	//BillingMasterDaoImpl
	
	@Transactional
	Response getSponsoredDetails (Integer admissionId, Integer paymentEntitlementId)throws ApplicationException;

	@Transactional
	Response getSponsoredDetailsForUpdatePayee(Integer admissionId, Integer encounterId)throws ApplicationException;

	Response inactiveSponsorDetailsForUpdatePayee(Integer admissionId, Integer encounterId)throws ApplicationException;

	@Transactional
	Response savePatientSponsorDetailsForCostEstimation(PatientSponsorForCostEstimationDto dto);

	@Transactional
	Response calculateAutoRenderForCostEstimation(PatientSponsorForCostEstimationDto dto);

	@Transactional
	Response getLastSponsorsByPatientId(Integer patientId, Integer visitTypeId, Integer unitId);
	
	public Response inactiveteSponsoredDetails(Integer patientId,List<Integer> sponsorIds);
	@Transactional
	Response updatePatientSponserDetailsClaimNumber(PatientSponsorDetailsDto patientSponsorDetailsDto);
	

}
