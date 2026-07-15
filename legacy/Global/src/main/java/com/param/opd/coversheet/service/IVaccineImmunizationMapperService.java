package com.param.opd.coversheet.service;
import java.util.List;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import in.param.exception.ApplicationException;


@SuppressWarnings("rawtypes")
public interface IVaccineImmunizationMapperService {
	
	public Response getListOfVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException;
	
	
   public Response saveVaccineImmunizationMapper(List<VaccineImmunizationMapperDto> vaccineImmunizationMapperDto)
			throws ApplicationException;



public Response updateVaccineImmunizationMapper(VaccineImmunizationMapperDto dto)throws ApplicationException;


public Response batchJobToSendMailAndMessageForVaccinationDueDate(List<Integer> unitIds)throws ApplicationException, Exception;

public Response updateVaccineImmunizationMapperForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto) throws ApplicationException;


public Response removeFreeTextVaccineForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto) throws ApplicationException;


public Response batchJobToSendMessageForCycliceAppointmentDueDate(List<Integer> unitIds)throws ApplicationException;


public Response dailyBasisPendingAmountIpPatientSMS(List<Integer> unitIds)throws ApplicationException;

}
