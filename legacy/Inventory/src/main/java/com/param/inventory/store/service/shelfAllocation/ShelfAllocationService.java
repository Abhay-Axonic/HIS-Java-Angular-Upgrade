package com.param.inventory.store.service.shelfAllocation;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Store;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.store.dao.shelfAllocation.IShelfAllocationDao;
import com.param.inventory.store.dto.shelfAllocation.RackShelfRequest;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ShelfAllocationService")
public class ShelfAllocationService implements IShelfAllocationService {

	@Autowired
	IShelfAllocationDao ShelfAllocationDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public Object getItemList(ShelfAllocationRequest request) {
		if (request.getAllocated()) {
			return ShelfAllocationDao.getAllocatedItemList(request);
		} else {
			return ShelfAllocationDao.getUnAllocatedItemList(request);
		}
	}

	@Override
	@Transactional
	public Object allocateShelf(ShelfAllocationRequest request) {

		Batch batch = GlobalDao.getBatch(request.getStoreId(), request.getBatchId(),request.getUnitId());

		for (RackShelfRequest shelf : request.getShelf()) {
			ShelfItem shelfItem=new ShelfItem(new Store(request.getStoreId()), new Shelf(shelf.getShelfId()),
					new Rack(shelf.getRackId()));
			//sessionService.setEntityDefaults(shelfItem);	
			batch.addShelfItem(shelfItem);
			shelfItem.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		}

		GlobalDao.saveBatch(batch);

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch allocated to the shelf successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getAllocatedItems(Integer storeId, Integer rackId, Integer shelfId,Integer unitId) {
		return ShelfAllocationDao.getAllocatedItems(storeId, rackId, shelfId,unitId);
	}

	@Override
	@Transactional
	public Object updateStatus(ShelfAllocationRequest request) {
		ShelfItem shelfItem = ShelfAllocationDao.getShelfItem(request.getId(),request.getUnitId());

		shelfItem.updateShelfItem(shelfItem.getShelf(), shelfItem.getRack(), request.getStatus(),request.getUserId());

		ShelfAllocationDao.updateShelfItem(shelfItem);

		CommonResponse response = new CommonResponse();
		response.setMessage("Status updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateAllocatedItem(ShelfAllocationRequest request) {
		ShelfItem shelfItem = ShelfAllocationDao.getShelfItem(request.getId(),request.getUnitId());

		Integer id = null;

		for (RackShelfRequest shelf : request.getShelf()) {
			if (id == null && shelf.getId().equals(shelfItem.getId())) {
				id = shelf.getId();
				shelfItem.updateShelfItem(new Shelf(shelf.getShelfId()), new Rack(shelf.getRackId()),
						shelfItem.getIsActive(),request.getUserId());
			} else {
				shelfItem.getBatch().addShelfItem(new ShelfItem(new Store(request.getStoreId()),
						new Shelf(shelf.getShelfId()), new Rack(shelf.getRackId())));
			}
		}

		if (id == null) {
			shelfItem.setIsDeleted(true);
		//	shelfItem.setUpdatedBy(shelfItem.getUser());
			shelfItem.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId() );
			shelfItem.setUpdatedDate(TimeZoneComponent.getDateByzone(shelfItem.getUnitId()));
		}

		ShelfAllocationDao.updateShelfItem(shelfItem);

		CommonResponse response = new CommonResponse();
		response.setMessage("Shelf allocation updated successfully");

		return response;
	}
}
