package com.param.pharmacy.master.global.service.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CombinationGeneric;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.ProductCategory;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.generic.IGenericDao;
import com.param.pharmacy.master.global.dao.productCategory.IProductCategoryDao;
import com.param.pharmacy.master.global.dto.generic.GenericDetailsResponse;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.pharmacy.master.global.dto.generic.GenericRabbitDto;
import com.param.pharmacy.master.global.dto.generic.GenericRequest;
import com.param.pharmacy.master.global.dto.generic.GenericRequestItem;
import com.param.global.common.ServiceException;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("GenericService")
public class GenericService implements IGenericService {

	@Autowired
	IGenericDao GenericDao;

	@Autowired
	IProductCategoryDao ProductCategoryDao;
	
	@Autowired
	IEmrService EmrService;
	

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(GenericService.class);

	@Override
	@Transactional
	public List<GenericListResponse> getAll(Integer orgId) {
		/* Code before performance tuning
		 * 
		 * List<GenericListResponse> list = new ArrayList<GenericListResponse>();
		 * 
		 * for (Generic generic : GenericDao.getAll()) { GenericListResponse genRes =
		 * new GenericListResponse(); genRes.setId((Integer) generic.getId());
		 * genRes.setAssetType((String)
		 * generic.getProductCategory().getAssetCategory().getAssetType().getType());
		 * genRes.setAssetCategory((String)
		 * generic.getProductCategory().getAssetCategory().getCategory());
		 * genRes.setProductCategory((String)
		 * generic.getProductCategory().getCategory()); genRes.setCode((String)
		 * generic.getCode()); genRes.setName((String) generic.getName());
		 * genRes.setStatus((Boolean) generic.getIsActive()); list.add(genRes); }
		 * 
		 * return list;
		 */
		
		//Code after performance tuning
		return GenericDao.getAllGenericMasters(orgId);
	}
	
	@Override
	@Transactional
	public List<GenericListResponse> getAllIsCombinationGeneric(Boolean isCombination) {
		
		return GenericDao.getAllIsCombinationGeneric(isCombination);
	}

	@Override
	@Transactional
	public GenericDetailsResponse getById(Integer id, Integer orgId) {
		Generic generic = GenericDao.getById(id,orgId);

		GenericDetailsResponse response = new GenericDetailsResponse();
		response.setId((Integer) generic.getId());
		response.setAssetTypeId((Integer) generic.getProductCategory().getAssetCategory().getAssetType().getId());
		response.setAssetCategoryId((Integer) generic.getProductCategory().getAssetCategory().getId());
		response.setProductCategoryId((Integer) generic.getProductCategory().getId());
		response.setCode((String) generic.getCode());
		response.setName((String) generic.getName());
		response.setStatus((Boolean) generic.getIsActive());
		List<GenericRequestItem> list = new ArrayList<GenericRequestItem>();
		if(generic.getIsCombination().equals(true))
		{
			for (CombinationGeneric detail : generic.getCombinationGenericList()) {
				GenericRequestItem genericRequestItem = new GenericRequestItem(detail.getId(),
						detail.getGeneric().getId(),detail.getGeneric().getName(),
						detail.getSubGeneric().getId(),detail.getStrengthUnit(),detail.getStrengthUnitId());
				list.add(genericRequestItem);
			}
		}
		response.setItems(list);
		return response;
	}
	
	@Override
	@Transactional
	public List<GenericRequestItem> getCombinationDetailsById(Integer id) {
		
		List<GenericRequestItem> genericList = new ArrayList<GenericRequestItem>();	
		for(CombinationGeneric generic : GenericDao.getCombinationDetailsById(id))
		{
			GenericRequestItem genericRequestItem = new GenericRequestItem(generic.getId(),generic.getGeneric().getId(),
					generic.getGeneric().getName(),generic.getSubGeneric().getId(),generic.getStrengthUnit(),generic.getStrengthUnitId());
			//genericRequestItem.setStrength(generic.getStrengthUnitId());
			genericList.add(genericRequestItem);
		}
		return genericList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public CommonResponse create(GenericRequest genericRequest) {
		try {
			Generic generic = null;
			
			if(genericRequest.getIsCombination().equals(true))
			{
				 generic = new Generic(genericRequest.getCode(),genericRequest.getName(),genericRequest.getStatus(),
						new ProductCategory(genericRequest.getProductCategoryId()),genericRequest.getIsCombination());
				 if(genericRequest.getItems() != null)
				 {
					for (GenericRequestItem item : genericRequest.getItems()) {
						CombinationGeneric combinationGenericDetail=new CombinationGeneric(new Generic(item.getSubGenericId()), item.getStrengthUnit(),item.getStrength());
					//	sessionService.setEntityDefaults(combinationGenericDetail);
						combinationGenericDetail.setCreateDefaults(genericRequest.getOrganizationId()!=null ? genericRequest.getOrganizationId() : sessionService.getOrganizationId(),genericRequest.getUnitId() !=null ? genericRequest.getUnitId() :sessionService.getUnitId() ,genericRequest.getUserId()!=null ? genericRequest.getUserId() : sessionService.getUserId());
						
						generic.addCombinationGenericList(combinationGenericDetail);
					}
				}
			}else
			{
				 generic = new Generic(genericRequest.getCode(),genericRequest.getName(),genericRequest.getStatus(),
						new ProductCategory(genericRequest.getProductCategoryId()));
			}
			CommonResponse response = new CommonResponse();
			//sessionService.setEntityDefaults(generic);
			generic.setCreateDefaults(genericRequest.getOrganizationId()!=null ? genericRequest.getOrganizationId() : sessionService.getOrganizationId(),genericRequest.getUnitId() !=null ? genericRequest.getUnitId() :sessionService.getUnitId() ,genericRequest.getUserId()!=null ? genericRequest.getUserId() : sessionService.getUserId());
			
			response.setId(GenericDao.create(generic).longValue());
			response.setMessage("Generic Inserted Successfully");

			GenericRabbitDto genericRabbitDto = new GenericRabbitDto(response.getId().intValue(),generic.getCode(),generic.getName(), generic.getProductCategory().getId(),
					generic.getAddedBy().getUserId(),generic.getAddedDate(),generic.getUpdatedBy().getUserId(),generic.getUpdatedDate(),generic.getIsDeleted(),generic.getIsActive(),
					generic.getOrganization().getOrganizationId(),generic.getUnit().getUnitId());

			generic.setId(response.getId().intValue());
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("create");
			payload.setData(genericRabbitDto);
			request.setKey(MasterKeyConstants.GenericMasterHISKey.toString());
			request.setPayload(payload);
			//EmrService.publish(request);

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public CommonResponse update(GenericRequest genericRequest) throws CustomeException {

		Generic generic = GenericDao.getById(genericRequest.getId(),genericRequest.getOrganizationId());

		if (genericRequest.getProductCategoryId() != null && genericRequest.getProductCategoryId() > 0) {
			generic.setProductCategory(ProductCategoryDao.getById(genericRequest.getProductCategoryId(),genericRequest.getOrganizationId()));
		}

		if (genericRequest.getCode() != null && genericRequest.getCode() != "") {
			generic.setCode(genericRequest.getCode());
		}

		if (genericRequest.getName() != null && genericRequest.getName() != "") {
			generic.setName(genericRequest.getName());
		}
		if (genericRequest.getStatus() != null) {
			generic.setIsActive(genericRequest.getStatus());
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Generic Updated Successfully");
		Long GenericCountFromItem = new Long(0);
		Long GenericCountFromStoreWise = new Long(0);
		Long GenericCountFromProductCategory = new Long(0);
		if (!generic.getIsActive()) {
			GenericCountFromItem = GenericDao.getGenericCountFromItem(genericRequest.getId());
			GenericCountFromStoreWise = GenericDao.getGenericCountFromStoreWiseMapper(genericRequest.getId());
		} else {
			GenericCountFromProductCategory = GenericDao.getGenericCountFromProductCategory(genericRequest.getId());
		}
		boolean UnableToUpdateStatus = false;
		if (GenericCountFromItem <= 0 && GenericCountFromStoreWise <= 0 && GenericCountFromProductCategory <= 0) {
			UnableToUpdateStatus = false;
			if (genericRequest.getStatus() != null) {
				generic.setUpdatedDate(TimeZoneComponent.getDateByzone(generic.getUnitId()));
				generic.setUpdatedByUser(genericRequest.getUserId()!=null ? genericRequest.getUserId() : sessionService.getUserId());
				generic.setIsActive(genericRequest.getStatus());
			}
		} else if (GenericCountFromItem > 0) {
			UnableToUpdateStatus = true;
			if (genericRequest.getStatus() != null) {
				generic.setIsActive(true);
				response.setMessage("Generic is assigned to Item");
			}
		} else if (GenericCountFromStoreWise > 0) {
			UnableToUpdateStatus = true;
			if (genericRequest.getStatus() != null) {
				generic.setIsActive(true);
				response.setMessage("Generic is assigned to Storewise Generic Mapper");
			}
		} else if (GenericCountFromProductCategory > 0) {
			UnableToUpdateStatus = true;
			if (genericRequest.getStatus() != null) {
				generic.setIsActive(false);
				response.setMessage("Product Category is not Active");
			}
		}
		if(genericRequest.getIsCombination() != null && genericRequest.getIsCombination().equals(true))
		{
			if(genericRequest.getStatus().equals(false))
			{
				for(CombinationGeneric combinationGeneric : generic.getCombinationGenericList())
				{
					combinationGeneric.setIsDeleted(true);
				}
				
			}else
			{
				if (genericRequest.getItems() != null) {
					for (CombinationGeneric detail : generic.getCombinationGenericList()) {
						Integer id = null;

						for (GenericRequestItem item : genericRequest.getItems()) {
							if (item.getId() != null && item.getId().equals(detail.getId())) {
								id = item.getId();
								detail.updateCombinationGeneric(item.getStrengthUnit(), item.getStrength());
								break;
							}
						}

						if (id == null) {
							detail.setIsDeleted(true);
						} else {
							detail.setIsDeleted(false);
						}

						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(generic.getUnitId()));
						detail.setUpdatedByUser(genericRequest.getUserId()!=null ? genericRequest.getUserId() : sessionService.getUserId());
					}

					if (genericRequest.getItems() != null) {
						for (GenericRequestItem item : genericRequest.getItems()) {
							if (item.getId() == null) {
								CombinationGeneric combinationGenericDetail=new CombinationGeneric(new Generic(item.getSubGenericId()), item.getStrengthUnit(), item.getStrength());
							//	sessionService.setEntityDefaults(combinationGenericDetail);
								combinationGenericDetail.setCreateDefaults(genericRequest.getOrganizationId()!=null ? genericRequest.getOrganizationId() : sessionService.getOrganizationId(),genericRequest.getUnitId() !=null ? genericRequest.getUnitId() :sessionService.getUnitId() ,genericRequest.getUserId()!=null ? genericRequest.getUserId() : sessionService.getUserId());
								generic.addCombinationGenericList(combinationGenericDetail);
							}
						}
					}
				}
			}
		}
		response.setId(GenericDao.update(generic).longValue());
		GenericRabbitDto genericRabbitDto = new GenericRabbitDto(response.getId().intValue(),generic.getCode(),generic.getName(), generic.getProductCategory().getId(),
				generic.getAddedBy().getUserId(),generic.getAddedDate(),generic.getUpdatedBy().getUserId(),generic.getUpdatedDate(),generic.getIsDeleted(),generic.getIsActive(),
				generic.getOrganization().getOrganizationId(),generic.getUnit().getUnitId());
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(genericRabbitDto);
		request.setKey(MasterKeyConstants.GenericMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);

		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),generic.getIsActive());
			throw customeException;
		}

		return response;
	}
}
