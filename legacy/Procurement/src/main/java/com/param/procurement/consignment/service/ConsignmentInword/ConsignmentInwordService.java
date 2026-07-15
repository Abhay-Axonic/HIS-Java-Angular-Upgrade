package com.param.procurement.consignment.service.ConsignmentInword;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ConsignmentInward;
import com.param.entity.model.procurement.ConsignmentInwardDetail;
import com.param.entity.model.procurement.ConsignmentInwardTaxComponent;
import com.param.entity.model.procurement.ConsignmentInwardUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.ConsignmentInword.IConsignmentInwordDao;
import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequestItems;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponseItems;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentInwordService implements IConsignmentInwordService {

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IConsignmentInwordDao ConsignmentInwordDao;

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.ConsignmentInward.getScreen();

	@Override
	@Transactional
	public ConsignmentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {

		ConsignmentDropdown response = new ConsignmentDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<ConsignmentResponse> getConsignmentInwardList(ConsignmentRequest request) {
		List<ConsignmentResponse> responses = new ArrayList<ConsignmentResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (ConsignmentInward consignmentInward : ConsignmentInwordDao.getConsignmentInwardList(request, screenId)) {
			ConsignmentResponse consignmentResponse = new ConsignmentResponse(consignmentInward.getId(),
					consignmentInward.getStore().getStoreId(), consignmentInward.getStore().getStoreName(),
					consignmentInward.getVendor().getId(), consignmentInward.getVendor().getName(),
					consignmentInward.getAddedBy().getUserId(), consignmentInward.getAddedBy().getUserName(),
					consignmentInward.getStatus().getId(), consignmentInward.getStatus().getStatus(),
					consignmentInward.getInwardNumber(), consignmentInward.getAddedDate(),
					consignmentInward.getRemark());
			responses.add(consignmentResponse);
		}
		return responses;
	}

	@Override
	@Transactional
	public ConsignmentResponse getConsignmentInwardDetails(Integer id,Integer unitId) {

		ConsignmentInward consignmentInward = ConsignmentInwordDao.getConsignmentInwardDetails(id,unitId);

		ConsignmentResponse consignmentResponse = new ConsignmentResponse(consignmentInward.getId(),
				consignmentInward.getStore().getStoreId(), consignmentInward.getStore().getStoreName(),
				consignmentInward.getVendor().getId(), consignmentInward.getVendor().getName(),
				consignmentInward.getAddedBy().getUserId(), consignmentInward.getAddedBy().getUserName(),
				consignmentInward.getStatus().getId(), consignmentInward.getStatus().getStatus(),
				consignmentInward.getInwardNumber(), consignmentInward.getAddedDate(), consignmentInward.getRemark(),
				consignmentInward.getDcNumber(), consignmentInward.getDcDate());

		List<ConsignmentResponseItems> items = new ArrayList<ConsignmentResponseItems>();
		for (ConsignmentInwardDetail consignmentInwardDetail : consignmentInward.getConsignmentInwardDetails()) {
			ConsignmentResponseItems item = null;
			consignmentInwardDetail.getLcop();
			
			
			item = new ConsignmentResponseItems(consignmentInwardDetail.getId(),
					consignmentInwardDetail.getItem().getId(), consignmentInwardDetail.getUomType() != null ? consignmentInwardDetail.getUomType().getId() : null,
					consignmentInwardDetail.getUomUnit() != null ? consignmentInwardDetail.getUomUnit().getId() : null, consignmentInwardDetail.getBatchNumber(),
					consignmentInwardDetail.getExpiryDate(), consignmentInwardDetail.getQuantity(),
					consignmentInwardDetail.getCop(),consignmentInwardDetail.getTotalAmount(),
					consignmentInwardDetail.getDiscountAmount(), consignmentInwardDetail.getDiscountPercentage(),
					consignmentInwardDetail.getTaxAmount(), consignmentInwardDetail.getNetAmount(),
					consignmentInwardDetail.getTax().getId(), consignmentInwardDetail.getTaxPercentage(),
					consignmentInwardDetail.getItem().getCode(), consignmentInwardDetail.getItem().getName(),
					consignmentInwardDetail.getUomType() != null ? consignmentInwardDetail.getUomType().getType() : null, consignmentInwardDetail.getUomUnit() != null ? consignmentInwardDetail.getUomUnit().getUnits() : null , consignmentInwardDetail.getLcop() != null ? consignmentInwardDetail.getLcop() : null);

			item.setUomList(PopulateUom(consignmentInwardDetail));
			items.add(item);
		}
		consignmentResponse.setItem(items);
		return consignmentResponse;
	}

	public static List<UomResponse> PopulateUom(ConsignmentInwardDetail consignmentInwardDetail)
	{
		List<UomResponse> response = new ArrayList<UomResponse>();
		for (ConsignmentInwardUomMapper uomMapper : consignmentInwardDetail.getConsignmentInwardUomMappers())
		{
			response.add(new UomResponse(uomMapper.getId(), uomMapper.getUomType().getId(),
					uomMapper.getUomType().getType(), uomMapper.getUomUnit().getId(), uomMapper.getUomUnit().getUnits(),
					uomMapper.getUomType().getCode() + " - " + uomMapper.getUomUnit().getUnits(),
					uomMapper.getConversions(), uomMapper.getIpUom(), uomMapper.getOpUom(), uomMapper.getStoreUom()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object saveConsignmentInward(ConsignmentRequest request) 
	{

		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String consignmentInwNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);

		ConsignmentInward consignmentInward = new ConsignmentInward(consignmentInwNumber, request.getDcDate(),
				request.getDcNo(), request.getTotalNetAmount(), request.getRemark(), new Store(request.getStoreId()),
				request.getTotalAmount(), request.getTotalDiscount(), request.getTotalTaxAmount(),
				new Vendor(request.getVendorId()), new Status(request.getStatusId()));
//		sessionService.setEntityDefaults(consignmentInward);	
		consignmentInward.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		
		for (ConsignmentRequestItems item : request.getItems()) 
		{
			UomType uomType = null;
            UomUnit uomUnit = null;

            if(item.getUomTypeId() != null)
                uomType = new UomType(item.getUomTypeId());

            if(item.getUomUnitId() != null)
                uomUnit = new UomUnit(item.getUomUnitId());
			
			ConsignmentInwardDetail detail = null;
			detail = new ConsignmentInwardDetail(item.getBatchNo(), item.getCop(), item.getTotalDiscount(),
					item.getDiscountPercentage(), item.getExpiryDate(), new Item(item.getItemId()),
					item.getTotalNetAmount(), item.getQuantity(), item.getTotalTaxAmount() != null ? item.getTotalTaxAmount() : 0, new Tax(item.getTaxId()),
					item.getTaxPercentage(), uomType, uomUnit,item.getTotalAmount(), item.getLcop());

			for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {

				/*
				 * Integer taxCompId = taxDetail.getTaxComponent().getId(); Double taxPercentage
				 * = taxDetail.getTaxPercentage(); Double amt = item.getCop();
				 */
				Double calTaxAmt = (item.getCop() * taxDetail.getTaxPercentage()) / 100;
				
				ConsignmentInwardTaxComponent consignmentInwardTaxComponent = new ConsignmentInwardTaxComponent(calTaxAmt,
						detail,
						taxDetail.getTaxPercentage(), new TaxComponent(taxDetail.getTaxComponent().getId()));
				
//				sessionService.setEntityDefaults(consignmentInwardTaxComponent);
				consignmentInwardTaxComponent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addConsignmentInwardTaxCom(consignmentInwardTaxComponent);

			}

			for (UomResponse uomResponse : item.getUom()) {

				ConsignmentInwardUomMapper uomMapper = new ConsignmentInwardUomMapper(uomResponse.getConversion(),
						uomResponse.getIpUom(), uomResponse.getOpUom(), uomResponse.getStoreUom(),
						new UomType(uomResponse.getUomTypeId()), new UomUnit(uomResponse.getUomUnitId()));
//				sessionService.setEntityDefaults(uomMapper);
				uomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addConsignmentInwardUomMapper(uomMapper);
			}
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			consignmentInward.addConsignmentInwardDetail(detail);
		}
		ConsignmentInwordDao.saveConsignmentInword(consignmentInward);

		CommonResponse response = new CommonResponse();
		response.setId(consignmentInward.getId().longValue());
		response.setMessage("Consignment Inward saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateConsignmentInword(ConsignmentRequest request) {

		ConsignmentInward consignmentInward = ConsignmentInwordDao.getConsignmentInwardDetails(request.getId(),request.getUnitId());

		for (ConsignmentInwardDetail detail : consignmentInward.getConsignmentInwardDetails()) 
		{
			Integer id = null;
			for (ConsignmentRequestItems item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					
					UomType uomType = null;
		            UomUnit uomUnit = null;

		            if(item.getUomTypeId() != null)
		                uomType = new UomType(item.getUomTypeId());

		            if(item.getUomUnitId() != null)
		                uomUnit = new UomUnit(item.getUomUnitId());
					
		            if(item.getDiscountPercentage() == null)
		            	item.setDiscountPercentage(0.0D);
		            
					detail.updateConsignmentInwardDetail(item.getBatchNo(), item.getCop(), item.getTotalDiscount(),
							item.getDiscountPercentage(), item.getExpiryDate(), new Item(item.getItemId()),
							item.getTotalNetAmount(), item.getQuantity(), item.getTotalTaxAmount(),
							new Tax(item.getTaxId()), item.getTaxPercentage(), uomType,
							uomUnit,item.getTotalAmount());

					for (ConsignmentInwardUomMapper uomMapper : detail.getConsignmentInwardUomMappers()) 
					{
						Integer uomMapperId = null;
						for (UomResponse uomResponse : item.getUom())
						{
							if (uomResponse.getId() != null && uomResponse.getId().equals(uomMapper.getId())) 
							{
								uomMapperId = uomResponse.getId();
								uomMapper.updateConsignmentInwardUomMapper(uomResponse.getConversion(),
										uomResponse.getIpUom(), uomResponse.getOpUom(), uomResponse.getStoreUom(),
										new UomType(uomResponse.getUomTypeId()),
										new UomUnit(uomResponse.getUomUnitId()));

							}
						}
						if (uomMapperId == null) {
							uomMapper.setIsDeleted(true);
						}

						uomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
						uomMapper.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
					}

					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
			detail.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
		}

		for (ConsignmentRequestItems item : request.getItems()) 
		{
			if (item.getId() == null) 
			{
				ConsignmentInwardDetail detail = null;
				
				UomType uomType = null;
	            UomUnit uomUnit = null;

	            if(item.getUomTypeId() != null)
	                uomType = new UomType(item.getUomTypeId());

	            if(item.getUomUnitId() != null)
	                uomUnit = new UomUnit(item.getUomUnitId());
	            
	            if(item.getDiscountPercentage() == null)
	            	item.setDiscountPercentage(0.0D);
				
				detail = new ConsignmentInwardDetail(item.getBatchNo(), item.getCop(), item.getTotalDiscount(),
						item.getDiscountPercentage(), item.getExpiryDate(), new Item(item.getItemId()),
						item.getTotalNetAmount(), item.getQuantity(), item.getTotalTaxAmount(),
						new Tax(item.getTaxId()), item.getTaxPercentage(), uomType,
						uomUnit,item.getTotalAmount());

				for (UomResponse uomResponse : item.getUom())
				{
					ConsignmentInwardUomMapper uomMapper = new ConsignmentInwardUomMapper(uomResponse.getConversion(),
							uomResponse.getIpUom(), uomResponse.getOpUom(), uomResponse.getStoreUom(),
							new UomType(uomResponse.getUomTypeId()), new UomUnit(uomResponse.getUomUnitId()));
//					sessionService.setEntityDefaults(uomMapper);
					uomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					detail.addConsignmentInwardUomMapper(uomMapper);
				}
//				sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				consignmentInward.addConsignmentInwardDetail(detail);
			}
		}
		consignmentInward.setDcNumber(request.getDcNo());
		consignmentInward.setDcDate(request.getDcDate());
		consignmentInward.setRemark(request.getRemark());
		consignmentInward.setNetAmount(request.getTotalNetAmount());
		consignmentInward.setTotalAmount(request.getTotalAmount());
		consignmentInward.setTotalDiscount(request.getTotalDiscount());
		consignmentInward.setTotalTax(request.getTotalTaxAmount());
		ConsignmentInwordDao.updateConsignmentInword(consignmentInward);

		CommonResponse response = new CommonResponse();
		response.setId(consignmentInward.getId().longValue());
		response.setMessage("Consignment Inward updated successfully");

		
		return response;
	}

	@Override
	@Transactional
	public Object approveConsignmentInword(ConsignmentRequest request) {

		for (Integer consInwId : request.getConsInwId()) {

			ConsignmentInward consignmentInward = ConsignmentInwordDao.getConsignmentInwardDetails(consInwId,request.getUnitId());

			Integer storeId = consignmentInward.getStore().getStoreId();

			Integer vendorId = consignmentInward.getVendor().getId();

			for (ConsignmentInwardDetail details : consignmentInward.getConsignmentInwardDetails()) {
				if (details.getIsActive() && !details.getIsDeleted()) {

					Batch batch = InsertApprovedItems(details, storeId, vendorId,details.getTax(), request,consInwId);

					for (ConsignmentInwardUomMapper uomMapper : details.getConsignmentInwardUomMappers())
					{
						if (uomMapper.getIsActive() && !uomMapper.getIsDeleted()) 
						{
							BatchUomMapper batchUomMapper=	new BatchUomMapper(uomMapper.getConversions(), uomMapper.getIpUom(),
									uomMapper.getOpUom(), uomMapper.getStoreUom(), uomMapper.getUomType(),
									uomMapper.getUomUnit());
//							sessionService.setEntityDefaults(batchUomMapper);
							batchUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
							batch.addBatchUom(batchUomMapper);
						}
					}

					ConsignmentInwordDao.saveBatch(batch);
					details.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
					details.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
				}
			}
			consignmentInward.setStatus(new Status(request.getStatusId()));
			consignmentInward.setApprovalDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
			consignmentInward.setApprovedBy(new User(request.getUserId()));

			consignmentInward.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
			consignmentInward.setUpdatedByUser(request.getUserId());

			ConsignmentInwordDao.updateConsignmentInword(consignmentInward);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Inward approved successfully");

		return response;
	}

	private Batch InsertApprovedItems(ConsignmentInwardDetail detail, Integer storeId, Integer vendorId, Tax tax, ConsignmentRequest request, Integer consInwId) {
		Integer leaseQuantity = detail.getQuantity();

		leaseQuantity = Util.GetLeaseQuantity(detail.getQuantity(), detail.getConsignmentInwardUomMappers(),
				detail.getUomType());

		Batch batch = new Batch(detail.getBatchNumber(), detail.getCop(), leaseQuantity, detail.getExpiryDate(), false,
				new Item(detail.getItem().getId()), leaseQuantity, detail.getCop(),
				detail.getUomUnit() != null ? new UomUnit(detail.getUomUnit().getId()) : null, false, new Vendor(vendorId),tax);
//		sessionService.setEntityDefaults(batch);
		batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		StoreStock storeStock=new StoreStock(leaseQuantity, new Store(storeId), 0, new Batch(batch.getId()));
//		sessionService.setEntityDefaults(storeStock);
		storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction=new StockTransaction(leaseQuantity, 
				new com.param.entity.model.master.Screen(screen.getId()),screen.getCode(), leaseQuantity,
				new Store(storeId), new Batch(batch.getId()),detail.getId());
		stockTransaction.setTransactionDetialsId(detail.getId());
		stockTransaction.setTransactionId(consInwId);
		stockTransaction.setInQty(leaseQuantity);
		stockTransaction.setItem(detail.getItem());
		stockTransaction.setIsTransit(false);
		batch.addStockTransaction(stockTransaction);
//		sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		return batch;
	}

	@Override
	@Transactional
	public Object cancelConsignmentInword(ConsignmentRequest request) {

		ConsignmentInward consignmentInward = ConsignmentInwordDao.getConsignmentInwardDetails(request.getId(),request.getUnitId());

		for (ConsignmentInwardDetail detail : consignmentInward.getConsignmentInwardDetails()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		consignmentInward.setCancelReason(new CancelReason(request.getCancelReasonId()));
		consignmentInward.setCancelNote(request.getCancelNote());

		consignmentInward.setStatus(new Status(request.getStatusId()));
		consignmentInward.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentInward.getUnitId()));
		consignmentInward.setUpdatedByUser(request.getUserId());

		ConsignmentInwordDao.updateConsignmentInword(consignmentInward);

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Inword cancelled successfully");

		return response;
	}

}
