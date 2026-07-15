package com.param.billing.global.transaction.config.dao;

import com.param.billing.common.Response;
import com.param.billing.global.model.TPatientContractCapDetails;
import com.param.billing.global.transaction.config.dto.UpdatePayeeInformationReqDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUpdatePayeeInformationDao extends IGenericDao<TPatientContractCapDetails, Integer>{
	
	public Response getPayeeInfoByVisitTypeId(UpdatePayeeInformationReqDto reqDto)throws ApplicationException;
	
}
