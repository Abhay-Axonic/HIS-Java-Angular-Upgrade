package com.param.global.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IDataObjectMasterDao;
import com.param.global.dto.DataObjectMasterDto;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({"rawtypes"})
public class DataObjectMasterServiceImpl implements IDataObjectMasterService, ICommonConstants{

	@Autowired
	private IDataObjectMasterDao iDataObjectMasterDao;
	
	@Override
	@Transactional
	public Response saveDataObject(DataObjectMasterDto dto) throws ApplicationException {
		try{
			//check already exists 
			Response validRes = iDataObjectMasterDao.checkAlreadyExists(dto.getOrgId(), dto.getUnitId(), dto.getTitle());
			if(validRes.getCode().equals(SUCCESS_CODE) && validRes.getListObject() != null && validRes.getListObject().size() > 0){
				return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}
			dto.setStatus(ACTIVE);
			dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			return iDataObjectMasterDao.saveDataObject(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getDataObjectList(int orgId, int unitId) throws ApplicationException {
		try{
			return iDataObjectMasterDao.getDataObjectList(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getDataObjectById(int id) throws ApplicationException {
		try{
			return iDataObjectMasterDao.getDataObjectById(id);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAllDataObject(int orgId, int unitId) throws ApplicationException {
		try{
			return iDataObjectMasterDao.getAllDataObject(orgId,unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateDataObjectStatus(DataObjectMasterDto dataObjectMasterDto) throws ApplicationException {
		try{
			return iDataObjectMasterDao.updateDataObjectStatus(dataObjectMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateDataObject(DataObjectMasterDto reqDto) throws ApplicationException {
		try{
			//check already exists 
			Response validRes = iDataObjectMasterDao.getDataObjectByNameNotId(reqDto);
			if(validRes.getCode().equals(SUCCESS_CODE) && validRes.getObject() != null){
				return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}
			reqDto.setStatus(ACTIVE);
			reqDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			return iDataObjectMasterDao.saveDataObject(reqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	
}