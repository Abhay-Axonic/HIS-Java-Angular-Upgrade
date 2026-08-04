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
import com.param.global.service.VerifiedIdentityService;

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

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	// BATCH-4 org-trust fix: cache key was unitId-only — a unit-1 entry cached under one org was
	// served to ANY org sharing unit id 1 (cross-org poisoning). Key is now org-scoped composite.
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
		organizationId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
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
			// BATCH-4 org-trust fix: bind verified tenant on create (client DTO org/unit were trusted)
			patientTypeDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientTypeDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
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
			// BATCH-4 org-trust fix (live-proven 2026-08-04): client path org/unit were trusted
			// unverified -- forged /getPatientType/999/999 from org-1 session returned org-999
			// victim PT-999-X. Bind verified tenant; cache key now org-scoped (was unitId-only).
			organizationId = verifiedIdentityService.getVerifiedOrganizationId();
			unitId = verifiedIdentityService.getVerifiedUnitId();
			Integer cacheKey = organizationId * 10000 + unitId;
			List<PatientTypeDto> responseCache = patientTypeMasterCache.getIfPresent(cacheKey);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPatientTypeDao.getPatientType(organizationId,unitId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				patientTypeMasterCache.put(cacheKey, res.getListObject());

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
			// BATCH-4 org-trust fix: bind verified tenant on status toggle
			patientTypeDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientTypeDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
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
			// BATCH-4 org-trust fix: bind verified tenant on update
			patientTypeDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientTypeDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
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
			// BATCH-4 org-trust fix: bind verified tenant on group status toggle
			patientTypeGroupWiseDetailsDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientTypeGroupWiseDetailsDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
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
			// BATCH-4 org-trust fix: bind verified tenant on service status toggle
			patientTypeServiceWiseDetailsDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientTypeServiceWiseDetailsDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			return iPatientTypeServiceWiseDetailsDao.updatePatientTypeServiceStatus(patientTypeServiceWiseDetailsDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
