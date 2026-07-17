package com.param.procurement.purchase.service.purchaseOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.BonusUomMapper;
import com.param.entity.model.procurement.PoAccessoriesMapper;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.dao.purchaseOrder.IPurchaseOrderDao;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperRequest;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomRequest;
import com.param.procurement.purchase.purchaseOrder.dto.LastPurchaseOrderDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailsRespDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDetailsDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderSearchDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.purchaseOrder.dto.QrDateDto;
import com.param.procurement.quotation.dao.quotationRequest.IQuotationRequestDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
@Transactional
public class PurchaseOrderService implements IPurchaseOrderService {

	private Integer screenId = Screen.PurchaseOrderCreation.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;

	@Autowired
	IQuotationRequestDao quotationRequestDao;

	@Override
	@Transactional
	public PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		PurchaseOrderDropDownDto purchaseOrdDropDownDto = new PurchaseOrderDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			purchaseOrdDropDownDto.addRequestingStore(
					new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			purchaseOrdDropDownDto
					.addDeliveryStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			purchaseOrdDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			purchaseOrdDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		for (PaymentMode paymentMode : GlobalDao.getPaymentModeList()) {
			purchaseOrdDropDownDto.addPaymentMode(new com.param.global.dto.global.PaymentMode(
					paymentMode.getPaymentModeId(), paymentMode.getPaymentModeCode()));
		}

		return purchaseOrdDropDownDto;
	}

	@Override
	@Transactional
	public Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto) {
		PurchaseOrdDto.setToDate((new DateTime(PurchaseOrdDto.getToDate())).plusDays(1).toDate());

		List<PurchaseOrderSearchDto> purchaseOrderSearchDtoList = new ArrayList<PurchaseOrderSearchDto>();

		for (PurchaseOrder purchaseOrder : purchaseOrderDao.searchPurchaseOrderList(PurchaseOrdDto, screenId)) {

			if(PurchaseOrdDto.getScreenId()!=null && PurchaseOrdDto.getScreenId().equals(2) && !purchaseOrder.getStatus().getId().equals(23) )
			{
				PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
						purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),
						purchaseOrder.getStore().getStoreId(), purchaseOrder.getAssetType().getType(),
						purchaseOrder.getRequestType().getType(), purchaseOrder.getApprovalStatus().getStatus(),
						purchaseOrder.getStatus().getStatus(), purchaseOrder.getAddedBy().getUserName(),
						purchaseOrder.getRemark(), purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate(),
						purchaseOrder.getRequestingStore().getStoreId(), purchaseOrder.getRequestingStore().getStoreName(),
						purchaseOrder.getIsConsignment());
				purchaseOrderSearchDtoList.add(purchaseReqSearchDto);
			}
			else if(PurchaseOrdDto.getScreenId()!=null && PurchaseOrdDto.getScreenId().equals(1) )
			{
				PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
						purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),
						purchaseOrder.getStore().getStoreId(), purchaseOrder.getAssetType().getType(),
						purchaseOrder.getRequestType().getType(), purchaseOrder.getApprovalStatus().getStatus(),
						purchaseOrder.getStatus().getStatus(), purchaseOrder.getAddedBy().getUserName(),
						purchaseOrder.getRemark(), purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate(),
						purchaseOrder.getRequestingStore().getStoreId(), purchaseOrder.getRequestingStore().getStoreName(),
						purchaseOrder.getIsConsignment());
				purchaseOrderSearchDtoList.add(purchaseReqSearchDto);
			}
		

			

		}
		return purchaseOrderSearchDtoList;
	}

	@Override
	@Transactional
	public Object savePurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) throws CustomException {

		/*
		 * Random rand = new Random(); int num = rand.nextInt(1000); String
		 * purchaseOrderNo = "PO-" + num;
		 */
		/*Integer storeId = purchaseOrderDto.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String purchaseOrderNo = GlobalService.getBillNoByGeneratedCode(purchaseOrderDto.getStoreId(), purchaseOrderDto.getUnitId()!=null ? purchaseOrderDto.getUnitId() : sessionService.getUnitId(), screenId);
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();

		PurchaseOrder purchaseOrder = new PurchaseOrder(new Status(purchaseOrderDto.getApprovalStatusId()),
				new AssetType(purchaseOrderDto.getAssetTypeId()), purchaseOrderDto.getIsConsignment(),
				purchaseOrderDto.getNetAmount(), purchaseOrderNo,
				purchaseOrderDto.getRemark() != null ? purchaseOrderDto.getRemark() : null,
				purchaseOrderDto.getRoundOffAmount(), new Status(purchaseOrderDto.getStatusId()),
				new Store(purchaseOrderDto.getStoreId()), new RequestType(purchaseOrderDto.getPoTypeId()),
				purchaseOrderDto.getTotalAmount(), purchaseOrderDto.getTotalDiscount(),
				purchaseOrderDto.getTotalOtherAmount(), purchaseOrderDto.getTotalTaxAmount(),
				new Vendor(purchaseOrderDto.getVendorId()), purchaseOrderDto.getPaymentTerm(),
				new Store(purchaseOrderDto.getRequestingStoreId()));
		purchaseOrder.setAddedByUser(purchaseOrderDto.getUserId());
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());
		purchaseOrder.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
		purchaseOrder.setOrganizationId(purchaseOrderDto.getOrganizationId());
		purchaseOrder.setUnitId(purchaseOrderDto.getUnitId());
		purchaseOrder.setDueDate(GlobalCommonDateUtils.getDate(purchaseOrderDto.getDueDate(), "dd-M-yyyy HH:mm:ss"));
		//sessionService.setEntityDefaults(purchaseOrder);
		if (purchaseOrderDto.getOtherCharges() != null) {
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
				if (poOtherCharges.getOtherChargesId() != null) {
					PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
							new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					purchaseOrderOtherChargesMapper.setIsFromPo(poOtherCharges.getIsFromPo());
					purchaseOrderOtherChargesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
				}
			}
		}
		if (purchaseOrderDto.getDiscount() != null) {
			purchaseOrder.setDiscount(purchaseOrderDto.getDiscount());
			purchaseOrder.setDiscountType(new DiscountType(purchaseOrderDto.getDiscountTypeId()));
		}
		PurchaseOrderDetail purchaseOrderDetail = null;

		for (PurchaseOrderDetailDto item : purchaseOrderDto.getItems()) {

			if (purchaseOrderDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				PurchaseOrderDetail purchaseOrderDetail2 = new PurchaseOrderDetail(new Item(item.getItemId()),
						item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
						item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
						new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
						item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
						item.getNetAmount(), new Manufacturer(item.getManufactureId()),
						item.getSpecification() != null ? item.getSpecification() : null,
						item.getRemark() != null ? item.getRemark() : null, new UomType(item.getPoUomTypeId()),
						new UomUnit(item.getPoUomUnitId()),
						item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
						item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,

						new Status(item.getStatusId()));
				purchaseOrderDetail2.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity() :0);
				purchaseOrderDetail2.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
				
				purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail2);

				if (item.getPoUom() != null) {
					for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
						PurchaseOrderUomMapper purchaseOrderUomMapper = new PurchaseOrderUomMapper(
								new UomType(poUomMapperDto.getUomTypeId()), new UomUnit(poUomMapperDto.getUomUnitId()),
								poUomMapperDto.getConversion(), poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(),
								poUomMapperDto.getStoreUom());
						purchaseOrderUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addPurchaseOrderUomMapperList(purchaseOrderUomMapper);

					}

				}
				if (item.getBonusUom() != null) {
					for (PurchaseOrderUomRequest bonusUomMapperDto : item.getBonusUom()) {
						BonusUomMapper bonusUomMapper = new BonusUomMapper(
								new UomType(bonusUomMapperDto.getUomTypeId()),
								new UomUnit(bonusUomMapperDto.getUomUnitId()), bonusUomMapperDto.getConversion(),
								bonusUomMapperDto.getIpUom(), bonusUomMapperDto.getOpUom(),
								bonusUomMapperDto.getStoreUom());
						bonusUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addBonusUomMapperList(bonusUomMapper);
					}
				}

				purchaseOrderDetail.setPoLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
						purchaseOrderDetail.getPurchaseOrderUomMapperList(), new UomType(item.getPoUomTypeId())));
				if (item.getBonusQuantity() != null) {
					purchaseOrderDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getBonusQuantity(),
							purchaseOrderDetail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId())));

				}

			} else if (purchaseOrderDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				PurchaseOrderDetail purchaseOrderDetailnew = new PurchaseOrderDetail(
						new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(), item.getCop(),
						item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null, new Tax(item.getTaxId()),
						item.getTaxPercentage() != null ? item.getTaxPercentage() : null, item.getTotalAmount(),
						item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
						item.getNetAmount(), new Manufacturer(item.getManufactureId()),
						item.getSpecification() != null ? item.getSpecification() : null,
						item.getRemark() != null ? item.getRemark() : null, new Status(item.getStatusId()),
						item.getWarrentyPeriod(), item.getSupportTerms(), item.getTechnicalSpecification(),
						item.getExpectedPayment(), item.getWarrantyDate(), new Vendor(item.getServiceProviderId()),item.getPendingQuantity()!=null ? item.getPendingQuantity():0 );
				purchaseOrderDetailnew.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
				purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetailnew);
				
				if (item.getPoAccessoriesMapper() != null) {
					com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapper = item
							.getPoAccessoriesMapper();
					for (Integer itemId : mapper.getItemIds()) {
						PoAccessoriesMapper poAccessoriesMapper = new PoAccessoriesMapper(new Item(itemId));
						poAccessoriesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addPoAccessoriesMapper(poAccessoriesMapper);
					}
				}
				
			}

			/*
			 * if (item.getOtherCharges() != null) { for (PoOtherChargesMapperRequest
			 * poOtherCharges : item.getOtherCharges()) { if
			 * (poOtherCharges.getOtherChargesId() != null) {
			 * PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new
			 * PurchaseOrderOtherChargesMapper( new
			 * OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
			 * sessionService.setEntityDefaults(purchaseOrderOtherChargesMapper);
			 * purchaseOrderDetail.addPoOtherChargesMapperList(
			 * purchaseOrderOtherChargesMapper); } } }
			 */

			if (item.getPurchaseOrderStaged() != null) {
				for (PurchaseOrderStagedDto pucrhaseOrderStaged : item.getPurchaseOrderStaged()) {
					com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
							pucrhaseOrderStaged.getQuantity(), pucrhaseOrderStaged.getStagedDate());
					purchaseOrderStaged.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					
					purchaseOrderDetail.addPurchaseOrderStaged(purchaseOrderStaged);
				}
			}

			/*
			 * if (purchaseOrderDto.getPoTypeId() ==
			 * com.param.common.constants.RequestType.AgainstPurchaseRequest
			 * .getRequestType() && item.getPrId() != null) { List<PurchaseRequestDetail>
			 * purchaseRequestDetails =
			 * purchaseOrderDao.getPurchaseRequestDetails(item.getPrId()); for
			 * (PurchaseRequestDetail prDetail : purchaseRequestDetails) {
			 * hm.put(prDetail.getId(), prDetail); } }
			 */

			if (purchaseOrderDto.getPoTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest
					.getRequestType() && item.getPrId() != null) {

				purchaseOrderDetail.setPutchRequestDetail(new PurchaseRequestDetail(item.getPrId().getFirst()));
				purchaseOrderDetail.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity():0);

			}
			if (purchaseOrderDto.getPoTypeId() == com.param.common.constants.RequestType.AgainstQuotationRequest
					.getRequestType() && item.getQrId() != null) {

				purchaseOrderDetail.setQuotationRequestDetail(new QuotationRequestDetail(item.getQrId().getFirst()));
				Integer pendingQuantity;
				for (Integer qrDetailId : item.getQrId()) {
					QuotationRequestDetail quotationRequestDetail = quotationRequestDao
							.getQuotationRequestPendingQuantity(qrDetailId, item.getItemId());
					if (quotationRequestDetail.getPendingQuantity() == null) {
						throw new CustomException("Pending Quantity Not Found");
					}
					pendingQuantity = quotationRequestDetail.getPendingQuantity() - item.getPoQuantity();
					quotationRequestDetail.setPendingQuantity(pendingQuantity);
					purchaseOrderDetail.setQuotationRequestDetail(quotationRequestDetail);
					purchaseOrderDetail.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity():0);

				}

			}

			/*
			 * if (purchaseOrderDto.getPoTypeId() ==
			 * com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType(
			 * ) && item.getPrId() != null) { List<PurchaseRequestDetail>
			 * purchaseRequestDetails =
			 * purchaseOrderDao.getPurchaseRequestDetails(item.getPrId()); for
			 * (PurchaseRequestDetail prDetail : purchaseRequestDetails) {
			 * purchaseOrderDetail.addPurchaseRequestDetailList(prDetail); } }
			 */

		}

		/*
		 * for (PurchaseOrderDetailDto item : purchaseOrderDto.getItems()) { if
		 * (purchaseOrderDto.getPoTypeId() ==
		 * com.param.common.constants.RequestType.AgainstPurchaseRequest
		 * .getRequestType() && item.getPrId() != null) { // Iterator<Map.Entry<Integer,
		 * PurchaseRequestDetail>> itr = // hm.entrySet().iterator(); // while
		 * (itr.hasNext()) { for (Integer prDetailId : item.getPrId()) { for
		 * (PurchaseOrderDetail poDetails : purchaseOrder.getPurchaseOrderDetailList())
		 * { if (poDetails.getItem().getId().equals(item.getItemId())) {
		 * PurchaseRequestDetail entry = hm.get(prDetailId);
		 * poDetails.addPurchaseRequestDetailList(entry); } } // } } } }
		 */
		
		purchaseOrderDetail.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
		purchaseOrderDao.savePurchaseOrder(purchaseOrder);
		CommonResponse response = new CommonResponse();
		response.setId(purchaseOrder.getId().longValue());
		response.setMessage("Purchase Order saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updatePurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrderDto.getId());
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();
		for (PurchaseOrderDetailDto item1 : purchaseOrderDto.getItems()) {
			purchaseOrderDao.updateTaxId(item1.getItemId(),item1.getTaxId(),purchaseOrder.getId());
		}
		if(purchaseOrderDto.getDiscountTypeId() == null) {
			purchaseOrderDto.setDiscountTypeId(1);
			}
		purchaseOrder.updatePurchaseOrder(
				purchaseOrderDto.getDiscountTypeId() != null ? new DiscountType(purchaseOrderDto.getDiscountTypeId())
						: (purchaseOrder.getDiscountType() != null
								? new DiscountType(purchaseOrder.getDiscountType().getId())
								: null),
				purchaseOrderDto.getDiscount() != null ? purchaseOrderDto.getDiscount() : purchaseOrder.getDiscount(),
				purchaseOrderDto.getNetAmount() != null ? purchaseOrderDto.getNetAmount()
						: purchaseOrder.getNetAmount(),
				purchaseOrderDto.getRemark() != null ? purchaseOrderDto.getRemark() : purchaseOrder.getRemark(),
				purchaseOrderDto.getRoundOffAmount(), purchaseOrderDto.getTotalAmount(),
				purchaseOrderDto.getTotalDiscount(), purchaseOrderDto.getTotalOtherAmount(),
				purchaseOrderDto.getTotalTaxAmount(),
				purchaseOrderDto != null ? purchaseOrderDto.getPaymentTerm() : purchaseOrder.getPaymentTerm());
				purchaseOrder.setDueDate(GlobalCommonDateUtils.getDate(purchaseOrderDto.getDueDate(), "dd-M-yyyy HH:mm:ss"));
		if(purchaseOrderDto.getVendorId()!=null) {
			purchaseOrder.setVendor(new Vendor(purchaseOrderDto.getVendorId()));
		}
		
		

		for (PurchaseOrderOtherChargesMapper poOtherChargesMapper : purchaseOrder.getPoOtherChargesList()) {
			Integer otherChargesId = null;
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
				if (poOtherCharges.getId() != null && poOtherCharges.getId() != 0
						&& poOtherCharges.getId().equals(poOtherChargesMapper.getId())) {
					otherChargesId = poOtherChargesMapper.getId();
					OtherCharge oc=new OtherCharge(poOtherCharges.getOtherChargesId());
					oc.setCreateDefaults(purchaseOrderDto.getOrganizationId(), purchaseOrderDto.getUnitId(), purchaseOrderDto.getUserId());
					poOtherChargesMapper.updatePoOtherChargesMapper(oc,
							poOtherCharges.getAmount(),poOtherCharges.getIsFromPo());
					
				}
			}
			if (otherChargesId == null) {
				poOtherChargesMapper.setIsDeleted(true);
			}
			poOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
			poOtherChargesMapper.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
			if (poOtherCharges.getId() == null && poOtherCharges.getAmount() != 0) {
				PurchaseOrderOtherChargesMapper omap=	new PurchaseOrderOtherChargesMapper(
						new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
				omap.setCreateDefaults(purchaseOrderDto.getOrganizationId(), purchaseOrderDto.getUnitId(), purchaseOrderDto.getUserId());
				omap.setIsFromPo(poOtherCharges.getIsFromPo());
				purchaseOrder.addPoOtherChargesMapperList(omap);
			}
		}

		for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
			Integer id = null;

			for (PurchaseOrderDetailDto item : purchaseOrderDto.getItems()) {
				
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
						detail = updateConsumableTypePODetail(item, detail);
					} else if (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						detail = updateFixedTypePODetail(item, detail);
					}
					detail.setPendingQuantity(item.getPendingQuantity() !=null ? item.getPendingQuantity()  :detail.getPendingQuantity() );
					/*
					 * for (PurchaseOrderOtherChargesMapper poOtherChargesMapper :
					 * detail.getPoOtherChargesList()) { Integer otherChargesId = null; for
					 * (PoOtherChargesMapperRequest poOtherCharges : item.getOtherCharges()) { if
					 * (poOtherCharges.getId() != null && poOtherCharges.getId() != 0 &&
					 * poOtherCharges.getId().equals(poOtherChargesMapper.getId())) { otherChargesId
					 * = poOtherChargesMapper.getId();
					 * poOtherChargesMapper.updatePoOtherChargesMapper( new
					 * OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					 * } } if (otherChargesId == null) { poOtherChargesMapper.setIsDeleted(true); }
					 * poOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
					 * poOtherChargesMapper.setUpdatedBy(poOtherChargesMapper.getUser()); }
					 * 
					 * for (PoOtherChargesMapperRequest poOtherCharges : item.getOtherCharges()) {
					 * if (poOtherCharges.getId() == null && poOtherCharges.getAmount() != 0) {
					 * detail.addPoOtherChargesMapperList(new PurchaseOrderOtherChargesMapper( new
					 * OtherCharge(poOtherCharges.getOtherChargesId()),
					 * poOtherCharges.getAmount())); } }
					 */

					for (PurchaseOrderStaged purchaseOrderStaged : detail.getPurchaseOrderStagedList()) {
						Integer poStagedId = null;
						for (PurchaseOrderStagedDto purchaseOrderstagedDto : item.getPurchaseOrderStaged()) {
							if (purchaseOrderstagedDto.getId() != null
									&& purchaseOrderstagedDto.getId().equals(purchaseOrderStaged.getId())) {
								poStagedId = purchaseOrderStaged.getId();
								purchaseOrderStaged.updatePurchaseOrderStaged(purchaseOrderstagedDto.getQuantity(),
										purchaseOrderstagedDto.getStagedDate());
								purchaseOrderStaged.setApprovedQunatity(purchaseOrderstagedDto.getQuantity());
							}
						}
						if (poStagedId == null) {
							purchaseOrderStaged.setIsDeleted(true);
						}
						purchaseOrderStaged.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
						purchaseOrderStaged.setUpdatedByUser(purchaseOrderDto.getUserId());
					}

					for (PurchaseOrderStagedDto purchaseOrderstagedDto : item.getPurchaseOrderStaged()) {
						if (purchaseOrderstagedDto.getId() == null) {
							com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
									purchaseOrderstagedDto.getQuantity(), purchaseOrderstagedDto.getStagedDate());
							purchaseOrderStaged.setAddedByUser(purchaseOrderDto.getUserId());
							purchaseOrderStaged.setUpdatedByUser(purchaseOrderDto.getUserId());
							purchaseOrderStaged.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							purchaseOrderStaged.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							purchaseOrderStaged.setOrganizationId(purchaseOrderDto.getOrganizationId());
							purchaseOrderStaged.setUnitId(purchaseOrderDto.getUnitId());
							purchaseOrderStaged.setApprovedQunatity(purchaseOrderstagedDto.getQuantity());

							//sessionService.setEntityDefaults(purchaseOrderStaged);
							detail.addPurchaseOrderStaged(purchaseOrderStaged);
						}
					}

					/*
					 * if (purchaseOrderDto.getPoTypeId() ==
					 * com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType(
					 * ) && item.getPrId() != null) { List<PurchaseRequestDetail>
					 * purchaseRequestDetails =
					 * purchaseOrderDao.getPurchaseRequestDetails(item.getPrId()); for
					 * (PurchaseRequestDetail prDetail : purchaseRequestDetails) {
					 * detail.addPurchaseRequestDetailList(prDetail); } }
					 */
				}
                if (item.getPoAccessoriesMapper() != null) {
                    com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapper = item
                            .getPoAccessoriesMapper();
                    for (Integer itemId : mapper.getItemIds()) {
                        PoAccessoriesMapper poAccessoriesMapper = new PoAccessoriesMapper(new Item(itemId));
                        poAccessoriesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
                        if(detail.getId().intValue() == item.getId().intValue()){
                        	detail.addPoAccessoriesMapper(poAccessoriesMapper);
                        }
                    }
                }
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdateDefaults(purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId(), purchaseOrder.getUnitId());
		}

		for (PurchaseOrderDetailDto item : purchaseOrderDto.getItems()) {
			if (item.getId() == null) {
				PurchaseOrderDetail purchaseOrderDetail = null;
				if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
					PurchaseOrderDetail purchaseOrderDetail2 = new PurchaseOrderDetail(new Item(item.getItemId()),
							item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
							item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
							item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
							item.getNetAmount(), new Manufacturer(item.getManufactureId()),
							item.getSpecification() != null ? item.getSpecification() : null,
							item.getRemark() != null ? item.getRemark() : null, new UomType(item.getPoUomTypeId()),
							new UomUnit(item.getPoUomUnitId()),
							item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
							item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
							new Status(item.getStatusId()));
					purchaseOrderDetail2.setPendingQuantity(item.getPendingQuantity() !=null ? item.getPendingQuantity() :null);
					purchaseOrderDetail2.setAddedByUser(purchaseOrderDto.getUserId());
					purchaseOrderDetail2.setUpdatedByUser(purchaseOrderDto.getUserId());
					purchaseOrderDetail2.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
					purchaseOrderDetail2.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
					purchaseOrderDetail2.setOrganizationId(purchaseOrderDto.getOrganizationId());
					purchaseOrderDetail2.setUnitId(purchaseOrderDto.getUnitId());
//sessionService.setEntityDefaults(purchaseOrderDetail2);
					purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail2);
					if (item.getPoUom() != null) {
						for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
							PurchaseOrderUomMapper purchaseOrderUomMapper = new PurchaseOrderUomMapper(
									new UomType(poUomMapperDto.getUomTypeId()),
									new UomUnit(poUomMapperDto.getUomUnitId()), poUomMapperDto.getConversion(),
									poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(), poUomMapperDto.getStoreUom());
							purchaseOrderUomMapper.setAddedByUser(purchaseOrderDto.getUserId());
							purchaseOrderUomMapper.setUpdatedByUser(purchaseOrderDto.getUserId());
							purchaseOrderUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							purchaseOrderUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							purchaseOrderUomMapper.setOrganizationId(purchaseOrderDto.getOrganizationId());
							purchaseOrderUomMapper.setUnitId(purchaseOrderDto.getUnitId());	//sessionService.setEntityDefaults(purchaseOrderUomMapper);
							purchaseOrderDetail.addPurchaseOrderUomMapperList(purchaseOrderUomMapper);
						}
					}
					if (item.getBonusUom() != null) {
						for (PurchaseOrderUomRequest bonusUomMapperDto : item.getBonusUom()) {
							BonusUomMapper bonusUomMapper = new BonusUomMapper(
									new UomType(bonusUomMapperDto.getUomTypeId()),
									new UomUnit(bonusUomMapperDto.getUomUnitId()), bonusUomMapperDto.getConversion(),
									bonusUomMapperDto.getIpUom(), bonusUomMapperDto.getOpUom(),
									bonusUomMapperDto.getStoreUom());
							bonusUomMapper.setAddedByUser(purchaseOrderDto.getUserId());
							bonusUomMapper.setUpdatedByUser(purchaseOrderDto.getUserId());
							bonusUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							bonusUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
							bonusUomMapper.setOrganizationId(purchaseOrderDto.getOrganizationId());
							bonusUomMapper.setUnitId(purchaseOrderDto.getUnitId());	//sessionService.setEntityDefaults(purchaseOrderUomMapper);
								//sessionService.setEntityDefaults(bonusUomMapper);
							purchaseOrderDetail.addBonusUomMapperList(bonusUomMapper);
						}
					}

					purchaseOrderDetail.setPoLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
							purchaseOrderDetail.getPurchaseOrderUomMapperList(), new UomType(item.getPoUomTypeId())));
					purchaseOrderDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
							purchaseOrderDetail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId())));

				} else if (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					PurchaseOrderDetail purchaseOrderDetail3 = new PurchaseOrderDetail(new Item(item.getItemId()),
							item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
							item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
							item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
							item.getNetAmount(), new Manufacturer(item.getManufactureId()),
							item.getSpecification() != null ? item.getSpecification() : null,
							item.getRemark() != null ? item.getRemark() : null, new Status(item.getStatusId()),
							item.getWarrentyPeriod(), item.getSupportTerms(), item.getTechnicalSpecification(),
							item.getExpectedPayment(), item.getWarrantyDate(), new Vendor(item.getServiceProviderId()));
					purchaseOrderDetail3.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity():null);
					purchaseOrderDetail3.setAddedByUser(purchaseOrderDto.getUserId());
					purchaseOrderDetail3.setUpdatedByUser(purchaseOrderDto.getUserId());
					purchaseOrderDetail3.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
					purchaseOrderDetail3.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
					purchaseOrderDetail3.setOrganizationId(purchaseOrderDto.getOrganizationId());
					purchaseOrderDetail3.setUnitId(purchaseOrderDto.getUnitId());	//sessionService.setEntityDefaults(purchaseOrderUomMapper);
						//sessionService.setEntityDefaults(purchaseOrderDetail3);
					purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail3);

					if (item.getPoAccessoriesMapper() != null) {
						com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapper = item
								.getPoAccessoriesMapper();
						for (Integer itemId : mapper.getItemIds()) {
							purchaseOrderDetail.addPoAccessoriesMapper(new PoAccessoriesMapper(new Item(itemId)));
						}
					}
				}

				/*
				 * if (item.getOtherCharges() != null) { for (PoOtherChargesMapperRequest
				 * poOtherCharges : item.getOtherCharges()) { if
				 * (poOtherCharges.getOtherChargesId() != null) {
				 * PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new
				 * PurchaseOrderOtherChargesMapper( new
				 * OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
				 * sessionService.setEntityDefaults(purchaseOrderOtherChargesMapper);
				 * purchaseOrderDetail.addPoOtherChargesMapperList(
				 * purchaseOrderOtherChargesMapper); } } }
				 */
				if (item.getPurchaseOrderStaged() != null) {
					for (PurchaseOrderStagedDto pucrhaseOrderStaged : item.getPurchaseOrderStaged()) {
						com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
								pucrhaseOrderStaged.getQuantity(), pucrhaseOrderStaged.getStagedDate());
					//	sessionService.setEntityDefaults(purchaseOrderStaged);
						purchaseOrderStaged.setAddedByUser(purchaseOrderDto.getUserId());
						purchaseOrderStaged.setUpdatedByUser(purchaseOrderDto.getUserId());
						purchaseOrderStaged.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
						purchaseOrderStaged.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
						purchaseOrderStaged.setOrganizationId(purchaseOrderDto.getOrganizationId());
						purchaseOrderStaged.setUnitId(purchaseOrderDto.getUnitId());	//sessionService.setEntityDefaults(purchaseOrderUomMapper);
						purchaseOrderStaged.setApprovedQunatity(pucrhaseOrderStaged.getQuantity());
						purchaseOrderDetail.addPurchaseOrderStaged(purchaseOrderStaged);
					}
				}

				/*
				 * if (purchaseOrderDto.getPoTypeId() ==
				 * com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType(
				 * ) && item.getPrId() != null) { List<PurchaseRequestDetail>
				 * purchaseRequestDetails =
				 * purchaseOrderDao.getPurchaseRequestDetails(item.getPrId()); for
				 * (PurchaseRequestDetail prDetail : purchaseRequestDetails) {
				 * purchaseOrderDetail.addPurchaseRequestDetailList(prDetail); } }
				 */

				/*
				 * if (purchaseOrderDto.getPoTypeId() ==
				 * com.param.common.constants.RequestType.AgainstPurchaseRequest
				 * .getRequestType() && item.getPrId() != null) { List<PurchaseRequestDetail>
				 * purchaseRequestDetails = purchaseOrderDao
				 * .getPurchaseRequestDetails(item.getPrId()); for (PurchaseRequestDetail
				 * prDetail : purchaseRequestDetails) { hm.put(prDetail.getId(), prDetail); } }
				 */

				if (purchaseOrderDto.getPoTypeId()
						.equals(com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType())
						&& item.getPrId() != null) {

					purchaseOrderDetail.setPutchRequestDetail(new PurchaseRequestDetail(item.getPrId().getFirst()));
					purchaseOrderDetail.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity():null);

				}
				if (purchaseOrderDto.getPoTypeId() == com.param.common.constants.RequestType.AgainstQuotationRequest
						.getRequestType() && item.getQrId() != null) {

					purchaseOrderDetail.setQuotationRequestDetail(new QuotationRequestDetail(item.getQrId().getFirst()));
					purchaseOrderDetail.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity():null);
					Integer pendingQuantity;
					for (Integer qrDetailId : item.getQrId()) {
						QuotationRequestDetail quotationRequestDetail = quotationRequestDao
								.getQuotationRequestPendingQuantity(qrDetailId, item.getItemId());
						pendingQuantity = quotationRequestDetail.getPendingQuantity() - item.getPoQuantity();
						quotationRequestDetail.setPendingQuantity(pendingQuantity);
						purchaseOrderDetail.setQuotationRequestDetail(quotationRequestDetail);

					}

				}

				//sessionService.setEntityDefaults(purchaseOrderDetail);
				purchaseOrderDetail.setAddedByUser(purchaseOrderDto.getUserId());
				purchaseOrderDetail.setUpdatedByUser(purchaseOrderDto.getUserId());
				purchaseOrderDetail.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
				purchaseOrderDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
				purchaseOrderDetail.setOrganizationId(purchaseOrderDto.getOrganizationId());
				purchaseOrderDetail.setUnitId(purchaseOrderDto.getUnitId());	//sessionService.setEntityDefaults(purchaseOrderUomMapper);
			
			}
		}

		/*
		 * for (PurchaseOrderDetailDto item : purchaseOrderDto.getItems()) { if
		 * (purchaseOrderDto.getPoTypeId() ==
		 * com.param.common.constants.RequestType.AgainstPurchaseRequest
		 * .getRequestType() && item.getPrId() != null) { for (PurchaseOrderDetail
		 * poDetails : purchaseOrder.getPurchaseOrderDetailList()) {
		 * Iterator<Map.Entry<Integer, PurchaseRequestDetail>> itr =
		 * hm.entrySet().iterator(); while (itr.hasNext()) { Map.Entry<Integer,
		 * PurchaseRequestDetail> entry = itr.next(); for (Integer poDetailId :
		 * item.getPrId()) { if (entry.getKey().equals(poDetailId)) {
		 * poDetails.addPurchaseRequestDetailList(entry.getValue()); } } } } } }
		 */

		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrderDto.getUnitId()));
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());

		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order updated successfully");

		return response;
	}

	private PurchaseOrderDetail updateConsumableTypePODetail(PurchaseOrderDetailDto item, PurchaseOrderDetail detail) {

		detail.updatePurchaseOrderDetail(new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(),
				item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : detail.getDiscount(),
				item.getTaxPercentage(), item.getTotalAmount(), item.getTotalDiscount(),
				item.getTaxAmount() != null ? item.getTaxAmount() : detail.getTaxAmount(), item.getTotalOtherAmount(),
				item.getNetAmount(),
				new Manufacturer(
						item.getManufactureId() != null ? item.getManufactureId() : detail.getManufacturer().getId()),
				item.getSpecification() != null ? item.getSpecification() : detail.getSpecification(),
				item.getRemark() != null ? item.getRemark() : detail.getRemark(), new UomType(item.getPoUomTypeId()),
				new UomUnit(item.getPoUomUnitId()),
				item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
				item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
				item.getPoQuantity() != null ? Util.GetLeaseQuantity(item.getPoQuantity(),
						detail.getPurchaseOrderUomMapperList(), new UomType(detail.getPoUomType().getId()))
						: detail.getPoLeaseQuantity(),
						item.getBonusQuantity() != null ? Util.GetLeaseQuantity(item.getBonusQuantity(),
						detail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId()))
						: detail.getBonusLeaseQuantity(), item.getPoApprovedQuantity() != null ? item.getPoApprovedQuantity() : 0);

		for (PurchaseOrderUomMapper poUomMapper : detail.getPurchaseOrderUomMapperList()) {
			for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
				if (poUomMapperDto.getId() != null && poUomMapperDto.getId().equals(poUomMapper.getId())) {
					poUomMapper.updatePurchaseOrderUomMapper(new UomType(poUomMapperDto.getUomTypeId()),
							new UomUnit(poUomMapperDto.getUomUnitId()), poUomMapperDto.getConversion(),
							poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(), poUomMapperDto.getStoreUom());
				}
			}

		}

		for (BonusUomMapper bonusMapper : detail.getBonusUomMapperList()) {
			for (PurchaseOrderUomRequest bonusMapperDto : item.getBonusUom()) {
				if (bonusMapperDto.getId() != null && bonusMapperDto.getId().equals(bonusMapper.getId())) {
					bonusMapper.updateBonusUomMapper(
							bonusMapperDto.getUomTypeId() != null ? new UomType(bonusMapperDto.getUomTypeId()) : null,
							bonusMapperDto.getUomUnitId() != null ? new UomUnit(bonusMapperDto.getUomUnitId()) : null,
							bonusMapperDto.getConversion() != null ? bonusMapperDto.getConversion() : null,
							bonusMapperDto.getIpUom() != null ? bonusMapperDto.getIpUom() : null,
							bonusMapperDto.getOpUom() != null ? bonusMapperDto.getOpUom() : null,
							bonusMapperDto.getStoreUom() != null ? bonusMapperDto.getStoreUom() : null);
				}
			}
		}

		return detail;
	}

	private PurchaseOrderDetail updateFixedTypePODetail(PurchaseOrderDetailDto item, PurchaseOrderDetail detail) {
		detail.updatePurchaseOrderDetail(new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(),
				item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : detail.getDiscount(),
				item.getTaxPercentage(), item.getTotalAmount(), item.getTotalDiscount(),
				item.getTaxAmount() != null ? item.getTaxAmount() : detail.getTaxAmount(), item.getTotalOtherAmount(),
				item.getNetAmount(),
				new Manufacturer(
						item.getManufactureId() != null ? item.getManufactureId() : detail.getManufacturer().getId()),
				item.getSpecification() != null ? item.getSpecification() : detail.getSpecification(),
				item.getRemark() != null ? item.getRemark() : detail.getRemark(),
				item.getWarrentyPeriod() != null ? item.getWarrentyPeriod() : detail.getwarrantyPeriod(),
				item.getSupportTerms() != null ? item.getSupportTerms() : detail.getSupportTerms(),
				item.getTechnicalSpecification() != null ? item.getTechnicalSpecification()
						: detail.getTechnicalSpecification(),
				item.getExpectedPayment() != null ? item.getExpectedPayment() : detail.getExpectedPayment(),
				item.getWarrantyDate() != null ? item.getWarrantyDate() : detail.getwarrantyDate(),
				new Vendor(item.getServiceProviderId() != null ? item.getServiceProviderId()
						: detail.getServiceProvider().getId()), item.getPoApprovedQuantity() != null ? item.getPoApprovedQuantity() : 0);

		/*
		 * for(PoAccessoriesMapper mapper : detail.getPoAccessoriesMapperList()) {
		 * for(com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper
		 * mapperdto: item.getPoAccessoriesMapper())
		 * if(mapperdto.getId().equals(mapper.getId())) { // }
		 * 
		 * }
		 */

		return detail;
	}

	@Override
	@Transactional
	public Object getPucrhaseOrder(Integer id,Integer unitId, Integer orgId) {
		PurchaseOrderMasterDetailsDto POMasterDetail = purchaseOrderDao.getPurchaseOrderBP(id);

		List<PurchaseOrderDetailsRespDto> PurchaseOrderDetailsRespDtoConsumables = purchaseOrderDao
				.getPoDetailsConsumablesByPoIdBP(id);

		List<Integer> detailsIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<Integer> fixedDetaildId = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<PurchaseOrderUomMapperDto> poUomBP = null;
		List<PurchaseOrderUomMapperDto> bonusUomBP = null;
		if (detailsIds.size() > 0) {
			poUomBP = purchaseOrderDao.getPurchaseOrderUomMapperBP(detailsIds);
			bonusUomBP = purchaseOrderDao.getBonusUomMapperBP(detailsIds);
		}
		List<PoOtherChargesMapperDto> otherChargesBP = null;
		otherChargesBP = purchaseOrderDao.getPoOtherChargesMapperBP(id);
		POMasterDetail.setOtherCharges(otherChargesBP);
		List<Integer> consumabelItemIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList());

		detailsIds.addAll(fixedDetaildId);
		List<PoPurchaseRequestArray> purchaseRequestTypeIdBP = null;

		List<PurchaseOrderStagedDto> purchaseOrderStagedDtoBP = null;

		List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDtoBP = null;
		if (detailsIds.size() > 0) {
			purchaseRequestTypeIdBP = purchaseOrderDao.getPurchaseRequestTypeIdBP(detailsIds);

			purchaseOrderStagedDtoBP = purchaseOrderDao.getPoStagedBP(detailsIds);
			
			initialPurchaseOrderStagedDtoBP = purchaseOrderDao.getInitialPurchaseOrderStagedDto(detailsIds);
		}
		List<UomResponse> uomResponses = null;
		if (consumabelItemIds.size() > 0) {
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds,orgId);
		}

		List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> accessoriesmapperBP = new ArrayList<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty()) {
			accessoriesmapperBP = purchaseOrderDao.getPoAccessoriesMapperBP(fixedDetaildId);
		}

		consumabelItemIds.addAll(PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(POMasterDetail.getStoreId(), consumabelItemIds,unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds,unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds,orgId);
		List<Quantities> leaseQuantities = itemsDao.getLeaseQunatityBP(POMasterDetail.getStoreId(), consumabelItemIds);

		List<Quantities> approvedPRAndPOQuantities = itemsDao
				.getApprovedPRandPOQuantityList(POMasterDetail.getStoreId(), consumabelItemIds,unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(POMasterDetail.getStoreId(), consumabelItemIds,unitId);

		for (PurchaseOrderDetailsRespDto purchaseOrderDetailsRespDto : PurchaseOrderDetailsRespDtoConsumables) {
			// purchaseOrderDetailsRespDto.get
			List<PoPurchaseRequestArray> purchaseRequestTypeId = purchaseRequestTypeIdBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());

			Integer prQunatity = null;
			if (purchaseRequestTypeId != null) {
				for (PoPurchaseRequestArray pr : purchaseRequestTypeId) {
					if (pr.getApprovedQuantity() != null) {
						prQunatity = pr.getApprovedQuantity();
					}
				}
			}

			List<PoOtherChargesMapperDto> otherCharges = otherChargesBP.stream()
					.filter(x -> x.getPoId().equals(purchaseOrderDetailsRespDto.getId())).collect(Collectors.toList());
			List<PurchaseOrderStagedDto> purchaseOrderStagedDto = purchaseOrderStagedDtoBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());
			List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto = initialPurchaseOrderStagedDtoBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());

			long storeStock = 0;
			long stockInTransit = 0;
			int maxStoreQuantity = 0;
			long leaseQty = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			List<Quantities> storeStock1 = storeStocks.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> stockInTransit1 = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> maxStoreQuantity1 = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> leaseQuantity = leaseQuantities.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());

			if (storeStock1 != null && !storeStock1.isEmpty()) {
				storeStock = storeStock1.getFirst().getQuantity();
				purchaseOrderDetailsRespDto.setStockAvailable(storeStock);
			}
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.getFirst().getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.getFirst().getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.getFirst().getQuantity();
			if (approvedPRAndPOQuantities.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).getFirst().getQuantity();
			}
			if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPOQuantity = approvedPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).getFirst().getQuantity();
			}
			/*Long maxOrderQuantity = maxStoreQuantity
					- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);*/
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
			// Long storeStock =
			// GlobalDao.getStoreStockByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Long stockInTransit =
			// GlobalDao.getStoreStockInTransitByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Integer maxStoreQuantity =
			// GlobalDao.getStoreMaxQuantityByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Long maxOrderQuantity = maxStoreQuantity
			// - (storeStock + stockInTransit +
			// itemsDao.getLeaseQunatity(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId()));

			if (purchaseOrderDetailsRespDto.getQrdid() != null) {
				List<QrDateDto> qrDateDto = purchaseOrderDao.getqrDetails(purchaseOrderDetailsRespDto.getQrdid());
				purchaseOrderDetailsRespDto.setQrDateDto(qrDateDto);
			}

			if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				// purchaseOrderDetailsRespDto.setOtherCharges(otherCharges);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setInitialPurchaseOrderStagedDto(initialPurchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				purchaseOrderDetailsRespDto.setPoUom(
						poUomBP.stream().filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
								.collect(Collectors.toList()));

				List<PurchaseOrderUomMapperDto> bonusUomBP1 = bonusUomBP.stream()
						.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
						.collect(Collectors.toList());

				purchaseOrderDetailsRespDto.setBonusUom(bonusUomBP1);
				purchaseOrderDetailsRespDto.setUom(
						uomResponses.stream().filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
								.collect(Collectors.toList()));
				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			} else if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				// purchaseOrderDetailsRespDto.setOtherCharges(otherCharges);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPoAccessoriesMapper(accessoriesmapperBP.stream()
						.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
						.collect(Collectors.toList()));
				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			}

		}
		
		POMasterDetail.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		POMasterDetail.setTaxList(GlobalDao.getTaxListBP(orgId));
		return POMasterDetail;

		// original
		/*
		 * List<PurchaseOrderDetailsRespDto> items = null;
		 * 
		 * PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(id);
		 * 
		 * PurchaseOrderMasterDetailsDto purchaseOrderMasterDetail = new
		 * PurchaseOrderMasterDetailsDto( purchaseOrder.getId(),
		 * purchaseOrder.getPoNumber(), purchaseOrder.getAssetType().getId(),
		 * purchaseOrder.getAssetType().getType(),
		 * purchaseOrder.getRequestType().getId(),
		 * purchaseOrder.getRequestType().getType(), purchaseOrder.getDiscountType() !=
		 * null ? purchaseOrder.getDiscountType().getId() : null,
		 * purchaseOrder.getDiscountType() != null ?
		 * purchaseOrder.getDiscountType().getType() : null,
		 * purchaseOrder.getDiscount(), purchaseOrder.getIsConsignment(),
		 * purchaseOrder.getNetAmount(), purchaseOrder.getRoundOffAmount(),
		 * purchaseOrder.getTotalAmount(), purchaseOrder.getTotalDiscount(),
		 * purchaseOrder.getTotalOtherAmount(), purchaseOrder.getTotalTaxAmount(),
		 * purchaseOrder.getStatus().getId(), purchaseOrder.getStatus().getStatus(),
		 * purchaseOrder.getStatus().getId(), purchaseOrder.getStatus().getStatus(),
		 * purchaseOrder.getStore().getStoreId(),
		 * purchaseOrder.getStore().getStoreName(), purchaseOrder.getVendor().getId(),
		 * purchaseOrder.getVendor().getName(), purchaseOrder.getPaymentTerm(),
		 * purchaseOrder.getRemark(), purchaseOrder.getAddedBy().getUserName(),
		 * purchaseOrder.getAddedDate(), items);
		 * 
		 * for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList())
		 * { if (detail.getIsActive().equals(true) &&
		 * detail.getIsDeleted().equals(false)) { List<PoPurchaseRequestArray>
		 * purchaseRequestTypeId = purchaseOrderDao
		 * .getPurchaseRequestTypeId(detail.getId());
		 * 
		 * Integer prQunatity = null; if (purchaseRequestTypeId != null) { for
		 * (PoPurchaseRequestArray pr : purchaseRequestTypeId) { if
		 * (pr.getApprovedQuantity() != null) { prQunatity = pr.getApprovedQuantity(); }
		 * } }
		 * 
		 * List<PoOtherChargesMapperDto> otherCharges =
		 * getPoOtherChargesList(detail.getId()); List<PurchaseOrderStagedDto>
		 * purchaseOrderStagedDto = getPurchaseOrderStagedList(detail.getId());
		 * 
		 * Long storeStock =
		 * GlobalDao.getStoreStockByItem(detail.getPurchaseOrder().getStore().getStoreId
		 * (), detail.getItem().getId()); Long stockInTransit =
		 * GlobalDao.getStoreStockInTransitByItem(
		 * detail.getPurchaseOrder().getStore().getStoreId(), detail.getItem().getId());
		 * Long lastMonthSale =
		 * GlobalDao.getLastMonthSale(detail.getPurchaseOrder().getStore().getStoreId(),
		 * detail.getItem().getId()); Long currentMonthSale =
		 * GlobalDao.getCurrentMonthSale(detail.getPurchaseOrder().getStore().getStoreId
		 * (), detail.getItem().getId()); Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(
		 * detail.getPurchaseOrder().getStore().getStoreId(), detail.getItem().getId());
		 * Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
		 * itemsDao .getLeaseQunatity(detail.getPurchaseOrder().getStore().getStoreId(),
		 * detail.getItem().getId()));
		 * 
		 * if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId()))
		 * {
		 * 
		 * List<PurchaseOrderUomMapperDto> poUom =
		 * getPurchaseOrderUomList(detail.getId()); List<PurchaseOrderUomMapperDto>
		 * bonusUom = getBonusUomList(detail.getId());
		 * 
		 * Integer leaseQuantity = null;
		 * 
		 * purchaseOrderMasterDetail.addItems(new
		 * PurchaseOrderDetailsRespDto(detail.getId(), detail.getItem().getId(),
		 * detail.getItem().getCode(), detail.getItem().getName(),
		 * detail.getPoQuantity(), prQunatity, leaseQuantity, maxStoreQuantity,
		 * maxOrderQuantity, detail.getPoUomType().getId(),
		 * detail.getPoUomType().getType(), detail.getPoUomUnit().getId(),
		 * detail.getPoUomUnit().getUnits(), detail.getBonusQuantity(),
		 * detail.getBonusUomType().getId(), detail.getBonusUomType().getType(),
		 * detail.getBonusUomUnit().getId(), detail.getBonusUomUnit().getUnits(),
		 * detail.getCop(), detail.getMrp(), detail.getDiscount(),
		 * detail.getTax().getId(), detail.getTax().getName(),
		 * detail.getTaxPercentage(), detail.getTotalAmount(), detail.getTaxAmount(),
		 * detail.getNetAmount(), detail.getTotalDiscount(),
		 * detail.getTotalOtherAmount(), detail.getStatus().getId(),
		 * detail.getStatus().getStatus(), detail.getManufacturer().getId(),
		 * detail.getManufacturer().getName(), detail.getSpecification(),
		 * detail.getRemark(), detail.getPoApprovedQuantity(),
		 * detail.getBonusApprovedQuantity(), detail.getRejectedQuantity(),
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId()), poUom,
		 * bonusUom, purchaseRequestTypeId, otherCharges, purchaseOrderStagedDto)); }
		 * else if
		 * (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
		 * List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>
		 * accessoriesmapper = getpoAccessoriesMapper( detail.getId());
		 * 
		 * purchaseOrderMasterDetail.addItems(new
		 * PurchaseOrderDetailsRespDto(detail.getId(), detail.getItem().getId(),
		 * detail.getItem().getCode(), detail.getItem().getName(),
		 * detail.getPoQuantity(), prQunatity, maxStoreQuantity, maxOrderQuantity,
		 * detail.getBonusQuantity(), detail.getCop(), detail.getMrp(),
		 * detail.getDiscount(), detail.getTax().getId(), detail.getTax().getCode(),
		 * detail.getTaxPercentage(), detail.getTotalAmount(), detail.getTaxAmount(),
		 * detail.getNetAmount(), detail.getTotalDiscount(),
		 * detail.getTotalOtherAmount(), detail.getStatus().getId(),
		 * detail.getStatus().getStatus(), detail.getManufacturer().getId(),
		 * detail.getManufacturer().getName(), detail.getSpecification(),
		 * detail.getRemark(), detail.getPoApprovedQuantity(),
		 * detail.getBonusApprovedQuantity(), detail.getRejectedQuantity(),
		 * detail.getwarrantyPeriod(), detail.getSupportTerms(),
		 * detail.getTechnicalSpecification(), detail.getExpectedPayment(),
		 * detail.getServiceProvider().getId(), detail.getServiceProvider().getName(),
		 * detail.getwarrantyDate(), accessoriesmapper, purchaseRequestTypeId,
		 * otherCharges, purchaseOrderStagedDto)); }
		 * 
		 * } } purchaseOrderMasterDetail.setOtherChargesList(getOtherCharges()); //if
		 * (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
		 * purchaseOrderMasterDetail.setTaxList(getTaxList()); //} return
		 * purchaseOrderMasterDetail;
		 */
	}

	@Transactional
	private List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> getpoAccessoriesMapper(
			Integer purchaseOrderDetailId) {

		List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> response = new ArrayList<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>();

		for (PoAccessoriesMapper mapper : purchaseOrderDao.getPoAccessoriesMapper(purchaseOrderDetailId)) {
			com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapperdto = new com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper(
					mapper.getId(), mapper.getItem().getId(), mapper.getItem().getName(), mapper.getItem().getCode());
			response.add(mapperdto);
		}
		return response;
	}

	@Transactional
	private List<OtherChargesResponse> getOtherCharges() {

		List<OtherChargesResponse> otherChargesList = new ArrayList<OtherChargesResponse>();
		for (OtherCharge otherCharges : GlobalDao.getOtherCharges()) {

			OtherChargesResponse response = new OtherChargesResponse(otherCharges.getId(),
					otherCharges.getDescription());
			otherChargesList.add(response);
		}
		return otherChargesList;
	}

	@Transactional
	private List<PurchaseOrderStagedDto> getPurchaseOrderStagedList(Integer purchaseOrderDetailId) {
		List<PurchaseOrderStagedDto> response = new ArrayList<PurchaseOrderStagedDto>();
		for (PurchaseOrderStaged poStaged : purchaseOrderDao.getPoStaged(purchaseOrderDetailId)) {
			PurchaseOrderStagedDto purchaseOrderStagedDto = new PurchaseOrderStagedDto();
			purchaseOrderStagedDto.setQuantity(poStaged.getQuantity());
			purchaseOrderStagedDto.setId(poStaged.getId());
			purchaseOrderStagedDto.setStagedDate(poStaged.getStagedDate());
			response.add(purchaseOrderStagedDto);
		}
		return response;
	}

	/*
	 * @Transactional private List<PoPurchaseRequestArray>
	 * getPurchaseRequestTypeIdList(Integer purchaseOrderDetailId) {
	 * List<PoPurchaseRequestArray> purchaseOrderList = new
	 * ArrayList<PoPurchaseRequestArray>();
	 * 
	 * for (PurchaseRequestDetail purchaseRequest :
	 * purchaseOrderDao.getPurchaseRequestTypeId(purchaseOrderDetailId)){
	 * 
	 * PoPurchaseRequestArray prIdResponse = new
	 * PoPurchaseRequestArray(purchaseRequest.getId(),purchaseRequest.
	 * getApprovedQuantity()); purchaseOrderList.add(prIdResponse);
	 * 
	 * } return purchaseOrderList; }
	 */
	@Transactional
	private List<PurchaseOrderUomMapperDto> getPurchaseOrderUomList(Integer purchaseOrderDetailId) {
		List<PurchaseOrderUomMapperDto> response = new ArrayList<PurchaseOrderUomMapperDto>();
		for (PurchaseOrderUomMapper poUomMapper : purchaseOrderDao.getPurchaseOrderUomMapper(purchaseOrderDetailId)) {
			response.add(new PurchaseOrderUomMapperDto(poUomMapper.getId(), poUomMapper.getUomType().getId(),
					poUomMapper.getUomType().getType(), poUomMapper.getUomUnit().getId(),
					poUomMapper.getUomUnit().getUnits(), poUomMapper.getConversions(), poUomMapper.getIpUom(),
					poUomMapper.getOpUom(), poUomMapper.getStoreUom()));
		}
		return response;
	}

	@Transactional
	private List<PurchaseOrderUomMapperDto> getBonusUomList(Integer purchaseOrderDetailId) {
		if (purchaseOrderDetailId != null) {
			List<PurchaseOrderUomMapperDto> response = new ArrayList<PurchaseOrderUomMapperDto>();
			for (BonusUomMapper bonusUomMapper : purchaseOrderDao.getBonusUomMapper(purchaseOrderDetailId)) {
				response.add(
						new PurchaseOrderUomMapperDto(bonusUomMapper.getId() != null ? bonusUomMapper.getId() : null,
								bonusUomMapper.getUomType() != null ? bonusUomMapper.getUomType().getId() : null,
								bonusUomMapper.getUomType() != null ? bonusUomMapper.getUomType().getType() : null,
								bonusUomMapper.getUomUnit() != null ? bonusUomMapper.getUomUnit().getId() : null,
								bonusUomMapper.getUomUnit() != null ? bonusUomMapper.getUomUnit().getUnits() : null,
								bonusUomMapper.getConversions() != null ? bonusUomMapper.getConversions() : null,
								bonusUomMapper.getIpUom() != null ? bonusUomMapper.getIpUom() : null,
								bonusUomMapper.getOpUom() != null ? bonusUomMapper.getOpUom() : null,
								bonusUomMapper.getStoreUom() != null ? bonusUomMapper.getStoreUom() : null));

			}
			return response;
		} else {
			return null;
		}

	}

	@Transactional
	private List<PoOtherChargesMapperDto> getPoOtherChargesList(Integer purchaseOrderDetailId) {
		List<PoOtherChargesMapperDto> poOtherChargesList = new ArrayList<PoOtherChargesMapperDto>();

		for (PurchaseOrderOtherChargesMapper poOtherChargesMapper : purchaseOrderDao
				.getPoOtherChargesMapper(purchaseOrderDetailId)) {
			poOtherChargesList.add(new PoOtherChargesMapperDto(poOtherChargesMapper.getId(),
					poOtherChargesMapper.getOtherCharge().getId(),
					poOtherChargesMapper.getOtherCharge().getDescription(), poOtherChargesMapper.getAmount()));
		}
		return poOtherChargesList;
	}

	@Override
	@Transactional
	public Object approvePurchaseOrder(PurchaseOrderStatusDto purchaseOrderStatusDto) {

		for (Integer id : purchaseOrderStatusDto.getPoIdList()) {
			PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(id);

			purchaseOrder.setStatus(new Status(purchaseOrderStatusDto.getStatusId()));
			purchaseOrder.setApprovalStatus(new Status(purchaseOrderStatusDto.getApprovalStatusId()));

			for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
				detail.setStatus(new Status(purchaseOrderStatusDto.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
				detail.setUpdatedByUser(purchaseOrderStatusDto.getUserId());
			}

		//	purchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(unitId));
		//	purchaseOrder.setApprovedBy(new User(purchaseOrderStatusDto.getUserId()));
			if(purchaseOrderStatusDto.getStatusId().equals(1))
			{
				purchaseOrder.setSubmittedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
				purchaseOrder.setSubmittedBy(new User(purchaseOrderStatusDto.getUserId()));	
			}
			else if(purchaseOrderStatusDto.getStatusId().equals(22))
			{
				purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
				purchaseOrder.setUpdatedByUser(purchaseOrderStatusDto.getUserId());
			}
			
		//	purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//	purchaseOrder.setUpdatedByUser(purchaseOrderStatusDto.getUserId());

			purchaseOrderDao.updatePurchaseOrder(purchaseOrder);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrderDto.getId());

		for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
			/*if(purchaseOrder.getRequestType().getId() ==  com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType())
			{
				//detail.getPoQuantity();
				
				//detail.getPutchRequestDetail().setApprovedQuantity(detail.getPutchRequestDetail().getApprovedQuantity()+detail.getPoQuantity());
				detail.setPoQuantity(0);
				detail.setPoApprovedQuantity(0);
			}*/
			detail.setStatus(new Status(purchaseOrderDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
			detail.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		purchaseOrder.setCancelReason(new CancelReason(purchaseOrderDto.getCancelReasonId()));
		purchaseOrder.setCancelNote(purchaseOrderDto.getCancelNote());

		purchaseOrder.setStatus(new Status(purchaseOrderDto.getStatusId()));
		purchaseOrder.setApprovalStatus(new Status(purchaseOrderDto.getApprovalStatusId()));

		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());

		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getLastPurchaseOrder(Integer itemid, Integer vendorid,Integer poType) {
		List<LastPurchaseOrderDto> purchaseOrderSearchDtoList = purchaseOrderDao.lastPurchaseOrderList(itemid,
				vendorid,poType);

		return purchaseOrderSearchDtoList;

	}

	@Override
	public Object forcefulyClosed(PurchaseOrderMasterDto purchaseOrderDto) {
		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrderDto.getId());
		purchaseOrder.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());
		purchaseOrder.setIsForcefuly(true);
		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order updated successfully");

		return response;
	
	}
}
