package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DrugAdministrationDropdown
{
	private List<Reason> reasons;

	public List<Reason> getReasons() {
		return reasons;
	}

	public void setReasons(List<Reason> reasons) {
		this.reasons = reasons;
	}
	
	public void addReason(Reason reasons) {
		if (this.reasons == null)
			this.reasons = new ArrayList<Reason>();
		this.reasons.add(reasons);
	}

}