package com.param.global.dao;

import java.util.List;

import com.param.billing.global.transaction.model.ServiceTarrifBedtypeManager;
import com.param.global.common.Response;
import com.param.global.dto.TarrifBedCategoryMpprDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface ITarrifBedCategoryDao extends IGenericDao<ServiceTarrifBedtypeManager, Integer>{

	Response saveServiceTarrifMaster(TarrifBedCategoryMpprDto tarrifBedCategoryMpprDto) throws ApplicationException;
	
	Response saveListServiceTarrifMaster(List<TarrifBedCategoryMpprDto> listTarrifBedCategoryMpprDto) throws ApplicationException;
	
	Response getBedCategoryFactorByTarrifId(Integer serviceTarrifId) throws ApplicationException;
}
