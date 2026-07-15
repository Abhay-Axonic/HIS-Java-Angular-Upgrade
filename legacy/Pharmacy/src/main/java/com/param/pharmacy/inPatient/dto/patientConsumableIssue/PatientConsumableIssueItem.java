package com.param.pharmacy.inPatient.dto.patientConsumableIssue;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;


@JsonInclude(Include.NON_NULL)
public class PatientConsumableIssueItem {
	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private String genericName;

	private Long batchId;

	private String batchNo;

	private Date expiry;

	private String uomType;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer indentQuantity;

	private Long availableStockQuantity;

	private Integer issueQuantity;

	private Integer patientIndentDetailId;

	private Integer implantIndentDetailId;

	private List<BatchResponse> batch;
	private List<PatientConsumableIssueItem> batchIssued;
	private List<SubstitutePatientConsumableDto> selectedItems;

	private Integer statusId;
	private Integer formulationTypeId;
	private String status;
	private Integer parentIssueDetailId;

	private Integer genericId;
	private Integer acceptedQuantity;
	private Integer rejectedQuantity;
	private String doseUnit;
	private Double dose;
	
	private Integer taxId;
	private Double taxPercentage;
	private List<PatientConsumableIssueItem> PatientConsumableIssueItemSub;
	private Integer indentUomTypeId;
	public List<PatientConsumableIssueItem> getPatientConsumableIssueItemSub() {
		return PatientConsumableIssueItemSub;
	}

	public void setPatientConsumableIssueItemSub(List<PatientConsumableIssueItem> patientConsumableIssueItemSub) {
		PatientConsumableIssueItemSub = patientConsumableIssueItemSub;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	//Ganesh
	private Integer markUpDown;
	private Double markUpDownPer;
	private Integer productCategoryId;
	private Integer storeId;

	private Integer batchMapperId;
	
	Double patientClassPercentage;
	
	boolean markupStatus;
	
	Integer priorityId;
	private Double rate;
	
    protected List<UomResponse> uomList;

    public List<UomResponse> getUomList() {
        return uomList;
    }

    public void setUomList(List<UomResponse> uomList) {
        this.uomList = uomList;
    }
	
    private boolean isThreeUOMApplicable;

    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
	
	private boolean isSelected;
	private Boolean isLasa;
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public boolean isMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Long getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Long availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public Integer getPatientIndentDetailId() {
		return patientIndentDetailId;
	}

	public void setPatientIndentDetailId(Integer patientIndentDetailId) {
		this.patientIndentDetailId = patientIndentDetailId;
	}

	public Integer getImplantIndentDetailId() {
		return implantIndentDetailId;
	}

	public void setImplantIndentDetailId(Integer implantIndentDetailId) {
		this.implantIndentDetailId = implantIndentDetailId;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PatientConsumableIssueItem(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Long batchId, String batchNo, Date expiry, String uomType, Integer uomTypeId, Integer uomUnitId,
			String uomUnit, Integer indentQuantity, Long availableStockQuantity, Integer issueQuantity, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.availableStockQuantity = availableStockQuantity;
		this.issueQuantity = issueQuantity;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public PatientConsumableIssueItem(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			String uomType, String uomUnit, Integer indentQuantity, Integer issueQuantity, Long availableStockQuantity,
			List<BatchResponse> batch) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.issueQuantity = issueQuantity;
		this.availableStockQuantity = availableStockQuantity;
		this.batch = batch;
	}

	public PatientConsumableIssueItem(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			String uomType, String uomUnit, Integer indentQuantity, Integer issueQuantity, Long availableStockQuantity,
			List<BatchResponse> batch, Integer patientIndentDetailId, Integer parentIssueDetailId, Integer uomTypeId , Integer uomUnitId, boolean isThreeUOMApplicable, Boolean isLasa) {
		this(id, itemId, itemCode, itemName, genericName, uomType, uomUnit, indentQuantity, issueQuantity,
				availableStockQuantity, batch);
		this.patientIndentDetailId = patientIndentDetailId;
		this.parentIssueDetailId = parentIssueDetailId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.isLasa = isLasa;
	}

	public PatientConsumableIssueItem(Integer id, Integer implantIndentDetailId, Integer itemId, String itemCode,
			String itemName, String genericName, String uomType, String uomUnit, Integer indentQuantity,
			Integer issueQuantity, Long availableStockQuantity, List<BatchResponse> batch) {
		this(id, itemId, itemCode, itemName, genericName, uomType, uomUnit, indentQuantity, issueQuantity,
				availableStockQuantity, batch);
		this.implantIndentDetailId = implantIndentDetailId;
	}

	public PatientConsumableIssueItem() {
		// TODO Auto-generated constructor stub
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer!=null ? markUpDownPer : 0;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	

	public List<SubstitutePatientConsumableDto> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<SubstitutePatientConsumableDto> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Integer getParentIssueDetailId() {
		return parentIssueDetailId;
	}

	public void setParentIssueDetailId(Integer parentIssueDetailId) {
		this.parentIssueDetailId = parentIssueDetailId;
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

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public List<PatientConsumableIssueItem> getBatchIssued() {
		return batchIssued;
	}

	public void setBatchIssued(List<PatientConsumableIssueItem> batchIssued) {
		this.batchIssued = batchIssued;
	}

	public Integer getIndentUomTypeId() {
		return indentUomTypeId;
	}

	public void setIndentUomTypeId(Integer indentUomTypeId) {
		this.indentUomTypeId = indentUomTypeId;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		isLasa = isLasa;
	}
}
