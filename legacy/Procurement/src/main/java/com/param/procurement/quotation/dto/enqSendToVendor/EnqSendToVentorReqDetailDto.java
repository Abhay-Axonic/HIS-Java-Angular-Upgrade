package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.param.entity.model.master.Status;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;

public class EnqSendToVentorReqDetailDto {
	
    private Integer id;
    private Integer vendorId;
	private Integer itemId;
	
	private Integer statusId;

	private Integer quantity;

	private Integer uomTypeId;

	private Integer uomUnitId;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	private List<Integer> prId;	

	private Integer qrDetailId;
	

	public EnqSendToVentorReqDetailDto() {
		super();
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public List<Integer> getPrId() {
		return prId;
	}

	public void setPrId(List<Integer> prId) {
		this.prId = prId;
	}

	public Integer getQrDetailId() {
		return qrDetailId;
	}

	public void setQrDetailId(Integer qrDetailId) {
		this.qrDetailId = qrDetailId;
	}
	
	

}
