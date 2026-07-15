package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IVaccineImmunizationDAO {
	 
	public Response getListOfVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException;
	
	
	public Response saveVaccineImmunizationMapper(List<VaccineImmunizationMapperDto> vaccineImmunizationMapperDto)
			throws ApplicationException;


	public Response updateVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto) throws ApplicationException;


	public Response getVaccineDueDatePatientsForSendingMailAndMsgs(int i, List<Integer> unitIds)throws ApplicationException;
	public Response updateVaccineImmunizationMapperForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)throws ApplicationException;


	public Response removeFreeTextVaccineForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)throws ApplicationException;


	public Response batchJobToSendMessageForCycliceAppointmentDueDate(List<Integer> unitIds)throws ApplicationException;


	public Response batchJobToSendMessageForCycliceAppointmentServiceDueDate(List<Integer> unitIds)throws ApplicationException;


	public Response dailyBasisPendingAmountIpPatientSMS(List<Integer> unitIds)throws ApplicationException;


	public Response dailyBasisDepositAmountIpPatientSMS(List<Integer> unitIds)throws ApplicationException;

}
	
