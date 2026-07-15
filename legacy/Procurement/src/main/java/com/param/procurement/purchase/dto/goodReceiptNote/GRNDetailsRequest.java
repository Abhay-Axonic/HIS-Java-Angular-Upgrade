package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;
import java.util.List;

import com.param.entity.model.procurement.GrnBonusBatchExpiry;

public class GRNDetailsRequest{
	
	
	protected Integer id;

	protected Integer itemId;

	protected Integer grnApprovedQuantity;

	protected Integer bonusApprovedQuantity;
	protected Integer grnQtyUomTypeWise;
	protected Integer pendingQuantity;
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	protected Integer grnReceivedQuantity;
	/*protected List<CreditNoteAmountList> creditNoteAmountList;*/
	
	protected Integer bonusReceivedQuantity;
	/*public List<CreditNoteAmountList> getCreditNoteAmountList() {
		return creditNoteAmountList;
	}

	public void setCreditNoteAmountList(List<CreditNoteAmountList> creditNoteAmountList) {
		this.creditNoteAmountList = creditNoteAmountList;
	}*/

	protected Double lcop;
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	protected String batchNo;
	protected String poDetailsId;
	
	public String getPoDetailsId() {
		return poDetailsId;
	}

	public void setPoDetailsId(String poDetailsId) {
		this.poDetailsId = poDetailsId;
	}

	protected Integer grnUomTypeId;

	protected Integer grnUomUnitId;

	protected Integer bonusUomTypeId;

	protected Integer bonusUomUnitId;

	protected Double cop;

	protected Double mrp;

	protected Double discount;
	protected Double creditNoteAmount;
	protected boolean isBatchRequired;
	
	
	public boolean isBatchRequired() {
		return isBatchRequired;
	}

	public void setBatchRequired(boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	protected Date expiryDate;
	
	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}

	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}

	protected String lotNumber;
	
	protected Integer manufacturerId;	
	
	protected Integer poDetailId;
	
	protected Integer taxId;

	protected Double taxPercentage;

	protected Double totalAmount;

	protected Double taxAmount;

	protected Double netAmount;

	protected Double totalDiscount;
	
	protected Double totalOtherAmount;
	
	protected Integer statusId;

	protected String grnSpecification;

	protected String remark;

	protected Integer approvedBy;
	
	protected Date approvedDate;
	
	protected Integer rejectedQuantity;
	
	protected Integer rejectReasonId;
	
	protected String rejectNote;
	
	private String warrentyPeriod;
	
	private String supportTerms;
	
	private String technicalSpecification;
	
	private String expectedPayment;
	
	private Integer serviceProviderId;
	
	private String serviceProvider;
	
	private Integer manufactureYear;
	
	private Date warrantyDate;
	private Date warrantyDateTo;
	
	public Date getWarrantyDateTo() {
		return warrantyDateTo;
	}

	public void setWarrantyDateTo(Date warrantyDateTo) {
		this.warrantyDateTo = warrantyDateTo;
	}

	//added by minesh
	private double otherChargesAmount;
	public double getOtherChargesAmount() {
		return otherChargesAmount;
	}

	public void setOtherChargesAmount(double otherChargesAmount) {
		this.otherChargesAmount = otherChargesAmount;
	}
	//end code by minesh
	protected List<GRNUomMapper> grnUom;
	
	protected List<GRNUomMapper> bonusUom;
	
	private List<GrnBonusBatchExpiry> grnBonusBatchExpiry;
	
	/*protected List<GRNOtherChargesMapper> otherCharges;*/
	
	protected List<GRNStagedDto> grnStagedQuantity;
	
	protected List<GrnAccessoriesMapper> grnAccessoriesMapper;
	
	protected List<GRNStagedList> grnStagedLists;
	
	
	public List<GRNStagedList> getGrnStagedLists() {
		return grnStagedLists;
	}

	public void setGrnStagedLists(List<GRNStagedList> grnStagedLists) {
		this.grnStagedLists = grnStagedLists;
	}

	public GRNDetailsRequest() {
		super();
	}

	public GRNDetailsRequest(Integer id, Integer itemId,Integer grnApprovedQuantity,Integer bonusApprovedQuantity, Integer grnReceivedQuantity, Integer bonusReceivedQuantity, String batchNo, Integer grnUomTypeId,
			Integer grnUomUnitId, Integer bonusUomTypeId, Integer bonusUomUnitId, Double cop, Double mrp,
			Double discount, Date expiryDate, String lotNumber, Integer manufacturerId,
			Integer taxId, Double taxPercentage, Double totalAmount, Double taxAmount, Double netAmount,
			Double totalDiscount,  Double totalOtherAmount, Integer statusId,
			String grnSpecification, String remark,Integer rejectedQuantity,
			List<GRNUomMapper> grnUom,
			List<GRNUomMapper> bonusUom/*, List<GRNOtherChargesMapper> otherCharges*/,List<GRNStagedDto> grnStagedQuantity, List<GRNStagedList> grnStagedLists) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.grnApprovedQuantity=grnApprovedQuantity;
		this.bonusApprovedQuantity=bonusApprovedQuantity;
		this.grnReceivedQuantity = grnReceivedQuantity;
		this.bonusReceivedQuantity = bonusReceivedQuantity;
		this.batchNo = batchNo;
		this.grnUomTypeId = grnUomTypeId;
		this.grnUomUnitId = grnUomUnitId;
		this.bonusUomTypeId = bonusUomTypeId;
		this.bonusUomUnitId = bonusUomUnitId;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.expiryDate = expiryDate;
		this.lotNumber = lotNumber;
		this.manufacturerId = manufacturerId;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.statusId = statusId;
		this.grnSpecification = grnSpecification;
		this.remark = remark;
		this.rejectedQuantity=rejectedQuantity;
		this.grnUom = grnUom;
		this.bonusUom = bonusUom;
		//this.otherCharges = otherCharges;
		this.grnStagedQuantity = grnStagedQuantity;
		this.grnStagedLists = grnStagedLists;
	}

	
	public GRNDetailsRequest(Integer id, Integer itemId,Integer grnApprovedQuantity,Integer bonusApprovedQuantity, Integer grnReceivedQuantity, Integer bonusReceivedQuantity, String batchNo,
			Double cop, Double mrp,
			Double discount, Date expiryDate, String lotNumber, Integer manufacturerId,
			Integer taxId, Double taxPercentage, Double totalAmount, Double taxAmount, Double netAmount,
			Double totalDiscount,  Double totalOtherAmount, Integer statusId,
			String grnSpecification, String remark,Integer rejectedQuantity,String warrentyPeriod,String supportTerms,String technicalSpecification,String expectedPayment,Integer serviceProviderId,String serviceProvider,Date warrantyDate,
			/*List<GRNOtherChargesMapper> otherCharges,*/List<GRNStagedDto> grnStagedQuantity, List<GRNStagedList> grnStagedLists) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.grnApprovedQuantity=grnApprovedQuantity;
		this.bonusApprovedQuantity=bonusApprovedQuantity;
		this.grnReceivedQuantity = grnReceivedQuantity;
		this.bonusReceivedQuantity = bonusReceivedQuantity;
		this.batchNo = batchNo;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.expiryDate = expiryDate;
		this.lotNumber = lotNumber;
		this.manufacturerId = manufacturerId;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.statusId = statusId;
		this.grnSpecification = grnSpecification;
		this.remark = remark;
		this.rejectedQuantity=rejectedQuantity;
	//	this.otherCharges = otherCharges;
		this.grnStagedQuantity = grnStagedQuantity;
		this.warrentyPeriod = warrentyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.serviceProviderId = serviceProviderId;
		this.serviceProvider = serviceProvider;
		this.warrantyDate = warrantyDate;
		this.grnStagedLists = grnStagedLists;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGrnApprovedQuantity() {
		return grnApprovedQuantity;
	}

	public void setGrnApprovedQuantity(Integer grnApprovedQuantity) {
		this.grnApprovedQuantity = grnApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public Integer getGrnReceivedQuantity() {
		return grnReceivedQuantity;
	}

	public void setGrnReceivedQuantity(Integer grnReceivedQuantity) {
		this.grnReceivedQuantity = grnReceivedQuantity;
	}

	public Integer getBonusReceivedQuantity() {
		return bonusReceivedQuantity;
	}

	public void setBonusReceivedQuantity(Integer bonusReceivedQuantity) {
		this.bonusReceivedQuantity = bonusReceivedQuantity;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGrnUomTypeId() {
		return grnUomTypeId;
	}

	public void setGrnUomTypeId(Integer grnUomTypeId) {
		this.grnUomTypeId = grnUomTypeId;
	}

	public Integer getGrnUomUnitId() {
		return grnUomUnitId;
	}

	public void setGrnUomUnitId(Integer grnUomUnitId) {
		this.grnUomUnitId = grnUomUnitId;
	}

	public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}

	public Integer getBonusUomUnitId() {
		return bonusUomUnitId;
	}

	public void setBonusUomUnitId(Integer bonusUomUnitId) {
		this.bonusUomUnitId = bonusUomUnitId;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	
	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getGrnSpecification() {
		return grnSpecification;
	}

	public void setGrnSpecification(String grnSpecification) {
		this.grnSpecification = grnSpecification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getWarrentyPeriod() {
		return warrentyPeriod;
	}

	public void setWarrentyPeriod(String warrentyPeriod) {
		this.warrentyPeriod = warrentyPeriod;
	}

	public String getSupportTerms() {
		return supportTerms;
	}

	public void setSupportTerms(String supportTerms) {
		this.supportTerms = supportTerms;
	}

	public String getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public String getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(String expectedPayment) {
		this.expectedPayment = expectedPayment;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public List<GRNUomMapper> getGrnUom() {
		return grnUom;
	}

	public void setGrnUom(List<GRNUomMapper> grnUom) {
		this.grnUom = grnUom;
	}

	public List<GRNUomMapper> getBonusUom() {
		return bonusUom;
	}

	public void setBonusUom(List<GRNUomMapper> bonusUom) {
		this.bonusUom = bonusUom;
	}
/*
	public List<GRNOtherChargesMapper> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<GRNOtherChargesMapper> otherCharges) {
		this.otherCharges = otherCharges;
	}
*/
	public List<GRNStagedDto> getGrnStagedQuantity() {
		return grnStagedQuantity;
	}

	public void setGrnStagedQuantity(List<GRNStagedDto> grnStagedQuantity) {
		this.grnStagedQuantity = grnStagedQuantity;
	}

	public List<GrnAccessoriesMapper> getGrnAccessoriesMapper() {
		return grnAccessoriesMapper;
	}

	public void setGrnAccessoriesMapper(List<GrnAccessoriesMapper> grnAccessoriesMapper) {
		this.grnAccessoriesMapper = grnAccessoriesMapper;
	}

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public Date getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiry() {
		return grnBonusBatchExpiry;
	}

	public void setGrnBonusBatchExpiry(List<GrnBonusBatchExpiry> grnBonusBatchExpiry) {
		this.grnBonusBatchExpiry = grnBonusBatchExpiry;
	}

	public Integer getGrnQtyUomTypeWise() {
		return grnQtyUomTypeWise;
	}

	public void setGrnQtyUomTypeWise(Integer grnQtyUomTypeWise) {
		this.grnQtyUomTypeWise = grnQtyUomTypeWise;
	}

}
