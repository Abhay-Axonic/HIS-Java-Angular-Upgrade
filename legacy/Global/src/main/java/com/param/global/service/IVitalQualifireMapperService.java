package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.VitalQualifireMapperDto;
import com.param.global.dto.VitalQualifireMapperDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IVitalQualifireMapperService {

	Response saveVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response updateVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response updateVitalQualifireMapperStatus(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

//	Response getVitalQualifireMapperById(Integer vitalQualifireId, Integer organizationId)throws ApplicationException;

	Response getVitalQualifireMapperList(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response getVitalQualifireMapperCount(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response getActiveVitalQualifireMapper(Integer organizationId)throws ApplicationException;
	

	Response getVitalQualifireMapperByNameNotId(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException;

	Response getVitalQualifireListByCategoryId(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response getVitalQualifireListByVitalId(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

	Response getVitalQualifireMapperListById(VitalQualifireMapperDto vitalQualifireMapperDto)throws ApplicationException;

}
