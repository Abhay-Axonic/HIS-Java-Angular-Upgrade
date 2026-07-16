package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_wo_quotation_selection database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_selection", schema = "procurement")
public class WoQuotationSelection extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_request_id")
	private WorkOrderQuotationRequest quotationRequest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	//bi-directional many-to-one association to TWoQuotationSelectionVendorMapper
	@OneToMany(fetch = FetchType.LAZY,mappedBy="woQuotationSelection", cascade = CascadeType.ALL)
	private List<WoQuotationSelectionDetail> woQuotationSelectionDetail;

	public WoQuotationSelection() {
		super();
	}
	
	public WoQuotationSelection(WorkOrderQuotationRequest quotationRequest, Status status) {
		super();
		this.quotationRequest = quotationRequest;
		this.status = status;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WorkOrderQuotationRequest getQuotationRequest() {
		return quotationRequest;
	}

	public void setQuotationRequest(WorkOrderQuotationRequest quotationRequest) {
		this.quotationRequest = quotationRequest;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<WoQuotationSelectionDetail> getWoQuotationSelectionDetail() {
		return woQuotationSelectionDetail;
	}

	public void setWoQuotationSelectionDetail(List<WoQuotationSelectionDetail> woQuotationSelectionDetail) {
		this.woQuotationSelectionDetail = woQuotationSelectionDetail;
	}
	
	public WoQuotationSelectionDetail addWoQuotationSelectionDetailList(WoQuotationSelectionDetail woQuotationSelectionDetail) {
		if (getWoQuotationSelectionDetail() == null)
			this.woQuotationSelectionDetail = new ArrayList<WoQuotationSelectionDetail>();

		getWoQuotationSelectionDetail().add(woQuotationSelectionDetail);
		woQuotationSelectionDetail.setWoQuotationSelection(this);
		return woQuotationSelectionDetail;
	}

	public WoQuotationSelectionDetail removeWoQuotationSelectionDetail(WoQuotationSelectionDetail woQuotationSelectionDetail) {
		getWoQuotationSelectionDetail().remove(woQuotationSelectionDetail);
		woQuotationSelectionDetail.setWoQuotationSelection(this);

		return woQuotationSelectionDetail;
	}
}