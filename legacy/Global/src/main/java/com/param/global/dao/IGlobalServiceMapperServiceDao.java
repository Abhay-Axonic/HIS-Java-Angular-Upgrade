package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.GlobalServiceMapper;
import com.param.global.model.GlobalServiceMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface  IGlobalServiceMapperServiceDao extends IGenericDao<GlobalServiceMapper, Integer>{


	Response saveGlobalMasterData(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

	Response callProcedure(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

}
