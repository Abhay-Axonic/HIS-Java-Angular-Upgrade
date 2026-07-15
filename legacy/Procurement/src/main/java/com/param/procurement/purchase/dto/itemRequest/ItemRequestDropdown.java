package com.param.procurement.purchase.dto.itemRequest;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.Status;

public class ItemRequestDropdown {
	
	List<Status> status;

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();

		this.status.add(status);
	}
}
