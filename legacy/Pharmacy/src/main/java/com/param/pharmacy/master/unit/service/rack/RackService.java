package com.param.pharmacy.master.unit.service.rack;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Store;
import com.param.pharmacy.master.unit.dao.rack.IRackDao;
import com.param.pharmacy.master.unit.dto.rack.RackDetailsResponse;
import com.param.pharmacy.master.unit.dto.rack.RackListResponse;
import com.param.pharmacy.master.unit.dto.rack.RackRequest;
import com.param.pharmacy.master.unit.dto.rack.ShelfRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("RackService")
public class RackService implements IRackService {

	@Autowired
	IRackDao RackDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<RackListResponse> getAll(Integer unitId, Integer orgId) {

		List<RackListResponse> responseList = new ArrayList<RackListResponse>();

		for (Rack rack : RackDao.getAll(unitId,orgId)) {

			RackListResponse rackResponse = new RackListResponse(rack.getId(), rack.getStore().getStoreId(),
					rack.getStore().getStoreName(), rack.getCode(), rack.getName(), rack.getIsActive());

			for (Shelf shelf : rack.getShelfList()) {
				if (!shelf.getIsDeleted()) {
				rackResponse.addItems(new ShelfRequest(shelf.getId(), shelf.getCode(), shelf.getName()));
				}
			}
			responseList.add(rackResponse);
		}
		return responseList;
	}

	@Override
	@Transactional
	public RackDetailsResponse getById(Integer id) {
		RackDetailsResponse responseList = new RackDetailsResponse();

		Rack rack = RackDao.getById(id);

		if (rack != null) {

			responseList = new RackDetailsResponse(rack.getId(), rack.getStore().getStoreId(),
					rack.getStore().getStoreName(), rack.getCode(), rack.getName(), rack.getIsActive());

			for (Shelf shelf : rack.getShelfList()) {
				if (!shelf.getIsDeleted()) {
					responseList.addItems(new ShelfRequest(shelf.getId(), shelf.getCode(), shelf.getName()));
				}
			}
			responseList.add(responseList);
		} else {
		}
		return responseList;
	}

	@Override
	@Transactional
	public CommonResponse create(RackRequest rackRequest) {

		Rack rack = new Rack(rackRequest.getRackCode(), rackRequest.getRackName(), new Store(rackRequest.getStoreId()));
		//sessionService.setEntityDefaults(rack);	
		rack.setCreateDefaults(rackRequest.getOrganizationId()!=null ? rackRequest.getOrganizationId() : sessionService.getOrganizationId(),rackRequest.getUnitId() !=null ? rackRequest.getUnitId() :sessionService.getUnitId() ,rackRequest.getUserId()!=null ? rackRequest.getUserId() : sessionService.getUserId());
		
		for (ShelfRequest items : rackRequest.getItems()) {
			Shelf shelf=new Shelf();
			shelf.setCode(items.getShelfCode());
			shelf.setName(items.getShelfName());
			shelf.setCreateDefaults(rackRequest.getOrganizationId()!=null ? rackRequest.getOrganizationId() : sessionService.getOrganizationId(),rackRequest.getUnitId() !=null ? rackRequest.getUnitId() :sessionService.getUnitId() ,rackRequest.getUserId()!=null ? rackRequest.getUserId() : sessionService.getUserId());
			rack.addShelfList(shelf);
		}
		RackDao.create(rack);
		CommonResponse response = new CommonResponse();
		response.setId(rack.getId().longValue());
		response.setMessage("Rack Inserted Successfully");
		return response;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public Object updateRack(RackRequest request) {
	 * 
	 * Rack rack = RackDao.getRack(request.getId());
	 * 
	 * rack.updateRack(request.getRackCode()!=null ? request.getRackCode() :
	 * rack.getCode(), request.getRackName()!=null ? request.getRackName() :
	 * rack.getName(), new Store(request.getStoreId()!=null ? request.getStoreId() :
	 * rack.getStore().getStoreId()),request.getStatus()!=null ? request.getStatus()
	 * : rack.getIsActive());
	 * 
	 * if(request.getItems()!=null) { for (Shelf detail : rack.getShelfList()) {
	 * Integer id = null;
	 * 
	 * for (ShelfRequest item : request.getItems()) { if (item.getShelfId() != null
	 * && item.getShelfId().equals(detail.getId())) { id = item.getShelfId();
	 * detail.updateShelf(item.getShelfId(),
	 * item.getShelfCode(),item.getShelfName()); break; } } if (id == null) {
	 * detail.setIsDeleted(true); }
	 * 
	 * detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId)); detail.setUpdatedBy(detail.getUser()); } }
	 * 
	 * if(request.getItems()!=null) { for (ShelfRequest item : request.getItems()) {
	 * if (item.getShelfId() == null) { rack.addShelfList(new
	 * Shelf(item.getShelfCode(),item.getShelfName())); } } }
	 * 
	 * rack.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId)); rack.setUpdatedBy(rack.getUser());
	 * 
	 * RackDao.updateRack(rack);
	 * 
	 * CommonResponse response = new CommonResponse();
	 * response.setMessage("Rack updated successfully");
	 * 
	 * return response; }
	 */

	@Override
	@Transactional
	public Object updateRack(RackRequest request) {

		Rack rack = RackDao.getRack(request.getId());

		rack.updateRack(request.getRackCode() != null ? request.getRackCode() : rack.getCode(),
				request.getRackName() != null ? request.getRackName() : rack.getName(),
				new Store(request.getStoreId() != null ? request.getStoreId() : rack.getStore().getStoreId()),
				request.getStatus() != null ? request.getStatus() : rack.getIsActive());

		if (request.getItems() != null) {
			for (Shelf detail : rack.getShelfList()) {
				Integer id = null;

				for (ShelfRequest item : request.getItems()) {
					if (item.getShelfId() != null && item.getShelfId().equals(detail.getId())) {
						id = item.getShelfId();
						detail.updateShelf(item.getShelfId(), item.getShelfCode(), item.getShelfName());
						break;
					}
				}
				if (id == null) {
					detail.setIsDeleted(true);
				}
                detail.setOrganizationId(rack.getOrganizationId());
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(rack.getUnitId()));
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			}
		}

		if (request.getItems() != null) {
			for (ShelfRequest item : request.getItems()) {
				if (item.getShelfId() == null) {
					Shelf shelf=new Shelf();
					shelf.setCode(item.getShelfCode());
					shelf.setName(item.getShelfName());
					shelf.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
					rack.addShelfList(shelf);
				}
			}
		}

		rack.setUpdatedDate(TimeZoneComponent.getDateByzone(rack.getUnitId()));
		rack.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		RackDao.updateRack(rack);

		CommonResponse response = new CommonResponse();
		response.setMessage("Rack updated successfully");

		return response;
	}
}
