package com.param.entity.model.procurement;

import java.io.Serial;
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
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;

/**
 * The persistent class for the t_vendor_selection database table.
 * 
 */
@Entity
@Table(name = "t_vendor_selection", schema = "procurement")
public class VendorSelection extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quotation_request_id")
	private QuotationRequest quotationRequest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	// bi-directional many-to-one association to TVendorSelectionDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorSelection", cascade = CascadeType.ALL)
	private List<VendorSelectionDetail> vendorSelectionDetailList;

	public VendorSelection() {
		super();
	}
	
	public VendorSelection(QuotationRequest quotationRequest,
			Status status) {
		super();
		this.quotationRequest = quotationRequest;
		this.status = status;
	}
	
	public void updateVendorSelection(String cancelNote, CancelReason cancelReason,
			Status status) {
		this.cancelNote = cancelNote;
		this.cancelReason = cancelReason;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public QuotationRequest getQuotationRequest() {
		return quotationRequest;
	}

	public void setQuotationRequest(QuotationRequest quotationRequest) {
		this.quotationRequest = quotationRequest;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<VendorSelectionDetail> getVendorSelectionDetailList() {
		return vendorSelectionDetailList;
	}

	public void setVendorSelectionDetailList(List<VendorSelectionDetail> vendorSelectionDetailList) {
		this.vendorSelectionDetailList = vendorSelectionDetailList;
	}

	public VendorSelectionDetail addVendorSelectionDetail(VendorSelectionDetail vendorSelectionDetail) {
		if (getVendorSelectionDetailList() == null)
			this.vendorSelectionDetailList = new ArrayList<VendorSelectionDetail>();
		getVendorSelectionDetailList().add(vendorSelectionDetail);
		vendorSelectionDetail.setVendorSelection(this);
		return vendorSelectionDetail;
	}

}