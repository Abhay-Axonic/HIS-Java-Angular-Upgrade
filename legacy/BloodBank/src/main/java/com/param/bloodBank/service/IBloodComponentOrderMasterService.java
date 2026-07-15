package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IBloodComponentOrderMasterService {
	public Response saveBloodComponentOrderMaster(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;
	
	public Response updateBloodComponentOrderMaster(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;
	
	public Response getBloodComponentOrderMasterList(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;
	
	public Response getBloodComponentOrderMasterById(Integer bloodComponentOrderId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodComponentOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getListOfProcedureOrderByPatientId(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response searchOrderedBloodComponentByDate(BloodComponentOrderMasterDto bloodComponentOrderMasterDto) throws ApplicationException;

	public Response closePatientRequisition(PatientRequisitionDetailsDto dto) throws ApplicationException;

	public Response updateIsExportedStatus(BloodComponentOrderMasterDto dto) throws ApplicationException;

    Response getDataByDateFromBloodComponentOrderMaster(BloodComponentOrderMasterDto dto) throws ApplicationException;

	Response getUniqueDateFromBloodComponentOrderMaster(BloodComponentOrderMasterDto dto) throws ApplicationException;
}
