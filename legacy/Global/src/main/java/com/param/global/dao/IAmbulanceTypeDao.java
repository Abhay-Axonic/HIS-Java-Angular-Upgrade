package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAmbulanceTypeDao extends IGenericDao<AmbulanceType, Integer>{

	
	Response getAmbulanceByName(AmbulanceTypeDto ambulanceTypeDto)throws ApplicationException;

	Response addAmbulanceType(AmbulanceTypeDto ambulanceTypeDto)throws ApplicationException;

	Response getAmbulanceTypeList(AmbulanceTypeDto ambulanceTypeDto)throws ApplicationException;
	
	Response getAmbulanceTypeActiveList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	Response getSingleAmbulanceType(AmbulanceTypeDto ambulanceTypeDto)throws ApplicationException;

	Response updateAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	Response getAmbulanceByID(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	Response changeAmbulanceStatus(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	Response getAmbulanceTarrifByName(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response saveAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response getAmbulanceTarrifList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;


	

}
