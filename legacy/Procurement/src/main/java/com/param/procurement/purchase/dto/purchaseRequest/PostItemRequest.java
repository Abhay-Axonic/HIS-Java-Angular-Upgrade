package com.param.procurement.purchase.dto.purchaseRequest;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostItemRequest {
	
	
		private Integer id;
		
		@NotNull
	    private Integer itemId;
		
	    private Integer uomTypeId;
	    private Integer uomUnitId;
	    
	    @NotNull
	    private Integer quantity;
	    
	    private Double cop ;	
		private Double amountCop;
		
	    private String specification;
	    
	    private Integer indentQuantity;	    
	    private List<Integer> indentId;
	    
	    @NotNull
	    private Integer statusId;
	    
	    
	    
	    
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
		public Double getAmountCop() {
			return amountCop;
		}
		public void setAmountCop(Double amountCop) {
			this.amountCop = amountCop;
		}
		public String getSpecification() {
			return specification;
		}
		public void setSpecification(String specification) {
			this.specification = specification;
		}
		public Integer getIndentQuantity() {
			return indentQuantity;
		}
		public void setIndentQuantity(Integer indentQuantity) {
			this.indentQuantity = indentQuantity;
		}
		public List<Integer> getIndentId() {
			return indentId;
		}
		public void setIndentId(List<Integer> indentId) {
			this.indentId = indentId;
		}
		public Integer getStatusId() {
			return statusId;
		}
		public void setStatusId(Integer statusId) {
			this.statusId = statusId;
		}
	    
	    
	    

}
