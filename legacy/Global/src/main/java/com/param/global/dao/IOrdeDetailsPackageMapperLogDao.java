package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.service.global.model.OrdeDetailsPackageMapperLog;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOrdeDetailsPackageMapperLogDao {

	Response savePackageConsumptionLog(OrdeDetailsPackageMapperLog master)throws ApplicationException;

	Response updatePackageConsumptionLog(OrderDetailsMasterDto detailsMasterDto)throws ApplicationException;

}
