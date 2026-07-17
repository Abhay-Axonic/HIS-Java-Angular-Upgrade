package com.param.pharmacy.master.unit.service.storewiseGenericMapper;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StorewiseGenericMapper;
import com.param.entity.model.master.Ved;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.storewiseGenericMapper.IStorewiseGenericMapperDao;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.GenericResponseDto;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperListResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperRequest;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.VedResponseList;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StorewiseGenericMapperService")
public class StorewiseGenericMapperService implements IStorewiseGenericMapperService {

	@Autowired
	IStorewiseGenericMapperDao StorewiseGenericMapperDao;
	
	@Autowired
	private SessionService sessionService;


	@Override
	@Transactional
	public List<StorewiseGenericMapperListResponse> getAll(Integer unitId) {
		List<StorewiseGenericMapperListResponse> list = new ArrayList<StorewiseGenericMapperListResponse>();

		for (StorewiseGenericMapper storewiseGenericMapper : StorewiseGenericMapperDao.getAll(unitId)) {
			StorewiseGenericMapperListResponse storewiseGenericResponse = new StorewiseGenericMapperListResponse();
			storewiseGenericResponse.setId((Integer) storewiseGenericMapper.getId());
			storewiseGenericResponse.setStoreId((Integer) storewiseGenericMapper.getStore().getStoreId());
			storewiseGenericResponse.setStoreName((String) storewiseGenericMapper.getStore().getStoreName());
			storewiseGenericResponse.setGenericId((Integer) storewiseGenericMapper.getGeneric().getId());
			storewiseGenericResponse.setGenericCode((String) storewiseGenericMapper.getGeneric().getCode());
			storewiseGenericResponse.setGenericName((String) storewiseGenericMapper.getGeneric().getName());
			storewiseGenericResponse
					.setProductCategoryId((Integer) storewiseGenericMapper.getGeneric().getProductCategory().getId());
			storewiseGenericResponse.setProductCategory(
					(String) storewiseGenericMapper.getGeneric().getProductCategory().getCategory());
			storewiseGenericResponse.setAssetCategoryId(
					(Integer) storewiseGenericMapper.getGeneric().getProductCategory().getAssetCategory().getId());
			storewiseGenericResponse.setAssetCategory(
					(String) storewiseGenericMapper.getGeneric().getProductCategory().getAssetCategory().getCategory());
			storewiseGenericResponse.setVedId((Integer) storewiseGenericMapper.getVed().getId());

			storewiseGenericResponse.setVedName((String) storewiseGenericMapper.getVed().getName());
			storewiseGenericResponse.setStatus((Boolean) storewiseGenericMapper.getIsActive());

			list.add(storewiseGenericResponse);
		}
		return list;
	}

	@Override
	@Transactional
	public StorewiseGenericMapperDetailsResponse getById(Integer id) {

		StorewiseGenericMapperDetailsResponse response = new StorewiseGenericMapperDetailsResponse();

		StorewiseGenericMapper storewiseGenericMapper = StorewiseGenericMapperDao.getById(id);

		if (storewiseGenericMapper != null) {
			response.setId((Integer) storewiseGenericMapper.getId());
			response.setStoreId((Integer) storewiseGenericMapper.getStore().getStoreId());
			response.setStoreName((String) storewiseGenericMapper.getStore().getStoreName());
			response.setGenericId((Integer) storewiseGenericMapper.getGeneric().getId());
			response.setGenericCode((String) storewiseGenericMapper.getGeneric().getCode());
			response.setGenericName((String) storewiseGenericMapper.getGeneric().getName());
			response.setProductCategoryId((Integer) storewiseGenericMapper.getGeneric().getProductCategory().getId());
			response.setProductCategory(
					(String) storewiseGenericMapper.getGeneric().getProductCategory().getCategory());
			response.setAssetCategoryId(
					(Integer) storewiseGenericMapper.getGeneric().getProductCategory().getAssetCategory().getId());
			response.setGenericName(
					(String) storewiseGenericMapper.getGeneric().getProductCategory().getAssetCategory().getCategory());
			response.setVedId((Integer) storewiseGenericMapper.getVed().getId());
			response.setStatus((Boolean) storewiseGenericMapper.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(StorewiseGenericMapperRequest storewiseGenericMapperRequest) {

		Store store = new Store();
		//sessionService.setEntityDefaults(store);	
		store.setCreateDefaults(storewiseGenericMapperRequest.getOrganizationId()!=null ? storewiseGenericMapperRequest.getOrganizationId() : sessionService.getOrganizationId(),storewiseGenericMapperRequest.getUnitId() !=null ? storewiseGenericMapperRequest.getUnitId() :sessionService.getUnitId() ,storewiseGenericMapperRequest.getUserId()!=null ? storewiseGenericMapperRequest.getUserId() : sessionService.getUserId());
		store.setStoreId(storewiseGenericMapperRequest.getStoreId());

		Generic generic = new Generic();
		//sessionService.setEntityDefaults(generic);	
		generic.setCreateDefaults(storewiseGenericMapperRequest.getOrganizationId()!=null ? storewiseGenericMapperRequest.getOrganizationId() : sessionService.getOrganizationId(),storewiseGenericMapperRequest.getUnitId() !=null ? storewiseGenericMapperRequest.getUnitId() :sessionService.getUnitId() ,storewiseGenericMapperRequest.getUserId()!=null ? storewiseGenericMapperRequest.getUserId() : sessionService.getUserId());
		
		generic.setId(storewiseGenericMapperRequest.getGenericId());

		Ved ved = new Ved();
		ved.setId(storewiseGenericMapperRequest.getVedId());

		StorewiseGenericMapper storewiseGenericMapper = new StorewiseGenericMapper();
	//	sessionService.setEntityDefaults(storewiseGenericMapper);
		storewiseGenericMapper.setCreateDefaults(storewiseGenericMapperRequest.getOrganizationId()!=null ? storewiseGenericMapperRequest.getOrganizationId() : sessionService.getOrganizationId(),storewiseGenericMapperRequest.getUnitId() !=null ? storewiseGenericMapperRequest.getUnitId() :sessionService.getUnitId() ,storewiseGenericMapperRequest.getUserId()!=null ? storewiseGenericMapperRequest.getUserId() : sessionService.getUserId());
		
		storewiseGenericMapper.setIsActive(storewiseGenericMapperRequest.getStatus());
		storewiseGenericMapper.setStore(store);
		storewiseGenericMapper.setGeneric(generic);
		storewiseGenericMapper.setVed(ved);

		CommonResponse response = new CommonResponse();
		response.setId(StorewiseGenericMapperDao.create(storewiseGenericMapper).longValue());
		response.setMessage("Storewise Generic Mapper Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(StorewiseGenericMapperRequest storewiseGenericMapperRequest) throws CustomeException {

		StorewiseGenericMapper storewiseGenericMapper = StorewiseGenericMapperDao
				.getById(storewiseGenericMapperRequest.getId());

		if (storewiseGenericMapperRequest.getStoreId() != null && storewiseGenericMapperRequest.getStoreId() > 0) {
			Store store = StorewiseGenericMapperDao.getStore(storewiseGenericMapperRequest.getStoreId());
			storewiseGenericMapper.setStore(store);
		}

		if (storewiseGenericMapperRequest.getGenericId() != null && storewiseGenericMapperRequest.getGenericId() > 0) {
			Generic generic = StorewiseGenericMapperDao.getGeneric(storewiseGenericMapperRequest.getGenericId());
			storewiseGenericMapper.setGeneric(generic);
		}

		if (storewiseGenericMapperRequest.getVedId() != null && storewiseGenericMapperRequest.getVedId() > 0) {
			Ved ved = StorewiseGenericMapperDao.getVed(storewiseGenericMapperRequest.getVedId());
			storewiseGenericMapper.setVed(ved);
		}

		if (storewiseGenericMapperRequest.getStatus() != null) {
			storewiseGenericMapper.setIsActive(storewiseGenericMapperRequest.getStatus());
		}
		CommonResponse response = new CommonResponse();

		response.setMessage("Storewise Generic Mapper Updated Successfully");
		Long countFromGeneric = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!storewiseGenericMapper.getIsActive()) {
			countFromGeneric = StorewiseGenericMapperDao.getStorewiseGenericFromGeneric(storewiseGenericMapper.getId());
		}
		if (countFromGeneric <= 0) {
			UnableToUpdateStatus = false;
			storewiseGenericMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(storewiseGenericMapper.getUnitId()));
			storewiseGenericMapper.setUpdatedByUser(storewiseGenericMapperRequest.getUserId()!=null ? storewiseGenericMapperRequest.getUserId() : sessionService.getUserId());
			if (storewiseGenericMapperRequest.getStatus() != null) {
				storewiseGenericMapper.setIsActive(storewiseGenericMapperRequest.getStatus());
			}
			response.setMessage("Storewise Generic is assigned to Generic");
		}else {
			UnableToUpdateStatus = true;
			if (storewiseGenericMapperRequest.getStatus() != null) {
				storewiseGenericMapper.setIsActive(false);
			}
			response.setMessage("Generic is not Active");
		}

		response.setId(StorewiseGenericMapperDao.update(storewiseGenericMapper).longValue());

		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					storewiseGenericMapper.getIsActive());
			throw customeException;
		}
		return response;
	}

	@Override
	@Transactional
	public List<VedResponseList> getDropdownList() {
		List<VedResponseList> response = new ArrayList<VedResponseList>();

		for (Ved ved : StorewiseGenericMapperDao.getVedList()) {
			response.add(new VedResponseList(ved.getId(), ved.getName(), ved.getCode()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getGenericList(String name, Integer unitId, Integer orgId) {
		List<GenericResponseDto> genericList = new ArrayList<GenericResponseDto>();
		for (Generic generic : StorewiseGenericMapperDao.getGenericList(name, unitId, orgId)) {

			/*
			 * GenericResponseDto genericResponse = new GenericResponseDto(generic.getId(),
			 * generic.getCode() + "|" + generic.getName() + "|" + generic.getCode(),
			 * generic.getName()); genericList.add(genericResponse);
			 */

			GenericResponseDto genericResponse = new GenericResponseDto(generic.getId(), generic.getCode(),
					generic.getName());
			genericList.add(genericResponse);
		}
		return genericList;
	}

}
