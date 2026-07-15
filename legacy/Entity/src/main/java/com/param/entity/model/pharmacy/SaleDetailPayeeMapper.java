package com.param.entity.model.pharmacy;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.TaxComponent;

@Entity(name = "SaleDetailPayeeMapper")
@Table(name = "t_sale_detail_payee_mapper", schema = "pharmacy")
@SequenceGenerator(name = "t_sale_detail_payee_mapper_seq", sequenceName = "pharmacy.t_sale_detail_payee_mapper_seq", allocationSize = 1)
public class SaleDetailPayeeMapper {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sale_detail_payee_mapper_seq")
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="contract_id")
	private Integer contractId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="payee_amount")
	private BigDecimal payeeAmount;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="quantity")
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_sale_details_id")
	private SaleDetail saleDetail;
	
	@Column(name="associate_company_id")
	private Integer associateCompanyId;
	
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SaleDetailPayeeMapper() {
	}
	
	public SaleDetailPayeeMapper(Integer itemId,Integer contractId,Integer payeeId,BigDecimal payeeAmount ,SaleDetail saleDetail) {
		super();
		this.itemId = itemId;
		this.contractId = contractId;
		this.payeeId = payeeId;
		this.payeeAmount = payeeAmount;
		this.saleDetail = saleDetail;
	}


	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getPayeeAmount() {
		return payeeAmount;
	}

	public void setPayeeAmount(BigDecimal payeeAmount) {
		this.payeeAmount = payeeAmount;
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

	public SaleDetail getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(SaleDetail saleDetail) {
		this.saleDetail = saleDetail;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
}
