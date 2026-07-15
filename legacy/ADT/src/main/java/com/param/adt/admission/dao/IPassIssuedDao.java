package com.param.adt.admission.dao;

import com.param.adt.admission.model.VisitorAgainstPatient;
import com.param.adt.admission.model.VisitorPatientMapper;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.global.VisitorDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPassIssuedDao extends IGenericDao<VisitorAgainstPatient, Integer>{

	
	Response getVisitorTypeList(AdmissionDto admissionDto) throws ApplicationException;

	Response saveVisitorAgainsPatient(VisitorDto visitorDto) throws ApplicationException;

	Response saveVisitorPatientMapper(VisitorDto visitorDto) throws ApplicationException;

	Response getVisitorsList(VisitorDto visitorDto) throws ApplicationException;

	Response updateVisitorsDetails(VisitorPatientMapper visitorPatientMapper, int visitorPatientId,
			Integer visitorPatientMapperId) throws ApplicationException;

	Response getBedListByWardId(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getAdmittedPatientListByMultipleCriteria(AdmissionDto admissionDto)throws ApplicationException;

	Response getPassNumberAlreadyIssueOrNot(VisitorDto visitorDto)throws ApplicationException;

	Response checkNoOfPassIssueAgainstAdmission(VisitorDto visitorDto)throws ApplicationException;

	Response getVisitorPassList(AdmissionDto admissionDto)throws ApplicationException;
	
	

}
