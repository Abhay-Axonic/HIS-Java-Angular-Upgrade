package com.param.bloodBank.dao;

import java.util.List;

import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IBloodComponentOrderMasterDao {
	
	public Response saveBloodComponentOrderMaster(List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList) throws ApplicationException;
	
	
	public Response getBloodComponentOrderMasterList(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;
	
	public Response getBloodComponentOrderMasterById(Integer bloodComponentOrderId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodComponentOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getListOfProcedureOrderByPatientId(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response searchOrderedBloodComponentByDate(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;

	public Response updateBloodComponentOrderMaster(List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList) throws ApplicationException;

	public Response closePatientRequisition(PatientRequisitionDetailsDto dto) throws ApplicationException;


	public Response updateIsExportedStatus(BloodComponentOrderMasterDto dto) throws ApplicationException;

}
