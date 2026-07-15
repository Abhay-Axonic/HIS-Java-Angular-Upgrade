package com.param.pharmacy.op.dto.billList;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.SalesType;
import com.param.global.dto.global.Status;

public class BillListDropdownResponse {
	protected List<SalesType> salesType;

	protected List<Status> status;

	public BillListDropdownResponse() {
		super();
	}

	public List<SalesType> getSalesType() {
		return salesType;
	}

	public void setSalesType(List<SalesType> salesType) {
		this.salesType = salesType;
	}

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
	
	public void addSalesType(SalesType salesType) {
		if (this.salesType == null)
			this.salesType = new ArrayList<SalesType>();

		this.salesType.add(salesType);
	}
}