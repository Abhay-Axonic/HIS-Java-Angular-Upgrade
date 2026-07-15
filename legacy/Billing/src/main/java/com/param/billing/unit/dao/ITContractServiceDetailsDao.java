package com.param.billing.unit.dao;

import java.util.List;

import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.Response;
import com.param.service.global.model.TContractServiceDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITContractServiceDetailsDao extends IGenericDao<TContractServiceDetails,Integer> {
	public  Response saveContractServiceDetails(List<TContractServiceDetailsDto> tContractServiceDetailsDto)throws ApplicationException;

}
