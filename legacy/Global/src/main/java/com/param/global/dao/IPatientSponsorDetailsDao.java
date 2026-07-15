package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.model.PatientSponsorDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientSponsorDetailsDao extends IGenericDao<PatientSponsorDetails, Integer>{

	public Response savePatientSponsorDetails(PatientSponsorDetailsDto patientSponsorDetailsDto)throws ApplicationException;

	public Response getSponsorDetailsByEncounterId(int encounterId) throws ApplicationException;
	
	public Response updatePatientSponsorDetails(PatientSponsorDetailsDto patientSponsorDetailsDto)throws ApplicationException;

	public Response saveCapDetailsByPatientAndContractId(List<PatientSponsorDetailsDto> listObject, Integer encounterId, Integer admissionId)throws ApplicationException;

	public Response getCapDetailsByContractIds(List<Integer> contractIds);
	
	public Response getSponsoredDetails (Integer admissionId, Integer paymentEntitlementId);
	
	public Response inactiveteSponsoredDetails(Integer patientId,List<Integer> sponsorIds);

	Response getSponsoredDetailsForUpdatePayee(Integer admissionId, Integer encounterId);

	public Response inactiveSponsorDetailsForUpdatePayee(Integer admissionId, Integer encounterId);

	public List<PatientSponsorDetails> findByAdmissionId(Integer admissionId);

	public List<PatientSponsorDetails> findByEncounterId(Integer encounterId);

	public Response savePatientSponsorDetailsForCostEstimation(PatientSponsorDetailsDto patientSponsorDetailsDto);

	public Response saveCapDetailsByPatientAndContractIdForCostEstimation(List<PatientSponsorDetailsDto> list);

	public Response inactivePatientSponsors(Integer patientId);

	public Response getLastPatientClass(Integer patientId, Integer visitTypeId, Integer unitId);

	public Response getLastSponsors(Integer visitId, Integer type);

	public String getDOAFromAdmissionId(Integer admissionId);

	public String getVisitDateFromEncounterId(Integer encounterId);
	
	public Response updatePatientSponserDetailsClaimNumber(PatientSponsorDetailsDto patientSponsorDetailsDto);
}
