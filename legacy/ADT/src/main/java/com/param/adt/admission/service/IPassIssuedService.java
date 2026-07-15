package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.global.VisitorDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPassIssuedService {

	@Transactional
	Response getVisitorTypeList(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response saveVisitorAgainsPatient(VisitorDto visitorDto)throws ApplicationException;

	@Transactional
	Response saveVisitorPatientMapper(VisitorDto visitorDto)throws ApplicationException;

	@Transactional
	Response getVisitorsList(VisitorDto visitorDto)throws ApplicationException;

	@Transactional
	Response updateVisitorsDetails(VisitorDto visitorDto)throws ApplicationException;

	
	@Transactional
	Response getBedListByWardId(BedMasterDto bedMasterDto)throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListByMultipleCriteria(AdmissionDto admissionDto);
	
	@Transactional
	Response getPassNumberAlreadyIssueOrNot(VisitorDto visitorDto)throws ApplicationException;
	
	@Transactional
	Response checkNoOfPassIssueAgainstAdmission(VisitorDto visitorDto)throws ApplicationException;
	
	@Transactional
	Response generatePassNumber(Integer orgId, Integer unitId)throws ApplicationException;

	@Transactional
	Response getVisitorPassList(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response saveOrupdateVisitorAgainsPatient(VisitorDto visitorDto)throws ApplicationException;
	
	
	

}
