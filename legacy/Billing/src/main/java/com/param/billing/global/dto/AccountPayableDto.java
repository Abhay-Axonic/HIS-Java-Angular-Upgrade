package com.param.billing.global.dto;

import java.util.Date;

public class AccountPayableDto {

	private Integer acc_pay_id;
	private Integer acc_payable_id;
	private Integer dept_id;
	private Integer sub_dept_id;
	private Integer service_id;
	private double charges_applicable;
	private Integer created_by;
	private Date created_date;
	private Integer updated_by;
	private Integer organization_id;
	private Integer unit_id;
	
	public Integer getAcc_pay_id() {
		return acc_pay_id;
	}
	
	public void setAcc_pay_id(Integer acc_pay_id) {
		this.acc_pay_id = acc_pay_id;
	}
	
	public Integer getAcc_payable_id() {
		return acc_payable_id;
	}
	
	public void setAcc_payable_id(Integer acc_payable_id) {
		this.acc_payable_id = acc_payable_id;
	}
	
	public Integer getDept_id() {
		return dept_id;
	}
	
	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}
	
	public Integer getSub_dept_id() {
		return sub_dept_id;
	}
	
	public void setSub_dept_id(Integer sub_dept_id) {
		this.sub_dept_id = sub_dept_id;
	}
	
	public Integer getService_id() {
		return service_id;
	}
	
	public void setService_id(Integer service_id) {
		this.service_id = service_id;
	}
	
	public double getCharges_applicable() {
		return charges_applicable;
	}
	
	public void setCharges_applicable(double charges_applicable) {
		this.charges_applicable = charges_applicable;
	}
	
	public Integer getCreated_by() {
		return created_by;
	}
	
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	
	public Date getCreated_date() {
		return created_date;
	}
	
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	
	public Integer getUpdated_by() {
		return updated_by;
	}
	
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	
	public Integer getOrganization_id() {
		return organization_id;
	}
	
	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}
	
	public Integer getUnit_id() {
		return unit_id;
	}
	
	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}
	
}
