package com.param.procurement.workOrder.dto.workOrderQuotationVendor;

import java.util.Date;

public class WorkOrderQRDto {
	
	private Integer id;
	
	private String qrNumber;
	
	private Date qrDate;
	

	public WorkOrderQRDto() {
		super();
	}
	
	public WorkOrderQRDto(Integer id, String qrNumber, Date qrDate) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

}
