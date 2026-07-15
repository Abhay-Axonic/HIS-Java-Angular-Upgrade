package com.param.global.dto.items;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class ItemsRequest extends CookieObjectDto {

	private Integer storeId;

	private List<Integer> itemIds;

	private Integer assetTypeId;

	private Integer assetCategoryId;

	private Integer productCategoryId;

	private Integer fromStoreId;

	private Integer toStoreId;

	private Boolean isConsignment;
	
	private Boolean isConsumable;

	private Integer vedId;

	private String itemCode;

	private String itemName;

	protected Date fromDate;
	
	private Integer screenId;

	protected Date toDate;
	
	protected Integer vendorId;
	
	protected Integer itemId;
	
	protected List<ItemsDetail> items;
	
	private Integer unitId;
	
	private Integer visitTypeId;
	
	private Integer requestingStoreId;
	
	private String batchName;
	
	private String batchNo;
	
	private Integer patientCategoryId;
	
	private Integer patientClassId;
	
	private Integer entitlementId;
	private Integer stockAvailable;
	
    protected Long batchId;
    
    protected String prePrintedBarcode;

    private boolean isThreeUOMApplicable;
    
    private Boolean checkKit;
    
    private Integer paymentEntitlementId;
    
    private String screenName;
    
    private Integer offset;
    
    private Integer noOfRecordsPerPage;
    String priPrintedBarcodeNo;

	private Integer printedCount;

	private Boolean fromWalkInPrescription;
	
	private Boolean fromScrap;


	private Integer organizationId;

	public String getPriPrintedBarcodeNo() {
		return priPrintedBarcodeNo;
	}

	public void setPriPrintedBarcodeNo(String priPrintedBarcodeNo) {
		this.priPrintedBarcodeNo = priPrintedBarcodeNo;
	}

	public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
    
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Integer entitlementId) {
		this.entitlementId = entitlementId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	private Integer admissionId;
	
	private Integer encounterId;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Integer getVedId() {
		return vedId;
	}

	public void setVedId(Integer vedId) {
		this.vedId = vedId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public List<ItemsDetail> getItems() {
		return items;
	}

	public void setItems(List<ItemsDetail> items) {
		this.items = items;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public Integer getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Integer stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}

	public Boolean getCheckKit() {
		return checkKit;
	}

	public void setCheckKit(Boolean checkKit) {
		this.checkKit = checkKit;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPrintedCount() {
		return printedCount;
	}

	public void setPrintedCount(Integer printedCount) {
		this.printedCount = printedCount;
	}

	public Boolean getFromWalkInPrescription() {
		return fromWalkInPrescription;
	}

	public void setFromWalkInPrescription(Boolean fromWalkInPrescription) {
		this.fromWalkInPrescription = fromWalkInPrescription;
	}

	public Boolean getFromScrap() {
		return fromScrap;
	}

	public void setFromScrap(Boolean fromScrap) {
		this.fromScrap = fromScrap;
	}

	@Override
	public Integer getOrganizationId() {
		return organizationId;
	}

	@Override
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

}