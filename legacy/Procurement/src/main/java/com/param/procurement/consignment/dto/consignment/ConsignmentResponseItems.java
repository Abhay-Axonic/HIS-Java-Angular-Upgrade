package com.param.procurement.consignment.dto.consignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.param.global.dto.items.UomResponse;

public class ConsignmentResponseItems extends ConsignmentRequestItems {

	protected String itemCode;

	protected String itemName;

	protected String uomType;

	protected String uomUnit;
	
	/* public void addBatches(BatchResponse batch)
	{
		if (this.batch == null)
			this.batch = new ArrayList<BatchResponse>();

		this.batch.add(batch);
	}*/
	protected List<UomResponse> uomList;
	
	private Long availableQuantity;

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

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public List<UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}

	public ConsignmentResponseItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalAmount, Double totalDiscount,
			Double discountPercentage, Double totalTaxAmount, Double totalNetAmount, Integer taxId,
			Double taxPercentage, String itemCode, String itemName, String uomType, String uomUnit)
	{
		super(id, itemId, uomTypeId, uomUnitId, batchNo, expiryDate, quantity, cop, totalAmount, totalDiscount,
				discountPercentage, totalTaxAmount, totalNetAmount, taxId, taxPercentage);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}
	
	public ConsignmentResponseItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalAmount, Double totalDiscount,
			Double discountPercentage, Double totalTaxAmount, Double totalNetAmount, Integer taxId,
			Double taxPercentage, String itemCode, String itemName, String uomType, String uomUnit, Double lcop)
	{
		super(id, itemId, uomTypeId, uomUnitId, batchNo, expiryDate, quantity, cop, totalAmount, totalDiscount,
				discountPercentage, totalTaxAmount, totalNetAmount, taxId, taxPercentage );
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.lcop = lcop;
	}
	
	public ConsignmentResponseItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalDiscount,
			Double discountPercentage, Double totalTaxAmount, Double totalNetAmount, Integer taxId,
			Double taxPercentage, String itemCode, String itemName, String uomType, String uomUnit)
	{
		super(id, itemId, uomTypeId, uomUnitId, batchNo, expiryDate, quantity, cop, totalDiscount,
				discountPercentage, totalTaxAmount, totalNetAmount, taxId, taxPercentage);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public ConsignmentResponseItems(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiry, Integer quantity, double cop,
			double totalAmount, Integer taxId, double taxPercentage, double taxAmount, double netAmount,Long availableQuantity)
	{
		super(id, itemId, uomTypeId, uomUnitId,batchId, batchNo,expiry,quantity,cop,totalAmount,taxId,taxPercentage,taxAmount,netAmount,availableQuantity);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.availableQuantity=availableQuantity;
	}
	
	public ConsignmentResponseItems(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiry, Integer quantity, double cop,
			double totalAmount, Integer taxId, double taxPercentage, double taxAmount, double netAmount,Long availableQuantity , Double lcop)
	{
		super(id, itemId, uomTypeId, uomUnitId,batchId, batchNo,expiry,quantity,cop,totalAmount,taxId,taxPercentage,taxAmount,netAmount,availableQuantity);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.availableQuantity=availableQuantity;
		this.lcop = lcop;
	}
	
	public void addBatches(List<UomResponse> populateUom)
	{
		if (this.uomList == null)
			this.uomList = new ArrayList<UomResponse>();
		this.uomList.addAll(populateUom);
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	}
