package com.param.procurement.loanInOut.service.loanIn;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.LoanIn;
import com.param.entity.model.procurement.LoanInDetail;
import com.param.entity.model.procurement.LoanInUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dao.loanIn.ILoanInDao;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutRequestItem;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.dto.LoanInOutResponseItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class LoanInService implements ILoanInService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	ILoanInDao loanInDao;

	private Integer screenId = Screen.LoanIn.getScreen();
	
	@Override
	@Transactional
	public List<LoanInOutResponse> getLoanInList(LoanInOutRequest request) {
		List<LoanInOutResponse> loanInList = new ArrayList<LoanInOutResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for(LoanIn loanIn : loanInDao.getLoanInList(request, screenId))
		{
			LoanInOutResponse loanInObj = new LoanInOutResponse(loanIn.getId(), loanIn.getInwardNo(), loanIn.getAddedDate(),
					loanIn.getStore().getStoreName(), loanIn.getStore().getStoreId(), loanIn.getVendor().getName(),
					loanIn.getVendor().getId(), loanIn.getAssetType().getType(), loanIn.getAssetType().getId(),
					loanIn.getRemark(), loanIn.getAddedBy().getUserName(), loanIn.getAddedBy().getUserId(),
					loanIn.getStatus().getStatus(), loanIn.getStatus().getId());
			loanInList.add(loanInObj);
		}
		return loanInList;
	}
	
	@Override
	@Transactional
	public LoanInOutResponse getLoanInDetails(Integer id,Integer unitId)
	{
		LoanIn loanIn = loanInDao.getLoanInDetails(id,unitId);
		LoanInOutResponse loanInOutResponse = new LoanInOutResponse(loanIn.getId(), loanIn.getInwardNo(), loanIn.getAddedDate(),
				loanIn.getStore().getStoreName(), loanIn.getStore().getStoreId(), loanIn.getVendor().getName(), loanIn.getVendor().getId(),
				loanIn.getAssetType().getType(), loanIn.getAssetType().getId(), loanIn.getRemark(), loanIn.getAddedBy().getUserName(),
				loanIn.getAddedBy().getUserId(), loanIn.getStatus().getStatus(), loanIn.getStatus().getId());
		loanInOutResponse.setDcNo(loanIn.getDcNo());
		loanInOutResponse.setDcDate(loanIn.getDcDate());
		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();
		for(LoanInDetail loanInDetail : loanInDao.getLoanInDetailList(loanIn.getId()))
		{
			LoanInOutResponseItem item = null;
			if (loanInOutResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
				item = new LoanInOutResponseItem(loanInDetail.getId(), loanInDetail.getItem().getId(),
						loanInDetail.getItem().getCode(), loanInDetail.getItem().getName(), loanInDetail.getUomType().getId(),
						loanInDetail.getUomType().getType(), loanInDetail.getUomUnit().getId(), loanInDetail.getUomUnit().getUnits(),
						loanInDetail.getBatchNo(), loanInDetail.getExpiryDate(), loanInDetail.getQuantity(), loanInDetail.getCop(),
						loanInDetail.getTotalAmount(), loanInDetail.getRemark(), loanInDetail.getSpecification());
				item.setUomList(PopulateUom(loanInDetail));
			}
			else if (loanInOutResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				item = new LoanInOutResponseItem(loanInDetail.getId(), loanInDetail.getItem().getId(),
						loanInDetail.getItem().getCode(), loanInDetail.getItem().getName(),
						loanInDetail.getBatchNo(), loanInDetail.getExpiryDate(), loanInDetail.getQuantity(), loanInDetail.getCop(),
						loanInDetail.getTotalAmount(), loanInDetail.getRemark(), loanInDetail.getSpecification());
			}
				items.add(item);
		}
		loanInOutResponse.setItems(items);
		return loanInOutResponse;
	}
	
	@Override
	@Transactional
	public Object saveLoanIn(LoanInOutRequest request)
	{
		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String loanInNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		
		LoanIn loanIn = new LoanIn(loanInNumber, new Store(request.getStoreId()), new Vendor(request.getVendorId()),
				new AssetType(request.getAssetTypeId()), request.getRemark(), new Status(request.getStatusId()),
				request.getTotalAmount(), request.getDcNo(), request.getDcDate());
//		sessionService.setEntityDefaults(loanIn);
		loanIn.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (LoanInOutRequestItem item : request.getItems()) 
		{
			LoanInDetail detail = null;
			if (loanIn.getAssetType().getId().equals(Constants.getConsumableId())) {
				 detail = new LoanInDetail( new Item(item.getItemId()), new UomType(item.getUomTypeId()),
						new UomUnit(item.getUomUnitId()), item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
						item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
						new Status(item.getStatusId()));
				
				for(UomResponse uomResponse : item.getUom())
				{
					LoanInUomMapper uom = new LoanInUomMapper(uomResponse.getConversion(), uomResponse.getIpUom(), uomResponse.getOpUom(),
							uomResponse.getStoreUom(), new UomType(uomResponse.getUomTypeId()), new UomUnit(uomResponse.getUomUnitId()));
//					sessionService.setEntityDefaults(uom);
					uom.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					detail.addLoanInUomMapper(uom);
				}
			}
			else if (loanIn.getAssetType().getId().equals(Constants.getFixedAssetId())) {
				detail = new LoanInDetail( new Item(item.getItemId()), 
						item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
						item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
						new Status(item.getStatusId()));
			}
//			sessionService.setEntityDefaults(detail);	
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			loanIn.addLoanInDetail(detail);
		}
		loanInDao.saveLoanIn(loanIn);
	
		CommonResponse response = new CommonResponse();
		response.setId(loanIn.getId().longValue());
		response.setMessage("Loan In saved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		LoanInOutDropdown response = new LoanInOutDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(),
					assetType.getType()));
		}
		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLoanIn(LoanInOutRequest request)
	{
		LoanIn loanIn = loanInDao.getLoanInDetails(request.getId(),request.getUnitId());

		for (LoanInDetail detail : loanIn.getLoanInDetails()) {
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		loanIn.setCancelReason(new CancelReason(request.getCancelReasonId()));
		loanIn.setCancelNote(request.getCancelNote());

		loanIn.setStatus(new Status(request.getStatusId()));
		loanIn.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
		loanIn.setUpdatedByUser(request.getUserId());

		loanInDao.updateLoanIn(loanIn);

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan In cancelled successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object approveLoanIn(LoanInOutRequest request)
	{
		for (Integer loanInId : request.getLoanInId()) {

			LoanIn loanIn = loanInDao.getLoanInDetails(loanInId,request.getUnitId());

			Integer storeId = loanIn.getStore().getStoreId();
			
			Integer vendorId = loanIn.getVendor().getId();

			for (LoanInDetail details : loanIn.getLoanInDetails()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					Batch batch = InsertApprovedItems(details, storeId, vendorId,loanIn.getAssetType().getId(),request);
					if(loanIn.getAssetType().getId().equals(Constants.getConsumableId())){
					for(LoanInUomMapper loanInUomMapper : details.getLoanInUomMapper())
					{
						if(loanInUomMapper.getIsActive() && !loanInUomMapper.getIsDeleted())
						{
							BatchUomMapper batchUomMapper=	new BatchUomMapper(loanInUomMapper.getConversions(), loanInUomMapper.getIpUom(), loanInUomMapper.getOpUom(),
									loanInUomMapper.getStoreUom(), loanInUomMapper.getUomType(), loanInUomMapper.getUomUnit());
//							sessionService.setEntityDefaults(batchUomMapper);
							batchUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
							batch.addBatchUom(batchUomMapper);
						}
					}
				
					loanInDao.saveBatch(batch);
					details.setApprovedQuantity(details.getQuantity());
					details.setBatch(new Batch(batch.getId()));
					details.setStatus(new Status(request.getStatusId()));
					details.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
					details.setUpdatedByUser(request.getUserId());
					}
					
					/* start code for resolved HIS-10527 Unable to add fixed type item in loan in out */
					else if (loanIn.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						loanInDao.saveBatch(batch);
						details.setApprovedQuantity(details.getQuantity());
						details.setBatch(new Batch(batch.getId()));
						details.setStatus(new Status(request.getStatusId()));
						details.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
						details.setUpdatedByUser(request.getUserId());
					}
					/* end code for resolved HIS-10527 Unable to add fixed type item in loan in out */
				}
			}

			loanIn.setStatus(new Status(request.getStatusId()));
			loanIn.setApprovalOn(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
			loanIn.setApprovedBy(new User(request.getUserId()));

			loanIn.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
			loanIn.setUpdatedByUser(request.getUserId());

			loanInDao.updateLoanIn(loanIn);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan In approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object updateLoanIn(LoanInOutRequest request)
	{
		LoanIn loanIn = loanInDao.getLoanInDetails(request.getId(),request.getUnitId());

		for (LoanInDetail detail : loanIn.getLoanInDetails()) {
			Integer id = null;

			for (LoanInOutRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					if(loanIn.getAssetType().getId().equals(Constants.getConsumableId())) {
					detail.updateLoanInDetail(new Item(item.getItemId()), new UomType(item.getUomTypeId()),
							new UomUnit(item.getUomUnitId()), item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
							item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
							new Status(item.getStatusId()));
					
					for(LoanInUomMapper uomMapper : detail.getLoanInUomMapper())
					{
						Integer uomMapperId = null;
						for(UomResponse uomResponse : item.getUom())
						{
							if (uomResponse.getId() != null && uomResponse.getId().equals(uomMapper.getId())) {
								uomMapperId = uomResponse.getId();
								uomMapper.updateLoanInUomMapper(uomResponse.getConversion(), uomResponse.getIpUom(), uomResponse.getOpUom(),
										uomResponse.getStoreUom(), new UomType(uomResponse.getUomTypeId()), new UomUnit(uomResponse.getUomUnitId()));
								
							}
						}
						if (uomMapperId == null) {
							uomMapper.setIsDeleted(true);
						}
						
						uomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
						uomMapper.setUpdatedByUser(request.getUserId());
					}
					}
					else if(loanIn.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						detail.updateLoanInDetail(new Item(item.getItemId()), item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
								item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
								new Status(item.getStatusId()));
					}
					
					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanIn.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (LoanInOutRequestItem item : request.getItems())
		{

			if (item.getId() == null)
			{
				LoanInDetail detail = null;
				if(loanIn.getAssetType().getId().equals(Constants.getConsumableId())) {
					detail = new LoanInDetail(new Item(item.getItemId()), new UomType(item.getUomTypeId()),
							new UomUnit(item.getUomUnitId()), item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
							item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
							new Status(item.getStatusId()));
	
					for(UomResponse uomResponse : item.getUom())
					{
						LoanInUomMapper uom = new LoanInUomMapper(uomResponse.getConversion(), uomResponse.getIpUom(), uomResponse.getOpUom(),
								uomResponse.getStoreUom(), new UomType(uomResponse.getUomTypeId()), new UomUnit(uomResponse.getUomUnitId()));
//						sessionService.setEntityDefaults(uom);	
						uom.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						detail.addLoanInUomMapper(uom);
					}
				}
				else if(loanIn.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					detail = new LoanInDetail(new Item(item.getItemId()), item.getBatchNo(), item.getExpiryDate(), item.getQuantity(),
							item.getCop(), item.getTotalAmount(), item.getRemark(), item.getSpecification(), 
							new Status(item.getStatusId()));
				}
//				sessionService.setEntityDefaults(detail);	
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				loanIn.addLoanInDetail(detail);
			}
		}
		loanIn.setDcNo(request.getDcNo());
		loanIn.setDcDate(request.getDcDate());
		loanIn.setRemark(request.getRemark());
		loanInDao.updateLoanIn(loanIn);

		CommonResponse response = new CommonResponse();
		response.setId(loanIn.getId().longValue());
		response.setMessage("Loan In updated successfully");

		return response;
	}
	
	private Batch InsertApprovedItems(LoanInDetail detail, Integer storeId, Integer vendorId,Integer assetTypeId, LoanInOutRequest request) {
		Integer leaseQuantity = detail.getQuantity();
		Batch batch = null;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(detail.getQuantity(), detail.getLoanInUomMapper(), detail.getUomType());
		
		 batch = new Batch(detail.getBatchNo(), detail.getCop(), leaseQuantity, detail.getExpiryDate(),
				false, new Item(detail.getItem().getId()), leaseQuantity, detail.getCop(), new UomUnit(detail.getUomUnit().getId()),
				false, new Vendor(vendorId));
		}
		else if(assetTypeId.equals(Constants.getFixedAssetId())) {
			batch = new Batch(detail.getBatchNo(), detail.getCop(), leaseQuantity, detail.getExpiryDate(),
					false, new Item(detail.getItem().getId()), leaseQuantity, detail.getCop(),
					false, new Vendor(vendorId));
		}
		StoreStock storeStock=new StoreStock(leaseQuantity, new Store(storeId), 0, new Batch(batch.getId()));
		storeStock.setTotalQty(leaseQuantity);
		storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction=new StockTransaction(leaseQuantity, new com.param.entity.model.master.Screen(screen.getId()), 
				screen.getCode(), leaseQuantity, new Store(storeId), new Batch(batch.getId()), detail.getId());
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		batch.addStockTransaction(stockTransaction);
//		sessionService.setEntityDefaults(batch);	
		
		batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		return batch;
	}
	
	public static List<UomResponse> PopulateUom(LoanInDetail loanInDetil) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (LoanInUomMapper uomMapper : loanInDetil.getLoanInUomMapper()) {
			response.add(new UomResponse(uomMapper.getId(), uomMapper.getUomType().getId(), uomMapper.getUomType().getType(),
					uomMapper.getUomUnit().getId(), uomMapper.getUomUnit().getUnits(),
					uomMapper.getUomType().getCode() + " - " + uomMapper.getUomUnit().getUnits(),
					uomMapper.getConversions(), uomMapper.getIpUom(), uomMapper.getOpUom(), uomMapper.getStoreUom()));
		}

		return response;
	}
}