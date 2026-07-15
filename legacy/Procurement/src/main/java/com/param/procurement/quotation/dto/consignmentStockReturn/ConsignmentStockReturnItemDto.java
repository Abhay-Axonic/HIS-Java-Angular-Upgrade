package com.param.procurement.quotation.dto.consignmentStockReturn;


import java.math.BigInteger;

/*create by Minesh*/

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.entity.model.inventory.Batch;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.procurement.quotation.dto.physicalInspection.GrnUomMapperResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ConsignmentStockReturnItemDto {
	
		private Integer id; /* grnDetailID */

		private Integer itemId;

		private String itemCode;

		private String itemName;

		private String batchNo;

		private Integer grnApprovedQuantity;

		private Integer grnUomTypeId;

		private String grnUomType;

		private Integer grnUomUnitId;

		private String grnUomUnit;

		private List<GrnUomMapperResponse> grnUom;

		private Integer bonusApprovedQuantity;

		private Integer bonusUomTypeId;

		private String bonusUomType;

		private Integer bonusUomUnitId;

		private String bonusUomUnit;

		private Integer approvedQuantity;

		private Integer rejectQuantity;
		
		private Integer returnQuantity;
		private BigInteger consumedQty;

		

		public BigInteger getConsumedQty() {
			return consumedQty;
		}

		public void setConsumedQty(BigInteger consumedQty) {
			this.consumedQty = consumedQty;
		}

		public List<BatchResponse> getBatchRes() {
			return batchRes;
		}

		public void setBatchRes(List<BatchResponse> batchRes) {
			this.batchRes = batchRes;
		}

		private Double cop;

		private Double mrp;

		private Double totalAmount;

		private String specifications;

		private String remark;

		private Integer receivedQuantity;

		private Integer receivedBonusQuantity;

		private String lotNo;

		private Date expiryDate;

		private String manufacturerName;

		private String receivedItemSpecification;

		private Integer statusId;

		private String status;

		/* added by Minesh */

		private Integer totalConsignmentStock;
		private Integer availableConsignmentStock;
		private BigInteger convertedConsignmentStock;
		//private BigInteger	storeStockTransfered;
		private Integer conversionQuantity;
		private Integer availableConvertedStock;
		private Integer returnedQuantity;
		private Long batchId;
		private List<Batch> batch;
		private List<BatchResponse> batchRes;
		private BigInteger storeStockTransfered;
		private Integer grnDetailId;
		private Integer totalReturnedQuantity;
		private BigInteger totalPOCreatedQuantity;
		/*List<UomResponse> batchUomList;
		private Integer batchUomTypeId;*/

		public BigInteger getStoreStockTransfered() {
			return storeStockTransfered;
		}

		public void setStoreStockTransfered(BigInteger storeStockTransfered) {
			this.storeStockTransfered = storeStockTransfered;
		}

		public Integer getTotalConsignmentStock() {
			return totalConsignmentStock;
		}

		public void setTotalConsignmentStock(Integer totalConsignmentStock) {
			this.totalConsignmentStock = totalConsignmentStock;
		}

		public Integer getAvailableConsignmentStock() {
			return availableConsignmentStock;
		}

		public void setAvailableConsignmentStock(Integer availableConsignmentStock) {
			this.availableConsignmentStock = availableConsignmentStock;
		}

	

		public BigInteger getConvertedConsignmentStock() {
			return convertedConsignmentStock;
		}

		public void setConvertedConsignmentStock(BigInteger convertedConsignmentStock) {
			this.convertedConsignmentStock = convertedConsignmentStock;
		}

		public Integer getConversionQuantity() {
			return conversionQuantity;
		}

		public void setConversionQuantity(Integer conversionQuantity) {
			this.conversionQuantity = conversionQuantity;
		}

		public Integer getAvailableConvertedStock() {
			return availableConvertedStock;
		}

		public void setAvailableConvertedStock(Integer availableConvertedStock) {
			this.availableConvertedStock = availableConvertedStock;
		}
		
		public Integer getReturnedQuantity() {
			return returnedQuantity;
		}

		public void setReturnedQuantity(Integer returnedQuantity) {
			this.returnedQuantity = returnedQuantity;
		}

		public Long getBatchId() {
			return batchId;
		}

		public void setBatchId(Long batchId) {
			this.batchId = batchId;
		}

		public List<Batch> getBatch() {
			return batch;
		}

		public void setBatch(List<Batch> batchLists) {
			this.batch = batchLists;
		}
		
		/*public List<UomResponse> getBatchUomList() {
			return batchUomList;
		}

		public void setBatchUomList(List<UomResponse> batchUomList) {
			this.batchUomList = batchUomList;
		}

		public Integer getBatchUomTypeId() {
			return batchUomTypeId;
		}

		public void setBatchUomTypeId(Integer batchUomTypeId) {
			this.batchUomTypeId = batchUomTypeId;
		}*/

		/* end added by Minesh */

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

		public Integer getBonusUomTypeId() {
			return bonusUomTypeId;
		}

		public void setBonusUomTypeId(Integer bonusUomTypeId) {
			this.bonusUomTypeId = bonusUomTypeId;
		}

		public String getBonusUomType() {
			return bonusUomType;
		}

		public void setBonusUomType(String bonusUomType) {
			this.bonusUomType = bonusUomType;
		}

		public Integer getBonusUomUnitId() {
			return bonusUomUnitId;
		}

		public void setBonusUomUnitId(Integer bonusUomUnitId) {
			this.bonusUomUnitId = bonusUomUnitId;
		}

		public String getBonusUomUnit() {
			return bonusUomUnit;
		}

		public void setBonusUomUnit(String bonusUomUnit) {
			this.bonusUomUnit = bonusUomUnit;
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

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public String getSpecifications() {
			return specifications;
		}

		public void setSpecifications(String specifications) {
			this.specifications = specifications;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Integer getGrnUomTypeId() {
			return grnUomTypeId;
		}

		public void setGrnUomTypeId(Integer grnUomTypeId) {
			this.grnUomTypeId = grnUomTypeId;
		}

		public String getGrnUomType() {
			return grnUomType;
		}

		public void setGrnUomType(String grnUomType) {
			this.grnUomType = grnUomType;
		}

		public Integer getGrnUomUnitId() {
			return grnUomUnitId;
		}

		public void setGrnUomUnitId(Integer grnUomUnitId) {
			this.grnUomUnitId = grnUomUnitId;
		}

		public String getGrnUomUnit() {
			return grnUomUnit;
		}

		public void setGrnUomUnit(String grnUomUnit) {
			this.grnUomUnit = grnUomUnit;
		}

		public List<GrnUomMapperResponse> getGrnUom() {
			return grnUom;
		}

		public void setGrnUom(List<GrnUomMapperResponse> grnUom) {
			this.grnUom = grnUom;
		}

		public String getBatchNo() {
			return batchNo;
		}

		public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}

		public String getLotNo() {
			return lotNo;
		}

		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}

		public Date getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}

		public String getManufacturerName() {
			return manufacturerName;
		}

		public void setManufacturerName(String manufacturerName) {
			this.manufacturerName = manufacturerName;
		}

		public String getReceivedItemSpecification() {
			return receivedItemSpecification;
		}

		public void setReceivedItemSpecification(String receivedItemSpecification) {
			this.receivedItemSpecification = receivedItemSpecification;
		}

		public Integer getReceivedQuantity() {
			return receivedQuantity;
		}

		public void setReceivedQuantity(Integer receivedQuantity) {
			this.receivedQuantity = receivedQuantity;
		}

		public Integer getReceivedBonusQuantity() {
			return receivedBonusQuantity;
		}

		public void setReceivedBonusQuantity(Integer receivedBonusQuantity) {
			this.receivedBonusQuantity = receivedBonusQuantity;
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

		public Integer getApprovedQuantity() {
			return approvedQuantity;
		}

		public void setApprovedQuantity(Integer approvedQuantity) {
			this.approvedQuantity = approvedQuantity;
		}

		public Integer getRejectQuantity() {
			return rejectQuantity;
		}

		public void setRejectQuantity(Integer rejectQuantity) {
			this.rejectQuantity = rejectQuantity;
		}

		public Integer getReturnQuantity() {
			return returnQuantity;
		}

		public void setReturnQuantity(Integer returnQuantity) {
			this.returnQuantity = returnQuantity;
		}
		
		public Integer getGrnDetailId() {
			return grnDetailId;
		}

		public void setGrnDetailId(Integer grnDetailId) {
			this.grnDetailId = grnDetailId;
		}

		public Integer getTotalReturnedQuantity() {
			return totalReturnedQuantity;
		}

		public void setTotalReturnedQuantity(Integer totalReturnedQuantity) {
			this.totalReturnedQuantity = totalReturnedQuantity;
		}		

		public BigInteger getTotalPOCreatedQuantity() {
			return totalPOCreatedQuantity;
		}

		public void setTotalPOCreatedQuantity(BigInteger totalPOCreatedQuantity) {
			this.totalPOCreatedQuantity = totalPOCreatedQuantity;
		}

		public ConsignmentStockReturnItemDto() {
			super();
		}

		public ConsignmentStockReturnItemDto(Integer id) {
			super();
			this.id = id;
		}
		
		public ConsignmentStockReturnItemDto(Integer id, Integer itemId, String itemCode, String itemName, String batchNo,
				 Double cop, Double mrp,Integer returnQuantity, String remark, String lotNo, Date expiryDate, String manufacturerName) {
			super();
			this.id = id;
			this.itemId = itemId;
			this.itemCode = itemCode;
			this.itemName = itemName;
			this.batchNo = batchNo;
			this.cop = cop;
			this.mrp = mrp;
			this.returnQuantity=returnQuantity;
			this.remark = remark;
			this.lotNo = lotNo;
			this.expiryDate = expiryDate;
			this.manufacturerName = manufacturerName;
		}
		
		public ConsignmentStockReturnItemDto(Integer id, Integer itemId, String itemCode, String itemName,Long batchId, String batchNo, String lotNo,
				 Double cop, Double mrp,Integer returnQuantity, String remark, Date expiryDate, String manufacturerName) {
			super();
			this.id = id;
			this.itemId = itemId;
			this.itemCode = itemCode;
			this.itemName = itemName;
			this.batchId = batchId;
			this.batchNo = batchNo;
			this.lotNo = lotNo;
			this.cop = cop;
			this.mrp = mrp;
			this.returnQuantity=returnQuantity;
			this.remark = remark;
			this.expiryDate = expiryDate;
			this.manufacturerName = manufacturerName;
		}

		public ConsignmentStockReturnItemDto(String itemCode, String itemName, String batchNo) {
			super();
			this.itemCode = itemCode;
			this.itemName = itemName;
			this.batchNo = batchNo;
		}

		
	

}
