package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;

/**
 * The persistent class for the t_vendor_negociation_mapper database table.
 * 
 */
@Entity
@Table(name="t_vendor_negociation_mapper", schema = "procurement")
public class VendorNegotiationMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	private Double cop;

	private String remarks;
	
    private Boolean accept;
	
	private Boolean reject;
	
	@Column(name="reject_note")
	private String rejectNote;
	
	@Column(name="on_hold")
	private Boolean onHold;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name="vendor_expected_payment")
	private String vendorExpectedPayment;

	@Column(name="vendor_support_terms")
	private String vendorSupportTerms;

	@Column(name="vendor_technical_specification")
	private String vendorTechnicalSpecification;

	@Column(name="vendor_warrenty_period")
	private String vendorwarrentyPeriod;

	//bi-directional many-to-one association to TVendorNegociationDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_negociation_detail_id")
	private VendorNegotiationDetail vendorNegociationDetail;
	
	//bi-directional many-to-one association to TVendorNegociationMapper
	@OneToOne(fetch = FetchType.LAZY, mappedBy="vendorNegotiationMapper", cascade = CascadeType.ALL)
	private VendorSelectionMapper vendorSelectionMapper;

	public VendorNegotiationMapper() {
		super();
	}
	
	public VendorNegotiationMapper(Integer id) {
		super();
		this.id = id;
	}

	
	public VendorNegotiationMapper(Double cop, String remarks, Boolean accept, Boolean reject, String rejectNote,
			Boolean onHold, Status status, String vendorExpectedPayment, String vendorSupportTerms,
			String vendorTechnicalSpecification, String vendorwarrentyPeriod) {
		super();
		this.cop = cop;
		this.remarks = remarks;
		this.accept = accept;
		this.reject = reject;
		this.rejectNote = rejectNote;
		this.onHold = onHold;
		this.status = status;
		this.vendorExpectedPayment = vendorExpectedPayment;
		this.vendorSupportTerms = vendorSupportTerms;
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
		this.vendorwarrentyPeriod = vendorwarrentyPeriod;
	}
	
	public void updateVendorNegotiationMapper(Double cop, String remarks, Boolean accept, Boolean reject, String rejectNote,
			Boolean onHold, Status status, String vendorExpectedPayment, String vendorSupportTerms,
			String vendorTechnicalSpecification, String vendorwarrentyPeriod) {
		this.cop = cop;
		this.remarks = remarks;
		this.accept = accept;
		this.reject = reject;
		this.rejectNote = rejectNote;
		this.onHold = onHold;
		this.status = status;
		this.vendorExpectedPayment = vendorExpectedPayment;
		this.vendorSupportTerms = vendorSupportTerms;
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
		this.vendorwarrentyPeriod = vendorwarrentyPeriod;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getVendorExpectedPayment() {
		return vendorExpectedPayment;
	}

	public void setVendorExpectedPayment(String vendorExpectedPayment) {
		this.vendorExpectedPayment = vendorExpectedPayment;
	}

	public String getVendorSupportTerms() {
		return vendorSupportTerms;
	}

	public void setVendorSupportTerms(String vendorSupportTerms) {
		this.vendorSupportTerms = vendorSupportTerms;
	}

	public String getVendorTechnicalSpecification() {
		return vendorTechnicalSpecification;
	}

	public void setVendorTechnicalSpecification(String vendorTechnicalSpecification) {
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
	}

	public String getVendorwarrentyPeriod() {
		return vendorwarrentyPeriod;
	}

	public void setVendorwarrentyPeriod(String vendorwarrentyPeriod) {
		this.vendorwarrentyPeriod = vendorwarrentyPeriod;
	}

	public VendorNegotiationDetail getVendorNegociationDetail() {
		return vendorNegociationDetail;
	}

	public void setVendorNegociationDetail(VendorNegotiationDetail vendorNegociationDetail) {
		this.vendorNegociationDetail = vendorNegociationDetail;
	}


	public Boolean getAccept() {
		return accept;
	}


	public void setAccept(Boolean accept) {
		this.accept = accept;
	}


	public Boolean getReject() {
		return reject;
	}


	public void setReject(Boolean reject) {
		this.reject = reject;
	}


	public String getRejectNote() {
		return rejectNote;
	}


	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}


	public Boolean getOnHold() {
		return onHold;
	}


	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}


	public VendorSelectionMapper getVendorSelectionMapper() {
		return vendorSelectionMapper;
	}


	public void setVendorSelectionMapper(VendorSelectionMapper vendorSelectionMapper) {
		this.vendorSelectionMapper = vendorSelectionMapper;
	}
}