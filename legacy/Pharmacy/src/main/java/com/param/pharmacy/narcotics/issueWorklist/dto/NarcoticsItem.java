package com.param.pharmacy.narcotics.issueWorklist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;

@JsonInclude(Include.NON_NULL)
public class NarcoticsItem
{
			private Integer id;
			
			private Integer orderDetailId;
			
			private Integer itemId;
			
            private String itemCode;
            
            private String itemName;
            
            private Integer genericId;
            
            private String genericName;
            
            private boolean interventionFlag;
            
            private Integer quantityOrdered;
            
            private Integer quantityIssued;
            
            private Integer manufacturerId;
            
            private String manufacturerName;
            
            private List<BatchResponse> batch;
            
            private Integer taxId;
            
            private String tax;
            
            private Double taxPercentage;

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

			public Integer getOrderDetailId() {
				return orderDetailId;
			}

			public void setOrderDetailId(Integer orderDetailId) {
				this.orderDetailId = orderDetailId;
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

			public Integer getGenericId() {
				return genericId;
			}

			public void setGenericId(Integer genericId) {
				this.genericId = genericId;
			}

			public String getGenericName() {
				return genericName;
			}

			public void setGenericName(String genericName) {
				this.genericName = genericName;
			}

			public boolean isInterventionFlag() {
				return interventionFlag;
			}

			public void setInterventionFlag(boolean interventionFlag) {
				this.interventionFlag = interventionFlag;
			}

			public Integer getQuantityOrdered() {
				return quantityOrdered;
			}

			public void setQuantityOrdered(Integer quantityOrdered) {
				this.quantityOrdered = quantityOrdered;
			}

			public Integer getQuantityIssued() {
				return quantityIssued;
			}

			public void setQuantityIssued(Integer quantityIssued) {
				this.quantityIssued = quantityIssued;
			}

			public Integer getManufacturerId() {
				return manufacturerId;
			}

			public void setManufacturerId(Integer manufacturerId) {
				this.manufacturerId = manufacturerId;
			}

			public String getManufacturerName() {
				return manufacturerName;
			}

			public void setManufacturerName(String manufacturerName) {
				this.manufacturerName = manufacturerName;
			}

			public List<BatchResponse> getBatch() {
				return batch;
			}

			public void setBatch(List<BatchResponse> batch) {
				this.batch = batch;
			}

			public Integer getTaxId() {
				return taxId;
			}

			public void setTaxId(Integer taxId) {
				this.taxId = taxId;
			}

			public String getTax() {
				return tax;
			}

			public void setTax(String tax) {
				this.tax = tax;
			}

			public Double getTaxPercentage() {
				return taxPercentage;
			}

			public void setTaxPercentage(Double taxPercentage) {
				this.taxPercentage = taxPercentage;
			}

			public NarcoticsItem(Integer id, Integer orderDetailId, Integer itemId, String itemCode, String itemName,
					Integer genericId, String genericName, boolean interventionFlag, Integer quantityOrdered,
					Integer quantityIssued, Integer manufacturerId, String manufacturerName, List<BatchResponse> batch,
					Integer taxId, String tax, Double taxPercentage) {
				super();
				this.id = id;
				this.orderDetailId = orderDetailId;
				this.itemId = itemId;
				this.itemCode = itemCode;
				this.itemName = itemName;
				this.genericId = genericId;
				this.genericName = genericName;
				this.interventionFlag = interventionFlag;
				this.quantityOrdered = quantityOrdered;
				this.quantityIssued = quantityIssued;
				this.manufacturerId = manufacturerId;
				this.manufacturerName = manufacturerName;
				this.batch = batch;
				this.taxId = taxId;
				this.tax = tax;
				this.taxPercentage = taxPercentage;
			}
            
}