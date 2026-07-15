package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.global.model.PatientSourceMaster;
import com.param.global.dto.PatientCategoryMasterDto;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IPatientServiceMasterDao;
import com.param.adt.master.global.dto.PatientSourceMasterDto;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientSourceMasterServiceImpl implements IPatientSourceMasterService, ICommonConstants {
	@Autowired
	private IPatientServiceMasterDao iPatientSourceMasterDao;

	private Cache<Integer, List<PatientSourceMasterDto>> patientSourceMasterCache;

	@PostConstruct
	public void initCache() {
		patientSourceMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response addPatientSourceMaster(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iPatientSourceMasterDao.getPatientSourceByName(patientSourceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*patientSourceMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				patientSourceMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res =iPatientSourceMasterDao.addPatientSourceTypeMaster(patientSourceMasterDto);

				if(res.getStatus().equals(SUCCESS)) {
					patientSourceMasterCache.invalidate(patientSourceMasterDto.getOrganizationId());
				}

				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientSourceMasterList(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			return iPatientSourceMasterDao.getPatientSourceMasterList(patientSourceMasterDto);
		} catch (HibernateException e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientSourceMaster(PatientSourceMasterDto patientSourceMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iPatientSourceMasterDao.getPatientSourceByNameNotId(patientSourceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*patientSourceMasterDto
				.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response res = iPatientSourceMasterDao.updatePatientSource(patientSourceMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					PatientSourceMaster master = (PatientSourceMaster) res.getObject();
					patientSourceMasterCache.invalidate(master.getOrganizationId());
				}

				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getPatientSourceById(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			return iPatientSourceMasterDao.getPatientSourceById(patientSourceMasterDto);
		} catch (HibernateException e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForPatientSource(PatientSourceMasterDto patientSourceMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iPatientSourceMasterDao.getPatientSourceById(patientSourceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*patientSourceMasterDto
				.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iPatientSourceMasterDao.updatePatientSourceStatus(patientSourceMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					patientSourceMasterCache.invalidate(patientSourceMasterDto.getOrganizationId() != null ? patientSourceMasterDto.getOrganizationId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		
			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActivePatientSourceList(Integer organizationId) throws ApplicationException {
		try {
			List<PatientSourceMasterDto> responseCache = patientSourceMasterCache.getIfPresent(organizationId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPatientSourceMasterDao.getActivePatientSourceList(organizationId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				patientSourceMasterCache.put(organizationId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (HibernateException e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientSourceCount(PatientSourceMasterDto patientSourceMasterDto) {
		try {
			return iPatientSourceMasterDao.getCount(patientSourceMasterDto);
		} catch (HibernateException e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
