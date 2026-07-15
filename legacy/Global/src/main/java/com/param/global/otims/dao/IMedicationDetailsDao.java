package com.param.global.otims.dao;

import java.util.List;


import com.param.global.common.Response;
import com.param.global.otims.dto.MedicationDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
public interface IMedicationDetailsDao {

	public Response saveMedicationDetails(List<MedicationDetailsDto> iMedicationDetailsDto,Integer procedureOrderId) throws ApplicationException;

	public Response getMedicationDetailsByProcedureId(MedicationDetailsDto medicationDetailsDto) throws ApplicationException;

	public Response getMedicationDetails(MedicationDetailsDto medicationDetailsDto) throws ApplicationException;

	public Response updateMedicationDetailsIsExported(List<MedicationDetailsDto> list) throws ApplicationException;

	public Response getMedicationDetailsByProcedureIdOnOTIMS(MedicationDetailsDto medicationDetailsDto) throws ApplicationException;

	public Response getMedicationListByProcedureId(MedicationDetailsDto medDto)throws ApplicationException;

}
