package com.param.procurement.workOrder.dto.workOrderNegotiation;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WONegotiationRequest extends CookieObjectDto {
	
	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;

	private Integer statusId;

	private Integer vendorQuotationId;
	
	private Integer woQuotationId;
	
	private List<WONegotiationDetailRequest> items;
	
	private List<Integer> negoIds;

	public WONegotiationRequest() {
		super();
	}
	
	
	public WONegotiationRequest(Integer id, Integer statusId) {
		super();
		this.id = id;
		this.statusId = statusId;
	}

	public WONegotiationRequest(Integer id) {
		this.id = id;
	}
	
	/*public WONegotiationRequest(Integer id,Integer statusId) {
		super();
		this.id = id;
		this.statusId = statusId;
	}*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getVendorQuotationId() {
		return vendorQuotationId;
	}

	public void setVendorQuotationId(Integer vendorQuotationId) {
		this.vendorQuotationId = vendorQuotationId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<WONegotiationDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WONegotiationDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getNegoIds() {
		return negoIds;
	}

	public void setNegoIds(List<Integer> negoIds) {
		this.negoIds = negoIds;
	}


	public Integer getWoQuotationId() {
		return woQuotationId;
	}


	public void setWoQuotationId(Integer woQuotationId) {
		this.woQuotationId = woQuotationId;
	}
	
	
	
}
