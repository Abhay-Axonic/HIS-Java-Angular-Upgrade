package com.param.procurement.purchase.service.goodReceiptNote;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.param.procurement.purchase.dto.goodReceiptNote.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
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
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.CreditNoteAmount;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GoodReceiptNoteDetailLog;
import com.param.entity.model.procurement.GrnAccessory;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnOtherChargesMapper;
import com.param.entity.model.procurement.GrnSettlement;
import com.param.entity.model.procurement.GrnSettlementDetail;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.billing.dto.PaymentReceiptDetailsDto1;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPatientPanCardDocDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;



@Service
public class GoodReceiptNoteService implements IGoodReceiptNoteService {

	final static Logger logger = Logger.getLogger(GoodReceiptNoteService.class);

	private Integer screenId = Screen.GoodReceiptNote.getScreen();
	private Integer lotNoScreenId = Screen.LotNoCreation.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IGoodReceiptNoteDao grnDao;

	@Autowired
	IItemsDao itemsDao;
	
	@Autowired
	ITaxComponentDetailsDao iTaxComponentDetailsDao;
	
	@Autowired
	IPatientPanCardDocDao iPatientPanCardDocDao;
	
	@Autowired
	IGrnBonusBatchExpiryDao BonusBatchExpiryDao;

	@Override
	@Transactional
	public GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		GRNDropdownDto grnDropDownDto = new GRNDropdownDto();

		grnDropDownDto.setRequestingStores(GlobalDao.getUserStoreListBP(userId,unitId));
		grnDropDownDto.setDeliveryStores(GlobalDao.getStoreListBP(unitId));
		grnDropDownDto.setStatus(GlobalDao.getScreenStatusBP(screenId));
		grnDropDownDto.setAssetType(GlobalDao.getAssetTypeListBP());

		/*
		 * for (Store store : GlobalDao.getUserStoreList()) {
		 * grnDropDownDto.addStore(new
		 * com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		 * }
		 * 
		 * for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
		 * grnDropDownDto.addStatus(new
		 * com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
		 * screenStatus.getStatus().getStatus())); }
		 * 
		 * for (AssetType assetType : GlobalDao.getAssetTypeList()) { grnDropDownDto
		 * .addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(),
		 * assetType.getType())); }
		 */
		return grnDropDownDto;
	}

	@Override
	@Transactional
	public Object getGRNList(GRNRequest grnRequest) {
		grnRequest.setToDate((new DateTime(grnRequest.getToDate())).plusDays(1).toDate());

		List<GRNSearchResponse> grnSearchListBP = grnDao.searchGRNListBP(grnRequest, screenId);

		return grnSearchListBP;

		// Origincal
		/*
		 * List<GRNSearchResponse> grnSearchList = new ArrayList<GRNSearchResponse>();
		 * 
		 * for (GoodReceiptNote goodReceiptNote : grnDao.searchGRNList(grnRequest,
		 * screenId)) {
		 * 
		 * GRNSearchResponse grnSearch = new GRNSearchResponse(goodReceiptNote.getId(),
		 * goodReceiptNote.getStore().getStoreId(),
		 * goodReceiptNote.getStore().getStoreName(), goodReceiptNote.getGrnNumber(),
		 * goodReceiptNote.getAddedDate(), goodReceiptNote.getInvoiceNumber(),
		 * goodReceiptNote.getInvoiceDate(), goodReceiptNote.getDcNumber(),
		 * goodReceiptNote.getDcDate(), goodReceiptNote.getVendor().getName(),
		 * goodReceiptNote.getAddedBy().getUserName(),
		 * goodReceiptNote.getStatus().getStatus(),
		 * goodReceiptNote.getApprovalStatus().getStatus());
		 * 
		 * grnSearchList.add(grnSearch);
		 * 
		 * } return grnSearchList;
		 */
	}

	@Override
	@Transactional
	public Object saveGoodReceiptNote(GRNRequest grnRequest) throws Exception {

		
		
		
		BigInteger count = grnDao.checkAlreadyExists(grnRequest);
		
		if(count.intValue()>0) {
			throw new CustomException("This transaction already exists. ");	
		}
		
		Random rand = new Random();
		int num = rand.nextInt(1000);

		/*
		 * Integer storeId = grnRequest.getStoreId(); Store storeNew =
		 * GlobalDao.getStore(storeId); Unit unit =
		 * GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String grnNo = GlobalService.getBillNoByGeneratedCode(grnRequest.getStoreId(), grnRequest.getUnitId(), screenId);
		if(grnNo==null)
		{
			throw new CustomException("Please configure prefix number");	
		}

		// System.out.println("grnNo:-" + grnNo);

		HashMap<Integer, PurchaseRequestDetail> prMap = new HashMap();
		HashMap<Integer, PurchaseOrderDetail> poMap = new HashMap();
		
		// removed invoice and dc number validation for grn against PO
		
		/*if(grnRequest.getGrnTypeId() == com.param.common.constants.RequestType.AgainstPurchaseOrder.getRequestType() )
		{
			
		}
		else
		{
			if (grnRequest.getInvoiceNumber() != null) {
				List<GoodReceiptNote> goodReceiptNotes = grnDao.getByInvoiceNumberAndVendor(grnRequest.getVendorId(),
						grnRequest.getInvoiceNumber());
				if (!goodReceiptNotes.isEmpty())
					throw new CustomException("Invoice number already exists for the vendor");
			}	
			
			if (grnRequest.getDcNumber() != null) {
				List<GoodReceiptNote> goodReceiptNotesByDCNo = grnDao
						.getByInvoiceNumberAndDCNumber(grnRequest.getVendorId(), grnRequest.getDcNumber());
				if (!goodReceiptNotesByDCNo.isEmpty())
					throw new CustomException("DC number already exists for the vendor");
			}
		}*/

		

		String billType = "";
		if (grnRequest.getBillTypeName() != null) {
			billType = grnRequest.getBillTypeName();
		}

		

		GoodReceiptNote grn = new GoodReceiptNote(new Status(grnRequest.getApprovalStatusId()),
				new AssetType(grnRequest.getAssetTypeId()),
				grnRequest.getDcDate() != null ? grnRequest.getDcDate() : null,
				grnRequest.getDcNumber() != null ? grnRequest.getDcNumber() : null, grnNo, grnRequest.getInvoiceDate(),
				grnRequest.getInvoiceNumber(), grnRequest.getInwardDate() != null ? grnRequest.getInwardDate() : null,
				grnRequest.getInwardNumber() != null ? grnRequest.getInwardNumber() : null,
				grnRequest.getIsConsignment(), grnRequest.getNetAmount(),
				grnRequest.getRemark() != null ? grnRequest.getRemark() : null,
				new RequestType(grnRequest.getGrnTypeId()), grnRequest.getRoundOff(),
				new Status(grnRequest.getStatusId()), new Store(grnRequest.getStoreId()), grnRequest.getTotalAmount(),
				grnRequest.getTotalDiscount() != null ? grnRequest.getTotalDiscount() : null,
				grnRequest.getTotalOtherAmount(), grnRequest.getTotalTaxAmount(),
				new Vendor(grnRequest.getVendorId() != null ? grnRequest.getVendorId() : null),
				new Store(grnRequest.getRequestingStoreId()), billType,
				grnRequest.getTotalCreditNoteAmount() != null ? grnRequest.getTotalCreditNoteAmount() : null);
		grn.setRoundOff(grnRequest.getRoundOff());
		grn.setAddedByUser(grnRequest.getUserId());
		grn.setUpdatedByUser(grnRequest.getUserId());
		grn.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		grn.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		grn.setOrganizationId(grnRequest.getOrgId());
		grn.setUnitId(grnRequest.getUnitId());
		grn.setDueDate(GlobalCommonDateUtils.getDate(grnRequest.getDueDate(), "dd-M-yyyy HH:mm:ss"));
		
		
		//sessionService.setEntityDefaults(grn);
		
		if (grnRequest.getOtherCharges() != null) {
			for (GRNOtherChargesMapper otherCharges : grnRequest.getOtherCharges()) {
				if (otherCharges.getOtherChargesId() != null) {
					GrnOtherChargesMapper grnOtherChargesMapper = new GrnOtherChargesMapper(
							new OtherCharge(otherCharges.getOtherChargesId()), otherCharges.getAmount());
					grnOtherChargesMapper.setAddedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setUpdatedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setOrganizationId(grnRequest.getOrgId());
					grnOtherChargesMapper.setUnitId(grnRequest.getUnitId());
					grnOtherChargesMapper.setIsFromGrn(otherCharges.getIsFromGrn());
					
					//sessionService.setEntityDefaults(grnOtherChargesMapper);
					grn.addGRNOtherChargesMapperList(grnOtherChargesMapper);
				}
			}
		}

		// added if condation by minesh for grn save issue.
		if (grnRequest.getCreditNoteAmountList() != null) {
			for (CreditNoteAmountList cna : grnRequest.getCreditNoteAmountList()) {
				CreditNoteAmount creditNoteAmount = new CreditNoteAmount();
				creditNoteAmount.setMateinwardid(cna.getMateinwardid());
				creditNoteAmount.setMateinwarddetailsid(cna.getMateinwarddetailsid());
				creditNoteAmount.setTaxamount(cna.getTaxamount());
				creditNoteAmount.setTaxpercentage(cna.getTaxpercentage() != null ? cna.getTaxpercentage() : null);
				creditNoteAmount.setTotalamount(cna.getTotalamount());
				creditNoteAmount.setCreditnoteamount(cna.getCreditnoteamount());
				creditNoteAmount.setCredittaxamount(cna.getCredittaxamount());
				creditNoteAmount.setRgnumber(cna.getRgnumber());
				creditNoteAmount.setRgdate(cna.getRgdate());
				creditNoteAmount.setReplacenumber(cna.getReplacenumber());
				creditNoteAmount.setCreditnetamount(cna.getCreditnetamount());
				creditNoteAmount.setConsumeNetAmount(cna.getConsumenetamount());
				creditNoteAmount.setIsSelectedCredit(cna.getIsSelectedCredit());
				creditNoteAmount.setIsChecked(cna.getIsChecked());
				creditNoteAmount.setAvailablenetamount(cna.getAvailablenetamount());
				//sessionService.setEntityDefaults(creditNoteAmount);
				creditNoteAmount.setAddedByUser(grnRequest.getUserId());
				creditNoteAmount.setUpdatedByUser(grnRequest.getUserId());
				creditNoteAmount.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				creditNoteAmount.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				creditNoteAmount.setOrganizationId(grnRequest.getOrgId());
				creditNoteAmount.setUnitId(grnRequest.getUnitId());
				
				grn.addCreditNoteAmountList(creditNoteAmount);
			}
		} // end added if condation by minesh for grn save issue.

		if (grnRequest.getDiscount() != null) {
			grn.setDiscount(grnRequest.getDiscount());
			grn.setDiscountType(new DiscountType(grnRequest.getDiscountTypeId()));
		}
		if (grnRequest.getPoId() != null) {
			grn.setPurchaseOrder(new PurchaseOrder(grnRequest.getPoId()));
		}

		for (GRNDetailsRequest item : grnRequest.getItems()) {
			//String lotNo = "LOT-" + num; 
			String lotNo = GlobalService.getBillNoByGeneratedCode(grnRequest.getStoreId(), grnRequest.getUnitId(), lotNoScreenId); //HIS-11226
			
			if(lotNo==null)
			{
				lotNo="LOT-"+num;
			}
			
			GoodReceiptNoteDetail grnDetail = null;
			if (grnRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
				if (grnRequest.getPoId() != null) {
					PurchaseOrderDetail poDetail = GlobalService.getPurchaseOrderDetail(item.getPoDetailId());

					List<GoodReceiptNoteDetail> grnDetailsList = poDetail.getGrnDetailsList();
					Iterator<GoodReceiptNoteDetail> de = grnDetailsList.iterator();
					while (de.hasNext()) {
						GoodReceiptNoteDetail d = de.next();
						if (d.getStatus().getId() == 8 || d.getStatus().getId() == 9) {
							de.remove();
						}

						// de.next().getGrnApprovedQuantity();
					}

					Integer grnReceivedQuantity = grnDetailsList.stream()
							.filter(grnDetails -> grnDetails.getGrnApprovedQuantity() == null)
							.mapToInt(grnQty -> grnQty.getGrnReceivedQuantity()).sum();

					Integer grnApprovedQuantity = grnDetailsList.stream()
							.filter(grnDetails -> grnDetails.getGrnApprovedQuantity() != null)
							.mapToInt(grnQty -> grnQty.getGrnApprovedQuantity()).sum();

					

					int conversion = 1;
					boolean calculate = false;
					int quantityCalculated  = 0;

					for (GRNUomMapper uom : item.getGrnUom()) {
						if (uom.getUomTypeId().equals(item.getGrnUomTypeId())) {
							conversion = conversion * uom.getConversion();
						}
					}
					quantityCalculated = item.getGrnReceivedQuantity() * conversion;
				
							
							
//					if ((grnReceivedQuantity + grnApprovedQuantity) > quantityCalculated) {
//						throw new InternalException("GRN quantity  is more than PO Quantity.");
//					}
				}

				GoodReceiptNoteDetail goodReceiptNoteDetail = new GoodReceiptNoteDetail(item.getBatchNo() !=null ? item.getBatchNo().toUpperCase() : null,
						item.getGrnReceivedQuantity(),
						item.getBonusReceivedQuantity() != null ? item.getBonusReceivedQuantity() : 0,
								item.getBonusUomTypeId()!=null ? new UomType(item.getBonusUomTypeId()) :null , item.getBonusUomUnitId() !=null ?new UomUnit(item.getBonusUomUnitId()) :null, item.getCop(),

						item.getDiscount() != null ? item.getDiscount() : null,
						item.getExpiryDate() != null ? item.getExpiryDate() : null, new UomType(item.getGrnUomTypeId()),
						new UomUnit(item.getGrnUomUnitId()), new Item(item.getItemId()), lotNo,
						new Manufacturer(item.getManufacturerId()), item.getMrp(), item.getNetAmount(),
						item.getRemark() != null ? item.getRemark() : null,
						item.getGrnSpecification() != null ? item.getGrnSpecification() : null,
						new Status(item.getStatusId()), item.getTaxAmount() != null ? item.getTaxAmount() : 0,
						new Tax(item.getTaxId() != null ? item.getTaxId() : null),
						item.getTaxPercentage() != null ? item.getTaxPercentage() : null, item.getTotalAmount(),
						item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTotalOtherAmount() != null ? item.getTotalOtherAmount() : null,
						item.getManufactureYear(), item.getPoDetailId() != null ? item.getPoDetailId() : null,
						item.getCreditNoteAmount() != null ? item.getCreditNoteAmount() : null,
						item.getLcop() != null ? item.getLcop() : null);
				goodReceiptNoteDetail
						.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : null);
				//sessionService.setEntityDefaults(goodReceiptNoteDetail);
				goodReceiptNoteDetail.setAddedByUser(grnRequest.getUserId());
				goodReceiptNoteDetail.setUpdatedByUser(grnRequest.getUserId());
				goodReceiptNoteDetail.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				goodReceiptNoteDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				goodReceiptNoteDetail.setOrganizationId(grnRequest.getOrgId());
				goodReceiptNoteDetail.setUnitId(grnRequest.getUnitId());
				goodReceiptNoteDetail.setRoundOffAmount(grnRequest.getRoundOff());
			
				/*
				 * for (CreditNoteAmountList cna : item.getCreditNoteAmountList()) {
				 * 
				 * CreditNoteAmount creditNoteAmount = new CreditNoteAmount();
				 * creditNoteAmount.setMateinwardid(cna.getMateinwardid());
				 * creditNoteAmount.setMateinwarddetailsid(cna.getMateinwarddetailsid());
				 * creditNoteAmount.setTaxamount(cna.getTaxamount());
				 * creditNoteAmount.setTaxpercentage(cna.getTaxpercentage()!=null ?
				 * cna.getTaxpercentage():null);
				 * creditNoteAmount.setTotalamount(cna.getTotalamount());
				 * creditNoteAmount.setCreditnoteamount(cna.getCreditnoteamount());
				 * creditNoteAmount.setCredittaxamount(cna.getCredittaxamount());
				 * creditNoteAmount.setRgnumber(cna.getRgnumber());
				 * creditNoteAmount.setRgdate(cna.getRgdate());
				 * creditNoteAmount.setReplacenumber(cna.getReplacenumber());
				 * creditNoteAmount.setCreditnetamount(cna.getCreditnetamount());
				 * creditNoteAmount.setConsumeNetAmount(cna.getConsumeNetAmount());
				 * creditNoteAmount.setIsSelectedCredit(cna.getIsSelectedCredit());
				 * creditNoteAmount.setIsChecked(cna.getIsChecked());
				 * creditNoteAmount.setAvailablenetamount(cna.getAvailablenetamount());
				 * sessionService.setEntityDefaults(creditNoteAmount);
				 * goodReceiptNoteDetail.addCreditNoteAmountList(creditNoteAmount); }
				 */

				grnDetail = grn.addGoodReceiptNoteDetailList(goodReceiptNoteDetail);

				if (item.getGrnUom() != null) {
					for (GRNUomMapper grnUomMapperDto : item.getGrnUom()) {
						GrnUomMapper grnUomMapper = new GrnUomMapper(grnUomMapperDto.getConversion(),
								grnUomMapperDto.getIpUom(), grnUomMapperDto.getOpUom(), grnUomMapperDto.getStoreUom(),
								new UomType(grnUomMapperDto.getUomTypeId()),
								new UomUnit(grnUomMapperDto.getUomUnitId()));
						//sessionService.setEntityDefaults(grnUomMapper);
						grnUomMapper.setAddedByUser(grnRequest.getUserId());
						grnUomMapper.setUpdatedByUser(grnRequest.getUserId());
						grnUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnUomMapper.setOrganizationId(grnRequest.getOrgId());
						grnUomMapper.setUnitId(grnRequest.getUnitId());
						grnDetail.addGRNUomMapperList(grnUomMapper);
					}
				}

				if (item.getBonusUom() != null) {
					for (GRNUomMapper bonusUomMapper : item.getBonusUom()) {
						GrnBonusUomMapper grnBonusUomMapper = new GrnBonusUomMapper(bonusUomMapper.getConversion(),
								bonusUomMapper.getIpUom(), bonusUomMapper.getOpUom(), bonusUomMapper.getStoreUom(),
								new UomType(bonusUomMapper.getUomTypeId()), new UomUnit(bonusUomMapper.getUomUnitId()));
						//sessionService.setEntityDefaults(grnBonusUomMapper);
						grnBonusUomMapper.setAddedByUser(grnRequest.getUserId());
						grnBonusUomMapper.setUpdatedByUser(grnRequest.getUserId());
						grnBonusUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnBonusUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnBonusUomMapper.setOrganizationId(grnRequest.getOrgId());
						grnBonusUomMapper.setUnitId(grnRequest.getUnitId());
						grnDetail.addGrnBonusUomMapperList(grnBonusUomMapper);
					}
				}
				grnDetail.setGrnLeaseQuantity(Util.GetLeaseQuantity(item.getGrnReceivedQuantity(),
						grnDetail.getGrnUomMappersList(), new UomType(item.getGrnUomTypeId())));
				if (item.getBonusReceivedQuantity() != null) {
					grnDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getBonusReceivedQuantity(),
							grnDetail.getGrnBonusUomMappersList(), new UomType(item.getBonusUomTypeId())));

				}
			} else if (grnRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				GoodReceiptNoteDetail goodReceiptNoteDetail = new GoodReceiptNoteDetail(item.getBatchNo()!=null ? item.getBatchNo().toUpperCase() :null,
						item.getGrnReceivedQuantity(),
						item.getBonusReceivedQuantity() != null ? item.getBonusReceivedQuantity() : null, item.getCop(),
						item.getDiscount() != null ? item.getDiscount() : null, new Item(item.getItemId()), lotNo,
						new Manufacturer(item.getManufacturerId()), item.getMrp(), item.getNetAmount(),
						item.getRemark() != null ? item.getRemark() : null,
						item.getGrnSpecification() != null ? item.getGrnSpecification() : null,
						new Status(item.getStatusId()), item.getTaxAmount() != null ? item.getTaxAmount() : 0,
						new Tax(item.getTaxId() != null ? item.getTaxId() : null),
						item.getTaxPercentage() != null ? item.getTaxPercentage() : null, item.getTotalAmount(),
						item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTotalOtherAmount() != null ? item.getTotalOtherAmount() : null, item.getWarrantyDate(),
						item.getWarrentyPeriod(), item.getSupportTerms(), item.getTechnicalSpecification(),
						item.getExpectedPayment(),
						item.getServiceProviderId() != null ? new Vendor(item.getServiceProviderId()) : null,
						item.getManufactureYear(),
						item.getCreditNoteAmount() != null ? item.getCreditNoteAmount() : null,
						item.getLcop() != null ? item.getLcop() : null);
				goodReceiptNoteDetail.setWarrantyDateTo(item.getWarrantyDateTo());
				goodReceiptNoteDetail
						.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : null);
				//sessionService.setEntityDefaults(goodReceiptNoteDetail);
				goodReceiptNoteDetail.setAddedByUser(grnRequest.getUserId());
				goodReceiptNoteDetail.setUpdatedByUser(grnRequest.getUserId());
				goodReceiptNoteDetail.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				goodReceiptNoteDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
				goodReceiptNoteDetail.setOrganizationId(grnRequest.getOrgId());
				goodReceiptNoteDetail.setUnitId(grnRequest.getUnitId());
				goodReceiptNoteDetail.setRoundOffAmount(grnRequest.getRoundOff());
				/*
				 * for (CreditNoteAmountList cna : item.getCreditNoteAmountList()) {
				 * CreditNoteAmount creditNoteAmount = new CreditNoteAmount();
				 * creditNoteAmount.setMateinwardid(cna.getMateinwardid());
				 * creditNoteAmount.setMateinwarddetailsid(cna.getMateinwarddetailsid());
				 * creditNoteAmount.setTaxamount(cna.getTaxamount());
				 * creditNoteAmount.setTaxpercentage(cna.getTaxpercentage()!=null ?
				 * cna.getTaxpercentage():null);
				 * creditNoteAmount.setTotalamount(cna.getTotalamount());
				 * creditNoteAmount.setCreditnoteamount(cna.getCreditnoteamount());
				 * creditNoteAmount.setCredittaxamount(cna.getCredittaxamount());
				 * creditNoteAmount.setRgnumber(cna.getRgnumber());
				 * creditNoteAmount.setRgdate(cna.getRgdate());
				 * creditNoteAmount.setReplacenumber(cna.getReplacenumber());
				 * creditNoteAmount.setCreditnetamount(cna.getCreditnetamount());
				 * creditNoteAmount.setConsumeNetAmount(cna.getConsumeNetAmount());
				 * creditNoteAmount.setIsSelectedCredit(cna.getIsSelectedCredit());
				 * creditNoteAmount.setIsChecked(cna.getIsChecked());
				 * creditNoteAmount.setAvailablenetamount(cna.getAvailablenetamount());
				 * sessionService.setEntityDefaults(creditNoteAmount);
				 * goodReceiptNoteDetail.addCreditNoteAmountList(creditNoteAmount); }
				 */
				grnDetail = grn.addGoodReceiptNoteDetailList(goodReceiptNoteDetail);
				if (item.getGrnAccessoriesMapper() != null) {
					for (GrnAccessoriesMapper mapper : item.getGrnAccessoriesMapper()) {
						GrnAccessory grnAccessory = new GrnAccessory(new Item(mapper.getItemId()), mapper.getSerialNo(),
								mapper.getWarrentyFrom(), mapper.getWarrentyTo(), mapper.getQuantity(),
								mapper.getSpecification());
						//sessionService.setEntityDefaults(grnAccessory);
						grnAccessory.setAddedByUser(grnRequest.getUserId());
						grnAccessory.setUpdatedByUser(grnRequest.getUserId());
						grnAccessory.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnAccessory.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						grnAccessory.setOrganizationId(grnRequest.getOrgId());
						grnAccessory.setUnitId(grnRequest.getUnitId());
						grnDetail.addGrnAccessoryList(grnAccessory);
					}
				}
			}

			if (item.getPoDetailId() != null) {
				grnDetail.setPurchaseOrderDetail(new PurchaseOrderDetail(item.getPoDetailId()));
			}

			/*
			 * if(grnDetail.getPurchaseOrderDetail()!=null) {
			 * grnDetail.setPurchaseOrderDetail(new
			 * PurchaseOrderDetail(item.getPoDetailId())); }
			 */

			/*
			 * if (item.getOtherCharges() != null) { for (GRNOtherChargesMapper otherCharges
			 * : item.getOtherCharges()) { if (otherCharges.getOtherChargesId() != null) {
			 * GrnOtherChargesMapper grnOtherChargesMapper = new GrnOtherChargesMapper( new
			 * OtherCharge(otherCharges.getOtherChargesId()), otherCharges.getAmount());
			 * sessionService.setEntityDefaults(grnOtherChargesMapper);
			 * grnDetail.addGRNOtherChargesMapperList(grnOtherChargesMapper); } } }
			 */

			/*
			 * if (item.getGrnStagedQuantity() != null) { for (GRNStagedDto
			 * grnStagedQuantity : item.getGrnStagedQuantity()) {
			 * if(grnStagedQuantity.getReceivedQuantity() > 0){
			 * com.param.entity.model.procurement.GRNStagedQuantity gRNStagedQuantity =new
			 * com.param.entity.model.procurement.GRNStagedQuantity(
			 * grnStagedQuantity.getReceivedQuantity(), new
			 * PurchaseOrderStaged(grnStagedQuantity.getPoStagedId()));
			 * grnDetail.addGRNStagedQuantityList(gRNStagedQuantity); } } }
			 */

			if (item.getGrnStagedLists() != null && !item.getGrnStagedLists().isEmpty()) {
				for (GRNStagedList grnStagedList : item.getGrnStagedLists()) {
					if (grnStagedList.getStagedReceivedQuantity() > 0) {
						com.param.entity.model.procurement.GRNStagedQuantity gRNStagedQuantity = new com.param.entity.model.procurement.GRNStagedQuantity(
								grnStagedList.getStagedReceivedQuantity(), grnStagedList.getStagedDcDate(),
								grnDetail.getPurchaseOrderDetail());
						//sessionService.setEntityDefaults(gRNStagedQuantity);
						gRNStagedQuantity.setAddedByUser(grnRequest.getUserId());
						gRNStagedQuantity.setUpdatedByUser(grnRequest.getUserId());
						gRNStagedQuantity.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						gRNStagedQuantity.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						gRNStagedQuantity.setOrganizationId(grnRequest.getOrgId());
						gRNStagedQuantity.setUnitId(grnRequest.getUnitId());
						grnDetail.addGRNStagedQuantityList(gRNStagedQuantity);
					}
				}
			}

			if (grnRequest.getGrnTypeId() == com.param.common.constants.RequestType.AgainstPurchaseOrder
					.getRequestType() && item.getPoDetailId() != null) {
				PurchaseOrderDetail PurchaseOrderDetail = grnDao.getPurchaseOrderDetails(item.getPoDetailId());
				if (PurchaseOrderDetail != null) {
					// grnDetail.addPurchaseOrderDetailList(PurchaseOrderDetail);
					poMap.put(PurchaseOrderDetail.getId(), PurchaseOrderDetail);
				}
				if (PurchaseOrderDetail.getPurchaseRequestDetailList() != null) {
					for (PurchaseRequestDetail prDetail : PurchaseOrderDetail.getPurchaseRequestDetailList()) {
						// update for pr
						// grnDetail.addPurchaseRequestDetailList(prDetail);
						prMap.put(prDetail.getId(), prDetail);
					}
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
			//sessionService.setEntityDefaults(grnDetail);
			grnDetail.setAddedByUser(grnRequest.getUserId());
			grnDetail.setUpdatedByUser(grnRequest.getUserId());
			grnDetail.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
			grnDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
			grnDetail.setOrganizationId(grnRequest.getOrgId());
			grnDetail.setUnitId(grnRequest.getUnitId());
		}

		// Integer item.getPoDetailId();
		for (GRNDetailsRequest item : grnRequest.getItems()) {
			if (grnRequest.getGrnTypeId() == com.param.common.constants.RequestType.AgainstPurchaseOrder
					.getRequestType() && item.getPoDetailId() != null) {
				// for (Integer poDetailId : item.getPoDetailId()) {
				for (GoodReceiptNoteDetail grnDetails : grn.getGoodReceiptNoteDetailsList()) {
					if (grnDetails.getItem().getId().equals(item.getItemId())) {
						PurchaseOrderDetail poDetail = poMap.get(item.getPoDetailId());
						grnDetails.addPurchaseOrderDetailList(poDetail);
						for (PurchaseRequestDetail prDetail : poDetail.getPurchaseRequestDetailList()) {
							PurchaseRequestDetail pr = prMap.get(prDetail.getId());
							grnDetails.addPurchaseRequestDetailList(pr);
						}

					}
				}
			}
		}
		
		grnDao.saveGoodReceiptNote(grn);
		CommonResponse response = new CommonResponse();
		response.setId(grn.getId().longValue());
		Integer index=0;
		Integer index1=0;
		for(GoodReceiptNoteDetail grnDetail:grn.getGoodReceiptNoteDetailsList())
		{
			
			GRNDetailsRequest item=grnRequest.getItems().get(index);
				if (grnRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
					BonusBatchExpiryDao.updateBonusBatchExpiry(grnDetail.getId());
					for (GrnBonusBatchExpiry grnBatchExpiry : item.getGrnBonusBatchExpiry()) {
	                    GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
	                            grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
	                            grnBatchExpiry1.setExpiryDate(grnBatchExpiry.getExpiryDate());
	                            grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
	                            grnBatchExpiry1.setGrnDetailId(grnDetail.getId());
	                            grnBatchExpiry1.setOrganizationId(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId():sessionService.getOrganizationId());
	                            grnBatchExpiry1.setUnitId(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() :sessionService.getUnitId());
	                            grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() :sessionService.getUnitId()));
	                            grnBatchExpiry1.setUpdatedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
	                            grnBatchExpiry1.setAddedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
	                            grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() : sessionService.getUnitId()));
	                            grnBatchExpiry1.setGrnBonusBatchExpiryId(grnBatchExpiry.getGrnBonusBatchExpiryId());
//	                            if(grnBatchExpiry1.getGrnBonusBatchExpiryId()!=null)
//	                            {
//	                                BonusBatchExpiryDao.updateBonusBatchExpiry(grnBatchExpiry1);
//	                            }
//	                            else
//	                            {
	                            BonusBatchExpiryDao.saveBonusBatchExpiry(grnBatchExpiry1);
//	                            }
	                }
				}
				index++;
		}
		for(GoodReceiptNoteDetail grnDetail:grn.getGoodReceiptNoteDetailsList())
		{
			
			GRNDetailsRequest item=grnRequest.getItems().get(index1);
				if (grnRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					BonusBatchExpiryDao.updateBonusBatchExpiry(grnDetail.getId());
					for (GrnBonusBatchExpiry grnBatchExpiry : item.getGrnBonusBatchExpiry()) {
	                    GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
	                            grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
	                            grnBatchExpiry1.setExpiryDate(null);
	                            grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
	                            grnBatchExpiry1.setGrnDetailId(grnDetail.getId());
	                            grnBatchExpiry1.setOrganizationId(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId():sessionService.getOrganizationId());
	                            grnBatchExpiry1.setUnitId(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() :sessionService.getUnitId());
	                            grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() :sessionService.getUnitId()));
	                            grnBatchExpiry1.setUpdatedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
	                            grnBatchExpiry1.setAddedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
	                            grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()!=null ? grnRequest.getUnitId() : sessionService.getUnitId()));
	                            grnBatchExpiry1.setGrnBonusBatchExpiryId(grnBatchExpiry.getGrnBonusBatchExpiryId());
//	                            if(grnBatchExpiry1.getGrnBonusBatchExpiryId()!=null)
//	                            {
//	                                BonusBatchExpiryDao.updateBonusBatchExpiry(grnBatchExpiry1);
//	                            }
//	                            else
//	                            {
	                            BonusBatchExpiryDao.saveBonusBatchExpiry(grnBatchExpiry1);
//	                            }
	                }
				}
				index1++;
		}
		// added if condation by minesh for grn save issue.
		if (grnRequest.getCreditNoteAmountList() != null) {
			for (CreditNoteAmountList cna : grnRequest.getCreditNoteAmountList()) {
				grnDao.updateCreditNoteAmt(cna.getMateinwardid(), cna.getConsumenetamount());
			}
		} // end added if condation by minesh for grn save issue.
		response.setMessage("Good Receipt Note saved successfully");
		return response;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Object updateGoodReceiptNote(GRNRequest grnRequest) {

		GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(grnRequest.getId());

		DiscountType discountType = null;
		if (grnRequest.getDiscount() != null) {
			if (grnRequest.getDiscountTypeId() != null) {
				discountType = grnDao.getDiscountTypeById(grnRequest.getDiscountTypeId());
			} else if (goodReceiptNote.getDiscountType() != null) {
				discountType = goodReceiptNote.getDiscountType();
			}
		}

		goodReceiptNote.updateGoodReceiptNote(
				grnRequest.getDcDate() != null ? grnRequest.getDcDate() : goodReceiptNote.getDcDate(),
				grnRequest.getDcNumber() != null ? grnRequest.getDcNumber() : goodReceiptNote.getDcNumber(),
				grnRequest.getInwardDate() != null ? grnRequest.getInwardDate() : goodReceiptNote.getInwardDate(),
				grnRequest.getInwardNumber() != null ? grnRequest.getInwardNumber() : goodReceiptNote.getInwardNumber(),
				grnRequest.getDiscount() != null ? grnRequest.getDiscount() : goodReceiptNote.getDiscount(),
//				grnRequest.getDiscount() != null
//						? new DiscountType(grnRequest.getDiscountTypeId() != null ? grnRequest.getDiscountTypeId()
//								: goodReceiptNote.getDiscountType().getId())
//						: null,
				discountType,
				grnRequest.getIsConsignment() != null ? grnRequest.getIsConsignment()
						: goodReceiptNote.getIsConsignment(),
				grnRequest.getNetAmount() != null ? grnRequest.getNetAmount() : goodReceiptNote.getNetAmount(),
				grnRequest.getRemark() != null ? grnRequest.getRemark() : goodReceiptNote.getRemark(),
				grnRequest.getRoundOff() != null ? grnRequest.getRoundOff() : goodReceiptNote.getRoundOff(),
				grnRequest.getTotalAmount() != null ? grnRequest.getTotalAmount() : goodReceiptNote.getTotalAmount(),
				grnRequest.getTotalDiscount() != null ? grnRequest.getTotalDiscount()
						: goodReceiptNote.getTotalDiscount(),
				grnRequest.getTotalOtherAmount() != null ? grnRequest.getTotalOtherAmount()
						: goodReceiptNote.getTotalOtherAmount(),
				grnRequest.getTotalTaxAmount() != null ? grnRequest.getTotalTaxAmount()
						: goodReceiptNote.getTotalTaxAmmount(),
				new Vendor(grnRequest.getVendorId() != null ? grnRequest.getVendorId()
						: goodReceiptNote.getVendor().getId()),
				grnRequest.getBillTypeName() != null ? grnRequest.getBillTypeName() : goodReceiptNote.getBillTypeName(),
				grnRequest.getTotalCreditNoteAmount() != null ? grnRequest.getTotalCreditNoteAmount()
						: goodReceiptNote.getTotalCreditNoteAmount());
		goodReceiptNote.setDueDate(GlobalCommonDateUtils.getDate(grnRequest.getDueDate(), "dd-M-yyyy HH:mm:ss"));
		
		if(grnRequest.getVendorId()!=null)
		{
			goodReceiptNote.setVendor(new Vendor(grnRequest.getVendorId()));
		}
		goodReceiptNote.setInvoiceNumber(grnRequest.getInvoiceNumber()!=null ? grnRequest.getInvoiceNumber() : goodReceiptNote.getInvoiceNumber());
		for (GrnOtherChargesMapper grnOtherChargesMapper : goodReceiptNote.getGrnOtherChargesMapperList()) {
			Integer otherChargesId = null;
			for (GRNOtherChargesMapper grnOtherCharges : grnRequest.getOtherCharges()) {
				if (grnOtherCharges.getId() != null && grnOtherCharges.getId().equals(grnOtherChargesMapper.getId())) {
					otherChargesId = grnOtherCharges.getId();
					grnOtherChargesMapper.updateGrnOtherChargesMapper(
							new OtherCharge(grnOtherCharges.getOtherChargesId()), grnOtherCharges.getAmount());
				}
			}
			if (otherChargesId == null) {
				grnOtherChargesMapper.setIsDeleted(true);
			}

			//sessionService.setEntityUpdateDefaults(grnOtherChargesMapper);
			// grnOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			// grnOtherChargesMapper.setUpdatedBy(grnOtherChargesMapper.getUser());
		}

		for (CreditNoteAmount creditNoteAmount : goodReceiptNote.getCreditNoteAmountList()) {
			Integer creditNoteAmountId = null;

			for (CreditNoteAmountList creditNoteAmountList : grnRequest.getCreditNoteAmountList()) {
				if (creditNoteAmountList.getId() != null
						&& creditNoteAmountList.getId().equals(creditNoteAmount.getId())) {
					creditNoteAmountId = creditNoteAmountList.getId();
					creditNoteAmount.updateCreditNoteAmount(creditNoteAmountList.getMateinwardid(),
							creditNoteAmountList.getMateinwarddetailsid() != null
									? creditNoteAmountList.getMateinwarddetailsid()
									: null,
							creditNoteAmountList.getTaxamount(),
							creditNoteAmountList.getTaxpercentage() != null ? creditNoteAmountList.getTaxpercentage()
									: null,
							creditNoteAmount.getTotalamount(), creditNoteAmount.getCreditnoteamount(),
							creditNoteAmount.getCredittaxamount(), creditNoteAmount.getRgnumber(),
							creditNoteAmount.getRgdate(), creditNoteAmount.getReplacenumber(),
							creditNoteAmount.getCreditnetamount(), creditNoteAmount.getConsumeNetAmount(),
							creditNoteAmount.getIsSelectedCredit(), creditNoteAmount.getIsChecked(),
							creditNoteAmount.getAvailablenetamount());
				}

			}
			
		
			
			if (creditNoteAmountId == null) {
//				creditNoteAmount.setIsDeleted(true);
			}
			creditNoteAmount.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
		}

		
		for (CreditNoteAmountList creditNoteAmountList : grnRequest.getCreditNoteAmountList()) {
				if(creditNoteAmountList.getId() == null) {
				
				Response rsz = iPatientPanCardDocDao.addConsumeAmount(creditNoteAmountList.getConsumenetamount(),creditNoteAmountList.getRgnumber(),creditNoteAmountList.getInwordno());
				if(rsz.getObject() != null) {
					Integer id = (Integer)rsz.getObject();
					CreditNoteAmount creditNoteAmount = new CreditNoteAmount();
					creditNoteAmount.setMateinwardid(id);
					creditNoteAmount.setRgnumber(creditNoteAmountList.getRgnumber());
					creditNoteAmount.setReplacenumber(creditNoteAmountList.getReplacenumber());
					creditNoteAmount.setCreditnetamount(creditNoteAmountList.getCreditnetamount());
					creditNoteAmount.setConsumeNetAmount(creditNoteAmountList.getConsumenetamount());
					creditNoteAmount.setAvailablenetamount(creditNoteAmountList.getAvailablenetamount());
					//sessionService.setEntityDefaults(creditNoteAmount);
					creditNoteAmount.setAddedByUser(grnRequest.getUserId());
					creditNoteAmount.setUpdatedByUser(grnRequest.getUserId());
					creditNoteAmount.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					creditNoteAmount.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					creditNoteAmount.setOrganizationId(goodReceiptNote.getOrganizationId());
					creditNoteAmount.setUnitId(goodReceiptNote.getUnitId());
					
					goodReceiptNote.addCreditNoteAmountList(creditNoteAmount);
				}
				
				}
			}
		
		if (grnRequest.getOtherCharges() != null) {
			for (GRNOtherChargesMapper grnOtherCharges : grnRequest.getOtherCharges()) {
				if (grnOtherCharges.getId() == null) {
					if (grnOtherCharges.getOtherChargesId() != null) {
						GrnOtherChargesMapper grnOtherChargesMapper = new GrnOtherChargesMapper(
								new OtherCharge(grnOtherCharges.getOtherChargesId()), grnOtherCharges.getAmount());
						grnOtherChargesMapper.setIsFromGrn(grnOtherCharges.getIsFromGrn());
						grnOtherChargesMapper.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());

						goodReceiptNote.addGRNOtherChargesMapperList(grnOtherChargesMapper);
					}
				}
			}
		}
		for (GoodReceiptNoteDetail grnDetail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
			Integer id = null;
			for (GRNDetailsRequest item : grnRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(grnDetail.getId())) {
					id = item.getId();

					if (goodReceiptNote.getAssetType().getId().equals(Constants.getConsumableId())) {
						if(grnRequest.getIsFromApprovalEdit()==true)
						{
						GoodReceiptNoteDetailLog grnLogDetail=new GoodReceiptNoteDetailLog();
						grnLogDetail.UpdateGoodReceiptNoteDetailLog(
								grnDetail.getBatchNo() != null ? grnDetail.getBatchNo().toUpperCase() : grnDetail.getBatchNo().toUpperCase(),
										grnDetail.getGrnReceivedQuantity(),
										grnDetail.getBonusReceivedQuantity() != null ? grnDetail.getBonusReceivedQuantity() : null,
												grnDetail.getBonusUomType() != null ? new UomType(grnDetail.getBonusUomType().getId()) : null,
														grnDetail.getBonusUomType() != null ? new UomUnit(grnDetail.getBonusUomType().getId()) : null,
																grnDetail.getCop(),
																grnDetail.getDiscount() != null ? grnDetail.getDiscount() : grnDetail.getDiscount(),
																		grnDetail.getExpiryDate() != null ? grnDetail.getExpiryDate() : grnDetail.getExpiryDate(),
								new UomType(grnDetail.getGrnUomType().getId()), new UomUnit(grnDetail.getGrnUomType().getId()), grnDetail.getMrp(),
								grnDetail.getNetAmount() != null ? grnDetail.getNetAmount() : grnDetail.getNetAmount(),
										grnDetail.getRemark() != null ? grnDetail.getRemark() : grnDetail.getRemark(),
												grnDetail.getSpecification() != null ? grnDetail.getSpecification()
										: grnDetail.getSpecification(),
										grnDetail.getTaxAmount() != null ? grnDetail.getTaxAmount() : grnDetail.getTaxAmount(),
								new Tax(grnDetail.getTax() != null ? grnDetail.getTax().getId() : grnDetail.getTax().getId()),
								grnDetail.getTaxPercentage() != null ? grnDetail.getTaxPercentage()
										: grnDetail.getTaxPercentage(),
										grnDetail.getTotalAmount(),
										grnDetail.getTotalDiscount() != null ? grnDetail.getTotalDiscount()
										: grnDetail.getTotalDiscount(),
										grnDetail.getTotalOtherAmount() != null ? grnDetail.getTotalOtherAmount()
										: grnDetail.getTotalOtherAmount());
						grnLogDetail.setGrnDetailId(grnDetail.getId());
						grnLogDetail.setItemId(grnDetail.getItemId()!=null ? grnDetail.getItemId():item.getItemId());
						grnLogDetail.setPendingQuantity(grnDetail.getPendingQuantity() != null ? grnDetail.getPendingQuantity()
								: grnDetail.getPendingQuantity());
						grnLogDetail.setOrganizationId(grnDetail.getOrganizationId());
						grnLogDetail.setUnitId(grnDetail.getUnitId());
						grnLogDetail.setGrnId(goodReceiptNote.getId());
						grnLogDetail.setStatusId(grnDetail.getStatus().getId());
						grnLogDetail.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
						
						grnDao.saveDetails(grnLogDetail);
					}
						grnDetail.UpdateGoodReceiptNoteDetail(
								item.getBatchNo() != null ? item.getBatchNo().toUpperCase() : grnDetail.getBatchNo().toUpperCase(),
								item.getGrnReceivedQuantity(),
								item.getBonusReceivedQuantity() != null ? item.getBonusReceivedQuantity() : null,
								item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
								item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
								item.getCop(),
								item.getDiscount() != null ? item.getDiscount() : grnDetail.getDiscount(),
								item.getExpiryDate() != null ? item.getExpiryDate() : grnDetail.getExpiryDate(),
								new UomType(item.getGrnUomTypeId()), new UomUnit(item.getGrnUomUnitId()), item.getMrp(),
								item.getNetAmount() != null ? item.getNetAmount() : grnDetail.getNetAmount(),
								item.getRemark() != null ? item.getRemark() : grnDetail.getRemark(),
								item.getGrnSpecification() != null ? item.getGrnSpecification()
										: grnDetail.getSpecification(),
								item.getTaxAmount() != null ? item.getTaxAmount() : grnDetail.getTaxAmount(),
								new Tax(item.getTaxId() != null ? item.getTaxId() : grnDetail.getTax().getId()),
								item.getTaxPercentage() != null ? item.getTaxPercentage()
										: grnDetail.getTaxPercentage(),
								item.getTotalAmount(),
								item.getTotalDiscount() != null ? item.getTotalDiscount()
										: grnDetail.getTotalDiscount(),
								item.getTotalOtherAmount() != null ? item.getTotalOtherAmount()
										: grnDetail.getTotalOtherAmount());
						grnDetail.setItemId(item.getItemId());
						grnDetail.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity()
								: grnDetail.getPendingQuantity());

						for (GrnUomMapper grnUomMapper : grnDetail.getGrnUomMappersList()) {
							for (GRNUomMapper grnUom : item.getGrnUom()) {
								if (grnUom.getId() != null && grnUom.getId().equals(grnUomMapper.getId())) {
									grnUomMapper.updateGrnUomMapper(new UomType(grnUom.getUomTypeId()),
											new UomUnit(grnUom.getUomUnitId()), grnUom.getConversion(),
											grnUom.getIpUom(), grnUom.getOpUom(), grnUom.getStoreUom());
								}
							}
						}

						for (GrnBonusUomMapper grnBonusMapper : grnDetail.getGrnBonusUomMappersList()) {
							for (GRNUomMapper grnBonusUomMapper : item.getBonusUom()) {
								if (grnBonusUomMapper.getId() != null
										&& grnBonusUomMapper.getId().equals(grnBonusMapper.getId())) {
									grnBonusMapper.updateGrnBonusUomMapper(
											new UomType(grnBonusUomMapper.getUomTypeId()),
											new UomUnit(grnBonusUomMapper.getUomUnitId()),
											grnBonusUomMapper.getConversion(), grnBonusUomMapper.getIpUom(),
											grnBonusUomMapper.getOpUom(), grnBonusUomMapper.getStoreUom());
								}
							}
						}
						grnDetail.setGrnLeaseQuantity(Util.GetLeaseQuantity(item.getGrnReceivedQuantity(),
								grnDetail.getGrnUomMappersList(), new UomType(item.getGrnUomTypeId())));
						if(item.getBonusReceivedQuantity()!=null)
						{
						grnDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getBonusReceivedQuantity(),
						grnDetail.getGrnBonusUomMappersList(), new UomType(item.getBonusUomTypeId())));
						}
					} else if (goodReceiptNote.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						if(grnRequest.getIsFromApprovalEdit()==true)
						{
						GoodReceiptNoteDetailLog grnLogDetail=new GoodReceiptNoteDetailLog();
						grnLogDetail.UpdateGoodReceiptNoteDetailLogForBonus(
								grnDetail.getBatchNo() != null ? grnDetail.getBatchNo().toUpperCase() : grnDetail.getBatchNo().toUpperCase(),
										grnDetail.getGrnReceivedQuantity(),
										grnDetail.getBonusReceivedQuantity() != null ? grnDetail.getBonusReceivedQuantity() : null,
												grnDetail.getBonusUomType() != null ? new UomType(grnDetail.getBonusUomType().getId()) : null,
														grnDetail.getBonusUomType() != null ? new UomUnit(grnDetail.getBonusUomType().getId()) : null,
																grnDetail.getCop(),
																grnDetail.getDiscount() != null ? grnDetail.getDiscount() : grnDetail.getDiscount(),
																		grnDetail.getExpiryDate() != null ? grnDetail.getExpiryDate() : grnDetail.getExpiryDate(),
								 grnDetail.getMrp(),
								grnDetail.getNetAmount() != null ? grnDetail.getNetAmount() : grnDetail.getNetAmount(),
										grnDetail.getRemark() != null ? grnDetail.getRemark() : grnDetail.getRemark(),
												grnDetail.getSpecification() != null ? grnDetail.getSpecification()
										: grnDetail.getSpecification(),
										grnDetail.getTaxAmount() != null ? grnDetail.getTaxAmount() : grnDetail.getTaxAmount(),
								new Tax(grnDetail.getTax() != null ? grnDetail.getTax().getId() : grnDetail.getTax().getId()),
								grnDetail.getTaxPercentage() != null ? grnDetail.getTaxPercentage()
										: grnDetail.getTaxPercentage(),
										grnDetail.getTotalAmount(),
										grnDetail.getTotalDiscount() != null ? grnDetail.getTotalDiscount()
										: grnDetail.getTotalDiscount(),
										grnDetail.getTotalOtherAmount() != null ? grnDetail.getTotalOtherAmount()
										: grnDetail.getTotalOtherAmount());
						grnLogDetail.setGrnDetailId(grnDetail.getId());
						grnLogDetail.setItemId(grnDetail.getItemId()!=null ? grnDetail.getItemId():item.getItemId());
						grnLogDetail.setPendingQuantity(grnDetail.getPendingQuantity() != null ? grnDetail.getPendingQuantity()
								: grnDetail.getPendingQuantity());
						grnLogDetail.setOrganizationId(grnDetail.getOrganizationId());
						grnLogDetail.setUnitId(grnDetail.getUnitId());
						grnLogDetail.setGrnId(goodReceiptNote.getId());
						grnLogDetail.setStatusId(grnDetail.getStatus().getId());
						grnLogDetail.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
						
						grnDao.saveDetails(grnLogDetail);
					}
						grnDetail.UpdateFixedGoodReceiptNoteDetail(
								item.getBatchNo() != null ? item.getBatchNo().toUpperCase() : grnDetail.getBatchNo().toUpperCase(),
								item.getGrnReceivedQuantity(),
								item.getBonusReceivedQuantity() != null ? item.getBonusReceivedQuantity() : null,
								item.getCop(),
								item.getDiscount() != null ? item.getDiscount() : grnDetail.getDiscount(),
								item.getExpiryDate() != null ? item.getExpiryDate() : grnDetail.getExpiryDate(),
								item.getMrp(),
								item.getNetAmount() != null ? item.getNetAmount() : grnDetail.getNetAmount(),
								item.getRemark() != null ? item.getRemark() : grnDetail.getRemark(),
								item.getGrnSpecification() != null ? item.getGrnSpecification()
										: grnDetail.getSpecification(),
								item.getTaxAmount() != null ? item.getTaxAmount() : grnDetail.getTaxAmount(),
								new Tax(item.getTaxId() != null ? item.getTaxId() : grnDetail.getTax().getId()),
								item.getTaxPercentage() != null ? item.getTaxPercentage()
										: grnDetail.getTaxPercentage(),
								item.getTotalAmount(),
								item.getTotalDiscount() != null ? item.getTotalDiscount()
										: grnDetail.getTotalDiscount(),
								item.getTotalOtherAmount() != null ? item.getTotalOtherAmount()
										: grnDetail.getTotalOtherAmount());
						grnDetail.setItemId(item.getItemId());
						grnDetail.setWarrantyDateTo(item.getWarrantyDateTo() != null ? item.getWarrantyDateTo()
								: grnDetail.getWarrantyDateTo());
						grnDetail.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity()
								: grnDetail.getPendingQuantity());

						// update GRNAccessories Mapper
						/*
						 * for(GrnAccessory grnAccessories : grnDetail.getGrnAccessoryList()) {
						 * for(grnAccessoriesMapper mapper : item.getGrnAccessoriesMapper()) {
						 * if(grnAccessories.getId().equals(mapper.getId())) } }
						 */
						if (item.getGrnAccessoriesMapper() != null) {
							for (GrnAccessoriesMapper mapper : item.getGrnAccessoriesMapper()) {
								GrnAccessory grnAccessory = new GrnAccessory(new Item(mapper.getItemId()),
										mapper.getSerialNo(), mapper.getWarrentyFrom(), mapper.getWarrentyTo(),
										mapper.getQuantity(), mapper.getSpecification());
								grnAccessory.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());

								grnDetail.addGrnAccessoryList(grnAccessory);
							}
						}

					}

					for (com.param.entity.model.procurement.GRNStagedQuantity grnStagedQuantity : grnDetail
							.getGrnStagedQuantityList()) {
						for (GRNStagedList grnStagedList : item.getGrnStagedLists()) {
							if (grnStagedList.getId() != null
									&& grnStagedList.getId().equals(grnStagedQuantity.getId())) {
								grnStagedQuantity.updateGRNStagedQuantity(
										grnStagedList.getStagedReceivedQuantity() != null
												? grnStagedList.getStagedReceivedQuantity()
												: grnStagedQuantity.getReceivedQuantity(),

										grnStagedList.getStagedDcDate() != null ? grnStagedList.getStagedDcDate()
												: grnStagedQuantity.getStagedDcDate());
							}
						}
					}

				}
			}
			if (id == null) {
				grnDetail.setIsDeleted(true);
				grnDetail.setIsActive(false);
			}
			// sessionService.setEntityDefaults(grnDetail);
			// grnDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			// grnDetail.setUpdatedBy(grnDetail.getUser());
			grnDetail.setUpdateDefaults(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId(), goodReceiptNote.getUnitId());
		}
		for (GRNDetailsRequest item : grnRequest.getItems()) {
			if (item.getId() == null) {
				Random rand = new Random();
				int num = rand.nextInt(1000);
				String lotNo = "LOT-" + num;

				GoodReceiptNoteDetail grnDetail = null;
				if (goodReceiptNote.getAssetType().getId().equals(Constants.getConsumableId())) {
					GoodReceiptNoteDetail goodReceiptNoteDetail = new GoodReceiptNoteDetail(item.getBatchNo().toUpperCase(),
							item.getGrnReceivedQuantity(), item.getBonusReceivedQuantity(),
							new UomType(item.getBonusUomTypeId()!=null ? item.getBonusUomTypeId() :item.getGrnUomTypeId()), new UomUnit(item.getBonusUomUnitId() != null ? item.getBonusUomUnitId() : item.getGrnUomUnitId()), item.getCop(),
							item.getDiscount() != null ? item.getDiscount() : null,
							item.getExpiryDate() != null ? item.getExpiryDate() : null,
							new UomType(item.getGrnUomTypeId()), new UomUnit(item.getGrnUomUnitId()),
							new Item(item.getItemId()), lotNo, new Manufacturer(item.getManufacturerId()),
							item.getMrp(), item.getNetAmount(), item.getRemark() != null ? item.getRemark() : null,
							item.getGrnSpecification() != null ? item.getGrnSpecification() : null,
							new Status(item.getStatusId()), item.getTaxAmount() != null ? item.getTaxAmount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage(), item.getTotalAmount(),
							item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTotalOtherAmount() != null ? item.getTotalOtherAmount() : null,
							item.getManufactureYear());
					goodReceiptNoteDetail
							.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : null);
					goodReceiptNoteDetail.setItemId(item.getItemId());
					goodReceiptNoteDetail.setLcop(item.getLcop());
					grnDetail = goodReceiptNote.addGoodReceiptNoteDetailList(goodReceiptNoteDetail);

					if (item.getGrnUom() != null) {
						for (GRNUomMapper grnUomMapperDto : item.getGrnUom()) {
							GrnUomMapper grnUomMapper = new GrnUomMapper(grnUomMapperDto.getConversion(),
									grnUomMapperDto.getIpUom(), grnUomMapperDto.getOpUom(),
									grnUomMapperDto.getStoreUom(), new UomType(grnUomMapperDto.getUomTypeId()),
									new UomUnit(grnUomMapperDto.getUomUnitId()));
							grnUomMapper.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
							
							grnDetail.addGRNUomMapperList(grnUomMapper);
						}
					}

					if (item.getBonusUom() != null) {
						for (GRNUomMapper bonusUomMapper : item.getBonusUom()) {
							GrnBonusUomMapper grnBonusUomMapper = new GrnBonusUomMapper(bonusUomMapper.getConversion(),
									bonusUomMapper.getIpUom(), bonusUomMapper.getOpUom(), bonusUomMapper.getStoreUom(),
									new UomType(bonusUomMapper.getUomTypeId()),
									new UomUnit(bonusUomMapper.getUomUnitId()));
							grnBonusUomMapper.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
							
							grnDetail.addGrnBonusUomMapperList(grnBonusUomMapper);
						}
					}
				} else if (goodReceiptNote.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					grnDetail = goodReceiptNote.addGoodReceiptNoteDetailList(new GoodReceiptNoteDetail(
							item.getBatchNo().toUpperCase(), item.getGrnReceivedQuantity(),
							item.getBonusReceivedQuantity() != null ? item.getBonusReceivedQuantity() : null,
							item.getCop(), item.getDiscount() != null ? item.getDiscount() : null,
							new Item(item.getItemId()), lotNo, new Manufacturer(item.getManufacturerId()),
							item.getMrp(), item.getNetAmount(), item.getRemark() != null ? item.getRemark() : null,
							item.getGrnSpecification() != null ? item.getGrnSpecification() : null,
							new Status(item.getStatusId()), item.getTaxAmount() != null ? item.getTaxAmount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage(), item.getTotalAmount(),
							item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTotalOtherAmount() != null ? item.getTotalOtherAmount() : null,
							item.getWarrantyDate(), item.getWarrentyPeriod(), item.getSupportTerms(),
							item.getTechnicalSpecification(), item.getExpectedPayment(),
							item.getServiceProviderId() != null ? new Vendor(item.getServiceProviderId()) : null,
							item.getManufactureYear(),
							item.getPendingQuantity() != null ? item.getPendingQuantity() : null));
					grnDetail.setItemId(item.getItemId());
					grnDetail.setLcop(item.getLcop());
					if (item.getGrnAccessoriesMapper() != null) {
						for (GrnAccessoriesMapper mapper : item.getGrnAccessoriesMapper()) {
							GrnAccessory grnAccessory = new GrnAccessory(new Item(mapper.getItemId()),
									mapper.getSerialNo(), mapper.getWarrentyFrom(), mapper.getWarrentyTo(),
									mapper.getQuantity(), mapper.getSpecification());
							grnAccessory.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
							
							grnDetail.addGrnAccessoryList(grnAccessory);
						}
					}
				}

				if (item.getPoDetailId() != null) {
					grnDetail.setPurchaseOrderDetail(new PurchaseOrderDetail(item.getPoDetailId()));
				}

				// sessionService.setEntityDefaults(grnDetail);
				//grnDetail.setUpdateDefaults(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
				grnDetail.setCreateDefaults(grnRequest.getOrganizationId()!=null ? grnRequest.getOrganizationId() : sessionService.getOrganizationId(),grnRequest.getUnitId() !=null ? grnRequest.getUnitId() :sessionService.getUnitId() ,grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
				
			}
		}

		// goodReceiptNote.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		// goodReceiptNote.setUpdatedBy(goodReceiptNote.getUser());
		goodReceiptNote.setUpdateDefaults(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId(), goodReceiptNote.getUnitId());
		GoodReceiptNote goodReceiptNoteResponse =grnDao.updateGoodReceiptNote(goodReceiptNote);
		Integer index=0;
		Integer index1=0;
		CommonResponse response = new CommonResponse();
		response.setObject(goodReceiptNote.getAssetType().getId());
		response.setListObject(goodReceiptNoteResponse.getGoodReceiptNoteDetailsList());
		response.setMessage("Good Receipt Note updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getGoodReceiptNote(Integer id, Integer unitId, Integer orgId) {
		GRNResponse grnResponseBP = grnDao.getGoodReceiptNoteBP(id);

		List<GRNDetailResponse> grnDetails = grnDao.getGoodReceiptNoteDetailsBP(id);

		List<Integer> consumableDetailIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());

		List<Integer> fixedDetaildId = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());

		List<Integer> consumabelItemIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getItemId)
				.distinct().collect(Collectors.toList());

		List<Integer> poDetailId = grnDetails.stream().map(GRNDetailResponse::getPoDetailId).distinct()
				.collect(Collectors.toList());

		// Below are for consumable only
		// alias not contains
		List<GRNUomMapper> grnUomBP = null;
		if (consumableDetailIds.size() > 0) {
			grnUomBP = grnDao.getGRNUomMapperBP(consumableDetailIds);
		}

		// alias not contains
		List<GRNUomMapper> bonusUomBP = null;
		if (consumableDetailIds.size() > 0) {
			bonusUomBP = grnDao.getGRNBonusUomMapperBP(consumableDetailIds);
		}
		List<UomResponse> uomResponses = null;
		if (consumabelItemIds.size() > 0) {
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds,orgId);
		}

		// Below is for fixed asset only
		List<GrnAccessoriesMapper> grnMapperListBP = new ArrayList<GrnAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty())
			grnMapperListBP = grnDao.getGrnAccessoryBP(fixedDetaildId);

		List<Integer> allDetailIds = new ArrayList<Integer>();
		allDetailIds.addAll(consumableDetailIds);
		allDetailIds.addAll(fixedDetaildId);

		// Below are commons.
		// alias contains
		List<PurchasedOrderResponse> poListBP = grnDetails.stream()
				.map(x -> new PurchasedOrderResponse(x.getPoDetailId(), x.getPoQuantity(), x.getPoNo(), x.getId(), x.getPoDate()))
				.collect(Collectors.toList());

		// alias not contains
		List<GRNOtherChargesMapper> otherChargesBP = grnDao.getGRNOtherChargesMapperBP1(id);
		// alias contains
		List<GRNStagedDto> grnStagedQunatityBP = grnDao.getGRNStagedQuantityBP(allDetailIds);
		// alias contains
		List<GRNStagedList> grnQuantityByDcDateBP = grnDao.getGRNQuantityByDcDateBP(poDetailId, id);

		consumabelItemIds.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(GRNDetailResponse::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(grnResponseBP.getStoreId(), consumabelItemIds,unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds,unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds,orgId);
		List<Quantities> leaseQuantities = itemsDao.getGrnLeaseQunatityBP(grnResponseBP.getStoreId(),
				consumabelItemIds);
		List<Quantities> approvedPRAndPOQuantities = itemsDao.getApprovedPRandPOQuantityList(grnResponseBP.getStoreId(),
				consumabelItemIds,unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(grnResponseBP.getStoreId(), consumabelItemIds,unitId);

		List<CreditNoteAmountList> creditNoteAmountlists = grnDao.getCreditNoteAmt(id);

		for (CreditNoteAmountList c : creditNoteAmountlists) {
			List<CreditNoteVendorWise> d = grnDao.getReturnableGatepassDetails(c.getMateinwardid());
			c.setRgDetails(d);
		}

		grnResponseBP.setCreditNoteAmountList(creditNoteAmountlists);
		grnResponseBP.setGrnOtherChargesMapper(otherChargesBP);

		for (GRNDetailResponse grnDetailResponse : grnDetails) {

			/*
			 * List <CreditNoteAmountList>
			 * creditNoteAmountlists=grnDao.getCreditNoteAmt(grnDetailResponse.getId());
			 * grnDetailResponse.setCreditNoteAmountList(creditNoteAmountlists);
			 */

			/*
			 * for(CreditNoteAmountList calist :grnDetailResponse.getCreditNoteAmountList())
			 * { //CreditNoteAmountList ca=new CreditNoteAmountList();
			 * ca.setConsumeNetAmount(calist.getConsumeNetAmount());
			 * ca.setCreditnetamount(calist.getCreditnetamount());
			 * ca.setCreditnoteamount(calist.getCreditnoteamount());
			 * ca.setCredittaxamount(calist.getCredittaxamount());
			 * ca.setIsChecked(calist.getIsChecked());
			 * ca.setIsSelectedCredit(calist.getIsSelectedCredit());
			 * ca.setMateinwarddetailsid(calist.getMateinwarddetailsid());
			 * ca.setMateinwardid(calist.getMateinwardid());
			 * ca.setReplacenumber(calist.getReplacenumber());
			 * ca.setRgdate(calist.getRgdate()); ca.setRgnumber(calist.getRgnumber());
			 * ca.setTaxamount(calist.getTaxamount());
			 * ca.setTaxpercentage(calist.getTaxpercentage());
			 * ca.setTotalamount(calist.getTotalamount()); CreditNoteAmountList
			 * ca=grnDao.getCreditNoteAmt(); creditNoteAmountlist.add(ca); }
			 */

			// creditNoteAmountlist.
			// grnDetailResponse.getCreditNoteAmountList().stream().filter(x ->
			// x.get.equals(grnDetailResponse.getCreditNoteAmountList())).collect(Collectors.toList());
			List<PurchasedOrderResponse> poList = poListBP.stream()
					.filter(x -> x.getId() != null && x.getId().equals(grnDetailResponse.getPoDetailId()))
					.collect(Collectors.toList());

			// List<GRNOtherChargesMapper> otherCharges = otherChargesBP.stream().filter(x
			// ->
			// x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList());

			List<GRNStagedDto> grnStagedQunatity = grnStagedQunatityBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList());

			// Collections.reverse(grnStagedQunatity);
			/*
			 * Integer grnRevQty=0;
			 * 
			 * for(GRNStagedDto g : grnStagedQunatity) {
			 * grnRevQty=grnRevQty+g.getReceivedQuantity(); }
			 * 
			 * 
			 * 
			 * 
			 * grnDetailResponse.setGrnReceivedQuantity(grnRevQty);
			 * 
			 * if(grnRevQty.equals(grnDetailResponse.getPoQuantity())) {
			 * grnDetailResponse.setPoQuantity(0); } else { // }
			 */

			List<GRNStagedList> grnQuantityByDcDate = new ArrayList<>();

			grnQuantityByDcDate.addAll(grnQuantityByDcDateBP.stream()
					.filter(x -> x.getPoDetailId().equals(grnDetailResponse.getPoDetailId()) && !x.getIsEditable())
					.collect(Collectors.toList()));

			Integer receivedQuantity = grnQuantityByDcDate.stream().mapToInt(GRNStagedList::getStagedReceivedQuantity)
					.sum();

			grnQuantityByDcDate.addAll(grnQuantityByDcDateBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()) && x.getIsEditable())
					.collect(Collectors.toList()));

			long storeStock = 0;
			long stockInTransit = 0;
			int maxStoreQuantity = 0;
			long leaseQty = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			List<Quantities> storeStock1 = storeStocks.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> stockInTransit1 = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> maxStoreQuantity1 = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> leaseQuantity = leaseQuantities.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());

			if (storeStock1 != null && !storeStock1.isEmpty())
				storeStock = storeStock1.get(0).getQuantity();
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.get(0).getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.get(0).getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.get(0).getQuantity();
			if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList())
						.get(0).getQuantity();
			}
			if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPOQuantity = approvedPOQuantities.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList())
						.get(0).getQuantity();
			}
			long maxOrderQuantity = maxStoreQuantity
					- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);

			if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
				Integer leaseQuantity1 = null;

				grnDetailResponse
						.setGrnUom(grnUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				grnDetailResponse.setBonusUom(
						bonusUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				// grnDetailResponse.setOtherCharges(otherCharges);
				grnDetailResponse.setGrnStagedQuantity(grnStagedQunatity);
				grnDetailResponse.setPoList(poList);
				grnDetailResponse.setPoGrnQuantity(receivedQuantity);
				grnDetailResponse.setGrnStagedLists(grnQuantityByDcDate);
				grnDetailResponse.setMaxStoreQuantity(maxStoreQuantity);
				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);
				grnDetailResponse.setUom(uomResponses.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList()));
				grnDetailResponse.setLeaseQuantity(leaseQuantity1);

				if (grnDetailResponse.getPoPendingQuantity() != null) {
					grnDetailResponse.setPoPendingQuantity(grnDetailResponse.getPoPendingQuantity() - receivedQuantity);
				} else {
					grnDetailResponse.setPoPendingQuantity(0);
				}
				List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(grnDetailResponse.getId());
				List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
				for (GrnBonusBatchExpiry getList1 : getList) {
					GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
					getList2.setBatchNo(getList1.getBatchNo());
					getList2.setBonusQuantity(getList1.getBonusQuantity());
					getList2.setExpiryDate(getList1.getExpiryDate());
					getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
					getList3.add(getList2);
				}
				grnDetailResponse.setGrnBonusBatchExpiry(getList3);
				grnResponseBP.addItems(grnDetailResponse);
			} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				List<GrnAccessoriesMapper> grnMapperList = grnMapperListBP.stream()
						.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList());

				// grnDetailResponse.setOtherCharges(otherCharges);
				grnDetailResponse.setGrnStagedQuantity(grnStagedQunatity);
				grnDetailResponse.setPoList(poList);
				grnDetailResponse.setGrnStagedLists(grnQuantityByDcDate);
				grnDetailResponse.setMaxStoreQuantity(maxStoreQuantity);
				/*
				 * GoodReceiptNoteDetail
				 * grnd=grnDao.getGoodReceiptNoteDetailById(grnDetailResponse.getId());
				 * if(grnd!=null) {
				 * grnDetailResponse.setServiceProviderId(grnd.getVendor().getId());
				 * grnDetailResponse.setServiceProvider(grnd.getVendor().getName()); }
				 */

				if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPRAndPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).get(0).getQuantity();
				}
				if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPOQuantity = approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).get(0).getQuantity();
				}
				maxOrderQuantity = maxStoreQuantity
						- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);

				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);
				grnDetailResponse.setGrnAccessoriesMapper(
						grnMapperListBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(grnDetailResponse.getId());
				List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
				for (GrnBonusBatchExpiry getList1 : getList) {
					GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
					getList2.setBatchNo(getList1.getBatchNo());
					getList2.setBonusQuantity(getList1.getBonusQuantity());
					getList2.setExpiryDate(getList1.getExpiryDate());
					getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
					getList3.add(getList2);
				}
				grnDetailResponse.setGrnBonusBatchExpiry(getList3);
				grnResponseBP.addItems(grnDetailResponse);
			}
		}

		grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		grnResponseBP.setTaxList(GlobalDao.getTaxListBP(orgId));
		return grnResponseBP;

		// ORIGINAL CODE
		// Need to write new method for this one
		/*
		 * GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(id); GRNResponse
		 * grnResponse = new GRNResponse(goodReceiptNote.getId(),
		 * goodReceiptNote.getAssetType().getId(),
		 * goodReceiptNote.getAssetType().getType(),
		 * goodReceiptNote.getRequestType().getId(),
		 * goodReceiptNote.getRequestType().getType(), goodReceiptNote.getGrnNumber(),
		 * goodReceiptNote.getAddedDate(), goodReceiptNote.getDiscountType() != null ?
		 * goodReceiptNote.getDiscountType().getId() : null,
		 * goodReceiptNote.getDiscountType() != null ?
		 * goodReceiptNote.getDiscountType().getType() : null,
		 * goodReceiptNote.getStatus().getId(), goodReceiptNote.getStatus().getStatus(),
		 * goodReceiptNote.getApprovalStatus().getId(),
		 * goodReceiptNote.getApprovalStatus().getStatus(),
		 * goodReceiptNote.getStore().getStoreId(),
		 * goodReceiptNote.getStore().getStoreName(), goodReceiptNote.getDcDate(),
		 * goodReceiptNote.getDcNumber(), goodReceiptNote.getDiscount(),
		 * goodReceiptNote.getInvoiceDate(), goodReceiptNote.getInvoiceNumber(),
		 * goodReceiptNote.getInwardDate(), goodReceiptNote.getInwardNumber(),
		 * goodReceiptNote.getIsConsignment(), goodReceiptNote.getNetAmount(),
		 * goodReceiptNote.getRoundOff(), goodReceiptNote.getTotalAmount(),
		 * goodReceiptNote.getTotalDiscount(), goodReceiptNote.getTotalOtherAmount(),
		 * goodReceiptNote.getTotalTaxAmmount(), goodReceiptNote.getVendor().getId(),
		 * goodReceiptNote.getVendor().getName(), goodReceiptNote.getRemark(),
		 * goodReceiptNote.getAddedBy().getUserName());
		 * 
		 * for (GoodReceiptNoteDetail detail :
		 * goodReceiptNote.getGoodReceiptNoteDetailsList()) { //alias contains
		 * List<PurchasedOrderResponse> poList =
		 * grnDao.getPurchaseOrderId(detail.getId()); //alias not contains
		 * List<GRNOtherChargesMapper> otherCharges =
		 * getGRNOtherChargesList(detail.getId()); //alias contains List<GRNStagedDto>
		 * grnStagedQunatity = grnDao.getGRNStagedQuantity(detail.getId());
		 * 
		 * //List<GRNStagedDto> grnStagedQunatityMainTable =
		 * grnDao.getGRNStagedQuantity(detail.getId()); //alias contains
		 * List<GRNStagedList> grnQuantityByDcDate =
		 * grnDao.getGRNQuantityByDcDate(detail.getId());
		 * 
		 * Long storeStock =
		 * GlobalDao.getStoreStockByItem(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); //System.out.println("storeStock:-"
		 * + storeStock); Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(
		 * detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId()); Long lastMonthSale =
		 * GlobalDao.getLastMonthSale(detail.getGoodReceiptNote().getStore().getStoreId(
		 * ), detail.getItem().getId()); Long currentMonthSale =
		 * GlobalDao.getCurrentMonthSale(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(
		 * detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId()); Long maxOrderQuantity = maxStoreQuantity -
		 * (storeStock + stockInTransit + itemsDao.getGrnLeaseQunatity(
		 * detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId())); //System.out.println("leaseQty:-" +
		 * itemsDao.getGrnLeaseQunatity(
		 * detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId()));
		 * 
		 * // Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); //Fixed Commented Code Is Only Valid
		 * For Direct GRN save And details match Pending Qty //Long maxOrderQuantity =
		 * maxStoreQuantity - (storeStock + stockInTransit); //Long maxOrderQuantity =
		 * maxStoreQuantity - (storeStock + stockInTransit +
		 * detail.getGrnReceivedQuantity() + // (detail.getBonusReceivedQuantity() !=
		 * null ? detail.getBonusReceivedQuantity() : 0));
		 * 
		 * if
		 * (goodReceiptNote.getAssetType().getId().equals(Constants.getConsumableId()))
		 * { //alias not contains List<GRNUomMapper> grnUom =
		 * getGRNUomList(detail.getId()); //alias not contains List<GRNUomMapper>
		 * bonusUom = getBonusUomList(detail.getId()); Integer leaseQuantity = null;
		 * 
		 * // List<UomResponse> uomResponse = //
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId()); //
		 * List<PurchaseOrderUomMapper> prUomResponse = new //
		 * ArrayList<PurchaseOrderUomMapper>(); for(UomResponse uom : uomResponse) { //
		 * PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new //
		 * UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()), //
		 * uom.getConversion(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom()); //
		 * prUomResponse.add(prUom); } Integer maxQuantity = //
		 * Util.GetLeaseQuantity(maxStoreQuantity, prUomResponse, //
		 * detail.getGrnUomType()); Long pendingQuantity = (long) //
		 * Util.GetLeaseQuantity(maxOrderQuantity.intValue(),prUomResponse,detail. //
		 * getGrnUomType()); Integer leaseQuantity = //
		 * Util.GetLeaseQuantity(detail.getGrnApprovedQuantity(), //
		 * detail.getGrnUomMappersList(), detail.getGrnUomType()) + //
		 * Util.GetLeaseQuantity(detail.getBonusApprovedQuantity(), //
		 * detail.getGrnBonusUomMappersList(), detail.getBonusUomType());
		 * 
		 * grnResponse.addItems( new GRNDetailResponse(detail.getId(),
		 * detail.getItem().getId(), detail.getGrnApprovedQuantity(),
		 * detail.getBonusApprovedQuantity() != null ? detail.getBonusApprovedQuantity()
		 * : null, detail.getPurchaseOrderDetail() != null ?
		 * detail.getPurchaseOrderDetail() .getPoApprovedQuantity() : null,
		 * detail.getPurchaseOrderDetail() != null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() != null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() : null : null,
		 * leaseQuantity, maxStoreQuantity, maxOrderQuantity,
		 * detail.getGrnReceivedQuantity(),
		 * 
		 * detail.getBonusReceivedQuantity() != null ? detail.getBonusReceivedQuantity()
		 * : null,
		 * 
		 * detail.getBatchNo(), detail.getGrnUomType().getId(),
		 * detail.getGrnUomUnit().getId(), detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getId() : null, detail.getBonusUomUnit() != null ?
		 * detail.getBonusUomUnit().getId() : null,
		 * 
		 * detail.getCop(), detail.getMrp(), detail.getDiscount(),
		 * detail.getExpiryDate(), detail.getLotNumber(),
		 * detail.getManufacturer().getId(), detail.getTax().getId(),
		 * detail.getTaxPercentage(), detail.getTotalAmount(), detail.getTaxAmount(),
		 * detail.getNetAmount(), detail.getTotalDiscount(),
		 * detail.getTotalOtherAmount(), detail.getStatus().getId(),
		 * detail.getSpecification(), detail.getPurchaseOrderDetail() != null ?
		 * detail.getPurchaseOrderDetail().getSpecification() : null,
		 * detail.getRemark(), detail.getRejectedQuantity(), grnUom, bonusUom,
		 * otherCharges, grnStagedQunatity, detail.getItem().getName(),
		 * detail.getItem().getCode(), detail.getGrnUomType().getType(),
		 * detail.getGrnUomUnit().getUnits(), detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getType() : null, detail.getBonusUomUnit() != null ?
		 * detail.getBonusUomUnit().getUnits() : null,
		 * 
		 * detail.getManufacturer().getName(), detail.getTax().getCode(),
		 * detail.getStatus().getStatus(),
		 * 
		 * //PT method contains
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId()), poList,
		 * detail.getGrnReceivedQuantity(), grnQuantityByDcDate)); } else if
		 * (goodReceiptNote.getAssetType().getId().equals(Constants.getFixedAssetId()))
		 * { List<GrnAccessoriesMapper> grnMapperList =
		 * getGrnAccessoriesMapper(detail.getId()); grnResponse.addItems( new
		 * GRNDetailResponse(detail.getId(), detail.getItem().getId(),
		 * detail.getGrnApprovedQuantity(), detail.getBonusApprovedQuantity() != null ?
		 * detail.getBonusApprovedQuantity() : null, detail.getPurchaseOrderDetail() !=
		 * null ? detail.getPurchaseOrderDetail() .getPoApprovedQuantity() : null,
		 * detail.getPurchaseOrderDetail() != null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() != null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() : null : null,
		 * maxStoreQuantity, maxOrderQuantity, detail.getGrnReceivedQuantity(),
		 * 
		 * detail.getBonusReceivedQuantity() != null ? detail.getBonusReceivedQuantity()
		 * : null,
		 * 
		 * detail.getBatchNo(), detail.getCop(), detail.getMrp(), detail.getDiscount(),
		 * detail.getExpiryDate(), detail.getLotNumber(),
		 * detail.getManufacturer().getId(), detail.getTax().getId(),
		 * detail.getTaxPercentage(), detail.getTotalAmount(), detail.getTaxAmount(),
		 * detail.getNetAmount(), detail.getTotalDiscount(),
		 * detail.getTotalOtherAmount(), detail.getStatus().getId(),
		 * detail.getSpecification(), detail.getPurchaseOrderDetail() != null ?
		 * detail.getPurchaseOrderDetail().getSpecification() : null,
		 * detail.getRemark(), detail.getRejectedQuantity(), detail.getWarrentyPeriod(),
		 * detail.getSupportTerm(), detail.getTechnicalSpec(),
		 * detail.getExpectedPayment(), detail.getVendor() != null ?
		 * detail.getVendor().getId() : null, detail.getVendor() != null ?
		 * detail.getVendor().getName() : null, detail.getWarrentyDate(), otherCharges,
		 * grnStagedQunatity, detail.getItem().getName(), detail.getItem().getCode(),
		 * detail.getManufacturer().getName(), detail.getTax().getCode(),
		 * detail.getStatus().getStatus(), poList, grnMapperList, grnQuantityByDcDate));
		 * } }
		 * 
		 * //PT method already contains
		 * grnResponse.setOtherChargesList(getOtherCharges()); //if
		 * (goodReceiptNote.getAssetType().getId().equals(Constants.getFixedAssetId()))
		 * {
		 * 
		 * //PT method already contains grnResponse.setTaxList(getTaxList()); //} return
		 * grnResponse;
		 */
	}

	private List<TaxDropDown> getTaxList(Integer orgId) {
		List<TaxDropDown> taxList = new ArrayList<TaxDropDown>();
		for (Tax tax : GlobalDao.getTaxList(orgId)) {

			TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(), tax.getTaxPercentage());
			taxList.add(taxDto);
		}
		return taxList;
	}

	private List<GrnAccessoriesMapper> getGrnAccessoriesMapper(Integer grnDetailId) {

		List<GrnAccessoriesMapper> response = new ArrayList<GrnAccessoriesMapper>();
		for (GrnAccessory mapper : grnDao.getGrnAccessory(grnDetailId)) {
			GrnAccessoriesMapper mapperDto = new GrnAccessoriesMapper(mapper.getId(), mapper.getItem().getId(),
					mapper.getItem().getName(), mapper.getItem().getCode(), mapper.getSerialNo(),
					mapper.getWarrentyFrom(), mapper.getWarrentyTo(), mapper.getQuantity(), mapper.getSpecification());
			response.add(mapperDto);
		}
		return response;
	}

	/*
	 * @Transactional private String getPoSpecification(GoodReceiptNoteDetail
	 * detail) { String poSpecification = null; List<PurchaseOrderDetail>
	 * purchaseOrderDetailList = detail.getPurchaseOrderDetailList();
	 * for(PurchaseOrderDetail poDetail : purchaseOrderDetailList) {
	 * if(detail.getItem().getId().equals(poDetail.getItem().getId())) {
	 * poSpecification = poDetail.getSpecification(); } } return poSpecification;
	 *
	 * }
	 */

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
	private List<GRNUomMapper> getGRNUomList(Integer grnDetailId) {
		List<GRNUomMapper> response = new ArrayList<GRNUomMapper>();
		for (GrnUomMapper grnUomMapper : grnDao.getGRNUomMapper(grnDetailId)) {
			response.add(new GRNUomMapper(grnUomMapper.getId(), grnUomMapper.getUomType().getId(),
					grnUomMapper.getUomType().getType(), grnUomMapper.getUomUnit().getId(),
					grnUomMapper.getUomUnit().getUnits(), grnUomMapper.getConversions(), grnUomMapper.getIpUom(),
					grnUomMapper.getOpUom(), grnUomMapper.getStoreUom()));
		}
		return response;
	}

	@Transactional
	private List<GRNUomMapper> getBonusUomList(Integer grnDetailId) {
		List<GRNUomMapper> response = new ArrayList<GRNUomMapper>();
		for (GrnBonusUomMapper bonusUomMapper : grnDao.getGRNBonusUomMapper(grnDetailId)) {
			response.add(new GRNUomMapper(bonusUomMapper.getId(), bonusUomMapper.getUomType().getId(),
					bonusUomMapper.getUomType().getType(), bonusUomMapper.getUomUnit().getId(),
					bonusUomMapper.getUomUnit().getUnits(), bonusUomMapper.getConversions(), bonusUomMapper.getIpUom(),
					bonusUomMapper.getOpUom(), bonusUomMapper.getStoreUom()));

		}
		return response;
	}

	@Transactional
	private List<GRNOtherChargesMapper> getGRNOtherChargesList(Integer grnDetailId) {
		List<GRNOtherChargesMapper> grnOtherChargesList = new ArrayList<GRNOtherChargesMapper>();

		for (GrnOtherChargesMapper grnOtherChargesMapper : grnDao.getGRNOtherChargesMapper(grnDetailId)) {
			grnOtherChargesList.add(new GRNOtherChargesMapper(grnOtherChargesMapper.getId(),
					grnOtherChargesMapper.getOtherCharge().getId(),
					grnOtherChargesMapper.getOtherCharge().getDescription(), grnOtherChargesMapper.getAmount()));
		}
		return grnOtherChargesList;
	}

	@SuppressWarnings({ "rawtypes", "rawtypes" })
	@Override
	@Transactional
	public Object approveGoodReceiptNote(GRNStatusDto grnStatusDto) {

		for (Integer id : grnStatusDto.getGrnIdList()) {
			GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(id);

			goodReceiptNote.setStatus(new Status(grnStatusDto.getStatusId()));
			goodReceiptNote.setApprovalStatus(new Status(grnStatusDto.getApprovalStatusId()));

			for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
				detail.setStatus(new Status(grnStatusDto.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
				detail.setUpdatedByUser(grnStatusDto.getUserId());
				
				List<TaxComponentDetailsDto> taxDetailsList = grnDao.getTaxDetails(detail.getTax().getId());
				
				Double commTaxAmount = detail.getTotalAmount() - (detail.getTotalDiscount()!=null ? detail.getTotalDiscount() :0);
				
				//Double natAmount = detail.getTotalAmount() - (detail.getDiscount()!=null ? detail.getDiscount() :0);HIS-22584 Orthocare Hospital :- Tally integration for Orthocare Hospital.
				
				if(taxDetailsList != null && taxDetailsList.size()>0) {
					
					for (TaxComponentDetailsDto taxDetail : taxDetailsList) {
						
						try {
							
							Double taxComponentAmount = (commTaxAmount) * (taxDetail.getTaxComponentPercentage()/100);		
							
						//	Double taxComponentAmount = (natAmount) * (taxDetail.getTaxComponentPercentage()/100);HIS-22584 Orthocare Hospital :- Tally integration for Orthocare Hospital.
							
							TaxComponentDetailsDto dto  = new TaxComponentDetailsDto();
							dto.setCreatedBy(grnStatusDto.getUserId());
							dto.setGrnDetailsId(detail.getId());
							dto.setGrnId(id);
							dto.setItemId(detail.getItem().getId());
							dto.setOrganizationId(goodReceiptNote.getOrganizationId());
							dto.setStatus('A');
							dto.setTaxAmount(detail.getTaxAmount());
							dto.setTaxComponentAmount(taxComponentAmount);
							dto.setTaxComponentId(taxDetail.getTaxComponentId());
							dto.setTaxComponentName(taxDetail.getTaxComponentName());
							dto.setTaxComponentPercentage(taxDetail.getTaxComponentPercentage());
							dto.setTaxId(detail.getTax().getId());
							dto.setTaxPercentage(detail.getTax().getTaxPercentage());
							dto.setUnitId(goodReceiptNote.getUnitId());
							dto.setUpdatedBy(grnStatusDto.getUserId());
							iTaxComponentDetailsDao.saveTaxComponentDetails(dto);
							
						} catch (Exception e) {
							logger.error("approveGoodReceiptNote failed", e);
							throw new ServiceException(e.getMessage());
						}
						
					}
					
				}
			}
	
			goodReceiptNote.setApprovedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
			goodReceiptNote.setApprovedBy(new User(grnStatusDto.getUserId()));

			goodReceiptNote.setUpdatedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
			goodReceiptNote.setUpdatedByUser(grnStatusDto.getUserId());

			grnDao.updateGoodReceiptNote(goodReceiptNote);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Good Receipt Note Submitted successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelGoodReceiptNote(GRNRequest grnRequest) {

		GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(grnRequest.getId());

		for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
			detail.setStatus(new Status(grnRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
			detail.setUpdatedByUser(grnRequest.getUserId());

			if (detail.getGoodReceiptNote().getRequestType()
					.getId() == com.param.common.constants.RequestType.AgainstPurchaseOrder.getRequestType()) {
				detail.setGrnApprovedQuantity(0);
				detail.setGrnReceivedQuantity(0);

			}

		}

		goodReceiptNote.setCancelReason(new CancelReason(grnRequest.getCancelReasonId()));
		goodReceiptNote.setCancelNote(grnRequest.getCancelNote());

		goodReceiptNote.setStatus(new Status(grnRequest.getStatusId()));
		goodReceiptNote.setApprovalStatus(new Status(grnRequest.getApprovalStatusId()));

		goodReceiptNote.setUpdatedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
		goodReceiptNote.setUpdatedByUser(grnRequest.getUserId());

		grnDao.updateGoodReceiptNote(goodReceiptNote);

		CommonResponse response = new CommonResponse();
		response.setMessage("Good Receipt Note cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object previousGrnDetails(Integer vendorId, Integer itemid, Integer grnId) {

		List<PreviousGRNSearchResponse> grnSearchListBP = grnDao.previousGrnDetails(vendorId, itemid, grnId);

		return grnSearchListBP;

	}

	@Override
	@Transactional
	public Object settledGoodReceiptNote(GrnSettledRequest grnSettledRequest) throws CustomException {

		CommonResponse response = new CommonResponse();

		List<Integer> grnIds = grnSettledRequest.getPaymentReceiptDetailsDtosList().stream()
				.map(grn -> grn.getGrnId()).collect(Collectors.toList());
		// check, is already settled!
		GRNSearchResponse1 getNetAndPaidAmt = grnDao.getNetAndPaidAmt(grnIds);

		if(getNetAndPaidAmt.getPaidAmount() != null) {
			if (getNetAndPaidAmt.getPaidAmount() + grnSettledRequest.getAmountData() > getNetAndPaidAmt.getNetAmount()) {
				response.setMessage("Already settled");
				return response;
			}
		}

		BigInteger lastId = grnDao.getLastId();
		GrnSettlement grnsettle = new GrnSettlement();
		grnsettle.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
		// GrnSettlement grnsettle = new
		// GrnSettlement(lastId.add(BigInteger.ONE).toString());
		Boolean saveFlag = false;
		Double creditTotalAmt=0d;
		if(grnSettledRequest.getTotalCreditNoteAmtUse() !=null && grnSettledRequest.getTotalCreditNoteAmtUse() > 0)
		{
			creditTotalAmt= grnSettledRequest.getTotalCreditNoteAmtUse();
			grnSettledRequest.setAmountData(grnSettledRequest.getAmountData() + grnSettledRequest.getTotalCreditNoteAmtUse());
			
		}
		grnsettle.setAmountData(grnSettledRequest.getAmountData());
		grnsettle.setAmountReceivedFrom(grnSettledRequest.getAmountReceivedFrom());
		grnsettle.setIdentificaitonNumber(grnSettledRequest.getIdentificaitonNumber());
		grnsettle.setReceiptNumber(lastId.add(BigInteger.ONE).toString());
		
		Double tempAmount = grnSettledRequest.getAmountData();
//		Double totAmt = grnSettledRequest.getAmountData();
		Double creditNoteConsumedAmt = 0.0;

		// Integer grnSettlementId = grnDao.saveGrnSettlement(grnsettle);
		// if (grnSettlementId > 0) {
		
		
		for (PaymentReceiptDetailsDto1 paymentReceiptDetailsDto1 : grnSettledRequest
				.getPaymentReceiptDetailsDtosList()) {

			Double totAmt = paymentReceiptDetailsDto1.getAmount();
			/*
			 * GrnNetAmount grnNetAmt =
			 * grnDao.getGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(),
			 * paymentReceiptDetailsDto1.getVendorId(),
			 * grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId());
			 */
			GrnNetAmount grnNetAmt = getGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(),
					paymentReceiptDetailsDto1.getVendorId(), grnSettledRequest.getOrgnizationId(),
					grnSettledRequest.getUnitId());
			Double newNetAmount = 0d;
			Double checkNewNetAmount = 0d;
			newNetAmount=grnDao.getNetAmountSumByGrnIdFromGrnDetails(paymentReceiptDetailsDto1.getGrnId(),grnSettledRequest.getUnitId(),grnSettledRequest.getOrgnizationId());
			double Output= new BigDecimal(newNetAmount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
			newNetAmount=Output;
			Double newRoundOffAmount=grnDao.geRoundOffAmountSumByGrnIdFromGrnDetails(paymentReceiptDetailsDto1.getGrnId(),grnSettledRequest.getUnitId(),grnSettledRequest.getOrgnizationId());
			Double creditNoteAmount=grnDao.getCreditNoteAmount(paymentReceiptDetailsDto1.getGrnId(),grnSettledRequest.getUnitId());
			// if (paymentReceiptDetailsDto1.getAmount() >= grnNetAmt.getNetamount()) {
			// if (grnSettledRequest.getAmountData() >= grnNetAmt.getNetamount()) {
			checkNewNetAmount = newNetAmount;
			checkNewNetAmount = (checkNewNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-(creditNoteAmount!=null ? creditNoteAmount:0.00);
			newNetAmount = (newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-(creditNoteAmount!=null ? creditNoteAmount:0.00);
			int amt = (int) checkNewNetAmount.doubleValue();
			int checkNew = (int) newNetAmount.doubleValue();
			if(amt != checkNew){
				grnNetAmt.setNetamount(newNetAmount);
//				grnNetAmt.setNetamount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount : 0.00))-(creditNoteAmount!=null ? creditNoteAmount:0.00));
			}
//			grnNetAmt.setNetamount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount : 0.00))-(creditNoteAmount!=null ? creditNoteAmount:0.00));
			Double paidAmount=grnDao.getPaidAmount(paymentReceiptDetailsDto1.getGrnId(),
					grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId());
			totAmt=totAmt+(paidAmount!=null ? paidAmount:0);
			if (totAmt >= grnNetAmt.getNetamount()) {
				// settled
				// minus from net
				if (paymentReceiptDetailsDto1.getAmount() > grnNetAmt.getNetamount()) {
					tempAmount=tempAmount-grnNetAmt.getNetamount();
					paymentReceiptDetailsDto1.setAmount(grnNetAmt.getNetamount());
				//	paymentReceiptDetailsDto1.setAmount(grnNetAmt.getNetamount());
					//throw new CustomException("Settle GRN Net amount is greater than GRN net amount ");
				}
				if (paymentReceiptDetailsDto1.getPaymentModeId() == 1) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 2) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setChequeNumber(paymentReceiptDetailsDto1.getChequeNumber());
					grnSettlementDetail.setChequeDate(paymentReceiptDetailsDto1.getChequeDate());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 8) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setTransactionTypeId(paymentReceiptDetailsDto1.getTransactionTypeId());
					grnSettlementDetail.setTransTransactionDate(paymentReceiptDetailsDto1.getTransTransactionDate());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setTransactionNo(paymentReceiptDetailsDto1.getTransactionNo());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 9) {

					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				}
				
				// Integer grnSettlementId = grnDao.saveGrnSettlement(grnsettle);
				//Integer grnSettlementId = saveGrnSettlement(grnsettle);
			//	if (grnSettlementId > 0) {
					// minus from net amount
					/*
					 * grnDao.updateGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(),
					 * paymentReceiptDetailsDto1.getVendorId(),
					 * grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
					 * paymentReceiptDetailsDto1.getAmount());
					 */
					if(creditNoteAmount!=null && creditNoteAmount>0)
					{
						paymentReceiptDetailsDto1.setAmount(paymentReceiptDetailsDto1.getAmount()+creditNoteAmount);
					}
					updateGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(), paymentReceiptDetailsDto1.getVendorId(),
							grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
							paymentReceiptDetailsDto1.getAmount());
					response.setMessage("Successfully");
					paymentReceiptDetailsDto1.setAmount(tempAmount);
				//}
					grnDao.updateSettlementReceiptDetailsStatusByGrnId(paymentReceiptDetailsDto1.getGrnId());

			} else if (totAmt < grnNetAmt.getNetamount()) {
				// partially settled
				// minus from net
				if (paymentReceiptDetailsDto1.getAmount() > grnNetAmt.getNetamount()) {
					tempAmount=tempAmount-grnNetAmt.getNetamount();
					paymentReceiptDetailsDto1.setAmount(grnNetAmt.getNetamount());
					//throw new CustomException("Settle GRN Net amount is greater than GRN net amount ");
				}
				if (paymentReceiptDetailsDto1.getPaymentModeId() == 1) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 2) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setChequeNumber(paymentReceiptDetailsDto1.getChequeNumber());
					grnSettlementDetail.setChequeDate(paymentReceiptDetailsDto1.getChequeDate());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 8) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setTransactionTypeId(paymentReceiptDetailsDto1.getTransactionTypeId());
					grnSettlementDetail.setTransTransactionDate(paymentReceiptDetailsDto1.getTransTransactionDate());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setTransactionNo(paymentReceiptDetailsDto1.getTransactionNo());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 9) {

					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				}
				
				//Integer grnSettlementId = saveGrnSettlement(grnsettle);
				//if (grnSettlementId > 0) {
					// minus from net amount
					/*
					 * grnDao.updatePartiallyGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(),
					 * paymentReceiptDetailsDto1.getVendorId(),
					 * grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
					 * paymentReceiptDetailsDto1.getAmount());
					 */
				if(creditNoteAmount!=null && creditNoteAmount>0)
				{
					paymentReceiptDetailsDto1.setAmount(paymentReceiptDetailsDto1.getAmount()+creditNoteAmount);
				}
					updateGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(), paymentReceiptDetailsDto1.getVendorId(),
							grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
							paymentReceiptDetailsDto1.getAmount());
					paymentReceiptDetailsDto1.setAmount(tempAmount);
				//}

			}

		}
		Integer grnSettlementId = saveGrnSettlement(grnsettle);
		for (CreditNoteAmountList cn : grnSettledRequest.getCreditNoteAmountList()) {
			cn.getAvailablenetamount();
			Double balanceAmt=0d;
			if(cn.getAvailablenetamount() !=null && cn.getAvailablenetamount() > 0)
			{
				balanceAmt = balanceAmt+cn.getAvailablenetamount();
			}
			if(cn.getBalance()!=null && cn.getBalance() > 0)
			{
				balanceAmt = balanceAmt-cn.getBalance();
			}
			cn.setBalance(balanceAmt);
			grnDao.updateCreditNoteAmt(cn.getMateinwardid(), cn.getBalance());
		}
		response.setMessage("Successfully");
		return response;

	}

	@Override
	@Transactional
	public Object previousNewGrnDetails(Integer vendorId, Integer itemid) {

		List<PreviousGRNSearchResponse> grnSearchListBP = grnDao.previousNewGrnDetails(vendorId, itemid);

		return grnSearchListBP;

	}

	@Override
	@Transactional
	public Integer saveGrnSettlement(GrnSettlement grnsettle) {
		return grnDao.saveGrnSettlement(grnsettle);
	}

	@Override
	@Transactional
	public void updateGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId, Double amount) {
		grnDao.updateGrnNetAmt(grnId, vendorId, orgnizationId, unitId, amount);

	}

	@Override
	@Transactional
	public GrnNetAmount getGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId) {
		return grnDao.getGrnNetAmt(grnId, vendorId, orgnizationId, unitId);
	}

	@Override
	public Object settledGoodReceiptNoteSave(GrnSettledRequest grnSettledRequest) throws CustomException {

		CommonResponse response = new CommonResponse();
		BigInteger lastId = grnDao.getLastId();
		GrnSettlement grnsettle = new GrnSettlement();
		// GrnSettlement grnsettle = new
		// GrnSettlement(lastId.add(BigInteger.ONE).toString());
		Boolean saveFlag = false;
		grnsettle.setAmountData(grnSettledRequest.getAmountData());
		grnsettle.setAmountReceivedFrom(grnSettledRequest.getAmountReceivedFrom());
		grnsettle.setIdentificaitonNumber(grnSettledRequest.getIdentificaitonNumber());
		grnsettle.setReceiptNumber(lastId.add(BigInteger.ONE).toString());
		grnsettle.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
		
		Double tempAmount = grnSettledRequest.getAmountData();
		Double totAmt = grnSettledRequest.getAmountData();
		Double creditNoteConsumedAmt = 0.0;

		for (PaymentReceiptDetailsDto1 paymentReceiptDetailsDto1 : grnSettledRequest
				.getPaymentReceiptDetailsDtosList()) {

			GrnNetAmount grnNetAmt = getGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(),
					paymentReceiptDetailsDto1.getVendorId(), grnSettledRequest.getOrgnizationId(),
					grnSettledRequest.getUnitId());
		
			if (totAmt >= grnNetAmt.getNetamount()) {
				// settled
				// minus from net
				if (paymentReceiptDetailsDto1.getAmount() > grnNetAmt.getNetamount()) {
					throw new CustomException("Settle GRN Net amount is greater than GRN net amount ");
				}
				if (paymentReceiptDetailsDto1.getPaymentModeId() == 1) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 2) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setChequeNumber(paymentReceiptDetailsDto1.getChequeNumber());
					grnSettlementDetail.setChequeDate(paymentReceiptDetailsDto1.getChequeDate());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 8) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setTransactionTypeId(paymentReceiptDetailsDto1.getTransactionTypeId());
					grnSettlementDetail.setTransTransactionDate(paymentReceiptDetailsDto1.getTransTransactionDate());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setTransactionNo(paymentReceiptDetailsDto1.getTransactionNo());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 9) {

					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				}
				// Integer grnSettlementId = grnDao.saveGrnSettlement(grnsettle);
				Integer grnSettlementId = saveGrnSettlement(grnsettle);
				if (grnSettlementId > 0) {
					updateGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(), paymentReceiptDetailsDto1.getVendorId(),
							grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
							paymentReceiptDetailsDto1.getAmount());
					response.setMessage("Successfully");
				}

			} else if (totAmt < grnNetAmt.getNetamount()) {
				// partially settled
				// minus from net
				if (paymentReceiptDetailsDto1.getAmount() > grnNetAmt.getNetamount()) {
					throw new CustomException("Settle GRN Net amount is greater than GRN net amount ");
				}
				if (paymentReceiptDetailsDto1.getPaymentModeId() == 1) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 2) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setChequeNumber(paymentReceiptDetailsDto1.getChequeNumber());
					grnSettlementDetail.setChequeDate(paymentReceiptDetailsDto1.getChequeDate());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 8) {
					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setTransactionTypeId(paymentReceiptDetailsDto1.getTransactionTypeId());
					grnSettlementDetail.setTransTransactionDate(paymentReceiptDetailsDto1.getTransTransactionDate());
					grnSettlementDetail.setBankId(paymentReceiptDetailsDto1.getBankId());
					grnSettlementDetail.setTransactionNo(paymentReceiptDetailsDto1.getTransactionNo());
					grnSettlementDetail.setStatusId(19);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();
				} else if (paymentReceiptDetailsDto1.getPaymentModeId() == 9) {

					GrnSettlementDetail grnSettlementDetail = new GrnSettlementDetail();
					grnSettlementDetail.setPaymentModeId(paymentReceiptDetailsDto1.getPaymentModeId());
					grnSettlementDetail.setAmount(paymentReceiptDetailsDto1.getAmount());
					grnSettlementDetail.setStatusId(20);
					grnSettlementDetail.setGrnId(paymentReceiptDetailsDto1.getGrnId());
					grnSettlementDetail.setCreateDefaults(grnSettledRequest.getOrgnizationId()!=null ? grnSettledRequest.getOrgnizationId() : sessionService.getOrganizationId(),grnSettledRequest.getUnitId() !=null ? grnSettledRequest.getUnitId() :sessionService.getUnitId() ,grnSettledRequest.getUserId()!=null ? grnSettledRequest.getUserId() : sessionService.getUserId());
					
					grnsettle.addGrnSettlementDetailList(grnSettlementDetail);
					totAmt = totAmt - paymentReceiptDetailsDto1.getAmount();

				}

				Integer grnSettlementId = saveGrnSettlement(grnsettle);
				if (grnSettlementId > 0) {
					
					updateGrnNetAmt(paymentReceiptDetailsDto1.getGrnId(), paymentReceiptDetailsDto1.getVendorId(),
							grnSettledRequest.getOrgnizationId(), grnSettledRequest.getUnitId(),
							paymentReceiptDetailsDto1.getAmount());

				}

			}

		}
		for (CreditNoteAmountList cn : grnSettledRequest.getCreditNoteAmountList()) {
			grnDao.updateCreditNoteAmt(cn.getMateinwardid(), cn.getBalance());
		}
		response.setMessage("Successfully");
		return response;

	}

	@Override
	@Transactional
	public Object getInvoiceNumber(GRNRequest grnRequest) {
		
		return grnDao.getInvoiceNumber(grnRequest);
	}

	@Override
	@Transactional
	public void updateBonusBatchExpiry(Integer id) {
		BonusBatchExpiryDao.updateBonusBatchExpiry(id);
		
	}

	@Override
	@Transactional
	public void saveBonusBatchExpiry(GrnBonusBatchExpiry grnBatchExpiry1) {
		BonusBatchExpiryDao.saveBonusBatchExpiry(grnBatchExpiry1);
		
	}

}