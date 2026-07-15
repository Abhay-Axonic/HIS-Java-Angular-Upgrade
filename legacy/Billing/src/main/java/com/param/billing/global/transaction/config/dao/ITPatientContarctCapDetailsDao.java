package com.param.billing.global.transaction.config.dao;

import com.param.billing.common.Response;
import com.param.billing.global.model.TPatientContractCapDetails;
import com.param.billing.global.transaction.config.dto.TPatientContractCapDetailsDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITPatientContarctCapDetailsDao extends IGenericDao<TPatientContractCapDetails, Integer>{
	
	public Response savePatientContarctCapDetails(TPatientContractCapDetailsDto tPatientContractCapDetailsDto) throws ApplicationException;
	//Integer deptId, Integer subDeptId,Integer payementEntitlementType,
	public Response getExistingContarctByPatientEncounter(
			 Integer encounterId, Integer visitTypeId, Integer admissionId, Integer serviceId ,Integer organisationId, Integer unitId)
			throws ApplicationException;
	public Response getContacrtConsumptionServiceDetailsByContarctIdServiceId(Integer contarctId, Integer serviceId)throws ApplicationException;
	
}
