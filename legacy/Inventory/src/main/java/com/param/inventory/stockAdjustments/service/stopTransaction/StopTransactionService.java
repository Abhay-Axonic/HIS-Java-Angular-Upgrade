package com.param.inventory.stockAdjustments.service.stopTransaction;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.StopTransactionItemExclusionDetail;
import com.param.entity.model.inventory.StopTransactionProdCatExclDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.stockAdjustments.dao.stopTransaction.IStopTransactionDao;
import com.param.inventory.stockAdjustments.dto.stopTransaction.ProductCategoryResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionItemExclResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionItemExclusionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionItemRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionItemResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionProductCatResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionProductExclusionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;
import com.param.inventory.stockAdjustments.service.uploadStock.IUploadStockService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StopTransactionService")
public class StopTransactionService implements IStopTransactionService {

	private static final Logger logger = Logger.getLogger(StopTransactionService.class);

	private Integer screenId = Screen.StopTransaction.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IUploadStockService UploadStockService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IStopTransactionDao StopTransactionDao;

	@Override
	@Transactional
	public Object saveAddNewStopTransaction(StopTransactionRequest request) {
		CommonResponse response = new CommonResponse();
		try {

			String startDate = request.getStartDateTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String stopDate = request.getStopDateTime();		

			Date startDateTime = formatter.parse(startDate);
			Date stopDateTime = formatter.parse(stopDate);
		
			String requestNo = UploadStockService.getPrefixRunningNumber(request.getStoreId(), request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId(), screenId);			
						
			StopTransaction	stopTransaction = new StopTransaction(requestNo,new Store(request.getStoreId()), startDateTime, stopDateTime,
					new Status(request.getStatusId()));  	
			//sessionService.setEntityDefaults(stopTransaction);	
			/*stopTransaction.setAddedBy(new User(request.getUserId()));
			stopTransaction.setUpdatedBy(new User(request.getUserId()));*/
			stopTransaction.setAddedByUser(request.getUserId());
			stopTransaction.setUpdatedByUser(request.getUserId());
			stopTransaction.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			stopTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			stopTransaction.setOrganizationId(request.getOrganizationId());
			stopTransaction.setUnitId(request.getUnitId());
			StopTransactionDao.saveAddNewStopTransaction(stopTransaction);

			response.setId(stopTransaction.getId().longValue());
			response.setMessage("Stop transaction saved successfully");

		} catch (ParseException e) {
			logger.error("Error while saving new stop transaction", e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@Override
	@Transactional
	public Object saveStopTransaction(StopTransactionRequest request) {

		StopTransaction stopTransaction = StopTransactionDao.getStopTransaction(request.getId());
		
		stopTransaction.saveStopTransaction(request.getIsVital(), request.getIsEssential(), request.getIsDiseriable(),
				request.getIsA(), request.getIsB(), request.getIsC(), request.getRemark(),
				new Status(request.getStatusId()));
		
//		  for (Long batchId : request.getBatchId()) {		  
//			  Batch batch = GlobalDao.getUomByBatchUomMapper(batchId);
//			  if(batch != null) {
//				  StopTransactionItemDetail stopTransactionItemDetail=  new StopTransactionItemDetail(new Batch(batchId), new Status(request.getStatusId()),
//						  new UomType(batch.getBatchUomList().get(0).getUomType().getId()),
//						  new UomUnit(batch.getBatchUomList().get(0).getUomUnit().getId()));
//				  sessionService.setEntityDefaults(stopTransactionItemDetail);	
//				  stopTransaction.addStopTransactionItemDetail(stopTransactionItemDetail);
//			  }		
//		  }  
//		
		
		for (StopTransactionItemRequest item : request.getItems()) {
			Boolean isExist = isStopTransactionItemDetail(request.getStopTransactionId().getFirst(),item.getBatchId());
			if(!isExist){
				StopTransactionItemDetail itemDetails=  new StopTransactionItemDetail(new Batch(item.getBatchId()), new Status(request.getStatusId()),
						new UomType(item.getUomTypeId()),new UomUnit(item.getUomUnitId()),item.getItemId());
				itemDetails.setAddedByUser(request.getUserId());
				itemDetails.setUpdatedByUser(request.getUserId());
				itemDetails.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				itemDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				itemDetails.setOrganizationId(request.getOrganizationId());
				itemDetails.setUnitId(request.getUnitId());

				
			  stopTransaction.addStopTransactionItemDetail(itemDetails);
			}
		}		
	
		for (StopTransactionProductExclusionRequest prductItem : request.getProductCategoryExclusion()) {
			if (prductItem.getProductCategoryId() != null) { 
				for (Integer productCategoryId : prductItem.getProductCategoryId()) {							
					Boolean isExist = isProductExclusionExist(request.getStopTransactionId().getFirst(),productCategoryId);
					if(!isExist){
						StopTransactionProdCatExclDetail stopTransactionProdCatExclDetailData = new StopTransactionProdCatExclDetail(
								new ProductCategory(productCategoryId), new Status(request.getStatusId()));
						stopTransactionProdCatExclDetailData.setAddedByUser(request.getUserId());
						stopTransactionProdCatExclDetailData.setUpdatedByUser(request.getUserId());
						stopTransactionProdCatExclDetailData.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						stopTransactionProdCatExclDetailData.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						stopTransactionProdCatExclDetailData.setOrganizationId(request.getOrganizationId());
						stopTransactionProdCatExclDetailData.setUnitId(request.getUnitId());
						stopTransaction.addStopTransactionProdCatExclDetail(stopTransactionProdCatExclDetailData);	
					}									
				}
			}
		}
		for (StopTransactionItemExclusionRequest itemExclusion : request.getItemExclusion()) {
			if (itemExclusion.getItemId() != null) { 
				for (Integer itemId : itemExclusion.getItemId()) { 
					StopTransactionItemExclusionDetail stopTransactionItemExclusionDetail = new StopTransactionItemExclusionDetail(
							new Item(itemId), new Status(request.getStatusId()));
					//sessionService.setEntityDefaults(stopTransactionItemExclusionDetail);	
					
				/*	stopTransactionItemExclusionDetail.setAddedBy(new User(request.getUserId()));
					stopTransactionItemExclusionDetail.setUpdatedBy(new User(request.getUserId()));*/
					stopTransactionItemExclusionDetail.setAddedByUser(request.getUserId());
					stopTransactionItemExclusionDetail.setUpdatedByUser(request.getUserId());
					stopTransactionItemExclusionDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					stopTransactionItemExclusionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					stopTransactionItemExclusionDetail.setOrganizationId(request.getOrganizationId());
					stopTransactionItemExclusionDetail.setUnitId(request.getUnitId());

									
					Boolean isExist = isItemWiseExclutionExist(request.getStopTransactionId().getFirst(),itemId);
					if(!isExist){
						stopTransaction.addStopTransactionItemExclusionDetail(stopTransactionItemExclusionDetail);
					}
				}
			}
		}

		//stopTransaction.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
		//stopTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//stopTransaction.setUpdatedBy(stopTransaction.getUser());
		stopTransaction.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(), stopTransaction.getUnitId());
		StopTransactionDao.updateStopTransaction(stopTransaction);	
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Stop transaction save successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getStopTransactionList(StopTransactionRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return StopTransactionDao.searchStopTransactionList(request, screenId);
	}

	@Override
	@Transactional
	public List<StopTransactionSearchResponse> getStopTransactionNewList(StopTransactionRequest request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<StopTransactionSearchResponse> list = new ArrayList<StopTransactionSearchResponse>();
		if(request.getVedId().isEmpty())
		{
//		for (StopTransactionSearchResponse response : StopTransactionDao.searchAllStopTransactionNewList(request,
//				screenId)) {
//			response.setAvilableQuantity(GlobalDao.getStoreStockByItem(response.getStoreId(), response.getItemId()));
//			response.setTotalMrp(response.getAvilableQuantity() * response.getMrp());
//			list.add(response);		
//			
//			}
	     list = StopTransactionDao.searchAllStopTransactionNewList(request,screenId);
		}
		else 
		{
//			for (StopTransactionSearchResponse response : StopTransactionDao.searchStopTransactionNewList(request,
//					screenId)) {
//				response.setAvilableQuantity(GlobalDao.getStoreStockByItem(response.getStoreId(), response.getItemId()));
//				response.setTotalMrp(response.getAvilableQuantity() * response.getMrp());
//				list.add(response);
				
				 list = StopTransactionDao.searchStopTransactionNewList(request,screenId); 
		}
		
		return list;
	}

	@Override
	@Transactional
	public Object getProductCategoryList(String name) {
		List<ProductCategoryResponse> ProductCategoryList = new ArrayList<ProductCategoryResponse>();		
		
		for (ProductCategory productCategory : StopTransactionDao.getProductCategoryList(name)) {

			ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse(productCategory.getId(),
					productCategory.getCode(), productCategory.getCategory(),productCategory.getAssetCategory().getCategory());
			
			ProductCategoryList.add(productCategoryResponse);
		}
		return ProductCategoryList;
	}

	@Override
	@Transactional
	public Object cancelStopTransaction(StopTransactionRequest request) {

		StopTransaction stopTransaction = StopTransactionDao.getStopTransaction(request.getId());

		stopTransaction.setCancelReason(new CancelReason(request.getCancelReasonId()));
		stopTransaction.setCancelNote(request.getCancelNote());

		stopTransaction.setStatus(new Status(request.getStatusId()));

	//	stopTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
	//	stopTransaction.setUpdatedBy(stopTransaction.getUser());
		stopTransaction.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),stopTransaction.getUnitId());
		StopTransactionDao.updateStopTransaction(stopTransaction);

		CommonResponse response = new CommonResponse();
		response.setMessage("stop transaction cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getStopTransaction(Integer id) {
		StopTransaction stopTransaction = StopTransactionDao.getStopTransaction(id);

		StopTransactionResponse response = new StopTransactionResponse(stopTransaction.getId(),
				stopTransaction.getStore().getStoreId(), stopTransaction.getStore().getStoreName(),
				stopTransaction.getStartDateTime(), stopTransaction.getStopDateTime(), stopTransaction.getIsVital(),
				stopTransaction.getIsEssential(), stopTransaction.getIsDiseriable(), stopTransaction.getIsA(),
				stopTransaction.getIsB(), stopTransaction.getIsC());	
		response.setStatusId(stopTransaction.getStatus().getId());
		response.setStatus(stopTransaction.getStatus().getStatus());
		response.setRemark(stopTransaction.getRemark()!=null ? stopTransaction.getRemark() :null);

		for (StopTransactionItemDetail detail : stopTransaction.getStopTransactionItemDetails()) {
			if(detail.getIsDeleted() == false){
			Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(stopTransaction.getStore().getStoreId(),
					detail.getBatch().getId());					
			
			response.addItems(new StopTransactionItemResponse(detail.getId(),
					detail.getBatch().getItem().getProductCategory() != null ? detail.getBatch().getItem().getProductCategory().getAssetCategory().getId() 
							: detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getId(),
					detail.getBatch().getItem().getProductCategory() != null ?	detail.getBatch().getItem().getProductCategory().getAssetCategory().getCategory() 
							: detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getCategory(),
					detail.getBatch().getItem().getProductCategory() != null ? detail.getBatch().getItem().getProductCategory().getId() 
							: detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
					detail.getBatch().getItem().getProductCategory() != null ?	detail.getBatch().getItem().getProductCategory().getCategory() 
							: detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
					detail.getBatch().getItem() != null ?	detail.getBatch().getItem().getCode() : null, 
					detail.getBatch().getItem()!=null ? detail.getBatch().getItem().getName() : null,
					detail.getUomType() != null ? detail.getUomType().getId() : 0 , detail.getUomUnit() != null ? detail.getUomUnit().getId() : 0, 
					detail.getBatch() != null ? detail.getBatch().getExpiryDate() : null,
					detail.getBatch() != null ? detail.getBatch().getBatchNo() : null, detail.getBatch() != null ? detail.getBatch().getRate() : 0, 
					detail.getBatch() != null ?	detail.getBatch().getCop() : 0,
					//detail.getBatch() != null ? detail.getBatch().getQuantity() : 0,					
					detail.getBatch() != null  ? storeStockAvailableQuantity : 0,						
					//detail.getBatch() != null ? detail.getBatch().getUomUnit().getUnits() : null, 			
					//detail.getUomType() != null ? detail.getUomType().getType() : null	
					detail.getUomUnit() != null ? detail.getUomUnit().getUnits() : null,
					detail.getUomType() != null ? detail.getUomType().getType() :null,
				    detail.getBatch() != null ? detail.getBatch().getId() : null,
				    detail.getClass() != null ? detail.getBatch().getItem().getId() : null
					));	
		}
	 }

		for (StopTransactionProdCatExclDetail productDetail : stopTransaction.getStopTransactionProdCatExclDetails()) {
			if(productDetail.getIsDeleted()== false){
				response.addProductCategoryItems(new StopTransactionProductCatResponse(productDetail.getId(),
						productDetail.getProductCategory().getAssetCategory()!=null ? 
								productDetail.getProductCategory().getAssetCategory().getCategory() : null,
						productDetail.getProductCategory().getCategory(), productDetail.getProductCategory().getId(),
						productDetail.getProductCategory().getCode()					
					));
			}			
		}

		for (StopTransactionItemExclusionDetail itemExclusionetail : stopTransaction
				.getStopTransactionItemExclusionDetails()) {
			if(itemExclusionetail.getIsDeleted()== false){
			response.addExclusionItems(new StopTransactionItemExclResponse(itemExclusionetail.getId(),
					itemExclusionetail.getItem().getProductCategory() != null ? itemExclusionetail.getItem().getProductCategory().getCategory() 
							: itemExclusionetail.getItem().getGeneric().getProductCategory().getCategory(),
					itemExclusionetail.getItem().getProductCategory() != null ? itemExclusionetail.getItem().getProductCategory().getAssetCategory().getCategory() 
							: itemExclusionetail.getItem().getGeneric().getProductCategory().getAssetCategory().getCategory(),
					itemExclusionetail.getItem() != null ? itemExclusionetail.getItem().getCode() : null, 
					itemExclusionetail.getItem() != null ? itemExclusionetail.getItem().getName() : null,
					itemExclusionetail.getItem() != null ? itemExclusionetail.getItem().getId() : null		
					));
			}
		}	 

		return response;
	}

	@Override
	@Transactional
	public Object approveStopTransaction(StopTransactionRequest request) {
		for (Integer id : request.getStopTransactionId()) {
			StopTransaction stopTransaction = StopTransactionDao.getStopTransaction(id);

			stopTransaction.setStatus(new Status(request.getStatusId()));

			stopTransaction.setApprovedDate(TimeZoneComponent.getDateByzone(stopTransaction.getUnitId()));
			stopTransaction.setApprovedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));

			stopTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(stopTransaction.getUnitId()));
			//stopTransaction.setUpdatedBy(stopTransaction.getUser());
			stopTransaction.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),stopTransaction.getUnitId());
			StopTransactionDao.updateStopTransaction(stopTransaction);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Stop Transaction approved successfully");

		return response;
	}			
	

	@Transactional
	public Boolean isProductExclusionExist(Integer stopTransactionid, Integer prodId) {
		Boolean isExit = false;
		try {
			isExit = StopTransactionDao.isProductExclusionExist(stopTransactionid, prodId);
		} catch (Exception e) {
			logger.error("Error while checking product exclusion existence", e);
			throw new ServiceException(e.getMessage());
		}
		return isExit;
	}

	@Transactional
	public Boolean isItemWiseExclutionExist(Integer stopTransactionid, Integer itemId) {
		Boolean isExit = false;
		try {
			isExit = StopTransactionDao.isItemWiseExclutionExist(stopTransactionid, itemId);
		} catch (Exception e) {
			logger.error("Error while checking item-wise exclusion existence", e);
			throw new ServiceException(e.getMessage());
		}
		return isExit;
	}
	
	@Transactional
	public Boolean isStopTransactionItemDetail(Integer stopTransactionid, Long batchId) {
		Boolean isExit = false;
		try {
			isExit = StopTransactionDao.isStopTransactionItemDetail(stopTransactionid, batchId);
		} catch (Exception e) {
			logger.error("Error while checking stop transaction item detail existence", e);
			throw new ServiceException(e.getMessage());
		}
		return isExit;
	}

	@Transactional
	public Long ChecStockInTransit(Integer storeId) {
		Long qty = 0l;
		try {
			qty = StopTransactionDao.ChecStockInTransit(storeId);
		} catch (Exception e) {
			logger.error("Error while checking stock in transit", e);
			throw new ServiceException(e.getMessage());
		}
		return qty;
	}
	
	@Transactional
	public Object CheckStopTransactionDuration(Integer storeId , Integer orgId, Integer  unitId ) {
		Object StopTransactionDuration = new Object(); 
		try {
			StopTransactionDuration = StopTransactionDao.CheckStopTransactionDuration(storeId , orgId , unitId);
		} catch (Exception e) {
			logger.error("Error while checking stop transaction duration", e);
			throw new ServiceException(e.getMessage());
		}
		return StopTransactionDuration;
	}
	//start code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	@Transactional
	public Object CheckStopTransactionDuration(StopTransactionRequest request) {
		Object StopTransactionDuration = new Object(); 
		try {
			StopTransactionDuration = StopTransactionDao.CheckStopTransactionDuration(request);
		} catch (Exception e) {
			logger.error("Error while checking stop transaction duration", e);
			throw new ServiceException(e.getMessage());
		}
		return StopTransactionDuration;
	}
	//end code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	
	@Transactional
	public Object CheckStopTransactionItemWise(StopTransactionRequest request) {
		Object StopTransactionDuration = new Object(); 				
		try {		 
			StopTransactionDuration = StopTransactionDao.CheckStopTransactionItemWise(request.getStoreId(),request.getItemIds(),  request);
		} catch (Exception e) {
			logger.error("Error while checking stop transaction item-wise", e);
			throw new ServiceException(e.getMessage());
		}
		return StopTransactionDuration;
	}
	
	
	@Transactional
	public Object CheckStopTransactionBatchWise(StopTransactionRequest request) {
		Object StopTransactionDuration = new Object(); 				
		try {		 
			StopTransactionDuration = StopTransactionDao.CheckStopTransactionBatchWise(request.getStoreId(),request.getBatchId(), request);
		} catch (Exception e) {
			logger.error("Error while checking stop transaction batch-wise", e);
			throw new ServiceException(e.getMessage());
		}
		return StopTransactionDuration;
	}

	@Transactional
	public Object sendStopTransactionAlert(Integer storeId , Integer unitId) {		
		try{
			Store store = StopTransactionDao.getStoreForAlert(storeId,unitId);
			//store.setIsStopTransaction(true);
			//StopTransactionDao.updateStoreforAlert(store);

			CommonResponse response = new CommonResponse();
			response.setMessage("Stop transaction alert send successfully");

			return response;
		}catch(Exception e){
			logger.error("Error while sending stop transaction alert", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	public Object removeItemsByProductId(Integer ProductId,Integer stopTransactionId, StopTransactionRequest request) {		
		try{			
			List<StopTransactionItemDetail> stopTransactionItemDetail = StopTransactionDao.getStopTransactionItemDetail(ProductId,stopTransactionId);
			
			for(StopTransactionItemDetail detail : stopTransactionItemDetail){				
				detail.setIsDeleted(true);		
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(detail.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());	
				
			   StopTransactionDao.updateStopTransactionItemDetail(detail);	
			}	
							
			CommonResponse response = new CommonResponse();
			response.setMessage("Stop transaction alert send successfully");

			return response;
		}catch(Exception e){
			logger.error("Error while removing items by product id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	public Object removeItembyItemId(StopTransactionRequest request) {		
		try {	
			List<StopTransactionItemDetail> stopTransactionItemDetail  = StopTransactionDao.getStopTransactionItemDetailByItemId(request.getItemIds(),request.getStopTransactionId().getFirst());
			  
			for(StopTransactionItemDetail detail : stopTransactionItemDetail){				
				detail.setIsDeleted(true);		
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(detail.getUnitId()));
			//	detail.setUpdatedBy(detail.getUser());	
				detail.setUpdatedByUser(request.getUserId());	

				
			   StopTransactionDao.updateStopTransactionItemDetail(detail);	
			}	
							
			CommonResponse response = new CommonResponse();
			response.setMessage("Stop transaction alert send successfully");

			return response;
		}catch(Exception e){
			logger.error("Error while removing item by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
	@Override
	@Transactional
	public List<BigInteger> CheckStopTransactionDetail(StopTransactionRequest request) {
		try {
			return StopTransactionDao.CheckStopTransactionDetail(request);
			}catch(Exception e){
				logger.error("Error while checking stop transaction detail", e);
				throw new ServiceException(e.getMessage());
			}
	}
	/* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */

	@Override
	@Transactional
	public Object getStopTransactionNonZeroNewList(StopTransactionRequest request) {


		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<StopTransactionSearchResponse> list = new ArrayList<StopTransactionSearchResponse>();
		if(request.getVedId().isEmpty())
		{
//		for (StopTransactionSearchResponse response : StopTransactionDao.searchAllStopTransactionNewList(request,
//				screenId)) {
//			response.setAvilableQuantity(GlobalDao.getStoreStockByItem(response.getStoreId(), response.getItemId()));
//			response.setTotalMrp(response.getAvilableQuantity() * response.getMrp());
//			list.add(response);		
//			
//			}

	     list = StopTransactionDao.searchAllStopTransactionNonZeroNewList(request,screenId);
	     List<UomResponse> uomListAll=GlobalDao.getAllUomListAll(request.getOrganizationId());
	     for (StopTransactionSearchResponse stopTransactionSearchResponse : list) {
	            Stream<UomResponse> uomListStream = uomListAll.stream();
	            List<UomResponse> uomList=uomListStream
	            .filter(str -> str.getItemId()!=null && str.getItemId().equals(stopTransactionSearchResponse.getItemId()))
	            .collect(Collectors.toList());
	    	 stopTransactionSearchResponse.setUom(uomList);
	     }
		}
		else 
		{
//			for (StopTransactionSearchResponse response : StopTransactionDao.searchStopTransactionNewList(request,
//					screenId)) {
//				response.setAvilableQuantity(GlobalDao.getStoreStockByItem(response.getStoreId(), response.getItemId()));
//				response.setTotalMrp(response.getAvilableQuantity() * response.getMrp());
//				list.add(response);
				
				 list = StopTransactionDao.searchStopTransactionNonZeroNewList(request,screenId); 
				 for (StopTransactionSearchResponse stopTransactionSearchResponse : list) {
			    	 stopTransactionSearchResponse.setUom( GlobalDao.getUnitOfMeasurementListByItem(stopTransactionSearchResponse.getItemId(), request.getOrganizationId()));
			     }
		}
		
		return list;
	
	}
}
