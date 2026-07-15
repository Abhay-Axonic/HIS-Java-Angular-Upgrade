package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.GlobalServiceMaster;
import com.param.global.model.UnitServiceMasterDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface  IGlobalServiceMasterServiceDao extends IGenericDao<GlobalServiceMaster, Integer>{

	Response glablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

	Response glablServiceMasterSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

	Response glablServiceMasterSubSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

	Response searchGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;

	Response getAllGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)throws ApplicationException;
}
