package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.Date;

public class QrDateDto {
	private Integer id;

	private String qrno;

	private Date qrdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQrno() {
		return qrno;
	}

	public void setQrno(String qrno) {
		this.qrno = qrno;
	}

	public Date getQrdate() {
		return qrdate;
	}

	public void setQrdate(Date qrdate) {
		this.qrdate = qrdate;
	}
}
