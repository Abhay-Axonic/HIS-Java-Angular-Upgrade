package com.param.pharmacy.master.unit.service.ItemRestriction;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemRestriction;
import com.param.pharmacy.master.unit.dao.itemRestriction.IItemRestrictionDao;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ItemRestrictionService")
public class ItemRestrictionService implements IItemRestrictionService {

	@Autowired
	IItemRestrictionDao ItemRestrictionDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<ItemRestrictionResponse> getAll() {
		List<ItemRestrictionResponse> responseList = new ArrayList<ItemRestrictionResponse>();

		for (StoreItemRestriction storeItemRestriction : ItemRestrictionDao.getAll()) {
			ItemRestrictionResponse itemRestrictionResponse = new ItemRestrictionResponse(storeItemRestriction.getId(),
					storeItemRestriction.getIsActive(), storeItemRestriction.getItem().getId(),
					storeItemRestriction.getStore().getStoreId());

			responseList.add(itemRestrictionResponse);
		}

		return responseList;
	}

	@Override
	@Transactional
	public Object updateStoreItemRestriction(ItemRestrictionRequest request) {

		StoreItemRestriction storeItemRestriction = ItemRestrictionDao.getStoreItemRestriction(request.getId());

		for (Integer itemId : request.getItemsList()) {
			storeItemRestriction.updateStoreItemRestriction(new Store(request.getStoreId()), new Item(itemId));
		}	
		storeItemRestriction.setUpdatedDate(TimeZoneComponent.getDateByzone(storeItemRestriction.getUnitId()));
		storeItemRestriction.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		ItemRestrictionDao.updateStoreItemRestriction(storeItemRestriction);

		CommonResponse response = new CommonResponse();
		response.setMessage("Store item restriction updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object saveStoreItemRestriction(ItemRestrictionRequest request) {

		StoreItemRestriction storeItemRestriction = null;
		for (Integer itemId : request.getItemsList()) {
			storeItemRestriction = new StoreItemRestriction(new Item(itemId), new Store(request.getStoreId()));
			storeItemRestriction.setIsActive(request.getStatus());
		//	sessionService.setEntityDefaults(storeItemRestriction);	
			storeItemRestriction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			ItemRestrictionDao.saveItemRestriction(storeItemRestriction);
		}

		CommonResponse response = new CommonResponse();
		response.setId(storeItemRestriction.getId().longValue());
		response.setMessage("Store Item Restriction saved successfully");

		return response;
	}

}
