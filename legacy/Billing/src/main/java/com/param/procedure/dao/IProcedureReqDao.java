package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.otims.model.ProcedureReqMaster;

import in.param.common.dao.IGenericDao;


@SuppressWarnings("rawtypes")
public interface IProcedureReqDao extends IGenericDao<ProcedureReqMaster, Integer>{

	public Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto)throws ServiceException;
	
	public Response updateProcedureReqStatus(int id,int statusId)throws ServiceException;
	
	public Response updateProcedureOrderFlag(Integer procedureOrderId)throws ServiceException;

	public Response updateProcedureRequestSeconderyToPrimary(Integer proReqId, Integer serviceId);

	public Response updateProcedureScheduleStatus(Integer procedureRequestId, int statusId)throws ServiceException;

	public Response cancelProcedureReq(Integer proReqId)throws ServiceException;
}
