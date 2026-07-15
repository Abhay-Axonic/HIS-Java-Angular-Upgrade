package com.param.inventory.store.service.returnableGatepass;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassAccessories;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.inventory.ReturnableGatepassTaxComponent;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.returnableGatepass.IReturnableBonusBatchExpiryDao;
import com.param.inventory.store.dao.returnableGatepass.IReturnableGatepassDao;
import com.param.inventory.store.dao.returnableGatepass.ReturnableBonusBatchExpiryDao;
import com.param.inventory.store.dto.returnableGatepass.ItemRequest;
import com.param.inventory.store.dto.returnableGatepass.ItemResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassDropdownResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassResponse;
import com.param.inventory.store.dto.returnableGatepass.accessoriesResponse;
import com.param.inventory.store.dto.returnableGatepass.accessoriesReuest;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dto.goodReceiptNote.IGrnBonusBatchExpiryDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;


@Service("ReturnableGatepassService")
public class ReturnableGatepassService implements IReturnableGatepassService {

	private static final Logger logger = Logger.getLogger(ReturnableGatepassService.class);

	private Integer screenId = Screen.ReturnableGatepass.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IReturnableGatepassDao ReturnableGatepassDao;
	
	@Autowired
	IGoodReceiptNoteDao grnDao;
	
	@Autowired
    IGrnBonusBatchExpiryDao BonusBatchExpiryDao;
	
	@Autowired
	ITaxComponentDetailsDao iTaxComponentDetailsDao;

	@Autowired
    IReturnableBonusBatchExpiryDao ReturnableBonusBatchExpiryDao;
	

	@Override
	@Transactional
	public ReturnableGatepassDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ReturnableGatepassDropdownResponse response = new ReturnableGatepassDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0, 0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getReturnableGatepassList(ReturnableGatepassRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ReturnableGatepassDao.getReturnableGatepassList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveReturnableGatepass(ReturnableGatepassRequest request) {
		Integer storeId = request.getStoreId();

		/*
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String gatepassNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
				screenId);

		ReturnableGatepass gatepass = new ReturnableGatepass(gatepassNumber, new Store(request.getStoreId()),
				new Vendor(request.getVendorId()), new AssetType(request.getAssetTypeId()),
				new RequestType(request.getAgainstId()), request.getReferenceNo(), request.getTotalAmount(),
				request.getTotalTaxAmount(), request.getNetAmount(), new Status(request.getStatusId()),
				new Status(request.getApprovalStatusId()), request.getRemark(),
				request.getGrnId() != null ? new GoodReceiptNote(request.getGrnId()) : null);

		// sessionService.setEntityDefaults(gatepass);
		gatepass.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		ReturnableGatepassDetail detail = null;

		for (ItemRequest item : request.getItems()) {
			if (request.getAssetTypeId() == Constants.consumableId) {

				// add if and else condition for resolved HIS-9736 Returnable Gatepass - Edit
				// mode shows the accepted quantity at physical inspection as available quantity
				if (request.getAgainstId().equals(15) || request.getAgainstId().equals(17)) {// 15 use for Rejected
																								// Against GRN
					// below code is origin code of cht
			                    
					if (item.getUomTypeId() != null && item.getUomUnitId() != null) {
						ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
								new Batch(item.getBatchId()),
								item.getVendorId() != null ? new Vendor(item.getVendorId())
										: new Vendor(request.getVendorId()),
								new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
								item.getReturnQuantity(), new Tax(item.getTaxId()), item.getTaxPercentage(),
								item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
								new Status(item.getStatusId()),
								item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId()) : null,
								item.getLcop() != null ? item.getLcop() : 0.0);
						returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
						// sessionService.setEntityDefaults(returnableGatepassDetail1);
						returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
						returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);
					} else {
						ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
								new Batch(item.getBatchId()),
								item.getVendorId() != null ? new Vendor(item.getVendorId())
										: new Vendor(request.getVendorId()),
								item.getReturnQuantity(), new Tax(item.getTaxId()), item.getTaxPercentage(),
								item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
								new Status(item.getStatusId()),
								item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId())
										: null);
						returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
						// sessionService.setEntityDefaults(returnableGatepassDetail1);
						returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
						returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);
					}
					// origin code end by cht
				} else {// add if and else condition for resolved HIS-9736 Returnable Gatepass - Edit
						// mode shows the accepted quantity at physical inspection as available quantity
					/* new code start by minesh for clubbatch and consignmentlogic */
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(item.getBatchId(),
							storeId, request.getUnitId());
					int returnQtyFromRequest = item.getReturnQuantity();
					for (StoreStock storeStockObj : StoreStockList) {
						if (returnQtyFromRequest != 0) {
							if (item.getUomTypeId() != null && item.getUomUnitId() != null) {
								Long storeAvaliableQuantity = Util.ConvertQuantity(Long.valueOf(returnQtyFromRequest),
										storeStockObj.getBatch().getBatchUomList(), new UomType(item.getUomTypeId()));
								Long batchId = storeStockObj.getBatch().getId();
								if (true) {
									ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
											new Batch(item.getBatchId()),
											item.getVendorId() != null ? new Vendor(item.getVendorId())
													: new Vendor(request.getVendorId()),
											new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
											returnQtyFromRequest, new Tax(item.getTaxId()), item.getTaxPercentage(),
											item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
											new Status(item.getStatusId()),
											item.getGrnDetailId() != null
													? new GoodReceiptNoteDetail(item.getGrnDetailId())
													: null,
											item.getLcop() != null ? item.getLcop() : 0.0);
									returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
									returnableGatepassDetail1.setReturnQty(
											item.getReturnQuantity() != null ? item.getReturnQuantity() : 0);
									returnableGatepassDetail1.setRejectQty(
											item.getRejectedQuantity() != null ? item.getRejectedQuantity() : 0);
									// sessionService.setEntityDefaults(returnableGatepassDetail1);
									returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(),
											request.getUnitId(), request.getUserId());
									returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
									detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);

									returnQtyFromRequest = 0;
								} else {
									int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();

									ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
											new Batch(item.getBatchId()),
											item.getVendorId() != null ? new Vendor(item.getVendorId())
													: new Vendor(request.getVendorId()),
											new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
											storeAvaliableQuantityINT, new Tax(item.getTaxId()),
											item.getTaxPercentage(), item.getTotalAmount(), item.getTotalTaxAmount(),
											item.getNetAmount(), new Status(item.getStatusId()),
											item.getGrnDetailId() != null
													? new GoodReceiptNoteDetail(item.getGrnDetailId())
													: null,
											item.getLcop() != null ? item.getLcop() : 0.0);
									returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
									// sessionService.setEntityDefaults(returnableGatepassDetail1);
									returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(),
											request.getUnitId(), request.getUserId());
									returnableGatepassDetail1.setReturnQty(
											item.getReturnQuantity() != null ? item.getReturnQuantity() : 0);
									returnableGatepassDetail1.setRejectQty(
											item.getRejectedQuantity() != null ? item.getRejectedQuantity() : 0);
									returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
									detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);

									returnQtyFromRequest = returnQtyFromRequest - storeAvaliableQuantityINT;
								}
							} else {
								Long storeAvaliableQuantity = (long) storeStockObj.getAvailableQty();
								Long batchId = storeStockObj.getBatch().getId();
								if (true) {

									ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
											new Batch(item.getBatchId()),
											item.getVendorId() != null ? new Vendor(item.getVendorId())
													: new Vendor(request.getVendorId()),
											returnQtyFromRequest, new Tax(item.getTaxId()), item.getTaxPercentage(),
											item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
											new Status(item.getStatusId()),
											item.getGrnDetailId() != null
													? new GoodReceiptNoteDetail(item.getGrnDetailId())
													: null);
									returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
									returnableGatepassDetail1.setReturnQty(
											item.getReturnQuantity() != null ? item.getReturnQuantity() : 0);
									returnableGatepassDetail1.setRejectQty(
											item.getRejectedQuantity() != null ? item.getRejectedQuantity() : 0);
									// sessionService.setEntityDefaults(returnableGatepassDetail1);
									returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(),
											request.getUnitId(), request.getUserId());
									returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
									detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);

									returnQtyFromRequest = 0;
								} else {
									int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();

									ReturnableGatepassDetail returnableGatepassDetail1 = new ReturnableGatepassDetail(
											new Batch(item.getBatchId()),
											item.getVendorId() != null ? new Vendor(item.getVendorId())
													: new Vendor(request.getVendorId()),
											storeAvaliableQuantityINT, new Tax(item.getTaxId()),
											item.getTaxPercentage(), item.getTotalAmount(), item.getTotalTaxAmount(),
											item.getNetAmount(), new Status(item.getStatusId()),
											item.getGrnDetailId() != null
													? new GoodReceiptNoteDetail(item.getGrnDetailId())
													: null);
									returnableGatepassDetail1.setTotalDiscount(item.getTotalDiscount());
									returnableGatepassDetail1.setReturnQty(
											item.getReturnQuantity() != null ? item.getReturnQuantity() : 0);
									// sessionService.setEntityDefaults(returnableGatepassDetail1);
									returnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(),
											request.getUnitId(), request.getUserId());
									returnableGatepassDetail1.setRejectQty(
											item.getRejectedQuantity() != null ? item.getRejectedQuantity() : 0);
									returnableGatepassDetail1.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
									detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail1);

									returnQtyFromRequest = returnQtyFromRequest - storeAvaliableQuantityINT;
								}

							}
						}
					}
					/* new code end by minesh for clubbatch and consignmentlogic */
				}

			} else if (request.getAssetTypeId() == Constants.fixedAssetId) {

				// add if and else condition for resolved HIS-9736 Returnable Gatepass - Edit
				// mode shows the accepted quantity at physical inspection as available quantity
				if (request.getAgainstId().equals(15) || request.getAgainstId().equals(17)) {// 15 use for Rejected
																								// Against GRN
					// below code is origin code of cht
					ReturnableGatepassDetail returnableGatepassDetail2 = new ReturnableGatepassDetail(
							new Batch(item.getBatchId()),
							item.getVendorId() != null ? new Vendor(item.getVendorId())
									: new Vendor(request.getVendorId()),
							item.getReturnQuantity(), item.getTotalAmount(), item.getTotalTaxAmount(),
							item.getNetAmount(), new Status(item.getStatusId()),
							item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId()) : null);
					returnableGatepassDetail2.setTotalDiscount(item.getTotalDiscount());
					returnableGatepassDetail2.setTaxPercentage(item.getTaxPercentage() != null ? item.getTaxPercentage() : null);
					// sessionService.setEntityDefaults(returnableGatepassDetail2);
//					returnableGatepassDetail2.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
					returnableGatepassDetail2.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail2);
					// origin code end by cht
				} // add if and else condition for resolved HIS-9736 Returnable Gatepass - Edit
					// mode shows the accepted quantity at physical inspection as available quantity
				else {
					/* new code start by minesh for clubbatch and consignmentlogic */
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(item.getBatchId(),
							storeId, request.getUnitId());
					int returnQtyFromRequest = item.getReturnQuantity();
					for (StoreStock storeStockObj : StoreStockList) {
						if (returnQtyFromRequest != 0) {
							Long storeAvaliableQuantity = (long) storeStockObj.getAvailableQty();
							Long batchId = storeStockObj.getBatch().getId();
							if (returnQtyFromRequest <= storeAvaliableQuantity) {
								ReturnableGatepassDetail returnableGatepassDetail2 = new ReturnableGatepassDetail(
										new Batch(item.getBatchId()),
										item.getVendorId() != null ? new Vendor(item.getVendorId())
												: new Vendor(request.getVendorId()),
										returnQtyFromRequest, item.getTaxId() != null ? new Tax(item.getTaxId()) : null,
										item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
										item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
										new Status(item.getStatusId()),
										item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId())
												: null);
								returnableGatepassDetail2.setTotalDiscount(item.getTotalDiscount());
//								returnableGatepassDetail2.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
								// sessionService.setEntityDefaults(returnableGatepassDetail2);
								returnableGatepassDetail2.setCreateDefaults(request.getOrganizationId(),
										request.getUnitId(), request.getUserId());
								detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail2);

								returnQtyFromRequest = 0;
							} else {
								int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();

								ReturnableGatepassDetail returnableGatepassDetail2 = new ReturnableGatepassDetail(
										new Batch(item.getBatchId()),
										item.getVendorId() != null ? new Vendor(item.getVendorId())
												: new Vendor(request.getVendorId()),
										storeAvaliableQuantityINT,
										item.getTaxId() != null ? new Tax(item.getTaxId()) : null,
										item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
										item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
										new Status(item.getStatusId()),
										item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId())
												: null);
								returnableGatepassDetail2.setTotalDiscount(item.getTotalDiscount());
//								returnableGatepassDetail2.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
								// sessionService.setEntityDefaults(returnableGatepassDetail2);
								returnableGatepassDetail2.setCreateDefaults(request.getOrganizationId(),
										request.getUnitId(), request.getUserId());
								detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail2);

								returnQtyFromRequest = returnQtyFromRequest - storeAvaliableQuantityINT;
							}
						}
					}
					/* new code end for by minesh clubbatch and consignmentlogic */
				}

				for (accessoriesReuest access : item.getAccessories()) {

					ReturnableGatepassAccessories returnableGatepassAccessories = new ReturnableGatepassAccessories(
							new Batch(item.getBatchId()), access.getReturnQuantity(), access.getSpecification());
					// sessionService.setEntityDefaults(returnableGatepassAccessories);
					returnableGatepassAccessories.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					detail.addReturnableGatepassAccessories(returnableGatepassAccessories);
				}
			}
			for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {

				Integer taxCompId = taxDetail.getTaxComponent().getId();
				Double taxPercentage = taxDetail.getTaxPercentage();
				Double amt = item.getTotalAmount();

				Double calTaxAmt = (amt * taxPercentage) / 100;

				ReturnableGatepassTaxComponent returnableGatepassTaxComponent = new ReturnableGatepassTaxComponent(
						calTaxAmt, taxPercentage,
						new ReturnableGatepassDetail(gatepass.getReturnableGatepassDetails().get(0).getId()),
						new TaxComponent(taxCompId));
				// sessionService.setEntityDefaults(returnableGatepassTaxComponent);
				returnableGatepassTaxComponent.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				detail.addReturnableGatepassTaxCom(returnableGatepassTaxComponent);

			}
		}
		// sessionService.setEntityDefaults(detail);
		detail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		ReturnableGatepassDao.saveReturnableGatepass(gatepass);
		CommonResponse response = new CommonResponse();
		response.setId(gatepass.getId().longValue());
        Integer index=0;
        Integer index1=0;
        for(ReturnableGatepassDetail grnDetail:gatepass.getReturnableGatepassDetails())
        {
            
            ItemRequest item=request.getItems().get(index);
                if (request.getAssetTypeId().equals(Constants.getConsumableId())) {
                	ReturnableBonusBatchExpiryDao.updateBonusBatchExpiry(grnDetail.getId());
                    for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry grnBatchExpiry : item.getReturnableBonusBatchExpiry()) {
                        returnableBonusBatchExpiry grnBatchExpiry1 = new returnableBonusBatchExpiry();
                                grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
                                grnBatchExpiry1.setExpiryDate(grnBatchExpiry.getExpiryDate());
                                grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
                                grnBatchExpiry1.setGrnDetailId(grnBatchExpiry.getGrnDetailId());
                                grnBatchExpiry1.setReturnableGatepassDetailId(grnDetail.getId());
                                grnBatchExpiry1.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId():sessionService.getOrganizationId());
                                grnBatchExpiry1.setUnitId(request.getUnitId()!=null ? request.getUnitId() :sessionService.getUnitId());
                                grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()!=null ? request.getUnitId() :sessionService.getUnitId()));
                                grnBatchExpiry1.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
                                grnBatchExpiry1.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
                                grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId()));
                                grnBatchExpiry1.setReturnableBonusBatchExpiryId(grnBatchExpiry.getReturnableBonusBatchExpiryId());
                                grnBatchExpiry1.setReturnQuantity(grnBatchExpiry.getReturnQuantity());
                                grnBatchExpiry1.setBatchId(grnBatchExpiry.getBatchId());
                                if(grnBatchExpiry1.getReturnQuantity()>=0)
                                {
                                	ReturnableBonusBatchExpiryDao.saveBonusBatchExpiry(grnBatchExpiry1);
                                }
                    }
                }
                index++;
        }
        for(ReturnableGatepassDetail grnDetail:gatepass.getReturnableGatepassDetails())
        {
            
            ItemRequest item=request.getItems().get(index1);
                if (request.getAssetTypeId().equals(Constants.getFixedAssetId())) {
                	ReturnableBonusBatchExpiryDao.updateBonusBatchExpiry(grnDetail.getId());
                    for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry grnBatchExpiry : item.getReturnableBonusBatchExpiry()) {
                        returnableBonusBatchExpiry grnBatchExpiry1 = new returnableBonusBatchExpiry();
                                grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
                                grnBatchExpiry1.setExpiryDate(null);
                                grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
                                grnBatchExpiry1.setGrnDetailId(grnBatchExpiry.getGrnDetailId());
                                grnBatchExpiry1.setReturnableGatepassDetailId(grnDetail.getId());
                                grnBatchExpiry1.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId():sessionService.getOrganizationId());
                                grnBatchExpiry1.setUnitId(request.getUnitId()!=null ? request.getUnitId() :sessionService.getUnitId());
                                grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()!=null ? request.getUnitId() :sessionService.getUnitId()));
                                grnBatchExpiry1.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
                                grnBatchExpiry1.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
                                grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId()));
                                grnBatchExpiry1.setReturnableBonusBatchExpiryId(grnBatchExpiry.getReturnableBonusBatchExpiryId());
                                grnBatchExpiry1.setBatchId(grnBatchExpiry.getBatchId());
                                grnBatchExpiry1.setReturnQuantity(grnBatchExpiry.getReturnQuantity());
                                if(grnBatchExpiry1.getReturnQuantity()>0)
                                {
                                	ReturnableBonusBatchExpiryDao.saveBonusBatchExpiry(grnBatchExpiry1);
                                }
                    }
                }
                index1++;
        }
		response.setMessage("Returnable Gatepass saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getReturnableGatepass(Integer id, Integer orgId, Integer unitId) {
		/*
		 * ReturnableGatepassResponse response = null; Integer assetType =
		 * ReturnableGatepassDao.getReturnableGatepassAssetType(id);
		 * 
		 * if (assetType.equals(Constants.consumableId)) { response =
		 * ReturnableGatepassDao.getReturnableGatepassResponse(id);
		 * 
		 * response.setItems(ReturnableGatepassDao.getReturnableGatepassDetails(id));
		 * 
		 * } else if (assetType.equals(Constants.fixedAssetId)) { response =
		 * ReturnableGatepassDao.getReturnableGatepassResponse(id);
		 */

		/*
		 * response.setItems(ReturnableGatepassDao.
		 * getReturnableGatepassResponseFixedType(id));
		 * response.addItems(ReturnableGatepassDao.
		 * getReturnableGatepassResponseFixedType(id));
		 */
		ReturnableGatepass returnableGatepass = ReturnableGatepassDao.getReturnableGatepass(id);

		ReturnableGatepassResponse response = new ReturnableGatepassResponse(returnableGatepass.getId(),
				returnableGatepass.getRetGpNo(), returnableGatepass.getAddedDate(),
				returnableGatepass.getStore().getStoreId(), returnableGatepass.getStore().getStoreName(),
				returnableGatepass.getVendor().getId(), returnableGatepass.getVendor().getName(),
				returnableGatepass.getAssetType().getId(), returnableGatepass.getAssetType().getType(),
				returnableGatepass.getRequestType().getId(), returnableGatepass.getRequestType().getType(),
				returnableGatepass.getReferenceNo(), returnableGatepass.getApprovalStatus().getId(),
				returnableGatepass.getApprovalStatus().getStatus(), returnableGatepass.getTotalAmt(),
				returnableGatepass.getTotalTaxAmount(), returnableGatepass.getNetAmount(),
				returnableGatepass.getStatus().getId(), returnableGatepass.getStatus().getStatus(),
				returnableGatepass.getRemark(), returnableGatepass.getAddedBy().getUserName());

		if (returnableGatepass.getAddedBy() != null)
			response.setRequestedBy(returnableGatepass.getAddedBy().getUserName());

		for (ReturnableGatepassDetail detail : returnableGatepass.getReturnableGatepassDetails()) {

			if (!detail.getIsDeleted()) {// start code for resolved HIS-10188 Returnable Gate Pass Master : After Update
											// Item Unable to Save Returnable Gate Pass

				// start code for resolved HIS-9736 Returnable Gatepass - Edit mode shows the
				// accepted quantity at physical inspection as available quantity
				Long getTotalAvailableQty = 0L;
				if (returnableGatepass.getRequestType().getId().equals(15) ) {// 15 use for Rejected Against GRN
					getTotalAvailableQty = (detail.getGoodReceiptNoteDetail().getRejectedQuantity().longValue());
					// getTotalAvailableQty = detail.getReturnQty().longValue();
					/*
					 * getTotalAvailableQty =
					 * Util.ConvertQuantity(Long.valueOf(getTotalAvailableQty),
					 * detail.getGoodReceiptNoteDetail().getBatch().getBatchUomList(),
					 * detail.getGoodReceiptNoteDetail().getGrnUomType());
					 */

				} else if (returnableGatepass.getRequestType().getId().equals(17)) {
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
							detail.getBatch().getId(), returnableGatepass.getStore().getStoreId(), unitId);
					int getTotalAvailableQty1 = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
					int getleaseQty=Util.GetLeaseQuantity(getTotalAvailableQty1, detail.getBatch().getBatchUomList(), detail.getUomType());
					getTotalAvailableQty = Util.ConvertQuantity(Long.valueOf(getleaseQty),
							detail.getBatch().getBatchUomList(), detail.getUomType());
				} else {
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
							detail.getBatch().getId(), returnableGatepass.getStore().getStoreId(), unitId);
					int getTotalAvailableQty1 = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
					int getleaseQty=Util.GetLeaseQuantity(getTotalAvailableQty1, detail.getBatch().getBatchUomList(), detail.getUomType());
					getTotalAvailableQty = Util.ConvertQuantity(Long.valueOf(getleaseQty),
							detail.getBatch().getBatchUomList(), detail.getUomType());

				}
				// end code for resolved HIS-9736 Returnable Gatepass - Edit mode shows the
				// accepted quantity at physical inspection as available quantity

				if (response.getAssetTypeId() == Constants.consumableId) {

					/*
					 * response.addItems(new ItemResponse(detail.getId(), detail.getBatch().getId(),
					 * detail.getVendor().getId(), detail.getUomType().getId(),
					 * detail.getUomUnit().getId(), detail.getTax().getId(),
					 * detail.getTaxPercentage(), detail.getReturnQty(), detail.getTotalAmt(),
					 * detail.getTotalTaxAmount(), detail.getNetAmount(),
					 * detail.getStatus().getId(), detail.getApprovedQty(), detail.getRejectQty(),
					 * detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
					 * detail.getBatch().getItem().getName(), detail.getUomType().getType(),
					 * detail.getUomUnit().getUnits(), detail.getBatch().getBatchNo(),
					 * detail.getBatch().getExpiryDate(), detail.getVendor().getName(),
					 * detail.getBatch().getStoreStockList().get(0).getAvailableQty(),
					 * detail.getBatch().getCop(), detail.getTax().getName(), detail.getLcop()));
					 */

					/* start code by minesh for clubbed batch */
					// getTotalAvailableQty =
					// Util.ConvertQuantity(Long.valueOf(getTotalAvailableQty),detail.getBatch().getBatchUomList(),
					// detail.getUomType()).intValue();
                    Integer gatepassId = ReturnableGatepassDao.getid(detail.getId());
                    Integer returnedQty = ReturnableGatepassDao.getDetailApprovedQantity(gatepassId,returnableGatepass.getRequestType().getId());
                    Integer grnQty = ReturnableGatepassDao.getDetailApprovedQantity1(gatepassId);
                    Double totalDiscountAmount = 0.00;
                    Double totalAmount = 0.00;
                    if(gatepassId == null){
                    	totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmountUsingBatchId(detail.getBatch().getId(),detail.getId()); 
                        totalAmount = ReturnableGatepassDao.totalAmountUsingBatchId(detail.getBatch().getId(),detail.getId()); 
                    }
                    else{
                    	totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmount(gatepassId); 
                        totalAmount = ReturnableGatepassDao.totalAmount(gatepassId); 
                    }
                    
                    Double actualDiscountPercentage=((totalDiscountAmount!=null ? totalDiscountAmount :0)*100)/(totalAmount!=null?totalAmount:0);
                    if (returnableGatepass.getRequestType().getId().equals(15) ) {
                    	getTotalAvailableQty=getTotalAvailableQty-returnedQty;
                    }
					ItemResponse ires = new ItemResponse(detail.getId(), detail.getBatch().getId(),
							detail.getVendor().getId(), detail.getUomType().getId(), detail.getUomUnit().getId(),
							detail.getTax().getId(), detail.getTaxPercentage(), detail.getReturnQty(),
							detail.getTotalAmt(), detail.getTotalTaxAmount(), detail.getNetAmount(),
							detail.getStatus().getId(), detail.getApprovedQty(), detail.getRejectQty(),
							detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
							detail.getBatch().getItem().getName(), detail.getUomType().getType(),
							detail.getUomUnit().getUnits(), detail.getBatch().getBatchNo(),
							// start code for resolved HIS-9746 Lot number not visible on the returnable
							// gate pass screen and visible on the returnable gate pass approval
							detail.getBatch().getLotNo() != null ? detail.getBatch().getLotNo() : "-",
							// end code for resolved HIS-9746 Lot number not visible on the returnable gate
							// pass screen and visible on the returnable gate pass approval
							detail.getBatch().getExpiryDate(), detail.getVendor().getName(),
							getTotalAvailableQty.intValue(), detail.getBatch().getCop(), detail.getTax().getName(),
							detail.getLcop(), detail.getBatch().getItem().getIsThreeUOMApplicable());
					ires.setTotalDiscount(detail.getTotalDiscount());
					ires.setPhyRejectQuantity(detail.getGoodReceiptNoteDetail()!=null ? detail.getGoodReceiptNoteDetail().getRejectedQuantity() : 0);
					if(ires.getPhyRejectQuantity()==null || ires.getPhyRejectQuantity()==0)
					{
						ires.setPhyRejectQuantity(detail.getGoodReceiptNoteDetail()!=null && detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail()!=null && detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail().get(0).getRejectQuantity()!=null ? detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail().get(0).getRejectQuantity():0);					}
					if(detail.getGoodReceiptNoteDetail() != null) {
					Integer bonusApprovedQuantity=GlobalDao.getBonusApprovedQty(detail.getGoodReceiptNoteDetail().getId());
					ires.setBonusApprovedQuantity(bonusApprovedQuantity);
					}
					if(Double.isNaN(actualDiscountPercentage)) {
						ires.setActualDiscountPercentage(0.0);
					}else {
						ires.setActualDiscountPercentage(actualDiscountPercentage!= null ? actualDiscountPercentage : 0.0);
					}
					List<returnableBonusBatchExpiry> getList= GlobalDao.getReturnableBonusBatchExpiryByGrnDetailId(detail.getId());
                    List<com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry> getList3=new ArrayList<>();
                    for (returnableBonusBatchExpiry getList1 : getList) {
                    	com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry getList2=new com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry();
                        getList2.setBatchNo(getList1.getBatchNo());
                        getList2.setBonusQuantity(getList1.getBonusQuantity());
                        getList2.setExpiryDate(getList1.getExpiryDate());
                        getList2.setReturnableBonusBatchExpiryId(getList1.getReturnableBonusBatchExpiryId());
                        getList2.setBatchId(getList1.getBatchId());
                        Integer returnedBonusQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
                        getList2.setReturnedQuantity(returnedBonusQty);
                        getList2.setReturnQuantity(getList1.getReturnQuantity());
                        getList2.setGrnDetailId(getList1.getGrnDetailId());
                        getList2.setReturnableGatepassDetailId(getList1.getReturnableGatepassDetailId());
                        getList3.add(getList2);
                    }
                    ires.setReturnableBonusBatchExpiry(getList3);
                    if(detail.getGoodReceiptNoteDetail() != null) {
	                    Integer getBonusTypeId=GlobalDao.getBonusTypeId(detail.getGoodReceiptNoteDetail().getId());
                    	if(getBonusTypeId.equals(1) && (detail.getBatch().getBatchUomList() != null && detail.getBatch().getBatchUomList().size() != 0 ))
						{
                    		ires.setBonusApprovedQuantity(ires.getBonusApprovedQuantity()*(detail.getBatch().getBatchUomList().get(0).getConversions()*detail.getBatch().getBatchUomList().get(1).getConversions()));
						}
						if(getBonusTypeId.equals(2) && (detail.getBatch().getBatchUomList() != null && detail.getBatch().getBatchUomList().size() != 0 ))
						{
							ires.setBonusApprovedQuantity(ires.getBonusApprovedQuantity()*(detail.getBatch().getBatchUomList().get(1).getConversions()));
						}
						ires.setBonusUomTypeId(getBonusTypeId);
					}
//                    }
                    
					ires.setTotalGRNQuantity(grnQty);
					ires.setApprovedReturnedQty(returnedQty);
					ires.setReturnQuantity(detail.getReturnQty() != null ? detail.getReturnQty() : 0);
					List<UomResponse> uomList = GlobalDao.getBatchUomList(detail.getBatch().getId());
					ires.setUomList(uomList);
					response.addItems(ires);
					/* end code by minesh for clubbed batch */

				} else if (response.getAssetTypeId() == Constants.fixedAssetId) {

					/*
					 * response.addItems(new ItemResponse(detail.getId(), detail.getBatch().getId(),
					 * detail.getVendor().getId(), detail.getTax().getId(),
					 * detail.getTaxPercentage(), detail.getReturnQty(), detail.getTotalAmt(),
					 * detail.getTotalTaxAmount(), detail.getNetAmount(),
					 * detail.getStatus().getId(), detail.getApprovedQty(), detail.getRejectQty(),
					 * detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
					 * detail.getBatch().getItem().getName(), detail.getBatch().getBatchNo(),
					 * detail.getBatch().getExpiryDate(), detail.getVendor().getName(),
					 * detail.getBatch().getStoreStockList().get(0).getAvailableQty(),
					 * detail.getBatch().getCop(), detail.getTax().getName(),
					 * PopulateAccessories(ReturnableGatepassDao.
					 * getReturnableGatepassAccessoriesList(detail.getId())), detail.getLcop()));
					 */

					/* start code by minesh for clubbed batch */
					 Integer gatepassId = ReturnableGatepassDao.getid(detail.getId());
					 Integer returnedQty = ReturnableGatepassDao.getDetailApprovedQantity(gatepassId,returnableGatepass.getRequestType().getId());
	                    Integer grnQty = ReturnableGatepassDao.getDetailApprovedQantity1(gatepassId);
	                    Double totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmount(gatepassId); 
	                    Double totalAmount = ReturnableGatepassDao.totalAmount(gatepassId); 
	                    Double actualDiscountPercentage=((totalDiscountAmount!=null ? totalDiscountAmount :0)*100)/(totalAmount!=null?totalAmount:0);
	                    if (returnableGatepass.getRequestType().getId().equals(15) ) {
	                    	getTotalAvailableQty=getTotalAvailableQty-returnedQty;
	                    }
	                    ItemResponse ires =new ItemResponse(detail.getId(), detail.getBatch().getId(),
							detail.getVendor().getId(), detail.getTax() != null ? detail.getTax().getId() : null,
							detail.getTaxPercentage() != null ? detail.getTaxPercentage() : detail.getTaxPercentage(),
							detail.getReturnQty(), detail.getTotalAmt(), detail.getTotalTaxAmount(),
							detail.getNetAmount(), detail.getStatus().getId(), detail.getApprovedQty(),
							detail.getRejectQty(), detail.getBatch().getItem().getId(),
							detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
							detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(),
							detail.getVendor().getName(), getTotalAvailableQty.intValue(), detail.getBatch().getCop(),
							detail.getTax() != null ? detail.getTax().getName() : null,
							PopulateAccessories(
									ReturnableGatepassDao.getReturnableGatepassAccessoriesList(detail.getId())),
							detail.getLcop(),detail.getTotalDiscount(), detail.getBatch().getItem().getIsThreeUOMApplicable());
	                    ires.setApprovedReturnedQty(returnedQty);
	                    ires.setActualDiscountPercentage(actualDiscountPercentage);
	                    ires.setTotalGRNQuantity(grnQty);
	                    ires.setPhyRejectQuantity(detail.getGoodReceiptNoteDetail()!=null ? detail.getGoodReceiptNoteDetail().getRejectedQuantity() : 0);
	                    if(ires.getPhyRejectQuantity()==null || ires.getPhyRejectQuantity()==0)
						{
	                    	ires.setPhyRejectQuantity(detail.getGoodReceiptNoteDetail()!=null && detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail()!=null && detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail().get(0).getRejectQuantity()!=null ? detail.getGoodReceiptNoteDetail().getPhysicalInspectionDetail().get(0).getRejectQuantity():0);						}
	                    response.addItems(ires);
	                    List<returnableBonusBatchExpiry> getList= GlobalDao.getReturnableBonusBatchExpiryByGrnDetailId(detail.getId());
	                    List<com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry> getList3=new ArrayList<>();
	                    for (returnableBonusBatchExpiry getList1 : getList) {
	                    	com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry getList2=new com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry();
	                        getList2.setBatchNo(getList1.getBatchNo());
	                        getList2.setBonusQuantity(getList1.getBonusQuantity());
	                        getList2.setExpiryDate(getList1.getExpiryDate());
	                        getList2.setReturnableBonusBatchExpiryId(getList1.getReturnableBonusBatchExpiryId());
	                        getList2.setBatchId(getList1.getBatchId());
	                        Integer returnedBonusQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
	                        getList2.setReturnedQuantity(returnedBonusQty);
	                        getList2.setReturnQuantity(getList1.getReturnQuantity());
	                        getList2.setGrnDetailId(getList1.getGrnDetailId());
	                        getList2.setReturnableGatepassDetailId(getList1.getReturnableGatepassDetailId());
	                        getList3.add(getList2);
	                    }
	                    ires.setReturnableBonusBatchExpiry(getList3);
					/* end code by minesh for clubbed batch */

				}

			} // end isDelete condation

		}

		return response;
	}

	private List<accessoriesResponse> PopulateAccessories(List<ReturnableGatepassAccessories> InwardAccessories) {
		List<accessoriesResponse> response = new ArrayList<accessoriesResponse>();

		for (ReturnableGatepassAccessories accessories : InwardAccessories) {
			response.add(new accessoriesResponse(accessories.getId(), accessories.getBatch().getId(),
					accessories.getBatch().getItem().getId(), accessories.getBatch().getItem().getCode(),
					accessories.getBatch().getItem().getName(), accessories.getQuantity(),
					accessories.getSpecification(), accessories.getApprovedQty()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateReturnableGatepass(ReturnableGatepassRequest request) {

		ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(request.getId());
		/*
		 * gatepass.updateReturnableGatepass(request.getTotalAmount(),
		 * request.getTotalTaxAmount(), request.getNetAmount(),
		 * request.getReferenceNo(), request.getRemark());
		 */

		if (request.getTotalAmount() != null) {
			gatepass.setTotalAmt(request.getTotalAmount());
		}
		if (request.getTotalTaxAmount() != null) {
			gatepass.setTotalTaxAmount(request.getTotalTaxAmount());
		}
		if (request.getNetAmount() != null) {
			gatepass.setNetAmount(request.getNetAmount());
		}
		if (request.getReferenceNo() != null) {
			gatepass.setReferenceNo(request.getReferenceNo());
		}
		if (request.getRemark() != null) {
			gatepass.setRemark(request.getRemark());
		}

		for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {

			Integer id = null;
			for (ItemRequest item : request.getItems()) {
				if (request.getAssetTypeId() == Constants.consumableId)
				{
					detail.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
				}
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					if (item.getReturnQuantity() != null) {
						detail.setReturnQty(item.getReturnQuantity());
					}
					if (item.getTotalAmount() != null) {
						detail.setTotalAmt(item.getTotalAmount());
					}
					if (item.getTotalTaxAmount() != null) {
						detail.setTotalTaxAmount(item.getTotalTaxAmount());
					}
					if (item.getNetAmount() != null) {
						detail.setNetAmount(item.getNetAmount());
					}
					/*
					 * detail.updateReturnableGatepassDetail(item.getReturnQuantity(),
					 * item.getTotalAmount(),item.getTotalTaxAmount(), item.getNetAmount());
					 */
					for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry grnBatchExpiry : item.getReturnableBonusBatchExpiry()) {
                        returnableBonusBatchExpiry grnBatchExpiry1 = new returnableBonusBatchExpiry();
                                grnBatchExpiry1.setReturnableBonusBatchExpiryId(grnBatchExpiry.getReturnableBonusBatchExpiryId());
                                grnBatchExpiry1.setReturnQuantity(grnBatchExpiry.getReturnQuantity());
                                if(grnBatchExpiry1.getReturnQuantity()==null)
                                {
                                	grnBatchExpiry1.setReturnQuantity(0);
                                }
                                	ReturnableBonusBatchExpiryDao.updateReturnQty(grnBatchExpiry1);
                    }
					if (request.getAssetTypeId() == Constants.fixedAssetId && item.getAccessories() != null) {
						Integer accessoriesId = null;
						for (accessoriesReuest access : item.getAccessories()) {
							accessoriesId = access.getId();
							if (access.getId() != null) {

								ReturnableGatepassAccessories accessories = new ReturnableGatepassAccessories();
								if (access.getReturnQuantity() != null) {
									accessories.setQuantity(access.getReturnQuantity());
								}
								if (access.getSpecification() != null) {
									accessories.setSpecification(access.getSpecification());
								}
								if (access.getBatchId() != null) {
									accessories.setBatch(new Batch(item.getBatchId()));
								}
								detail.updateReturnableGatepassAccessories(accessories);

								break;
							}

							if (accessoriesId == null) {
								detail.setIsDeleted(true);

							}
							detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							// detail.setUpdatedBy(detail.getUser());
							detail.setUpdatedByUser(request.getUserId());
						}
						if (item.getAccessories() != null) {
							for (accessoriesReuest access : item.getAccessories()) {

								if (access.getId() == null) {
									ReturnableGatepassAccessories accessories = new ReturnableGatepassAccessories(
											new Batch(item.getBatchId()), access.getReturnQuantity(),
											access.getSpecification());
									// sessionService.setEntityDefaults(accessories);
									accessories.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
											request.getUserId());
									detail.addReturnableGatepassAccessories(accessories);

									break;
								}
							}
						}

					} else if (request.getAssetTypeId() == Constants.consumableId) {
						if (item.getUomTypeId() != null) {
							detail.setUomType(new UomType(item.getUomTypeId()));
						}
						if (item.getUomUnitId() != null) {
							detail.setUomUnit(new UomUnit(item.getUomUnitId()));
						}

					}

				}

			}

			/*
			 * start code for resolved HIS-10188 Returnable Gate Pass Master : After Update
			 * Item Unable to Save Returnable Gate Pass
			 */
			if (id == null) {
				detail.setIsDeleted(true);
			}
			/*
			 * end code for resolved HIS-10188 Returnable Gate Pass Master : After Update
			 * Item Unable to Save Returnable Gate Pass
			 */

		}

		ReturnableGatepassDetail detail = null;

		for (ItemRequest item : request.getItems()) {
			if (item.getId() == null) {

				if (request.getAssetTypeId() == Constants.consumableId) {

					ReturnableGatepassDetail returnableGatepassDetail = new ReturnableGatepassDetail(
							new Batch(item.getBatchId()),
							item.getVendorId() != null ? new Vendor(item.getVendorId())
									: new Vendor(request.getVendorId()),
							new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
							item.getReturnQuantity(), new Tax(item.getTaxId()), item.getTaxPercentage(),
							item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
							new Status(item.getStatusId()));
					/*
					 * start code for resolved HIS-10188 Returnable Gate Pass Master : After Update
					 * Item Unable to Save Returnable Gate Pass
					 */
					returnableGatepassDetail.setBonusUomType(new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() : item.getUomUnitId()));
					returnableGatepassDetail.setGoodReceiptNoteDetail(
							item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId()) : null);
					// returnableGatepassDetail.setLcop(item.getLcop());
					/*
					 * end code for resolved HIS-10188 Returnable Gate Pass Master : After Update
					 * Item Unable to Save Returnable Gate Pass
					 */

					// sessionService.setEntityDefaults(returnableGatepassDetail);
					returnableGatepassDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());

					detail = gatepass.addReturnableGatepassDetail(returnableGatepassDetail);
				} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
					/*
					 * detail = gatepass.addReturnableGatepassDetail(new ReturnableGatepassDetail(
					 * new Batch(item.getBatchId()), new Vendor(item.getVendorId()),
					 * item.getReturnQuantity(), new Tax(item.getTaxId()), item.getTaxPercentage(),
					 * item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(), new
					 * Status(item.getStatusId())));
					 */

					/*
					 * start code for resolved HIS-10188 Returnable Gate Pass Master : After Update
					 * Item Unable to Save Returnable Gate Pass
					 */
					ReturnableGatepassDetail returnableGatepassDetail = new ReturnableGatepassDetail(
							new Batch(item.getBatchId()),
							item.getVendorId() != null ? new Vendor(item.getVendorId())
									: new Vendor(request.getVendorId()),
							item.getReturnQuantity(), new Tax(item.getTaxId()), item.getTaxPercentage(),
							item.getTotalAmount(), item.getTotalTaxAmount(), item.getNetAmount(),
							new Status(item.getStatusId()));
					returnableGatepassDetail.setGoodReceiptNoteDetail(
							item.getGrnDetailId() != null ? new GoodReceiptNoteDetail(item.getGrnDetailId()) : null);
					/*
					 * end code for resolved HIS-10188 Returnable Gate Pass Master : After Update
					 * Item Unable to Save Returnable Gate Pass
					 */
					if (item.getAccessories() != null) {

						for (accessoriesReuest access : item.getAccessories()) {

							ReturnableGatepassAccessories returnableGatepassAccessories = new ReturnableGatepassAccessories(
									new Batch(item.getBatchId()), access.getReturnQuantity(),
									access.getSpecification());
							// sessionService.setEntityDefaults(returnableGatepassAccessories);
							returnableGatepassAccessories.setCreateDefaults(request.getOrganizationId(),
									request.getUnitId(), request.getUserId());
							detail.updateReturnableGatepassAccessories(returnableGatepassAccessories);

						}
					}
				}
				// sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			}

		}

		ReturnableGatepassDao.updateReturnableGatepass(gatepass);

		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object submitReturnableGatepass(ReturnableGatepassRequest request) {
		for (Integer id : request.getGatepassId()) {
			ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(id);

			gatepass.setStatus(new Status(request.getStatusId()));
			gatepass.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
				if (!detail.getIsDeleted()) {
					detail.setStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
					// detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());
					
					
					List<TaxComponentDetailsDto> taxDetailsList = grnDao.getTaxDetails(detail.getTax()!=null ? detail.getTax().getId():0);
					
					Double natAmount = detail.getTotalAmt();
					
					if(taxDetailsList != null && taxDetailsList.size()>0) {
						
						for (TaxComponentDetailsDto taxDetail : taxDetailsList) {
							
							try {
								
								Double taxComponentAmount = (natAmount / 100)*taxDetail.getTaxComponentPercentage();
								
								TaxComponentDetailsDto dto  = new TaxComponentDetailsDto();
								dto.setCreatedBy(request.getUserId());
								dto.setItemId(detail.getBatch().getItem().getId());
								dto.setOrganizationId(detail.getOrganizationId());
								dto.setStatus('A');
								dto.setGrnRetrunId(id);
								dto.setGrnRetrunDetailsId(detail.getId());
								dto.setTaxAmount(detail.getTotalTaxAmount());
								dto.setTaxComponentAmount(taxComponentAmount);
								dto.setTaxComponentId(taxDetail.getTaxComponentId());
								dto.setTaxComponentName(taxDetail.getTaxComponentName());
								dto.setTaxComponentPercentage(taxDetail.getTaxComponentPercentage());
								dto.setTaxId(detail.getTax().getId());
								dto.setTaxPercentage(detail.getTax().getTaxPercentage());
								dto.setUnitId(gatepass.getUnitId());
								dto.setUpdatedBy(request.getUserId());
								iTaxComponentDetailsDao.saveTaxComponentDetails(dto);

							} catch (Exception e) {
								logger.error("Error while saving tax component details for returnable gatepass", e);
								throw new ServiceException(e.getMessage());
							}
							
						}
						
					}
					
				}
			}

			gatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
			// gatepass.setUpdatedBy(gatepass.getUser());
			gatepass.setUpdatedByUser(request.getUserId());

			ReturnableGatepassDao.updateReturnableGatepass(gatepass);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass submitted successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelReturnableGatepass(ReturnableGatepassRequest request) {

		ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(request.getId());

		for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
			if (!detail.getIsDeleted()) {
				detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
				// detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}
		}

		gatepass.setCancelReason(new CancelReason(request.getCancelReasonId()));
		gatepass.setCancelNote(request.getCancelNote());

		gatepass.setStatus(new Status(request.getStatusId()));

		gatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
		// gatepass.setUpdatedBy(gatepass.getUser());
		gatepass.setUpdatedByUser(request.getUserId());

		ReturnableGatepassDao.updateReturnableGatepass(gatepass);

		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object checkReturnableGatePass(List<ReturnableGatepassRequest> request) {

		List<ReturnableGatepassResponse> list = new ArrayList<>();

		list = ReturnableGatepassDao.checkReturnableGatePass(request);

		for(int i =0 ; i< request.size(); i++){
			if(list.size()>0){
				for(int j =0 ; j< list.size(); j++){
					System.out.println(request.get(i).getGrnDetailId());
					System.out.println(list.get(j).getGrnDetailId());
					if(list.get(j).getGrnDetailId().equals(request.get(i).getGrnDetailId())){
						request.get(i).setCheckReturnableGatePassFlag(true);
					}
					else{
						if(!request.get(i).getCheckReturnableGatePassFlag()){
							request.get(i).setCheckReturnableGatePassFlag(false);
						}
					}

				}
			}
			else{
				request.get(i).setCheckReturnableGatePassFlag(false);
			}
		}
		return request;
	}

}
