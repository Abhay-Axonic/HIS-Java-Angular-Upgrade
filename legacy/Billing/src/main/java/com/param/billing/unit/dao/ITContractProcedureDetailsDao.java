package com.param.billing.unit.dao;

import com.param.global.common.Response;
import com.param.service.dto.TContractProcedureDetailsDto;
import com.param.service.global.model.ContractProcedureDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITContractProcedureDetailsDao extends IGenericDao<ContractProcedureDetails,Integer> {
	public  Response saveContractProcedureDetails(TContractProcedureDetailsDto tContractGroupDetailsDto)throws ApplicationException;

}
