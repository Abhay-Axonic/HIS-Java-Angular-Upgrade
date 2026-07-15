package com.param.billing.unit.dao;

import com.param.billing.common.Response;
import com.param.billing.global.model.DepositMatrixConfigMaster;
import com.param.billing.unit.dto.DepositMatrixConfigMasterDto;
import com.param.billing.unit.dto.DepositMatrixReqDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDepositMatrixConfigDao extends IGenericDao<DepositMatrixConfigMaster, Integer> {
	
	public Response getDepositMatrixList(int orgId ,int unitId,int offset, int recordPerPage) throws ApplicationException;

	public Response saveDepositMatrixConfig(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) throws ApplicationException;

	public Response getDepositMatrixConfig(int mDepositMatrixId) throws ApplicationException;

	public Response updateDepositMatrixConfig(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) throws ApplicationException;

	public Response updateDepositMatrixConfigStatus(int mDepositMatrixId,char status) throws ApplicationException;

	public Response getDepositMatrixConfigCount(int orgId ,int unitId) throws ApplicationException;
	
	public Response checkDepositMatrixExist(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) throws ApplicationException;

	public Response getDepositMatrixConfigList(DepositMatrixReqDto depositMatrixReqDto) throws ApplicationException;
}
