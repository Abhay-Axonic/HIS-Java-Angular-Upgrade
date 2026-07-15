package com.param.procedure.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.procedure.dao.IProcedureReqDao;

@Service
@SuppressWarnings("rawtypes")
public class ProcedureReqServiceImpl implements IProcedureReqService, ICommonConstants {

	@Autowired
	private IProcedureReqDao iProcedureReqDao;
	
	@Override
	@Transactional
	public Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto) throws ServiceException {
		try{
			return iProcedureReqDao.saveProcedureReq(procedureReqMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}

}
