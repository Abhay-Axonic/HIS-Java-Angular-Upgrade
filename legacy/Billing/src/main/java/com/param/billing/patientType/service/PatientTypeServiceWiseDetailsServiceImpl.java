package com.param.billing.patientType.service;

import in.param.exception.ApplicationException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.dto.BankMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.model.PatientTypeGroupWiseDetailsMaster;
import com.param.billing.patientType.dao.IPatientTypeDao;
import com.param.billing.patientType.dao.IPatientTypeGroupWiseDetailsDao;
import com.param.billing.patientType.dao.IPatientTypeServiceWiseDetailsDao;
import com.param.billing.patientType.dto.PatientTypeDto;
import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.billing.patientType.dto.PatientTypeServiceWiseDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings({"rawtypes","unchecked","unused"})
@Service
public class PatientTypeServiceWiseDetailsServiceImpl implements IPatientTypeServiceWiseDetailsService,ICommonConstants{

	@Autowired
	IPatientTypeServiceWiseDetailsDao iPatientTypeServiceWiseDetailsDao;
	
	@Autowired
	IPatientTypeGroupWiseDetailsDao iPatientTypeGroupWiseDetailsDao;
	
	@Autowired
	IPatientTypeDao iPatientTypeDao;

	private Cache<Integer, List<PatientTypeDto>> patientTypeMasterCache;

	@PostConstruct
	public void initCache() {
		patientTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	@Transactional
	public Response savePatientTypeServiceWiseDetails(PatientTypeDto patientTypeDto) throws Exception{
		try {
			
			//Response procedureOrderRes =  iPatientTypeServiceWiseDetailsDao.savePatientTypeServiceWiseDetails(patientTypeDto.getPatientTypeServiceWiseDetails());
			
			Response procedure = iPatientTypeGroupWiseDetailsDao.savePatientTypeGroupWiseDetails(patientTypeDto.getPatientTypeGroupWiseDetails());
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response getPatientTypeServiceWiseDetails(int patientTypeId,int organizationId,int unitId) {
		
		try {
			PatientTypeDto patientTypeDto = new PatientTypeDto();
			
			//Response patientTypeService =  iPatientTypeServiceWiseDetailsDao.getPatientTypeServiceWiseDetails(patientTypeId,organizationId,unitId);
			//patientTypeDto.setPatientTypeServiceWiseDetails(patientTypeService.getListObject());
			Response patientTypeGroup = iPatientTypeGroupWiseDetailsDao.getPatientTypeGroupWiseDetails(patientTypeId,organizationId,unitId);
			patientTypeDto.setPatientTypeGroupWiseDetails(patientTypeGroup.getListObject());
			
			
			return new Response(SUCCESS, SUCCESS_CODE, "success", null, patientTypeDto);
			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	@Override
	@Transactional
	public Response savePatientType(PatientTypeDto patientTypeDto) throws Exception {
		try {
			
			Response res =  iPatientTypeDao.checkPatientType(patientTypeDto);
			
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				
				return new Response(ERROR, ERROR_CODE, "Already Exists Patient Type Description", null, null);
				
			}
			
				Response saveRes = iPatientTypeDao.savePatientType(patientTypeDto);
				if(saveRes.getStatus().equals(SUCCESS)) {
					patientTypeMasterCache.invalidate(patientTypeDto.getUnitId());
				}

				return saveRes;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getPatientType(int organizationId , int unitId) {
		try {
			List<PatientTypeDto> responseCache = patientTypeMasterCache.getIfPresent(unitId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPatientTypeDao.getPatientType(organizationId,unitId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				patientTypeMasterCache.put(unitId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updatePatientTypeStatus(PatientTypeDto patientTypeDto) throws Exception {
		try {

			Response saveRes = iPatientTypeDao.updatePatientTypeStatus(patientTypeDto);
			if(saveRes.getStatus().equals(SUCCESS) && saveRes.getObject() != null){
				Integer filterId = (Integer) saveRes.getObject();
				patientTypeMasterCache.invalidate(patientTypeDto.getUnitId() != null ? patientTypeDto.getUnitId() : filterId);
			}
			return saveRes;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updatePatientType(PatientTypeDto patientTypeDto) throws Exception {
		try {

			Response res =  iPatientTypeDao.updateCheckPatientType(patientTypeDto);
			
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				
				return new Response(ERROR, ERROR_CODE, "Already Exists Patient Type Description", null, null);
				
			}
				
				Response updateRes = iPatientTypeDao.updatePatientType(patientTypeDto);

				if(updateRes.getStatus().equals(SUCCESS)) {
					patientTypeMasterCache.invalidate(patientTypeDto.getUnitId());
				}
				return updateRes;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updatePatientTypeGroupStatus(PatientTypeGroupWiseDetailsDto patientTypeGroupWiseDetailsDto) throws Exception {
		try {

			return iPatientTypeGroupWiseDetailsDao.updatePatientTypeGroupStatus(patientTypeGroupWiseDetailsDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response updatePatientTypeServiceStatus(PatientTypeServiceWiseDetailsDto patientTypeServiceWiseDetailsDto)
			throws Exception {
		try {

			return iPatientTypeServiceWiseDetailsDao.updatePatientTypeServiceStatus(patientTypeServiceWiseDetailsDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
