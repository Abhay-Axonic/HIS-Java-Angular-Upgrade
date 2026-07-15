package com.param.adt.discharge.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.DischargeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMarkedForDischargeService {

	@Transactional
	Response getDischagePatientListForNursing(DischargeDto dischargeDto)throws ApplicationException;

	@Transactional
	Response updatePatientServiceOrder(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getPatientReadyForBillingList(DischargeDto dischargeDto)throws ApplicationException;
	
	@Transactional
	Response updatePatientReadyForBillingStatus(DischargeDto dischargeDto)throws ApplicationException;

	@Transactional
	Response getPatientFinalDischargeList(DischargeDto dischargeDto)throws ApplicationException;

	@Transactional
	Response vacateBed(DischargeDto dischargeDto, HttpServletRequest request)throws ApplicationException;
	
	@Transactional
	Response getDischagePatientListByDoctorId(DischargeDto dischargeDto)throws ApplicationException;
	
	@Transactional
	Response getPatientDischargedList(DischargeDto dischargeDto)throws ApplicationException;

	@Transactional
	Response getPatientDischargedListForPrint(DischargeDto dischargeDto)throws ApplicationException;

	void postRoomRentChargesForDischargeInitiate(AdmissionsForBatchProcessDto admission);

	@Transactional
	Response getPatientFinalDischargeListNewForListAndCount(DischargeDto dischargeDto)throws ApplicationException;
	
	@Transactional
	Response getDischargePatientListForNursingByVisitTypeId(DischargeDto dischargeDto)throws ApplicationException;

	@Transactional
	Response getDischargedPatientsList(DischargeDto dischargeDto)throws ApplicationException;

}
