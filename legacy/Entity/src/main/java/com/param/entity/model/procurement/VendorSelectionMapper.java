package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Vendor;

@Entity
@Table(name="t_vendor_selection_mapper", schema = "procurement")
public class VendorSelectionMapper extends BaseEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="is_accept")
	private Boolean isAccept;
	
	@Column(name="warranty_period")
	private Boolean warrantyPeriod;
	
	@Column(name="support_term")
	private Boolean supportTerm;

	@Column(name="technical_specification")
	private Boolean technicalSpecification;

	@Column(name="expected_payment")
	private Boolean expectedPayment;

	private Integer quantity;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_selection_detail_id")
	private VendorSelectionDetail vendorSelectionDetail;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_negociation_mapper_id")
	private VendorNegotiationMapper vendorNegotiationMapper;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	@Column(name="reject_note")
	private String rejectNote;

	public VendorSelectionMapper() {
		super();
	}

	public VendorSelectionMapper(VendorNegotiationMapper vendorNegotiationMapper,Vendor vendor,Boolean isAccept,Boolean warrantyPeriod,Boolean supportTerm,
			Boolean technicalSpecification,Boolean expectedPayment) {
		super();
		this.vendorNegotiationMapper = vendorNegotiationMapper;
		this.vendor = vendor;
		this.isAccept = isAccept;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerm = supportTerm;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
	}
	
	public VendorSelectionMapper(VendorNegotiationMapper vendorNegotiationMapper,Vendor vendor,Boolean isAccept,Boolean warrantyPeriod,Boolean supportTerm,
			Boolean technicalSpecification,Boolean expectedPayment, VendorSelectionDetail vendorSelectionDetail ) {
		super();
		this.vendorNegotiationMapper = vendorNegotiationMapper;
		this.vendor = vendor;
		this.isAccept = isAccept;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerm = supportTerm;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.vendorSelectionDetail = vendorSelectionDetail;
	}
	
	public void updateVendorSelectionMapper(Boolean isAccept, Boolean warrantyPeriod, Boolean supportTerm,
			Boolean technicalSpecification, Boolean expectedPayment, Integer quantity, String remark,
			String rejectNote) {
		this.isAccept = isAccept;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerm = supportTerm;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.quantity = quantity;
		this.remark = remark;
		this.rejectNote = rejectNote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public Boolean getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(Boolean warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public Boolean getSupportTerm() {
		return supportTerm;
	}

	public void setSupportTerm(Boolean supportTerm) {
		this.supportTerm = supportTerm;
	}

	public Boolean getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(Boolean technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public Boolean getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(Boolean expectedPayment) {
		this.expectedPayment = expectedPayment;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public VendorSelectionDetail getVendorSelectionDetail() {
		return vendorSelectionDetail;
	}

	public void setVendorSelectionDetail(VendorSelectionDetail vendorSelectionDetail) {
		this.vendorSelectionDetail = vendorSelectionDetail;
	}

	public VendorNegotiationMapper getVendorNegotiationMapper() {
		return vendorNegotiationMapper;
	}

	public void setVendorNegotiationMapper(VendorNegotiationMapper vendorNegotiationMapper) {
		this.vendorNegotiationMapper = vendorNegotiationMapper;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
}
