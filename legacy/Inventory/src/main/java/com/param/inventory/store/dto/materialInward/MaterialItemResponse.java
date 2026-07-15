package com.param.inventory.store.dto.materialInward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class MaterialItemResponse {

	protected Integer id;
	protected String returnableGatePassNo;
	public String getReturnableGatePassNo() {
		return returnableGatePassNo;
	}

	public void setReturnableGatePassNo(String returnableGatePassNo) {
		this.returnableGatePassNo = returnableGatePassNo;
	}

	protected Integer itemId;

	protected String itemCode;

	protected Integer statusId;

	protected String status;

	protected String itemName;

	protected String uomType;

	protected String uomUnit;

	protected String batchNo;
	protected String taxCode;
	protected Integer approvedQuantity;
	protected Integer rejectedQuantity ;
	protected Integer returnableGatePassDetailId;
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectedQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectedQuantity = rejectQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	protected Integer uomTypeId;
	
	protected Integer mrpUomTypeId;

	protected Integer uomUnitId;

	protected Integer taxId;

	protected Integer returnQuantity;

	protected Double returnAmount;

	protected Long batchId;

	protected Date expiryDate;

	protected Double cop;

	protected Double markup;

	protected String taxName;

	protected Double taxPercent;

	protected List<TaxDropDown> taxList;

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	protected Double taxAmount;

	protected Double totalAmount;

	protected Double netAmount;

	protected Integer receivedQuantity;

	protected Double totalNetAmount;

	protected Date returnableGatepassDate;

	protected Double creditNoteAmount;

	protected Double creditTaxAmount;

	protected Double creditNetAmount;

	protected String specification;

	protected Double mrp;

	protected List<UomResponse> uom;
	
	protected List<AsseccoriesResponse> accessories;
	protected boolean isReject;
	
	public boolean isReject() {
		return isReject;
	}

	public void setIsReject(boolean isReject) {
		this.isReject = isReject;
	}
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public MaterialItemResponse(Integer id, Integer returnableGatePassDetailId, Integer itemId, String itemCode,
								String itemName, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, String batchNo,
								Date expiryDate, Double cop, Integer taxId, String taxName, Double taxPercent, Double taxAmount,
								Double totalAmount, Double netAmount, Integer receivedQty, Integer statusId, String statusName,
								Double markup, Integer returnQuantity, Double mrp, String specification, Double returnAmount,
								Integer mrpUomTypeId, List<UomResponse> uom, Integer approveQuantity , Integer rejectQuantity, boolean isThreeUOMApplicable) {
		this.id = id;
		this.returnableGatePassDetailId=returnableGatePassDetailId;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.receivedQuantity = receivedQty;
		this.statusId = statusId;
		this.status = statusName;
		this.markup = markup;
		this.returnQuantity = returnQuantity;
		this.mrp = mrp;
		this.specification = specification;
		this.returnAmount = returnAmount;
		this.mrpUomTypeId = mrpUomTypeId;
		this.uom = uom;
		this.approvedQuantity=approveQuantity;
		this.rejectedQuantity=rejectQuantity;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	public MaterialItemResponse(Integer id, Integer returnableGatePassDetailId, Integer itemId, String itemCode,
								String itemName, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, String batchNo,
								Date expiryDate, Double cop, Integer taxId, String taxName, Double taxPercent, Double taxAmount,
								Double totalAmount, Double netAmount, Integer receivedQty, Integer statusId, String statusName,
								Double markup, Integer returnQuantity, Double mrp, String specification, Double returnAmount,
								Integer mrpUomTypeId, List<UomResponse> uom,List<TaxDropDown> taxList, boolean isThreeUOMApplicable) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.receivedQuantity = receivedQty;
		this.statusId = statusId;
		this.status = statusName;
		this.markup = markup;
		this.returnQuantity = returnQuantity;
		this.mrp = mrp;
		this.specification = specification;
		this.returnAmount = returnAmount;
		this.mrpUomTypeId = mrpUomTypeId;
		this.uom = uom;
		this.taxList = taxList;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	
	
	public MaterialItemResponse(Integer id, Integer returnableGatePassDetailId, Integer itemId, String itemCode,
								String itemName, String batchNo, Date expiryDate, Double cop, Integer taxId, String taxName, Double taxPercent,
								Double taxAmount, Double totalAmount, Double netAmount, Integer receivedQty, Integer statusId, String statusName,
								Double markup, Integer returnQuantity, Double mrp, String specification, Double returnAmount, List<AsseccoriesResponse> accessories, boolean isThreeUOMApplicable) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.receivedQuantity = receivedQty;
		this.statusId = statusId;
		this.status = statusName;
		this.markup = markup;
		this.returnQuantity = returnQuantity;
		this.mrp = mrp;
		this.specification = specification;
		this.returnAmount = returnAmount;
		this.accessories = accessories;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public MaterialItemResponse(Integer id, Integer returnableGatePassDetailId, Integer itemId, String itemCode,
								String itemName, String batchNo, Date expiryDate, Double cop, Integer taxId, String taxName, Double taxPercent,
								Double taxAmount, Double totalAmount, Double netAmount, Integer receivedQty, Integer statusId, String statusName,
								Double markup, Integer returnQuantity, Double mrp, String specification, Double returnAmount, List<AsseccoriesResponse> accessories,List<TaxDropDown> taxList, boolean isThreeUOMApplicable) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.receivedQuantity = receivedQty;
		this.statusId = statusId;
		this.status = statusName;
		this.markup = markup;
		this.returnQuantity = returnQuantity;
		this.mrp = mrp;
		this.specification = specification;
		this.returnAmount = returnAmount;
		this.accessories = accessories;
		this.taxList = taxList;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public List<AsseccoriesResponse> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AsseccoriesResponse> accessories) {
		this.accessories = accessories;
	}

	public MaterialItemResponse(Integer id, Date returnableGatepassDate, Double totalAmount, Double netAmount, Double taxAmount,
			Integer statusId, String status, Double creditNoteAmount, Double creditNetAmount, Double  creditTaxAmount) {
		super();
		this.id = id;
		this.returnableGatepassDate = returnableGatepassDate;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.taxAmount = taxAmount;
		this.statusId = statusId;
		this.status = status;
		this.creditNoteAmount = creditNoteAmount;
		this.creditNetAmount = creditNetAmount;
		this.creditTaxAmount =  creditTaxAmount;
	}

	public MaterialItemResponse(Integer id, String itemCode, String itemName, Integer uomTypeId, String uomType,
								Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Integer taxId, String taxName,
								Double taxPercent, Integer returnQuantity, List<UomResponse> uom) {

		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.returnQuantity = returnQuantity;
		this.uom = uom;
	}
	
	public MaterialItemResponse(Integer id, String itemCode, String itemName, Integer uomTypeId, String uomType,
								Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Integer taxId, String taxName,
								Double taxPercent, Integer returnQuantity, List<UomResponse> uom, Double cop) {

		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.returnQuantity = returnQuantity;
		this.uom = uom;
		this.cop = cop;
	}
	
	public MaterialItemResponse(Integer id, String itemCode, String itemName, Integer uomTypeId, String uomType,
								Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Integer taxId, String taxName,
								Double taxPercent, Integer returnQuantity, List<UomResponse> uom, Double cop, Integer itemId, boolean isThreeUOMApplicable) {

		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.returnQuantity = returnQuantity;
		this.uom = uom;
		this.cop = cop;
		this.itemId = itemId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public MaterialItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
								String uomType, Integer uomUnitId, String uomUnit, Integer taxId, String taxName, Double taxPercent,
								Integer returnQuantity, Double returnAmount, List<UomResponse> uom,List<TaxDropDown> taxList, boolean isThreeUOMApplicable) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.returnQuantity = returnQuantity;
		this.returnAmount = returnAmount;
		this.uom = uom;
		this.taxList = taxList;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public void addItems(UomResponse uom) {
		if (this.uom == null)
			this.uom = new ArrayList<UomResponse>();

		this.uom.add(uom);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public String getUomUnit() {
		return uomUnit;
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

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}

	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}

	public Double getCreditTaxAmount() {
		return creditTaxAmount;
	}

	public void setCreditTaxAmount(Double creditTaxAmount) {
		this.creditTaxAmount = creditTaxAmount;
	}

	public Double getCreditNetAmount() {
		return creditNetAmount;
	}

	public void setCreditNetAmount(Double creditNetAmount) {
		this.creditNetAmount = creditNetAmount;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Date getReturnableGatepassDate() {
		return returnableGatepassDate;
	}

	public void setReturnableGatepassDate(Date returnableGatepassDate) {
		this.returnableGatepassDate = returnableGatepassDate;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getMrpUomTypeId() {
		return mrpUomTypeId;
	}

	public void setMrpUomTypeId(Integer mrpUomTypeId) {
		this.mrpUomTypeId = mrpUomTypeId;
	}

	public Integer getReturnableGatePassDetailId() {
		return returnableGatePassDetailId;
	}

	public void setReturnableGatePassDetailId(Integer returnableGatePassDetailId) {
		this.returnableGatePassDetailId = returnableGatePassDetailId;
	}

}
