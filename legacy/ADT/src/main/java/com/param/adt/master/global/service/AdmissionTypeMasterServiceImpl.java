package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.global.dto.BedStatusMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IAdmissionTypeMasterDao;
import com.param.adt.master.global.dto.AdmissionTypeMasterDto;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionTypeMasterServiceImpl implements IAdmissionTypeMasterService,ICommonConstants {
	
	@Autowired
	IAdmissionTypeMasterDao iAdmissionTypeMasterDao;

	private Cache<String, List<AdmissionTypeMasterDto>> admissionTypeMasterCache;

	@PostConstruct
	public void initCache() {
		admissionTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.build();
	}

	@Override
	public Response addAdmissionTypeMaster(AdmissionTypeMasterDto admissionTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iAdmissionTypeMasterDao.getAdmissionTypeByName(admissionTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				iAdmissionTypeMasterDao.addAdmissionTypeMaster(admissionTypeMasterDto);
				admissionTypeMasterCache.invalidate("admissionTypeList");
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAdmissionTypeMasterList(AdmissionTypeMasterDto admissionTypeMasterDto) throws ApplicationException {
		try {
			return iAdmissionTypeMasterDao.getAdmissionTypeMasterList(admissionTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAdmissionTypeMaster(AdmissionTypeMasterDto admissionTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iAdmissionTypeMasterDao.getAdmissionTypeByNameNotId(admissionTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				/*admissionTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				iAdmissionTypeMasterDao.updateAdmissionTypeMaster(admissionTypeMasterDto);
				admissionTypeMasterCache.invalidate("admissionTypeList");
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAdmissionTypeById(AdmissionTypeMasterDto admissionTypeMasterDto) throws ApplicationException {
		try {
			return iAdmissionTypeMasterDao.getAdmissionTypeById(admissionTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForAdmissionType(AdmissionTypeMasterDto admissionTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iAdmissionTypeMasterDao.getAdmissionTypeById(admissionTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*admissionTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				iAdmissionTypeMasterDao.updateAdmissionTypeStatus(admissionTypeMasterDto);
				admissionTypeMasterCache.invalidate("admissionTypeList");
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveAdmissionTypeList(Integer organizationId) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			List<AdmissionTypeMasterDto> responseCache = admissionTypeMasterCache.getIfPresent("admissionTypeList");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iAdmissionTypeMasterDao.getActiveAdmissionTypeMasterList(organizationId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				admissionTypeMasterCache.put("admissionTypeList", res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAdmissionTypeCount(AdmissionTypeMasterDto admissionTypeMasterDto) throws ApplicationException {
		try {
			return iAdmissionTypeMasterDao.getCount(admissionTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

} 
