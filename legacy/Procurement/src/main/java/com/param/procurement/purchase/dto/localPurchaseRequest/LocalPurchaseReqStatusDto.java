package com.param.procurement.purchase.dto.localPurchaseRequest;

import java.math.BigInteger;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class LocalPurchaseReqStatusDto extends CookieObjectDto{
	
	private List<Long> prId;
	
	private Integer statusId;
	
	private Integer approvalStatusId;

	public List<Long> getPrId() {
		return prId;
	}

	public void setPrId(List<Long> prId) {
		this.prId = prId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}
	
	

}
