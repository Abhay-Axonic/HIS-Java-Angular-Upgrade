package com.param.procurement.consignment.service.consignment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ConsignmentOutward;
import com.param.entity.model.procurement.ConsignmentOutwardDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.consignment.IConsignmentOutwordDao;
import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequestItems;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponseItems;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ConsignmentOutwordService")
public class ConsignmentOutwordService implements IConsignmentOutwordService {
	
	@Autowired 
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService  GlobalService ;
	
	@Autowired
	IConsignmentOutwordDao ConsignmentOutwordDao;

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.ConsignmentOutward.getScreen();
	
	@Override
	@Transactional
	public ConsignmentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		ConsignmentDropdown response = new ConsignmentDropdown();
		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId))
		{
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		return response;
	}
	
	@Override
	@Transactional
	public List<ConsignmentResponse> getConsignmentOutwardList(ConsignmentRequest request)
	{
		List<ConsignmentResponse> responses = new ArrayList<ConsignmentResponse>();
		//request.setFromDate((new DateTime(request.getFromDate())).plusDays(1).toDate());
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		
		for (ConsignmentOutward consignmentOutward : ConsignmentOutwordDao.getConsignmentOutwardList(request, screenId))
		{
			ConsignmentResponse consignmentResponse = new ConsignmentResponse(
			consignmentOutward.getId(),consignmentOutward.getOutwardNumber(),consignmentOutward.getAddedDate(),
			consignmentOutward.getStore().getStoreId(),consignmentOutward.getStore().getStoreName(),
			consignmentOutward.getVendor().getName(),consignmentOutward.getVendor().getId(),
			consignmentOutward.getRemark(),consignmentOutward.getAddedBy().getUserName(),
			consignmentOutward.getAddedBy().getUserId(),consignmentOutward.getStatus().getStatus(),
			consignmentOutward.getStatus().getId());
			
			responses.add(consignmentResponse);
		}
		return responses;
	}	
		  
  	@Override
	@Transactional
	public Object saveConsignmentOutward(ConsignmentRequest request)
  	{	
		/*Integer storeId = request.getStoreId();
		Store storeNew = GlobalDao.getStore(storeId);
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		String consignmentOutNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		
				ConsignmentOutward consignmentOutward = new ConsignmentOutward(consignmentOutNumber, new Store(request.getStoreId()),
				new Vendor(request.getVendorId()),request.getDcDate(), request.getDcNo(),new Status(request.getStatusId()),
				request.getTotalAmount(),request.getTotalTaxAmount(), request.getTotalNetAmount(),request.getRemark());
				
		for (ConsignmentRequestItems item : request.getItems())
		{
			
			UomType uomType = null;
            UomUnit uomUnit = null;

            if(item.getUomTypeId() != null)
                uomType = new UomType(item.getUomTypeId());

            if(item.getUomUnitId() != null)
                uomUnit = new UomUnit(item.getUomUnitId());
			
			ConsignmentOutwardDetail detail = null;
			detail=new ConsignmentOutwardDetail(new Batch(item.getBatchId()),item.getQuantity(),new Tax(item.getTaxId()),
					item.getTaxPercentage(),item.getTotalTaxAmount(),item.getTotalNetAmount(),item.getCop(),
					uomUnit,uomType, item.getLcop()!=null ? item.getLcop() : 0.0);
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			consignmentOutward.addConsignmentOutwardDetail(detail);
		}
//		sessionService.setEntityDefaults(consignmentOutward);
		consignmentOutward.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		ConsignmentOutwordDao.saveConsignmentOutward(consignmentOutward);
		CommonResponse response = new CommonResponse();
		response.setId(consignmentOutward.getId().longValue());
		response.setMessage("Consignment Outward saved successfully");
		return response;
	
  	}
  	
  	@Override
  	@Transactional
	public ConsignmentResponse getConsignmentOutwardDetails(Integer id,Integer unitId)
  	{
		ConsignmentOutward consignmentOutward = ConsignmentOutwordDao.getConsignmentOutwardDetails(id,unitId);
		ConsignmentResponse consignmentResponse = new ConsignmentResponse(consignmentOutward.getId(),consignmentOutward.getOutwardNumber(),
				consignmentOutward.getAddedDate(),consignmentOutward.getDcDate(),consignmentOutward	.getDcNumber(),
				consignmentOutward.getStore().getStoreName(),consignmentOutward.getStore().getStoreId(),
				consignmentOutward.getVendor().getName(),consignmentOutward.getVendor().getId(),consignmentOutward.getRemark(),
				consignmentOutward.getAddedBy().getUserName(),consignmentOutward.getAddedBy().getUserId(),
				consignmentOutward.getStatus().getStatus(),consignmentOutward.getStatus().getId(),
				consignmentOutward.getTotalAmount(),consignmentOutward.getNetAmount(),consignmentOutward.getTotalTax());
	
		for (ConsignmentOutwardDetail consignmentOutwardDetail : consignmentOutward.getConsignmentOutwardDetails())
		{
			Long storeStock = GlobalDao.getStoreStockByItem(consignmentOutward.getStore().getStoreId(),
			consignmentOutwardDetail.getBatch().getItem().getId(),unitId);
			Long availableStock = Util.ConvertQuantityByUom(storeStock, consignmentOutwardDetail.getBatch().getItem().getUnitOfMeasurementList(), consignmentOutwardDetail.getUomType());
			
			ConsignmentResponseItems item = null;
			item = new ConsignmentResponseItems(consignmentOutwardDetail.getId(),
					consignmentOutwardDetail.getBatch().getItem().getId(),consignmentOutwardDetail.getBatch().getItem().getCode(),
					consignmentOutwardDetail.getBatch().getItem().getName(),
					consignmentOutwardDetail.getUomType() != null ? consignmentOutwardDetail.getUomType().getId() : null,consignmentOutwardDetail.getUomType() != null ? consignmentOutwardDetail.getUomType().getType() : null,
					consignmentOutwardDetail.getUomUnit() != null ? consignmentOutwardDetail.getUomUnit().getId() : null,consignmentOutwardDetail.getUomUnit() != null ? consignmentOutwardDetail.getUomUnit().getUnits() : null,
					consignmentOutwardDetail.getBatch().getId(),consignmentOutwardDetail.getBatch().getBatchNo(),
					consignmentOutwardDetail.getBatch().getExpiryDate(),consignmentOutwardDetail.getReturnQunatity(),
					consignmentOutwardDetail.getCop(), consignmentOutwardDetail.getTotalAmount(),consignmentOutwardDetail.getTax().getId(),
					consignmentOutwardDetail.getTaxPercentage(),consignmentOutwardDetail.getTaxAmount(),
					consignmentOutwardDetail.getNetAmount(),availableStock, consignmentOutwardDetail.getLcop());
			
				// batchproperty=new BatchProperty();
				//batchproperty.setIsReturn(false);
				List<Batch> batchList = GlobalDao.getBatchDetails(consignmentOutwardDetail.getBatch().getItem().getId(), consignmentResponse.getStoreId(),unitId);
			
				if (!batchList.isEmpty())
				{
					//Batch batch=new Batch();
					for (Batch batch : batchList)
					{
						item.addBatches(Util.PopulateUom(batch));
					}
				}	
				consignmentResponse.addItems(item);
		}
		
		return consignmentResponse;
  	}
  	
	@Override
	@Transactional
	public Object updateConsignmentOutword(ConsignmentRequest request)
	{
		ConsignmentOutward consignmentOutward = ConsignmentOutwordDao.getConsignmentOutwardDetails(request.getId(),request.getUnitId());
		
		for (ConsignmentOutwardDetail detail : consignmentOutward.getConsignmentOutwardDetails())
		{
			Integer id = null;
			for (ConsignmentRequestItems item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId()))
				{
					id = item.getId();
					
					UomType uomType = null;
		            UomUnit uomUnit = null;

		            if(item.getUomTypeId() != null)
		                uomType = new UomType(item.getUomTypeId());

		            if(item.getUomUnitId() != null)
		                uomUnit = new UomUnit(item.getUomUnitId());
					
					detail.updateConsignmentOutwardDetail(item.getId(), new Batch(item.getBatchId()), item.getQuantity(),uomType,
							uomUnit,item.getCop(),new Tax(item.getTaxId()),item.getTaxPercentage(),
							item.getTotalAmount(),item.getTaxAmount(),item.getNetAmount());
				}
				break;
			}
			if (id == null)
			{
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
			detail.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
		}
		
		for (ConsignmentRequestItems item : request.getItems())
		{
			if (item.getId() == null)
			{
				ConsignmentOutwardDetail detail = null;
				
				UomType uomType = null;
	            UomUnit uomUnit = null;

	            if(item.getUomTypeId() != null)
	                uomType = new UomType(item.getUomTypeId());

	            if(item.getUomUnitId() != null)
	                uomUnit = new UomUnit(item.getUomUnitId());
				
				detail = new ConsignmentOutwardDetail(item.getId(), new Batch(item.getBatchId()), item.getQuantity(),uomType,
						uomUnit,item.getCop(),new Tax(item.getTaxId()),item.getTaxPercentage(),
						item.getTotalAmount(),item.getTaxAmount(),item.getNetAmount());
//				sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				consignmentOutward.addConsignmentOutwardDetail(detail);
			}
		}
		consignmentOutward.setId(request.getId());
		consignmentOutward.setDcNumber(request.getDcNo());
		consignmentOutward.setDcDate(request.getDcDate());
		consignmentOutward.setVendor(new Vendor(request.getVendorId()));
		consignmentOutward.setTotalAmount(request.getTotalAmount());
		consignmentOutward.setTotalTax(request.getTotalTaxAmount());
		consignmentOutward.setNetAmount(request.getTotalNetAmount());
		consignmentOutward.setRemark(request.getRemark());
		ConsignmentOutwordDao.updateConsignmentOutword(consignmentOutward);

		CommonResponse response = new CommonResponse();
		response.setId(consignmentOutward.getId().longValue());
		response.setMessage("Consignment Outward updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveConsignmentOutword(ConsignmentRequest request)
	{
		for (Integer consOutwId : request.getConsOutwId())
		{
			ConsignmentOutward consignmentOutward = ConsignmentOutwordDao.getConsignmentOutwardDetails(consOutwId,request.getUnitId());
			
			Integer storeId = consignmentOutward.getStore().getStoreId();
			
			Integer vendorId = consignmentOutward.getVendor().getId();
			
			for (ConsignmentOutwardDetail details : consignmentOutward.getConsignmentOutwardDetails()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					//Batch batch = InsertApprovedItems(details, storeId, vendorId);
					//ConsignmentOutwordDao.saveBatch(batch);
					details.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),request.getUserId());
					details.setBatch(InsertApprovedItems(details, storeId, vendorId,request.getUnitId()));
					details.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
					details.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
				}
			}
			consignmentOutward.setStatus(new Status(request.getStatusId()));
			consignmentOutward.setApprovalDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
			consignmentOutward.setApprovedBy(new User(request.getUserId()));
			consignmentOutward.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
			consignmentOutward.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
			ConsignmentOutwordDao.updateConsignmentOutword(consignmentOutward);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Out approved successfully");
		return response;
	}

	private Batch InsertApprovedItems(ConsignmentOutwardDetail detail, Integer storeId, Integer vendorId,Integer unitId) {
		/*
		 * Integer leaseQuantity = detail.getBatch().getQuantity();
		 * 
		 * leaseQuantity = Util.GetLeaseQuantity(detail.getReturnQunatity(),
		 * detail.getBatch().getBatchUomList(),detail.getUomType());
		 * 
		 * Batch batch = new Batch(detail.getBatch().getBatchNo(), detail.getCop(),
		 * leaseQuantity, detail.getBatch().getExpiryDate(), false, new
		 * Item(detail.getId()), leaseQuantity, detail.getCop(), detail.getUomUnit() !=
		 * null ? new UomUnit(detail.getUomUnit().getId()) : null, false, new
		 * Vendor(vendorId)); sessionService.setEntityDefaults(batch); StoreStock
		 * storeStock= new StoreStock(leaseQuantity, new Store(storeId), 0, new
		 * Batch(batch.getId())); sessionService.setEntityDefaults(storeStock);
		 * batch.addStoreStock(storeStock);
		 * 
		 * com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		 * 
		 * StockTransaction stockTransaction=new StockTransaction(leaseQuantity, new
		 * com.param.entity.model.master.Screen(screen.getId()), screen.getCode(),
		 * leaseQuantity, new Store(storeId), new Batch(batch.getId()));
		 * sessionService.setEntityDefaults(stockTransaction);
		 * batch.addStockTransaction(stockTransaction);
		 */
		
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		// get batch needs to be issued
				Batch batch = GlobalDao.getBatch(storeId, detail.getBatch().getId(),unitId);

				// Get existing batch store stock
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

				// Get lease quantity
				Integer leaseQuantity = Util.GetLeaseQuantity(detail.getReturnQunatity(), batch.getBatchUomList(), detail.getUomType());

				// Calculate store stock balance quantity
				Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

				// Deduct issued quantity from the batch store stock
				batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);

				// Calculate current batch quantity

				// Set current batch rejected quantity
				
					batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
				

				// Add stock transaction
					StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
							batch.getStoreStockList().getFirst().getStore(), detail.getId());
					stockTransaction.setCreateDefaults(detail.getOrganizationId(), detail.getUnitId(), detail.getUpdatedByUser());
				batch.addStockTransaction(stockTransaction);
		
		return batch;
	}
	
	@Override
	@Transactional
	public Object cancelConsignmentOutword(ConsignmentRequest request) {

		ConsignmentOutward consignmentOutward = ConsignmentOutwordDao.getConsignmentOutwardDetails(request.getId(),request.getUnitId());

		for (ConsignmentOutwardDetail detail : consignmentOutward.getConsignmentOutwardDetails()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
			detail.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
		}
		consignmentOutward.setCancelReason(new CancelReason(request.getCancelReasonId()));
		consignmentOutward.setCancelNote(request.getCancelNote());
		consignmentOutward.setCanceledDate(request.getCanceledDate());
		consignmentOutward.setCanceledBy(consignmentOutward.getCanceledBy());

		consignmentOutward.setStatus(new Status(request.getStatusId()));
		consignmentOutward.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentOutward.getUnitId()));
		consignmentOutward.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());

		ConsignmentOutwordDao.updateConsignmentOutword(consignmentOutward);

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Outword cancelled successfully");

		return response;
	}
}
