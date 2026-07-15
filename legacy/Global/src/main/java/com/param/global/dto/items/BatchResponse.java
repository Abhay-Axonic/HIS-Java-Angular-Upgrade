package com.param.global.dto.items;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.common.ICommonConstants;

@JsonInclude(Include.NON_NULL)
public class BatchResponse implements Cloneable, ICommonConstants {

	public Integer getSelectedUomTypeId() {
		return selectedUomTypeId;
	}

	public void setSelectedUomTypeId(Integer selectedUomTypeId) {
		this.selectedUomTypeId = selectedUomTypeId;
	}

	public Integer getSelectedUnitId() {
		return selectedUnitId;
	}

	public void setSelectedUnitId(Integer selectedUnitId) {
		this.selectedUnitId = selectedUnitId;
	}

	protected Integer id;
	protected Integer availableQuantity;

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	protected String rackNo;

	protected Long batchId;

	protected String batchNo;

	protected String batchDetails;

	protected Date expiry;

	protected Double sellingPrice;

	protected Integer availableStockQuantity;

	protected List<UomResponse> uom;

	protected Boolean isSelected;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer selectedUomTypeId;

	protected Integer selectedUnitId;

	protected Integer issueQuantity;

	protected Integer pickerQuantity;

	protected Integer currentQuantity;

	protected String uomType;

	protected String uomUnit;

	protected Integer currentVendorId;

	protected String currentVendorName;

	protected Double markup;
	protected Double totalstock;
	protected Boolean isconsignment;
	protected Boolean quarantine;
	protected Double mrp;
	protected Double rate;
	protected String rack;
	protected String shelf;

	private String lotNo;
	private Integer quantity;
	private Date expiryDate;
	private Double cop;
	private Integer yearOfManufacture;
	private Integer rejectedQuantity;
	private Integer returnedQuantity;
	private  Double taxPercentage;
	private Integer taxId;
	private  Double taxAmount;  
	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalstock() {
		return totalstock;
	}

	public void setTotalstock(Double totalstock) {
		this.totalstock = totalstock;
	}

	public Boolean getIsconsignment() {
		return isconsignment;
	}

	public void setIsconsignment(Boolean isconsignment) {
		this.isconsignment = isconsignment;
	}

	public Boolean getQuarantine() {
		return quarantine;
	}

	public void setQuarantine(Boolean quarantine) {
		this.quarantine = quarantine;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	protected String priority;

	protected List<AsseccoriesResponse> asseccories;

	protected Integer ItemId;

	private Integer assignPickerId;

	private Integer opUomId;

	private Double lendingPrice;

	private Double actualLendingPrice;

	/* added by minesh */
	private Boolean isConsignment;
	protected String batchType;

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getBatchType() {
		if (this.batchNo != null && this.isConsignment != null) {
			if (this.isConsignment) {
				return this.batchNo + " | C";
			} else {
				return batchNo;
			}
		}
		return batchDetails;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			Double markup, Double rate, Boolean isConsignment, List<UomResponse> uom) {
		this(batchId, batchNo, expiry, availableStockQuantity, markup, rate, uom);
		this.sellingPrice = sellingPrice;
		this.isConsignment = isConsignment;
	}

	/* end added by minesh */

	public Integer getItemId() {
		return ItemId;
	}

	public void setItemId(Integer itemId) {
		ItemId = itemId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(Integer currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public BatchResponse() {
		super();
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, List<UomResponse> uom) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double markup, Double rate, List<UomResponse> uom) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.markup = markup;
		this.rate = rate;
		this.uom = uom;
	}
	
	public BatchResponse(Long batchId, String batchNo, Date expiry, Double markup, Double rate) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.markup = markup;
		this.rate = rate;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Integer currentVendorId, String currentVendorName) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.currentVendorId = currentVendorId;
		this.currentVendorName = currentVendorName;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
	}

	public BatchResponse(Long batchId, Integer issueQuantity, Date expiry) {
		super();
		this.batchId = batchId;
		this.issueQuantity = issueQuantity;
		this.expiry = expiry;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Integer availableStockQuantity,
			List<UomResponse> uom) {
		this(batchId, batchNo, expiry, uom);
		this.availableStockQuantity = availableStockQuantity;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Integer availableStockQuantity) {
		this(batchId, batchNo, expiry);
		this.availableStockQuantity = availableStockQuantity;
	}

	public BatchResponse(Long batchId, String batchNo, String batchDetails, Date expiry, List<UomResponse> uom) {
		this(batchId, batchNo, expiry, uom);
		this.batchDetails = batchDetails;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, List<UomResponse> uom) {
		this(batchId, batchNo, expiry, uom);
		this.sellingPrice = sellingPrice;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Integer availableStockQuantity, Double markup,
			Double rate, List<UomResponse> uom) {
		this(batchId, batchNo, expiry, uom);
		this.availableStockQuantity = availableStockQuantity;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Double markup, Double rate,
			List<UomResponse> uom  , Double actualLendingPrice ) {
		this(batchId, batchNo, expiry, markup, rate, uom);
		this.sellingPrice = sellingPrice;
		this.actualLendingPrice=actualLendingPrice;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, List<UomResponse> uom, Double sellingPrice,
			boolean isSelected) {
		this(batchId, batchNo, expiry, sellingPrice, uom);
		this.isSelected = isSelected;

	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, List<UomResponse> uom, Double sellingPrice,
			String priority) {
		this(batchId, batchNo, expiry, sellingPrice, uom);

		this.priority = priority;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, List<UomResponse> uom, Double sellingPrice) {
		this(batchId, batchNo, expiry, sellingPrice, uom);
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			List<UomResponse> uom) {
		this(batchId, batchNo, expiry, availableStockQuantity, uom);
		this.sellingPrice = sellingPrice;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			Double markup, Double rate, List<UomResponse> uom) {
		this(batchId, batchNo, expiry, availableStockQuantity, markup, rate, uom);
		this.sellingPrice = sellingPrice;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			List<UomResponse> uom, Boolean isSelected) {
		this(batchId, batchNo, expiry, availableStockQuantity, uom);
		this.sellingPrice = sellingPrice;
		this.isSelected = isSelected;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			Double markup, Double rate, List<UomResponse> uom, Boolean isSelected) {
		this(batchId, batchNo, expiry, markup, rate, uom);
		this.availableStockQuantity = availableStockQuantity;
		this.sellingPrice = sellingPrice;
		this.isSelected = isSelected;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Integer availableStockQuantity,
			Double markup, Double rate, List<UomResponse> uom, String priority) {
		this(batchId, batchNo, expiry, markup, rate, uom);
		this.sellingPrice = sellingPrice;
		this.priority = priority;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice,
			Integer availableStockQuantity) {
		this(batchId, batchNo, expiry, availableStockQuantity);
		this.sellingPrice = sellingPrice;
	}

	public BatchResponse(Long batchId, String batchNo, Date expiry, Integer availableStockQuantity,
			List<AsseccoriesResponse> asseccories, Double sellingPrice) {
		this(batchId, batchNo, expiry, availableStockQuantity);
		this.sellingPrice = sellingPrice;
		this.asseccories = asseccories;
	}

	public BatchResponse(Long id2, String batchNo2, Date expiryDate, Integer availableQty,
			AsseccoriesResponse populateAccessories, Double sellingPrice2) {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
	 * 
	 * public String getBatchNo() { return batchNo!=null ? batchNo: NOT_APPLICABLE;
	 * }
	 * 
	 * public void setBatchId(Long batchId) { this.batchId = batchId; } public Long
	 * getBatchId() { return getBatchNo()!=null &&
	 * !getBatchNo().equals(ICommonConstants.NOT_APPLICABLE) ? batchId:0; }
	 */

	public BatchResponse(Long batchId, String batchNo, Date expiry, Double sellingPrice, Double markup,
			Double rate,Double actualLendingPrice) {
		this(batchId, batchNo, expiry, markup, rate);
		this.sellingPrice = sellingPrice;
		this.actualLendingPrice=actualLendingPrice;
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

	public String getBatchDetails() {
		if (this.batchNo != null && this.expiry != null) {
			Format formatter = new SimpleDateFormat("MMM-yyyy");
			return this.batchNo + " | " + formatter.format(this.expiry);
		}
		return batchNo = ICommonConstants.NOT_APPLICABLE;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public void addUom(UomResponse uom) {
		if (this.uom == null)
			this.uom = new ArrayList<UomResponse>();

		this.uom.add(uom);
	}

	public Integer getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Integer availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Integer getPickerQuantity() {
		return pickerQuantity;
	}

	public void setPickerQuantity(Integer pickerQuantity) {
		this.pickerQuantity = pickerQuantity;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
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

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendorName() {
		return currentVendorName;
	}

	public void setCurrentVendorName(String currentVendorName) {
		this.currentVendorName = currentVendorName;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public List<AsseccoriesResponse> getAsseccories() {
		return asseccories;
	}

	public void setAsseccories(List<AsseccoriesResponse> asseccories) {
		this.asseccories = asseccories;
	}

	public AsseccoriesResponse addAccessories(AsseccoriesResponse asseccories) {
		if (this.asseccories == null)
			this.asseccories = new ArrayList<AsseccoriesResponse>();

		this.asseccories.add(asseccories);
		return asseccories;
	}

	public Integer getAssignPickerId() {
		return assignPickerId;
	}

	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}

	public Integer getOpUomId() {
		return opUomId;
	}

	public void setOpUomId(Integer opUomId) {
		this.opUomId = opUomId;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public Double getLendingPrice() {
		return lendingPrice;
	}

	public void setLendingPrice(Double lendingPrice) {
		this.lendingPrice = lendingPrice;
	}

	public Double getActualLendingPrice() {
		return actualLendingPrice;
	}

	public void setActualLendingPrice(Double actualLendingPrice) {
		this.actualLendingPrice = actualLendingPrice;
	}

	public BatchResponse clone() throws CloneNotSupportedException {
		return (BatchResponse) super.clone();
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}
}
