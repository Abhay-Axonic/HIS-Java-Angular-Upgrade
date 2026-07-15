package com.param.inventory.store.dto.drugRecall;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.adt.master.global.model.ReasonMaster;

@JsonInclude(Include.NON_NULL)
public class DrugRecallDropdownResponse {

	protected List<ReasonMaster> reason;

	public List<ReasonMaster> getReason() {
		return reason;
	}

	public void setReason(List<ReasonMaster> reason) {
		this.reason = reason;
	}

	public void addReason(ReasonMaster reason) {
		if (this.reason == null)
			this.reason = new ArrayList<ReasonMaster>();

		this.reason.add(reason);

	}
}
