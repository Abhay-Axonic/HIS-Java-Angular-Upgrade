package com.param.adt.discharge.dao;

import com.param.adt.common.Response;
import com.param.adt.discharge.model.Discharge;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DischargeDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMarkedForDischargeDao extends IGenericDao<Discharge, Integer>{


	Response getDischagePatientListForNursing(DischargeDto dischargeDto)throws ApplicationException;

	Response updatePatientServiceOrder(AdmissionDto admissionDto)throws ApplicationException;

	Response getPatientReadyForBillingList(DischargeDto dischargeDto)throws ApplicationException;

	Response updatePatientReadyForBillingStatus(DischargeDto dischargeDto)throws ApplicationException;

	Response getPatientFinalDischargeList(DischargeDto dischargeDto)throws ApplicationException;

	Response vacateBed(DischargeDto dischargeDto)throws ApplicationException;

	Response saveHousekeepingLog(DischargeDto dischargeDto2)throws ApplicationException;

	Response getAdmissionDetailsByAdmissionId(DischargeDto dischargeDto)throws ApplicationException;

	Response getDischagePatientListByDoctorId(DischargeDto dischargeDto)throws ApplicationException;

	Response getPatientDischargedList(DischargeDto dischargeDto)throws ApplicationException;

	Response getPatientDischargedListForPrint(DischargeDto dischargeDto)throws ApplicationException;

	Response getPatientFinalDischargeListNewForListAndCount(DischargeDto dischargeDto)throws ApplicationException;
	
	Response getDischargePatientListForNursingByVisitTypeId(DischargeDto dischargeDto)throws ApplicationException;

	Response getDischargedPatientsList(DischargeDto dischargeDto)throws ApplicationException;

}
