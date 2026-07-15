package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PrescriptionDetailsList {
	private Integer prescriptionDetailId;
	private Integer itemId;
	private Integer handOverQty;

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getHandOverQty() {
		return handOverQty;
	}

	public void setHandOverQty(Integer handOverQty) {
		this.handOverQty = handOverQty;
	}


}
