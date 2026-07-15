package com.param.procurement.loanInOut.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class LoanInOutResponseItem {
	private Integer id;

	private Integer loanInDetailId;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer uomTypeId;

	private String uom;

	private Integer uomUnitId;

	private String uomUnit;

	private Long batchId;

	private String batchNo;

	private Date expiryDate;

	private Integer quantity;

	private Integer returnQuantity;

	private Integer loanInQuantity;

	private Integer loanOutQuantity;

	private Integer receiveQuantity;

	private Double cop;

	private Double totalAmount;

	private String remark;

	private String specification;

	private List<UomResponse> uomList;

	protected Long stockAvailable;
	
	private Integer returnQty;
	private Integer returnedQuantity;
	private Integer  AvailableForReturn;

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public Integer getAvailableForReturn() {
		return AvailableForReturn;
	}

	public void setAvailableForReturn(Integer availableForReturn) {
		AvailableForReturn = availableForReturn;
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

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanInDetailId() {
		return loanInDetailId;
	}

	public void setLoanInDetailId(Integer loanInDetailId) {
		this.loanInDetailId = loanInDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getLoanInQuantity() {
		return loanInQuantity;
	}

	public void setLoanInQuantity(Integer loanInQuantity) {
		this.loanInQuantity = loanInQuantity;
	}

	public Integer getLoanOutQuantity() {
		return loanOutQuantity;
	}

	public void setLoanOutQuantity(Integer loanOutQuantity) {
		this.loanOutQuantity = loanOutQuantity;
	}

	public Integer getReceiveQuantity() {
		return receiveQuantity;
	}

	public void setReceiveQuantity(Integer receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
	}

	public List<UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public LoanInOutResponseItem() {
		super();
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, String batchNo, Date expiryDate, Integer quantity,
			Double cop, Double totalAmount, String remark, String specification) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalAmount, String remark, String specification) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.remark = remark;
		this.specification = specification;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, String batchNo, Date expiryDate, Integer loanOutQuantity,
			Integer receiveQuantity, Double cop, String remark, Double totalAmount) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.receiveQuantity = receiveQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate,
			Integer loanOutQuantity, Integer receiveQuantity, Double cop, String remark, Double totalAmount) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.receiveQuantity = receiveQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Long batchId,
			String batchNo, Date expiryDate, Integer loanOutQuantity, Integer receiveQuantity, Double cop,
			String remark, Double totalAmount) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.receiveQuantity = receiveQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, String batchNo, Date expiryDate, Double cop,
			Double totalAmount, String remark) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo,
			Date expiryDate, Double cop, Double totalAmount, String remark) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, String batchNo, Date expiryDate, Integer loanInQuantity,
			Double cop) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanInQuantity = loanInQuantity;
		this.cop = cop;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo,
			Date expiryDate, Integer loanInQuantity, Double cop) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanInQuantity = loanInQuantity;
		this.cop = cop;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double cop,
			Integer loanOutQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.loanOutQuantity = loanOutQuantity;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Long batchId,
			String batchNo, Date expiryDate, Double cop, Integer loanOutQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.loanOutQuantity = loanOutQuantity;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate,
			Integer loanOutQuantity, Double cop, String remark, Double totalAmount, Long stockAvailable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.stockAvailable = stockAvailable;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Long batchId,
			String batchNo, Date expiryDate, Integer loanOutQuantity, Double cop, String remark, Double totalAmount,
			Long stockAvailable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.stockAvailable = stockAvailable;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uom, Integer uomUnitId, String uomUnit, String batchNo, Date expiryDate, Integer loanOutQuantity,
			Double cop, String remark, Double totalAmount) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponseItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo,
			Date expiryDate, Integer loanOutQuantity, Double cop, String remark, Double totalAmount) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.loanOutQuantity = loanOutQuantity;
		this.cop = cop;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

}
