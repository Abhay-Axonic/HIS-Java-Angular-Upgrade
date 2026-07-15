package com.param.global.otims.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.MedicationDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IMedicationDetailsService {

	public Response saveMedicationDetails(List<MedicationDetailsDto> medicationDetailsDto) throws ApplicationException;

	public Response getMedicationDetailsByProcedureId(MedicationDetailsDto medicationDetailsDto) throws ApplicationException;

	public Response getMedicationDetails(MedicationDetailsDto medicationDetailsDto) throws ApplicationException;

	public Response updateMedicationDetailsIsExported(List<MedicationDetailsDto> list) throws ApplicationException;

	public Response getMedicationDetailsByProcedureIdOnOTIMS(MedicationDetailsDto iMedicationDetailsDto) throws ApplicationException;

}
