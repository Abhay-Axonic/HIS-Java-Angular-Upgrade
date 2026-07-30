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
import com.param.global.service.VerifiedIdentityService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StorewiseGenericMapperService")
public class StorewiseGenericMapperService implements IStorewiseGenericMapperService {

	@Autowired
	IStorewiseGenericMapperDao StorewiseGenericMapperDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;


	@Override
	@Transactional
	public List<StorewiseGenericMapperListResponse> getAll(Integer unitId) {
		// BR-121: unitId was previously taken directly from the URL path with
		// no session/JWT cross-check -- any unit's list was readable simply by
		// changing the path segment. Always use the verified value instead.
		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
		List<StorewiseGenericMapperListResponse> list = new ArrayList<StorewiseGenericMapperListResponse>();

		for (StorewiseGenericMapper storewiseGenericMapper : StorewiseGenericMapperDao.getAll(verifiedUnitId)) {
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

		// BR-121: getById(id) previously had no ownership check at all -- any
		// id was readable by any authenticated user regardless of unit. A
		// cross-tenant id now falls through to the same pre-existing
		// null/not-found handling below as a genuinely nonexistent id.
		StorewiseGenericMapper storewiseGenericMapper = StorewiseGenericMapperDao.getById(id,
				verifiedIdentityService.getVerifiedUnitId());

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

		// BR-121: organizationId/unitId previously came from the client body
		// when present, overriding the session default -- a forged org/unit
		// in the request was honored outright. Always use the JWT-verified
		// values instead, never the client-supplied ones.
		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
		Integer userId = storewiseGenericMapperRequest.getUserId()!=null ? storewiseGenericMapperRequest.getUserId() : sessionService.getUserId();

		Store store = new Store();
		//sessionService.setEntityDefaults(store);
		store.setCreateDefaults(verifiedOrgId, verifiedUnitId, userId);
		store.setStoreId(storewiseGenericMapperRequest.getStoreId());

		Generic generic = new Generic();
		//sessionService.setEntityDefaults(generic);
		generic.setCreateDefaults(verifiedOrgId, verifiedUnitId, userId);

		generic.setId(storewiseGenericMapperRequest.getGenericId());

		Ved ved = new Ved();
		ved.setId(storewiseGenericMapperRequest.getVedId());

		StorewiseGenericMapper storewiseGenericMapper = new StorewiseGenericMapper();
	//	sessionService.setEntityDefaults(storewiseGenericMapper);
		storewiseGenericMapper.setCreateDefaults(verifiedOrgId, verifiedUnitId, userId);

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

		// BR-121: same ownership-check gap as the read path above -- a
		// cross-tenant id now falls through to the same pre-existing
		// null-entity behavior as a genuinely nonexistent id (this method has
		// no null guard of its own, a distinct pre-existing issue, not
		// introduced by this fix -- see docs/ANGULAR_PENDING_FINDINGS.md).
		StorewiseGenericMapper storewiseGenericMapper = StorewiseGenericMapperDao
				.getById(storewiseGenericMapperRequest.getId(), verifiedIdentityService.getVerifiedUnitId());

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
		// BR-121: unitId/orgId previously came directly from the URL path
		// with no session/JWT cross-check -- combined with the DAO query's
		// own OR-precedence bug (fixed alongside this), any tenant's generics
		// were enumerable via this search. Always use verified values.
		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
		List<GenericResponseDto> genericList = new ArrayList<GenericResponseDto>();
		for (Generic generic : StorewiseGenericMapperDao.getGenericList(name, verifiedUnitId, verifiedOrgId)) {

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
