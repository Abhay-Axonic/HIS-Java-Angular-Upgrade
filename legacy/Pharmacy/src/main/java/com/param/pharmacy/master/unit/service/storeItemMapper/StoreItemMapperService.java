package com.param.pharmacy.master.unit.service.storeItemMapper;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemMapper;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.storeItemMapper.IStoreItemMapperDao;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.StoreStockRequestDate;
import com.param.pharmacy.master.unit.dto.storeItemMapper.GetStoreItemRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.SIResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StoreItemMapperService")
public class StoreItemMapperService implements IStoreItemMapperService {

	@Autowired
	IStoreItemMapperDao StoreItemMapperDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<StoreItemMapperListResponse> getAll(Integer storeId) {
		/*
		 * List<StoreItemMapperListResponse> list = new
		 * ArrayList<StoreItemMapperListResponse>();
		 * 
		 * for (StoreItemMapper storeItemMapper : StoreItemMapperDao.getAll(storeId)) {
		 * StoreItemMapperListResponse storeItemMapRes = new
		 * StoreItemMapperListResponse();
		 * 
		 * storeItemMapRes.setId((BigInteger) storeItemMapper.getId());
		 * 
		 * storeItemMapRes.setStoreId((Integer)
		 * storeItemMapper.getStore().getStoreId());
		 * storeItemMapRes.setStoreName((String)
		 * storeItemMapper.getStore().getStoreName());
		 * 
		 * storeItemMapRes.setProductCategoryId( storeItemMapper.getItem().getGeneric()
		 * != null ? storeItemMapper.getItem().getGeneric().getProductCategory().getId()
		 * : storeItemMapper.getItem().getProductCategory().getId());
		 * 
		 * storeItemMapRes.setProductCategory( storeItemMapper.getItem().getGeneric() !=
		 * null ? (String)
		 * storeItemMapper.getItem().getGeneric().getProductCategory().getCategory() :
		 * (String) storeItemMapper.getItem().getProductCategory().getCategory() );
		 * 
		 * storeItemMapRes.setAssetCategoryId( storeItemMapper.getItem().getGeneric() !=
		 * null ? (Integer)
		 * storeItemMapper.getItem().getGeneric().getProductCategory().getAssetCategory(
		 * ).getId() : (Integer)
		 * storeItemMapper.getItem().getProductCategory().getAssetCategory().getId());
		 * 
		 * storeItemMapRes.setAssetCategory( storeItemMapper.getItem().getGeneric() !=
		 * null ? (String)
		 * storeItemMapper.getItem().getGeneric().getProductCategory().getAssetCategory(
		 * ).getCategory() : (String)
		 * storeItemMapper.getItem().getProductCategory().getAssetCategory().getCategory
		 * ());
		 * 
		 * storeItemMapRes.setItemId((Integer) storeItemMapper.getItem().getId());
		 * storeItemMapRes.setItemName((String) storeItemMapper.getItem().getName());
		 * storeItemMapRes.setItemCode((String) storeItemMapper.getItem().getCode());
		 * 
		 * storeItemMapRes.setMinQty((Integer) storeItemMapper.getMinQty());
		 * storeItemMapRes.setMaxQty((Integer) storeItemMapper.getMaxQty());
		 * storeItemMapRes.setStatus((Boolean) storeItemMapper.getIsActive());
		 * list.add(storeItemMapRes); } return list;
		 */
		
		return StoreItemMapperDao.getListBetterPerformance(storeId);
	}

	@Override
	@Transactional
	public StoreItemMapperDetailsResponse getById(BigInteger id) {
		StoreItemMapperDetailsResponse response = new StoreItemMapperDetailsResponse();

		StoreItemMapper storeItemMapper = StoreItemMapperDao.getById(id);

		if (storeItemMapper != null) {
			response.setId((BigInteger) storeItemMapper.getId());
			response.setStoreId((Integer) storeItemMapper.getStore().getStoreId());
			response.setStoreName((String) storeItemMapper.getStore().getStoreName());
			response.setProductCategoryId(
					(Integer) storeItemMapper.getItem().getGeneric().getProductCategory().getId());
			response.setProductCategory(
					(String) storeItemMapper.getItem().getGeneric().getProductCategory().getCategory());
			response.setAssetCategoryId(
					(Integer) storeItemMapper.getItem().getGeneric().getProductCategory().getAssetCategory().getId());
			response.setAssetCategory((String) storeItemMapper.getItem().getGeneric().getProductCategory()
					.getAssetCategory().getCategory());
			response.setItemId((Integer) storeItemMapper.getItem().getId());
			response.setItemName((String) storeItemMapper.getItem().getName());
			response.setItemCode((String) storeItemMapper.getItem().getCode());
			response.setMinQty((Integer) storeItemMapper.getMinQty());
			response.setMaxQty((Integer) storeItemMapper.getMaxQty());
			response.setStatus((Boolean) storeItemMapper.getIsActive());
		} else {

		}
		return response;
	}

	
	@Override
	@Transactional
	public CommonResponse create(List<GetStoreItemRequest>  getStoreItemRequest) {

		CommonResponse response = new CommonResponse();
		
      		
		//for(MultipleStoreRequest s : getStoreItemRequest.getStores()) {
		for(GetStoreItemRequest s : getStoreItemRequest) {/*
			for(Integer storeId : s.getStoreIds()) {

					//Store store = new Store();
				//store.setStoreId(s.getId());
				//store.setStoreId(storeId);
				//sessionService.setEntityDefaults(new Store(storeId));

				//	List<GetStoreItemResponse> items = s.getItems();
				//for (GetStoreItemResponse mapperResp : items) {
				StoreItemMapper storeItemMapper = new StoreItemMapper();
				//storeItemMapper.setId(s.getId());
				storeItemMapper.setStore(new Store(storeId));
				//Item item = new Item();
				//sessionService.setEntityDefaults(item);
				//item.setId(s.getItemId());
				storeItemMapper.setItem( new Item(s.getItemId()));
				storeItemMapper.setMinQty(s.getMinQty());
				storeItemMapper.setMaxQty(s.getMaxQty());
				storeItemMapper.setIsSellable(s.getIsSellable() != null ? s.getIsSellable() : false);
				sessionService.setEntityDefaults(storeItemMapper);

				StoreItemMapperDao.create(storeItemMapper).longValue();

		}
			response.setMessage("Store Item Mapper Inserted Successfully");
			//}
		*/
			
			Store store = new Store();
		//	sessionService.setEntityDefaults(store);	
			store.setCreateDefaults(s.getOrganizationId()!=null ? s.getOrganizationId() : sessionService.getOrganizationId(),s.getUnitId() !=null ? s.getUnitId() :sessionService.getUnitId() ,s.getUserId()!=null ? s.getUserId() : sessionService.getUserId());
			
			//store.setStoreId(s.getId());
			store.setStoreId(s.getStoreId());

		//	List<GetStoreItemResponse> items = s.getItems();
			//for (GetStoreItemResponse mapperResp : items) {
				StoreItemMapper storeItemMapper = new StoreItemMapper();
				//sessionService.setEntityDefaults(storeItemMapper);	
				storeItemMapper.setCreateDefaults(s.getOrganizationId()!=null ? s.getOrganizationId() : sessionService.getOrganizationId(),s.getUnitId() !=null ? s.getUnitId() :sessionService.getUnitId() ,s.getUserId()!=null ? s.getUserId() : sessionService.getUserId());
				
				storeItemMapper.setId(s.getId());
				storeItemMapper.setStore(store);
				Item item = new Item();
				//sessionService.setEntityDefaults(item);	
				item.setCreateDefaults(s.getOrganizationId()!=null ? s.getOrganizationId() : sessionService.getOrganizationId(),s.getUnitId() !=null ? s.getUnitId() :sessionService.getUnitId() ,s.getUserId()!=null ? s.getUserId() : sessionService.getUserId());
				
				item.setId(s.getItemId());
				storeItemMapper.setItem(item);
				storeItemMapper.setMinQty(s.getMinQty());
				storeItemMapper.setMaxQty(s.getMaxQty());
				storeItemMapper.setIsSellable(s.getIsSellable() != null ?  s.getIsSellable() : false );
				
				response.setId(StoreItemMapperDao.create(storeItemMapper).longValue());
				response.setMessage("Store Item Mapper Inserted Successfully");

			//}
		
		
		
		
		}
		
		
	
		return response;
	}
	
//	@Override
//	@Transactional
//	public CommonResponse update(SIRequest request) {
/*
		StoreItemMapper storeItemMapper = StoreItemMapperDao.getStoreItemMapper(request.getId());

		storeItemMapper.updateStoreItemMapper(request.getId(), new Item(request.getItemId()),new Store(request.getStoreId()),request.getMinQty(),request.getMaxQty());

		storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		storeItemMapper.setUpdatedBy(storeItemMapper.getUser());

		StoreItemMapperDao.update(storeItemMapper);

		CommonResponse response = new CommonResponse();
		response.setMessage("Store Item Mapper updated successfully");

		return response;
	}*/

	@Override
	@Transactional
	public CommonResponse update(List<SIResponse> requestList,Integer userId) {
		
		for(SIResponse sipResponse : requestList) {
			
			StoreItemMapper storeItemMapper = StoreItemMapperDao.getById(sipResponse.getId());
			storeItemMapper.updateStoreItemMapper(new Item(sipResponse.getItemId()),new Store(sipResponse.getStoreId()),
					sipResponse.getMinQty(),sipResponse.getMaxQty());
			storeItemMapper.setIsSellable((Boolean) sipResponse.getIsSellable());
			storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(storeItemMapper.getUnitId()));
			storeItemMapper.setUpdatedByUser(sipResponse.getUserId()!=null ? sipResponse.getUserId() : sessionService.getUserId());
			
			StoreItemMapperDao.update(storeItemMapper);

			
		}
		
/*		for (StoreItemMapper storeItemMapper : response) {
		
			SIResponse storeItemMapRes = new SIResponse();
//		StoreItemMapper storeItemMapper = StoreItemMapperDao.getStoreItemMapper(request.getId());

		storeItemMapper.updateStoreItemMapper(storeItemMapRes.getId(), new Item(storeItemMapRes.getItemId()),new Store(storeItemMapRes.getStoreId()),
				storeItemMapRes.getMinQty(),storeItemMapRes.getMaxQty());
		list.add(storeItemMapRes);

		storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		storeItemMapper.setUpdatedBy(storeItemMapper.getUser());

//		StoreItemMapperDao.update(storeItemMapper);
*/
		CommonResponse response = new CommonResponse();
		response.setMessage("Store Item Mapper updated successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse GetMultipleStore(StoreItemMapperListResponse request) {
		return StoreItemMapperDao.GetMultipleStore(request);
	}

	@Override
	@Transactional
	public CommonResponse removeItemMapping(SIResponse request) throws CustomeException{
		    CommonResponse response = new CommonResponse();
	        StoreItemMapper storeItemMapper = StoreItemMapperDao.getById(request.getId());
	        StoreStockRequestDate req = new StoreStockRequestDate();
	        req.setFromDate(request.getFromDate());
	        req.setStoreId(request.getStoreId());
	        req.setItemId(request.getItemId());
	        List<com.param.pharmacy.master.unit.dto.StoreStockResponse> stockResponse= StoreItemMapperDao.checkStock(req);
	        
	        if(stockResponse.size() > 0 && stockResponse.getFirst().getCurrentStock() > 0)
	        {
	        	
	        //	throw new CustomeException(null, "Stock is available", null);
	        	response.setMessage("Stock is available");
	        	return response;
	        }
	        
	        storeItemMapper.setIsActive(false);
	        storeItemMapper.setIsDeleted(true);
			storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(storeItemMapper.getUnitId()));
			storeItemMapper.setUpdatedByUser(request.getUserId());
			
			StoreItemMapperDao.update(storeItemMapper);

			
		

		
		response.setMessage("Store Item Mapper remove successfully");

		return response;
	
	}

	@Override
	@Transactional
	public List<StoreItemMapperListResponse> GetTransactionCount(List<MultipleStoreRequest> request,
			BigInteger itemId) {
		return StoreItemMapperDao.GetTransactionCount(request,itemId);
	}

}
