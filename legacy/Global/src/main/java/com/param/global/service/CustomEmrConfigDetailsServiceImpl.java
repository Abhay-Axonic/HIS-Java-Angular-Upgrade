package com.param.global.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICustomEmrConfigDetailsDao;
import com.param.global.dto.CustomEMRConfigurationDetailsDto;

import scala.Array;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class CustomEmrConfigDetailsServiceImpl implements ICustomEmrConfigDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(CustomEmrConfigDetailsServiceImpl.class);

	@Autowired
	ICustomEmrConfigDetailsDao iCustomEmrConfigDetailsDao;
	
	
	@Override
	public Response getCustomEMRCategorySubcategoryList(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			List<CustomEMRConfigurationDetailsDto> listDto=new ArrayList<>();
			List<CustomEMRConfigurationDetailsDto> listDtoFinal=new ArrayList<>();
			Response res1=iCustomEmrConfigDetailsDao.getCustomEMRCategorySubcategoryList(customEMRConfigurationDetailsDto);
			
			if(res1.getListObject() != null && res1.getListObject().size() > 0){
				listDto=res1.getListObject();
				for (CustomEMRConfigurationDetailsDto dto : listDto) {
					
					CustomEMRConfigurationDetailsDto newData=new CustomEMRConfigurationDetailsDto();
					newData.setOrganizationId(customEMRConfigurationDetailsDto.getOrganizationId());
					newData.setUnitId(customEMRConfigurationDetailsDto.getUnitId());
					newData.setDoctorId(customEMRConfigurationDetailsDto.getDoctorId());
					newData.setPatientId(customEMRConfigurationDetailsDto.getPatientId());
					newData.setCusEmrCategoryId(dto.getCusEmrCategoryId());
					newData.setCusEmrSubCategoryId(dto.getCusEmrSubCategoryId());
					Response res=iCustomEmrConfigDetailsDao.checkAvailableCustomData(newData);
					
					if(res.getListObject().size() == 0){
						CustomEMRConfigurationDetailsDto finalData=new CustomEMRConfigurationDetailsDto();
						finalData.setCusEmrSubCategoryDesc(dto.getCusEmrSubCategoryDesc());
						finalData.setCusEmrCategoryDesc(dto.getCusEmrCategoryDesc());
						finalData.setCusEmrCategoryId(dto.getCusEmrCategoryId());
						finalData.setCusEmrSubCategoryId(dto.getCusEmrSubCategoryId());
						listDtoFinal.add(finalData);
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listDtoFinal, null);

			}else{
				return iCustomEmrConfigDetailsDao.getCustomEMRCategorySubcategoryList(customEMRConfigurationDetailsDto);
			}
			
	
	} catch (Exception e) {
	logger.error("getCustomEMRCategorySubcategoryList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateCustomEMRCategorySubcategory(
			CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			
			return iCustomEmrConfigDetailsDao.updateCustomEMRCategorySubcategory(customEMRConfigurationDetailsDto);
	
	} catch (Exception e) {
	logger.error("updateCustomEMRCategorySubcategory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAllListoCustomEMRByPatientId(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			
			return iCustomEmrConfigDetailsDao.getAllListoCustomEMRByPatientId(customEMRConfigurationDetailsDto);
	
	} catch (Exception e) {
	logger.error("getAllListoCustomEMRByPatientId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
