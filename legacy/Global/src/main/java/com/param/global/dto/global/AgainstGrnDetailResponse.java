package com.param.global.dto.global;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class AgainstGrnDetailResponse extends BatchListResponse {

	protected Integer grnId;
	
	protected Integer grnDetailId;
	protected BigInteger batchIdForBarcode;
	protected Integer pendingQuan; 
	protected Integer availQuan;
	protected Integer grnQuantity;
	protected Long grnRejectQuantity;
	protected Long totalGRNQuantity;
	protected String batchNo;
	protected Integer bonusBatchExpiryId;
	protected Integer printedCount;


	public Long getGrnRejectQuantity() {
		return grnRejectQuantity;
	}

	public void setGrnRejectQuantity(Long grnRejectQuantity) {
		this.grnRejectQuantity = grnRejectQuantity;
	}

	protected Long pendingQuantity;
	protected Long  phyRejectQuantity;

	protected Integer storeId;

	protected String Store;
	
	protected Double mrp;
	protected String hospitalName;
	protected Integer approvedQuantity;
    protected Double discount;
    protected Integer discountType;
    protected Double totalDiscount;
    protected Double totalAmount;
    protected Double actualDiscountPercentage;
    protected boolean isManualBarcodeRequired;
    protected Integer bonusApprovedQuantity;
    protected Integer bonusUomTypeId;
    protected List<GrnBonusBatchExpiry> grnBonusBatchExpiry; 
    protected List<AgainstGrnDetailResponse> bonusBatchExpiry;

	protected String prePrintedBarcode;
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Long getPhyRejectQuantity() {
		return phyRejectQuantity;
	}

	public void setPhyRejectQuantity(Long phyRejectQuantity) {
		this.phyRejectQuantity = phyRejectQuantity;
	}

	/* start code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the accepted quantity at physical inspection as available quantity*/
	List<UomResponse> uomList;
	
	public List<UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}
	/* end code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the accepted quantity at physical inspection as available quantity*/

	public AgainstGrnDetailResponse()
	{
		super();
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Integer getGrnQuantity() {
		return grnQuantity;
	}

	public void setGrnQuantity(Integer grnQuantity) {
		this.grnQuantity = grnQuantity;
	}

	public Long getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Long pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return Store;
	}

	public void setStore(String store) {
		Store = store;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

	public Long getTotalGRNQuantity() {
		return totalGRNQuantity;
	}

	public void setTotalGRNQuantity(Long totalGRNQuantity) {
		this.totalGRNQuantity = totalGRNQuantity;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getActualDiscountPercentage() {
		return actualDiscountPercentage;
	}

	public void setActualDiscountPercentage(Double actualDiscountPercentage) {
		this.actualDiscountPercentage = actualDiscountPercentage;
	}

	public boolean getIsManualBarcodeRequired() {
		return isManualBarcodeRequired;
	}

	public void setIsManualBarcodeRequired(boolean isManualBarcodeRequired) {
		this.isManualBarcodeRequired = isManualBarcodeRequired;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}

	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiry() {
		return grnBonusBatchExpiry;
	}

	public void setGrnBonusBatchExpiry(List<GrnBonusBatchExpiry> grnBonusBatchExpiry) {
		this.grnBonusBatchExpiry = grnBonusBatchExpiry;
	}
	
	protected char isApprovalPending;

	public char getIsApprovalPending() {
		return isApprovalPending;
	}

	public void setIsApprovalPending(char isApprovalPending) {
		this.isApprovalPending = isApprovalPending;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigInteger getBatchIdForBarcode() {
		return batchIdForBarcode;
	}

	public void setBatchIdForBarcode(BigInteger batchIdForBarcode) {
		this.batchIdForBarcode = batchIdForBarcode;
	}

	public Integer getPendingQuan() {
		return pendingQuan;
	}

	public void setPendingQuan(Integer pendingQuan) {
		this.pendingQuan = pendingQuan;
	}

	public Integer getAvailQuan() {
		return availQuan;
	}

	public void setAvailQuan(Integer availQuan) {
		this.availQuan = availQuan;
	}

	public List<AgainstGrnDetailResponse> getBonusBatchExpiry() {
		return bonusBatchExpiry;
	}

	public void setBonusBatchExpiry(List<AgainstGrnDetailResponse> bonusBatchExpiry) {
		this.bonusBatchExpiry = bonusBatchExpiry;
	}

	public Integer getBonusBatchExpiryId() {
		return bonusBatchExpiryId;
	}

	public void setBonusBatchExpiryId(Integer bonusBatchExpiryId) {
		this.bonusBatchExpiryId = bonusBatchExpiryId;
	}

	public Integer getPrintedCount() {
		return printedCount;
	}

	public void setPrintedCount(Integer printedCount) {
		this.printedCount = printedCount;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}
}
