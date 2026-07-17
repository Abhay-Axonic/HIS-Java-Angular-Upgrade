package com.param.procurement.loanInOut.service.loanOut;

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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dao.loanOut.ILoanOutDao;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutRequestItem;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.dto.LoanInOutResponseItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class LoanOutService implements ILoanOutService {

	private Integer screenId = Screen.LoanOut.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	ILoanOutDao LoanOutDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {

		LoanInOutDropdown response = new LoanInOutDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getLoanOutList(LoanInOutRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return LoanOutDao.searchLoanOutList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveLoanOut(LoanInOutRequest request) {

		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String loanOutNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		
		LoanOut loanOut = new LoanOut(new AssetType(request.getAssetTypeId()), request.getDcDate(), request.getDcNo(),
				loanOutNumber, request.getRemark(), new Status(request.getStatusId()), new Store(request.getStoreId()),
				request.getTotalAmount(), new Vendor(request.getVendorId()));
//		sessionService.setEntityDefaults(loanOut);	
		loanOut.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (LoanInOutRequestItem item : request.getItems()) 
		{
			LoanOutDetail detail = null;
			if (request.getAssetTypeId().equals(Constants.getConsumableId())) {
				detail = new LoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(), item.getRemark(),
						item.getTotalAmount(), new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
						item.getCop());
			} else if (request.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				detail = new LoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(), item.getRemark(),
						item.getTotalAmount(), item.getCop());
			}
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			loanOut.addLoanOutDetail(detail);
		}
		LoanOutDao.saveLoanIn(loanOut);

		CommonResponse response = new CommonResponse();
		response.setId(loanOut.getId().longValue());
		response.setMessage("Loan Out saved successfully");

		return response;
	}

	@Override
	@Transactional
	public LoanInOutResponse getLoanOutDetails(Integer id,Integer unitId) {

		LoanOut loanOut = LoanOutDao.getLoanOut(id,unitId);

		LoanInOutResponse loanInOutResponse = new LoanInOutResponse(loanOut.getId(), loanOut.getLoanOutNo(),
				loanOut.getAddedDate(), loanOut.getStore().getStoreName(), loanOut.getStore().getStoreId(),
				loanOut.getVendor().getName(), loanOut.getVendor().getId(), loanOut.getAssetType().getType(),
				loanOut.getAssetType().getId(), loanOut.getRemark(), loanOut.getAddedBy().getUserName(),
				loanOut.getAddedBy().getUserId(), loanOut.getStatus().getStatus(), loanOut.getStatus().getId(),
				loanOut.getTotalAmount());
		loanInOutResponse.setDcNo(loanOut.getDcNo());
		loanInOutResponse.setDcDate(loanOut.getDcDate());

		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();
		for (LoanOutDetail loanOutDetail : LoanOutDao.getLoanOutDetails(loanOut.getId())) {

			/*Long storeStock = GlobalDao.getStoreStockByItem(loanOut.getStore().getStoreId(),
					loanOutDetail.getBatch().getItem().getId(),unitId);*/
			
			Long storeStock = GlobalDao.getStoreStockByItem(loanOut.getStore().getStoreId(),
					loanOutDetail.getBatch().getItem().getId(),loanOutDetail.getBatch().getId(),unitId);
			
			LoanInOutResponseItem item = null;
			if (loanOut.getAssetType().getId().equals(Constants.getConsumableId())) {
				
				Long availableStock = Util.ConvertQuantityByUom(storeStock,
						loanOutDetail.getBatch().getItem().getUnitOfMeasurementList(), loanOutDetail.getUomType());
				item = new LoanInOutResponseItem(loanOutDetail.getId(), loanOutDetail.getBatch().getItem().getId(),
						loanOutDetail.getBatch().getItem().getCode(), loanOutDetail.getBatch().getItem().getName(),
						loanOutDetail.getUomType().getId(), loanOutDetail.getUomType().getType(),
						loanOutDetail.getUomUnit().getId(), loanOutDetail.getUomUnit().getUnits(),
						loanOutDetail.getBatch().getId(), loanOutDetail.getBatch().getBatchNo(),
						loanOutDetail.getBatch().getExpiryDate(), loanOutDetail.getLoanOutQuantity(),
						loanOutDetail.getCop(), loanOutDetail.getRemark(), loanOutDetail.getTotalAmount(),
						availableStock);
				item.setUomList(PopulateUom(loanOutDetail));
			} else if (loanOut.getAssetType().getId().equals(Constants.getFixedAssetId())) {
				item = new LoanInOutResponseItem(loanOutDetail.getId(), loanOutDetail.getBatch().getItem().getId(),
						loanOutDetail.getBatch().getItem().getCode(), loanOutDetail.getBatch().getItem().getName(),
						loanOutDetail.getBatch().getId(), loanOutDetail.getBatch().getBatchNo(),
						loanOutDetail.getBatch().getExpiryDate(), loanOutDetail.getLoanOutQuantity(),
						loanOutDetail.getCop(), loanOutDetail.getRemark(), loanOutDetail.getTotalAmount(),
						storeStock);
			}
			items.add(item);
		}
		loanInOutResponse.setItems(items);
		return loanInOutResponse;
	}

	private List<UomResponse> PopulateUom(LoanOutDetail loanOutDetail) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (BatchUomMapper uomMapper : loanOutDetail.getBatch().getBatchUomList()) {
			response.add(new UomResponse(uomMapper.getId(), uomMapper.getUomType().getId(),
					uomMapper.getUomType().getType(), uomMapper.getUomUnit().getId(), uomMapper.getUomUnit().getUnits(),
					uomMapper.getUomType().getCode() + " - " + uomMapper.getUomUnit().getUnits(),
					uomMapper.getConversions(), uomMapper.getIpUom(), uomMapper.getOpUom(), uomMapper.getStoreUom()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object cancelLoanOut(LoanInOutRequest request) {

		LoanOut loanOut = LoanOutDao.getLoanOut(request.getId(),request.getUnitId());

		for (LoanOutDetail detail : loanOut.getLoanOutDetails()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOut.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		loanOut.setCancelReason(new CancelReason(request.getCancelReasonId()));
		loanOut.setCancelNote(request.getCancelNote());

		loanOut.setStatus(new Status(request.getStatusId()));
		loanOut.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOut.getUnitId()));
		loanOut.setUpdatedByUser(request.getUserId());

		LoanOutDao.updateLoanOut(loanOut);

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan Out cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveLoanOut(LoanInOutRequest request) {

		for (Integer loanOutId : request.getLoanOutId()) {

			LoanOut loanOut = LoanOutDao.getLoanOut(loanOutId,request.getUnitId());

			Integer storeId = loanOut.getStore().getStoreId();

			for (LoanOutDetail detail : loanOut.getLoanOutDetails()) {
				if (detail.getIsActive() && !detail.getIsDeleted()) {

					/*
					 * Long batchId = detail.getBatch().getId(); Integer quantity =
					 * detail.getLoanOutQuantity(); UomType uomType = detail.getUomType();
					 */
					
					Batch batch = updateBatchStoreTransaction(storeId, detail.getBatch().getId(),
							detail.getLoanOutQuantity(), detail.getUomType() != null ? detail.getUomType() : null,
							loanOut.getAssetType().getId(), detail.getBatch().getItem().getId(),loanOut.getUnit().getUnitId(),request.getOrganizationId(),request.getUserId(),detail);
				}
			}

			loanOut.setStatus(new Status(request.getStatusId()));
			loanOut.setApprovalOn(TimeZoneComponent.getDateByzone(loanOut.getUnitId()));
			loanOut.setApprovedBy(new User(request.getUserId()));

			loanOut.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOut.getUnitId()));
			loanOut.setUpdatedByUser(request.getUserId());

			LoanOutDao.updateLoanOut(loanOut);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Loan Out approved successfully");

		return response;
	}

	private Batch updateBatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer assetTypeId , Integer itemId,Integer unitId, Integer orgId, Integer userId, LoanOutDetail d) {

		screen = GlobalDao.getScreen(screenId);

		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,itemId);//0 itemId

		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		}

		

		if(leaseQuantity < 0)
		{
			leaseQuantity=0;
		}
		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
		Integer currentQuantity = batch.getCurrentQuantity() - leaseQuantity;

		
		if(currentQuantity < 0)
		{
			currentQuantity=0;
		}
		if(balanceQuantity < 0)
		{
			balanceQuantity=0;
		}
		
		batch.setCurrentQuantity(currentQuantity);

		batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);

		// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().getFirst().getStore(), d.getId());
		stockTransaction.setCreateDefaults(orgId,unitId,userId);
		batch.addStockTransaction(stockTransaction);

		//sessionService.setEntityDefaults(batch);	
		return batch;
	}

	@Override
	@Transactional
	public Object updateLoanOut(LoanInOutRequest request) {

		LoanOut loanOut = LoanOutDao.getLoanOut(request.getId(),request.getUnitId());

		for (LoanOutDetail detail : loanOut.getLoanOutDetails()) {
			Integer id = null;

			for (LoanInOutRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					if (loanOut.getAssetType().getId().equals(Constants.getConsumableId())) {
						detail.updateLoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(),
								item.getRemark(), item.getCop(), item.getTotalAmount(),
								new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()));
					} else if (loanOut.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						detail.updateLoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(),
								item.getRemark(), item.getCop(), item.getTotalAmount());
					}

				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOut.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (LoanInOutRequestItem item : request.getItems())
		{

			if (item.getId() == null) 
			{
				LoanOutDetail detail = null;
				if (loanOut.getAssetType().getId().equals(Constants.getConsumableId())) {
					detail = new LoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(),
							item.getRemark(), item.getTotalAmount(), new UomType(item.getUomTypeId()),
							new UomUnit(item.getUomUnitId()), item.getCop());
				} else if (loanOut.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					detail = new LoanOutDetail(new Batch(item.getBatchId()), item.getLoanOutQuantity(),
							item.getRemark(), item.getTotalAmount(), item.getCop());
				}
//				sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				loanOut.addLoanOutDetail(detail);
			}
		}
		loanOut.setTotalAmount(request.getTotalAmount());
		loanOut.setRemark(request.getRemark());
		LoanOutDao.updateLoanOut(loanOut);

		CommonResponse response = new CommonResponse();
		response.setId(loanOut.getId().longValue());
		response.setMessage("Loan Out updated successfully");

		return response;
	}

}
