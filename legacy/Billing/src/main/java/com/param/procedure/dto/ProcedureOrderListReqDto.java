package com.param.procedure.dto;

public class ProcedureOrderListReqDto {
	
	private String fromDate;
	private String toDate;
	private int visitTypeId;
	private int orderStatusId;
	private int orgId;
	private int unitId;
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public int getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(int visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public int getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(int orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	 

}
