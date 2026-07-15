package com.param.global.procedure.dao;

import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfiguration;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationDao extends IGenericDao<ProcedureConfiguration, Integer>{
	public Integer saveProcedureConfiguration(ProcedureConfigurationDto dto)throws ApplicationException;
	public Response getProcedureConfigList(int orgId,int unitId)throws ApplicationException;
	public boolean validProcConfByServiceOrgUnit(int serviceId, int orgId, int unitId)throws ApplicationException;
	public Response updateProcedureConfigStatus(int id,char status)throws ApplicationException;
	public Integer updateProcedureConfiguration(ProcedureConfigurationDto dto)throws ApplicationException;
	public boolean validProcConfByServiceOrgUnitConfigId(int serviceId, int orgId, int unitId, int configId)throws ApplicationException;
	public String getProcedureNameById(Integer integer);
	public String getSpecialityNameById(Integer specialityId);
	
	public Response getFuturedOrderProcedureByEncounter(int patientId,int doctorId) throws ApplicationException;
	public Response convertProcedure(int admissionId,int visitTypeId,int proReqId) throws ApplicationException;
	public Response getorderDetailsIdByProcedureReqId(Integer proReqId)throws ApplicationException;
	public Response getFuturedOrderProcedureByAdmission(Integer admissionId)throws ApplicationException;
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proConfigId)throws ApplicationException;
	
}