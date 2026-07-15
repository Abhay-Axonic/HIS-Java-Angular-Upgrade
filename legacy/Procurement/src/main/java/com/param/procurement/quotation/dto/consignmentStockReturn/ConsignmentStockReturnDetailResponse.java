package com.param.procurement.quotation.dto.consignmentStockReturn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNOtherChargesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnAccessoriesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.PurchasedOrderResponse;
import com.param.service.global.model.CookieObjectDto;

public class ConsignmentStockReturnDetailResponse extends CookieObjectDto {
	
	//use for saving
	protected String itemName;
	protected String itemCode;
	protected String batchNo;	
	protected Double cop;	
	protected Date expiryDate;	
	protected Integer grnUomTypeId;	
	protected Integer grnUomUnitId;	
	protected String lotNumber;	
	protected Double mrp;	
	protected Double totalAmount;	
	protected String remark;	
	protected Integer statusId;		
	protected String grnUomType;
	protected String grnUomUnit;	
	private Integer assetTypeId;	
	private Integer itemId;	
	private Integer storeId;
	private String lotNo;
	private String specification;
	private Integer consignmentReturnedQuantity;
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	//end for save 
	private Integer totalConsignmentStock;
	private BigInteger  consumedQty;
	private BigInteger  convertedConsignmentStock;
	private BigInteger  returnedConsignmentStock;
	private BigInteger storeStockTransfered;	
	private Integer approvedQuantity;
	private Integer poApprovedQuantity;
	
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public BigInteger getStoreStockTransfered() {
		return storeStockTransfered;
	}

	public void setStoreStockTransfered(BigInteger storeStockTransfered) {
		this.storeStockTransfered = storeStockTransfered;
	}

	private Integer availableStockforReturn;
	private Integer availableConsignmentStock;
	//protected Integer  batchId;
	protected Integer returnQuantity;
	//protected Long batchId;

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	

	public Integer getAvailableConsignmentStock() {
		return availableConsignmentStock;
	}

	public void setAvailableConsignmentStock(Integer availableConsignmentStock) {
		this.availableConsignmentStock = availableConsignmentStock;
	}

	public BigInteger getReturnedConsignmentStock() {
		return returnedConsignmentStock;
	}

	public void setReturnedConsignmentStock(BigInteger returnedConsignmentStock) {
		this.returnedConsignmentStock = returnedConsignmentStock;
	}

	public Integer getTotalConsignmentStock() {
		return totalConsignmentStock;
	}

	public void setTotalConsignmentStock(Integer totalConsignmentStock) {
		this.totalConsignmentStock = totalConsignmentStock;
	}

	public BigInteger getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(BigInteger conQty) {
		this.consumedQty = conQty;
	}

	public BigInteger getConvertedConsignmentStock() {
		return convertedConsignmentStock;
	}

	public void setConvertedConsignmentStock(BigInteger convertedConsignmentStock2) {
		this.convertedConsignmentStock = convertedConsignmentStock2;
	}

	/*public Integer getReturnedConsignmentStock() {
		return returnedConsignmentStock;
	}

	public void setReturnedConsignmentStock(Integer returnedConsignmentStock) {
		this.returnedConsignmentStock = returnedConsignmentStock;
	}
*/
	public Integer getAvailableStockforReturn() {
		return availableStockforReturn;
	}

	public void setAvailableStockforReturn(Integer availableStockforReturn) {
		this.availableStockforReturn = availableStockforReturn;
	}

	//for response to get grn detail
	protected Double netAmount;
	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	protected Integer grnReceivedQuantity;
	protected Integer goodReceiptNoteDetailId;
	protected Integer manufacturerId;
	protected String manufacturerName;
	protected String taxCode;
	protected Integer taxId;
	protected Double taxPercentage;
	protected Double taxAmount;
	protected String status;
	protected Integer leaseQuantity;
	//for end response to get grn detail
	// common use 	
	protected List<GRNUomMapper> grnUom;
	protected List<UomResponse> grnUomRes;

	public List<UomResponse> getGrnUomRes() {
		return grnUomRes;
	}

	public void setGrnUomRes(List<UomResponse> grnUomRes) {
		this.grnUomRes = grnUomRes;
	}

	protected List<GRNOtherChargesMapper> otherCharges;

	protected List<UomResponse> uom;
	
	/* added by minesh for consignment return */
	
	private Integer id;//item pk id
	private Long batchId;	
	private Integer availableBatchQuantity;	
	private Integer returnBatchQuantity;
	protected List<UomResponse> batchUomBP;
	
	// for detail consignmentStockReturn
	private Integer consignmentStockReturnId;
	private String storeName;
	private String crNumber;
	private String assetType;
	private String vendorName;
	private List<ConsignmentStockReturnItemDto> items;
	
	//for approved po history
	BigInteger totalPOCreatedQuantity;

	public BigInteger getTotalPOCreatedQuantity() {
		return totalPOCreatedQuantity;
	}

	public void setTotalPOCreatedQuantity(BigInteger totalPOCreatedQuantity) {
		this.totalPOCreatedQuantity = totalPOCreatedQuantity;
	}	
	//end approved po history	

	public Integer getConsignmentStockReturnId() {
		return consignmentStockReturnId;
	}

	public void setConsignmentStockReturnId(Integer consignmentStockReturnId) {
		this.consignmentStockReturnId = consignmentStockReturnId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCrNumber() {
		return crNumber;
	}

	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<ConsignmentStockReturnItemDto> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentStockReturnItemDto> items) {
		this.items = items;
	}
	
	// end detail consignmentStockReturn

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getAvailableBatchQuantity() {
		return availableBatchQuantity;
	}
	
	public void setAvailableBatchQuantity(Integer availableBatchQuantity) {
		this.availableBatchQuantity = availableBatchQuantity;
	}
	
	public Integer getReturnBatchQuantity() {
		return returnBatchQuantity;
	}

	public void setReturnBatchQuantity(Integer returnBatchQuantity) {
		this.returnBatchQuantity = returnBatchQuantity;
	}

	public List<UomResponse> getBatchUomBP() {
		return batchUomBP;
	}

	public void setBatchUomBP(List<UomResponse> batchUomBP) {
		this.batchUomBP = batchUomBP;
	}
	
	/* added by minesh for consignment return */

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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
	
	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	
	
	public String getGrnUomType() {
		return grnUomType;
	}

	public void setGrnUomType(String grnUomType) {
		this.grnUomType = grnUomType;
	}

	public String getGrnUomUnit() {
		return grnUomUnit;
	}

	public void setGrnUomUnit(String grnUomUnit) {
		this.grnUomUnit = grnUomUnit;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GRNUomMapper> getGrnUom() {
		return grnUom;
	}

	public void setGrnUom(List<GRNUomMapper> grnUom) {
		this.grnUom = grnUom;
	}

	public List<GRNOtherChargesMapper> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<GRNOtherChargesMapper> otherCharges) {
		this.otherCharges = otherCharges;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}
	
	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getGrnReceivedQuantity() {
		return grnReceivedQuantity;
	}

	public void setGrnReceivedQuantity(Integer grnReceivedQuantity) {
		this.grnReceivedQuantity = grnReceivedQuantity;
	}

	public Integer getGoodReceiptNoteDetailId() {
		return goodReceiptNoteDetailId;
	}

	public void setGoodReceiptNoteDetailId(Integer goodReceiptNoteDetailId) {
		this.goodReceiptNoteDetailId = goodReceiptNoteDetailId;
	}
	
	public ConsignmentStockReturnDetailResponse() {
		super();
	}

	public ConsignmentStockReturnDetailResponse(Integer consignmentStockReturnId, String crNumber, String storeName, String assetType, String vendorName,List<ConsignmentStockReturnItemDto> items, Integer storeId) {
		this.id=consignmentStockReturnId;
		this.crNumber=crNumber;
		this.storeName=storeName;
		this.assetType=assetType;
		this.vendorName=vendorName;
		this.items=items;
		this.storeId= storeId;
	}

	public ConsignmentStockReturnDetailResponse(Integer consignmentStockReturnId, String crNumber, String storeName, String assetType, String vendorName,List<ConsignmentStockReturnItemDto> items, Integer storeId,Integer statusId,String status) {
		this.id=consignmentStockReturnId;
		this.crNumber=crNumber;
		this.storeName=storeName;
		this.assetType=assetType;
		this.vendorName=vendorName;
		this.items=items;
		this.storeId= storeId;
		this.statusId= statusId;
		this.status= status;
	}

	public Integer getConsignmentReturnedQuantity() {
		return consignmentReturnedQuantity;
	}

	public void setConsignmentReturnedQuantity(Integer consignmentReturnedQuantity) {
		this.consignmentReturnedQuantity = consignmentReturnedQuantity;
	}

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}
}
