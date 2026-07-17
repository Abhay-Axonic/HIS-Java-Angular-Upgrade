package com.param.inventory.stockAdjustments.service.uploadStock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.UomDto;
import com.param.global.service.global.IGlobalService;
import com.param.inventory.stockAdjustments.dao.uploadStock.IUploadStockDao;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequestItem;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponseItem;
import com.param.pharmacy.master.global.dao.item.IItemDao;
import com.param.pharmacy.master.global.dao.uomUnit.IUomUnitDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
public class UploadStockService implements IUploadStockService {

	private static final Logger logger = Logger.getLogger(UploadStockService.class);

	private Integer screenId = Screen.UploadStock.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IUploadStockDao UploadStockDao;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IItemDao iItemDao;
	
	@Autowired
	IUomUnitDao iUomUnitDao;
	
	@Override
	@Transactional
	public StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId) {

		StockAdjustmentDropdownResponse response = new StockAdjustmentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getUploadStockList(UploadStockRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return UploadStockDao.searchUploadStockList(request, screenId);
	}

	@Override
	@Transactional
	public UploadStockResponse getUploadStockDetails(Integer id , Integer unitId) {

		UploadStock uploadStock = UploadStockDao.getUploadStockWithUnitId(id, unitId);
		UploadStockResponse response = new UploadStockResponse(uploadStock.getId(), uploadStock.getUploadNo(),
				uploadStock.getAddedDate(), uploadStock.getStopTransaction()!=null ?uploadStock.getStopTransaction().getRequestNo():null,
						uploadStock.getStopTransaction()!=null ?uploadStock.getStopTransaction().getStartDateTime():null, uploadStock.getStopTransaction()!=null ? 
								uploadStock.getStopTransaction().getStopDateTime():null,
				uploadStock.getAddedBy().getUserName(), uploadStock.getStore().getStoreName(),
				uploadStock.getStore().getStoreId(), uploadStock.getStatus().getStatus(),
				uploadStock.getStatus().getId(), uploadStock.getApprovalStatus().getStatus(),
				uploadStock.getApprovalStatus().getId());
		response.setRemark(uploadStock.getRemark());

		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();
		
		List<UploadStockDetail> uploadDetails = uploadStock.getUploadStockDetails();
//		Comparator<UploadStockDetail> compareByCOP = (UploadStockDetail o1, UploadStockDetail o2) -> o1.getBatch().getCop().compareTo(o2.getBatch().getCop());
//		Collections.sort(uploadDetails, compareByCOP.reversed());
		
		for (UploadStockDetail uploadStockDetail : uploadDetails) {		
		
			UploadStockResponseItem item = new UploadStockResponseItem(uploadStockDetail.getId(),
					uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
					uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getId() :uploadStockDetail.getBatch().getItem().getProductCategory().getAssetCategory().getId(),
					uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
					uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getCategory() : uploadStockDetail.getBatch().getItem().getProductCategory().getAssetCategory().getCategory(),
					uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
					uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getId() :uploadStockDetail.getBatch().getItem().getProductCategory().getId(),
					uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
					uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getCategory() : uploadStockDetail.getBatch().getItem().getProductCategory().getCategory(),
					uploadStockDetail.getBatch().getItem().getId(), uploadStockDetail.getBatch().getItem().getCode(),
					uploadStockDetail.getBatch().getItem().getName(), uploadStockDetail.getUomType()!=null ? uploadStockDetail.getUomType().getId():null,
					uploadStockDetail.getUomType()!=null ? uploadStockDetail.getUomType().getType():null,
					uploadStockDetail.getUomUnit()!=null ? uploadStockDetail.getUomUnit().getId():null,
					uploadStockDetail.getUomUnit()!=null ? uploadStockDetail.getUomUnit().getUnits():null, 
					uploadStockDetail.getBatch().getId(),
					uploadStockDetail.getBatch().getBatchNo(), uploadStockDetail.getBatch().getExpiryDate()!=null ? uploadStockDetail.getBatch().getExpiryDate():null,
					uploadStockDetail.getBatch().getRate(), uploadStockDetail.getQty(),
					uploadStockDetail.getStatus().getId(), uploadStockDetail.getStatus().getStatus(),
					uploadStockDetail.getApprovedUploadQuantity(),	
					uploadStockDetail.getUploadStock().getStopTransaction()!=null ?uploadStockDetail.getUploadStock().getStopTransaction().getId():null,
					uploadStockDetail.getBatch().getCop(), uploadStockDetail.getBatch().getItem().getIsThreeUOMApplicable(),
					GlobalDao.getUnitOfMeasurementListByItem(uploadStockDetail.getBatch().getItem().getId(),uploadStock.getOrganizationId())
				 	);
			item.setVarianceQty(uploadStockDetail.getVarianceQty()!=null ? uploadStockDetail.getVarianceQty() :null);
			item.setVarianceCop(uploadStockDetail.getVarianceCop()!=null ? uploadStockDetail.getVarianceCop() :null);
			item.setVarianceMrp(uploadStockDetail.getVarianceMrp()!=null ? uploadStockDetail.getVarianceMrp() :null);
			if(uploadStockDetail.getIsActive()==true)
			{
				items.add(item);
			}

		}
		if(uploadDetails != null && uploadDetails.size()>0) {
			response.setAsseteTypeId(uploadDetails.getFirst().getBatch().getItem().getGeneric()!=null ? uploadDetails.getFirst().getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getAssetType().getId() 
					: uploadDetails.getFirst().getBatch().getItem().getProductCategory().getAssetCategory().getAssetType().getId());
		}
		Collections.sort(items, (s1, s2) ->
	    Integer.compare(s2.getId(), s1.getId()));
		Collections.reverse(items);
		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object saveUploadStock(UploadStockRequest request) {
		
		String indentNumber = getPrefixRunningNumber(request.getStoreId(), request.getUnitId(), screenId);		
		if(indentNumber == null)
			indentNumber = "UploadStock1";			
			
		UploadStock uploadStock = new UploadStock(new Status(request.getStatusId()),
				request.getStopTransactionId()!=null ? new StopTransaction(request.getStopTransactionId()) :null, new Store(request.getStoreId()), indentNumber,
				new Status(request.getApprovalStatusId()));
		//sessionService.setEntityDefaults(uploadStock);
		/*uploadStock.setAddedBy(new User(request.getUserId()));
		uploadStock.setUpdatedBy(new User(request.getUserId()));*/
		uploadStock.setAddedByUser(request.getUserId());
		uploadStock.setUpdatedByUser(request.getUserId());
		
		uploadStock.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		uploadStock.setOrganizationId(request.getOrganizationId());
		uploadStock.setId(request.getId()!=null? request.getId(): null);
		uploadStock.setUnitId(request.getUnitId());
		uploadStock.setRemark(request.getRemark());
		if(request.getId()!=null)
		{
			UploadStockDao.inactiveOldItems(request.getId());
		}
		for (UploadStockRequestItem item : request.getItems()) {

			UploadStockDetail	uploadStockDetail=new UploadStockDetail(new Batch(item.getBatchId()), item.getQuantity(),
					item.getUomTypeId()!=null ? new UomType(item.getUomTypeId()) :null,item.getUomUnitId()!=null ? new UomUnit(item.getUomUnitId()) :null,
					new Status(item.getStatusId()),item.getUploadedQuantity());
			//sessionService.setEntityDefaults(uploadStockDetail);
			/*uploadStockDetail.setAddedBy(new User(request.getUserId()));
			uploadStockDetail.setUpdatedBy(new User(request.getUserId()));*/
			uploadStockDetail.setAddedByUser(request.getUserId());
			uploadStockDetail.setUpdatedByUser(request.getUserId());
			uploadStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			uploadStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			uploadStockDetail.setOrganizationId(request.getOrganizationId());
			uploadStockDetail.setUnitId(request.getUnitId());
 			uploadStockDetail.setVarianceQty(item.getVarianceQty()!=null ? item.getVarianceQty() :null);
 			uploadStockDetail.setVarianceCop(item.getVarianceCop()!=null ? item.getVarianceCop() :null);
 			uploadStockDetail.setVarianceMrp(item.getVarianceMrp()!=null ? item.getVarianceMrp() :null);
 			uploadStockDetail.setId(item.getId()!=null ? item.getId():null);
			uploadStock.addUploadStockDetail(uploadStockDetail);

		}
		UploadStockDao.saveUploadStock(uploadStock);

		CommonResponse response = new CommonResponse();
		response.setId(uploadStock.getId().longValue());
		response.setMessage("upload stock saved successfully");

		return response;
	}

	@Override
	@Transactional
	public List<UploadStockResponse> getUploadStockItemList(UploadStockRequest request) {

		List<UploadStockResponse> items = new ArrayList<UploadStockResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (StopTransaction stopTransaction : UploadStockDao.getStopTransactionItems(request)) {
			
			if (stopTransaction.getStatus().getStatus().equals("Pending")) {
				UploadStockResponse item = new UploadStockResponse(stopTransaction.getId(),
						stopTransaction.getRequestNo(), stopTransaction.getAddedDate(),
						stopTransaction.getStartDateTime(), stopTransaction.getStopDateTime(),
						stopTransaction.getAddedBy().getUserName(), stopTransaction.getStore().getStoreName(),
						stopTransaction.getStore().getStoreId(), stopTransaction.getStatus().getStatus(),
						stopTransaction.getStatus().getId());
				items.add(item);
			}
		}
		return items;
	}

	@Override
	@Transactional
	public UploadStockResponse getuploadStockAddItemList(UploadStockRequest request) {

		UploadStockResponse response = new UploadStockResponse();
		StopTransaction master = UploadStockDao.getStopTransaction(request);
		response.setRequestNo(master.getRequestNo());

		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();

		List<StopTransactionItemDetail> details = UploadStockDao.getStopTransactionItemsDetails(request);

		for (StopTransactionItemDetail detail : details) {
			
			Integer storeStockAvailableQuantity = UploadStockDao.getStoreStockByBatch(detail.getStopTransaction().getStore().getStoreId(),
					detail.getBatch().getId());	
			
			 UploadStockResponseItem item = new UploadStockResponseItem(detail.getId(),
						detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getId(),
						detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getCategory(),
						detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
						detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
						detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
						detail.getBatch().getItem().getName(),
						detail.getUomType() != null ? detail.getUomType().getId() : 0,
						detail.getUomType() != null ? detail.getUomType().getType() : null,
						detail.getUomUnit() != null ? detail.getUomUnit().getId() : 0,
						detail.getUomUnit() != null ? detail.getUomUnit().getUnits() : null,
						detail.getBatch() != null ? detail.getBatch().getId() : 0,
						detail.getBatch() != null ? detail.getBatch().getBatchNo() : null,
						detail.getBatch() != null ? detail.getBatch().getExpiryDate() : null,
						detail.getBatch() != null ? detail.getBatch().getRate() : null,
//						detail.getBatch() != null ? detail.getBatch().getQuantity() : 0,
//						detail.getBatch() != null ? detail.getBatch().getQuantity() : 0
						detail.getBatch() != null ? storeStockAvailableQuantity : 0, 
						detail.getBatch() != null ? storeStockAvailableQuantity : 0,
						detail.getBatch() != null ? detail.getBatch().getCop() : null	
						);
				
				items.add(item);
			}					
		
		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object cancelUploadStock(UploadStockRequest request) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(request.getId());

		for (UploadStockDetail detail : uploadStock.getUploadStockDetails()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			/*detail.setUpdatedBy(uploadStock.getUser());*/
			detail.setUpdatedByUser(request.getUserId());
		}
		uploadStock.setCancelReason(new CancelReason(request.getCancelReasonId()));
		uploadStock.setCancelNote(request.getCancelNote());

		uploadStock.setStatus(new Status(request.getStatusId()));
        uploadStock.setApprovalStatus(new Status(request.getApprovalStatusId()));
		uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
		//uploadStock.setUpdatedBy(uploadStock.getUser());
		uploadStock.setUpdatedByUser(request.getUserId());

		UploadStockDao.updateUploadStock(uploadStock);

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload Stock cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveUploadStock(UploadStockRequest request) {

		for (Integer id : request.getUploadStockId()) {

			UploadStock uploadStock = UploadStockDao.getUploadStockWithUnitId(id, request.getUnitId());

			uploadStock.setStatus(new Status(request.getStatusId()));
			uploadStock.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (UploadStockDetail detail : uploadStock.getUploadStockDetails()) {

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			//	detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}

			uploadStock.setApprovedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			uploadStock.setApprovedBy(new User(request.getUserId()));

			uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
		//	uploadStock.setUpdatedBy(uploadStock.getUser());
			uploadStock.setUpdatedByUser(request.getUserId());

			UploadStockDao.updateUploadStock(uploadStock);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload Stock approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateUploadStock(UploadStockRequest request) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(request.getId());
		for (UploadStockDetail detail : uploadStock.getUploadStockDetails()) {
			Integer id = null;

			for (UploadStockRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					detail.updateUploadStockDetail(item.getQuantity());
					
					/* start change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
					detail.setApprovedUploadQuantity(item.getUploadedQuantity());
					/* end change as per discuss with Satishbhai and Dilipbhai for editable upload stock */
				}

			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			//detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}
		UploadStockDao.updateUploadStock(uploadStock);

		CommonResponse response = new CommonResponse();
		response.setId(uploadStock.getId().longValue());
		response.setMessage("Upload Stock updated successfully");

		return response;
	}	
	
	
	
	public  String getPrefixRunningNumber(Integer storeId,int unit, Integer screenId) {

		String prefixRunningNumber = UploadStockDao.getPrefixRunningNumber(storeId, unit ,screenId);

		return prefixRunningNumber;
	}
	
	@Override
	@Transactional
	public UploadStockResponse getuploadStockAddItemListOldBatch(UploadStockRequest request) {

		UploadStockResponse response = new UploadStockResponse();
		StopTransaction master = UploadStockDao.getStopTransaction(request);
		response.setRequestNo(master.getRequestNo());

		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();

		List<StopTransactionItemDetail> details = UploadStockDao.getStopTransactionItemsDetailsOldBatch(request);

		for (StopTransactionItemDetail detail : details) {
			
			Integer storeStockAvailableQuantity = UploadStockDao.getStoreStockByBatch(detail.getStopTransaction().getStore().getStoreId(),
					detail.getBatch().getId());			
							
			UploadStockResponseItem item = new UploadStockResponseItem(detail.getId(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getId(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getCategory(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
					detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
					detail.getBatch().getItem().getName(),
					detail.getUomType() != null ? detail.getUomType().getId() : 0,
					detail.getUomType() != null ? detail.getUomType().getType() : null,
					detail.getUomUnit() != null ? detail.getUomUnit().getId() : 0,
					detail.getUomUnit() != null ? detail.getUomUnit().getUnits() : null,
					detail.getBatch() != null ? detail.getBatch().getId() : 0,
					detail.getBatch() != null ? detail.getBatch().getBatchNo() : null,
					detail.getBatch() != null ? detail.getBatch().getExpiryDate() : null,
					detail.getBatch() != null ? detail.getBatch().getRate() : null,
					detail.getBatch() != null ? storeStockAvailableQuantity : 0,
					detail.getBatch() != null ? storeStockAvailableQuantity : 0,
				    detail.getBatch() != null ? detail.getBatch().getCop() : null	
					);
			
				items.add(item);
			}		
		response.setItems(items);
		return response;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public Response readCSVFile(UploadStockRequest dto) throws ApplicationException {
		try {
			
			

			InputStreamReader reader=new InputStreamReader(dto.getFile().getInputStream());
		    BufferedReader br =new BufferedReader(reader);  
		    String line="";
		    List<ItemListResponse> itemlist = new ArrayList<>();
		    
		    Response res = iItemDao.getItemIdAndName(dto.getOrganizationId());
		    
		    if(res.getListObject() != null && res.getListObject().size()>0) {
		    	itemlist.addAll(res.getListObject());
		    }
		    
		    List<UomUnit> uomUnitList =  iUomUnitDao.getAll(dto.getOrganizationId());
		    
		    List<UomDto> uomTypeList = new ArrayList<>();
		    Response tyepRes = iItemDao.getUomType(dto.getOrganizationId()); 
		    if(tyepRes.getListObject() != null && tyepRes.getListObject().size()>0) {
		    	uomTypeList.addAll(tyepRes.getListObject());
		    }
		    //count variable
		    int count = 0;
		    Integer successCount=0,failureCount=0;
		    
		    
		    String indentNumber = getPrefixRunningNumber(dto.getStoreId(), dto.getUnitId(), screenId);		
			if(indentNumber == null)
				indentNumber = "UploadStock1";			
				
			UploadStock uploadStock = new UploadStock(new Status(dto.getStatusId()),
					new StopTransaction(dto.getStopTransactionId()), new Store(dto.getStoreId()), indentNumber,
					new Status(dto.getApprovalStatusId()));
			uploadStock.setAddedByUser(dto.getUserId());
			uploadStock.setUpdatedByUser(dto.getUserId());
			
			uploadStock.setAddedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			uploadStock.setOrganizationId(dto.getOrganizationId());
			uploadStock.setUnitId(dto.getUnitId());

		String errorMessages = "";
		    int i= 0;
		    //read line by line.
		    while((line = br.readLine())!=null) 
		    {
		    	
		    	//after reading line at once, split by , bz, csv format & store in String[].
		    	String [] strServices=line.trim().split(",");
		    	
		    	//count++
		    	count++;
		    	
		    	if(count>1) 
		    	{    
		    		List<BatchDto>  batchList1 = new ArrayList<>();
		    		List<ItemListResponse> itemDtoList = new ArrayList<>();
		    		Stream<ItemListResponse> itemStream = itemlist.stream();
		    		itemDtoList = itemStream.filter(str -> str.getItemName().toLowerCase().equals(strServices[4].trim().toLowerCase())).collect(Collectors.toList());
		    		if(itemDtoList != null && itemDtoList.size()>0) {
		    			
		    			Response BatchRes =  iItemDao.getBatchByItemId(itemDtoList.getFirst().getItemId(),dto.getStoreId());
		    			
		    			if(BatchRes.getListObject() != null && BatchRes.getListObject().size()>0) {
		    				List<BatchDto>  batchList = new ArrayList<>();
		    				
		    				batchList.addAll(BatchRes.getListObject());
		    				Stream<BatchDto> batchStream = batchList.stream();
		    				batchList1 = batchStream.filter(str ->  (str.getBatchNo()!= null ?str.getBatchNo().toLowerCase(): "").equals(strServices[7].trim().toLowerCase())).collect(Collectors.toList());

		    			}else {
		    				errorMessages =  "for "+strServices[4].trim() +" Batch is not found ";
			    			 return new Response("error","500", errorMessages, null, null);
		    			}
		    		}else {
		    			 errorMessages =  strServices[4].trim() +" item is not found ";
		    			 return new Response("error","500", errorMessages, null, null);
		    		}
		    		  List<UomUnit> uomUnit = new ArrayList<>();
		    		  Stream<UomUnit> uomUnitStream = uomUnitList.stream();
		    		  uomUnit = uomUnitStream.filter(str ->(str.getUnits()!= null ? str.getUnits().toLowerCase() : "").equals(strServices[6].trim().toLowerCase())).collect(Collectors.toList());
		    		  if(uomUnit != null && uomUnit.size()>0) {
		    			  
		    		  }else {
		    			  	 errorMessages =  "for "+strServices[4].trim() +" Uom Unit is not found ";
			    			 return new Response("error","500", errorMessages, null, null);
		    		  }
		    		  
		    		  List<UomDto> uomType = new ArrayList<>();	
		    		  Stream<UomDto> uomTypeStream = uomTypeList.stream();
		    		  uomType = uomTypeStream.filter(str ->  str.getUomType().toLowerCase().equals(strServices[5].trim().toLowerCase())).collect(Collectors.toList());
		    		 
		    		  if(uomType != null && uomType.size()>0) {
		    			  
		    		  }else {
		    			  	 errorMessages =  "for "+strServices[4].trim() +" Uom Type is not found ";
			    			 return new Response("error","500", errorMessages, null, null);
		    		  }
		    		  
		    		  UploadStockDetail	uploadStockDetail=new UploadStockDetail(new Batch(batchList1.getFirst().getLongBatchId().longValue()),
								new UomType(uomType.getFirst().getUomTypeId()), new UomUnit(uomUnit.getFirst().getId()),
								new Status(dto.getStatusId()));
		    		  	uploadStockDetail.setQty(Integer. parseInt(strServices[11].trim()));
		    		  	uploadStockDetail.setApprovedUploadQuantity(Integer. parseInt(strServices[12].trim()));
						uploadStockDetail.setAddedByUser(dto.getUserId());
						uploadStockDetail.setUpdatedByUser(dto.getUserId());
						uploadStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						uploadStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						uploadStockDetail.setOrganizationId(dto.getOrganizationId());
						uploadStockDetail.setUnitId(dto.getUnitId());
						uploadStock.addUploadStockDetail(uploadStockDetail);
						successCount++;
		    	}
		    }
		    
		    //***********Save service master call****************
	       if(uploadStock.getUploadStockDetails().size()>0) 
	       {
	    	     UploadStockDao.saveUploadStock(uploadStock);
	       }
	       UploadStockRequest finalDto=new UploadStockRequest();
	       finalDto.setSuccessCount(successCount);
	       finalDto.setFailureCount(failureCount);
	       
	       
		   return new Response("success","200", "Successfully Uploaded", null, finalDto);
		
		} catch (Exception e) {
			logger.error("Error while reading upload stock CSV file", e);
			throw new ServiceException(e.getMessage());
		}
	}
		
}
