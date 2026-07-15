package com.param.billing.global.transaction.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_bill_details_package_mapper",schema = "billing")
public class BillDetailsPackageMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="bill_details_id")
	private Integer billDetailsId;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	@Column(name="package_id")
	private Integer packageId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="is_package_service")
	private char isPackageService;
	
	@Column(name="is_cap_service")
	private char isCapService;
	
	@Column(name="package_amount")
	private BigDecimal packageAmount;	
	
	@Column(name="due_amount")
	private BigDecimal dueAmount;
	
	@Column(name="t_sale_details_id")
	private Integer saleDetailsId;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bill_details_id", insertable = false, nullable = false, updatable = false)
	private BillingDetails billingDetails;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillDetailsId() {
		return billDetailsId;
	}

	public void setBillDetailsId(Integer billDetailsId) {
		this.billDetailsId = billDetailsId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}


	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}


	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = (isPackageService == '\u0000') ? 'N' : isPackageService;
	}

	public char getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = (isCapService == '\u0000') ? 'N' : isCapService;
	}

	public BigDecimal getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(BigDecimal packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
		


		
}
