package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;

import java.sql.Timestamp;


/**
 * The persistent class for the t_wo_quotation_selection_vendor_mapper database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_selection_vendor_mapper", schema = "procurement")
public class WoQuotationSelectionVendorMapper extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="is_accepted")
	private Boolean isAccepted;
	
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_negotiation_id")
	private WoQuotationNegotiationMapper quotationNegotiationMapper;

	@Column(name="reject_note")
	private String rejectNote;

	//bi-directional many-to-one association to TWoQuotationSelectionDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_selection_detail_id")
	private WoQuotationSelectionDetail woQuotationSelectionDetail;
	
	@Column(name="exclusion_details")
	private Boolean exclusionDetails;
	
	@Column(name="inclusion_details")
	private Boolean inclusionDetails;
	
	@Column(name="payment_terms")
	private Boolean paymentTerms;
	
	@Column(name="remark")
	private String remark;
	
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public WoQuotationSelectionVendorMapper() {
		super();
	}
	
	public WoQuotationSelectionVendorMapper(Boolean isAccepted, Integer quantity,
			WoQuotationNegotiationMapper quotationNegotiationMapper, String rejectNote) {
		super();
		this.isAccepted = isAccepted;
		this.quantity = quantity;
		this.quotationNegotiationMapper = quotationNegotiationMapper;
		this.rejectNote = rejectNote;
	}
	
	

	public void  updateWoQuotationSelectionVendorMapper(Boolean isAccepted, String rejectNote,Integer quantity,String remark,
			Boolean inclusionDetails,Boolean exclusionDetails,Boolean paymentTerms) {
		this.isAccepted = isAccepted;
		this.rejectNote = rejectNote;
		this.quantity=quantity;
		this.remark=remark;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails= exclusionDetails;
		this.paymentTerms=paymentTerms;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public WoQuotationNegotiationMapper getQuotationNegotiationMapper() {
		return quotationNegotiationMapper;
	}

	public void setQuotationNegotiationMapper(WoQuotationNegotiationMapper quotationNegotiationMapper) {
		this.quotationNegotiationMapper = quotationNegotiationMapper;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public WoQuotationSelectionDetail getWoQuotationSelectionDetail() {
		return woQuotationSelectionDetail;
	}

	public void setWoQuotationSelectionDetail(WoQuotationSelectionDetail woQuotationSelectionDetail) {
		this.woQuotationSelectionDetail = woQuotationSelectionDetail;
	}

	public Boolean getExclusionDetails() {
		return exclusionDetails;
	}
	
	public void setExclusionDetails(Boolean exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public Boolean getInclusionDetails() {
		return inclusionDetails;
	}
	public void setInclusionDetails(Boolean inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public Boolean getPaymentTerms() {
		return paymentTerms;
	}
	
	public void setPaymentTerms(Boolean paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

}