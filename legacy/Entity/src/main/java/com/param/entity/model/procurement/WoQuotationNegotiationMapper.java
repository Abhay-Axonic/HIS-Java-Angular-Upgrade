package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;

import java.sql.Timestamp;


/**
 * The persistent class for the t_wo_quotation_negotiation_mapper database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_negotiation_mapper", schema = "procurement")
public class WoQuotationNegotiationMapper extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="exclusion_detail")
	private String exclusionDetail;

	@Column(name="inclusion_detail")
	private String inclusionDetail;

	@Column(name="payment_term")
	private String paymentTerm;

	private String remark;
	
	private Boolean accept;
	
	private Boolean reject;
	
	@Column(name="on_hold")
	private Boolean onHold;
	
	@Column(name="reject_note")
	private String rejectNote;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="quantity")
	private Integer quantity;

	//bi-directional many-to-one association to TWoQuotationNegotiationDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="negotiation_detail_id")
	private WoQuotationNegotiationDetail woQuotationNegotiationDetail;

	public WoQuotationNegotiationMapper() {
		super();
	}
	
	public WoQuotationNegotiationMapper(Integer id) {
		this.id = id;
	}


	public WoQuotationNegotiationMapper(String exclusionDetail, String inclusionDetail, String paymentTerm,
			String remark,Boolean accept,Boolean reject,Boolean onHold,String rejectNote,Double amount,Integer quantity) {
		super();
		this.exclusionDetail = exclusionDetail;
		this.inclusionDetail = inclusionDetail;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
		this.accept = accept;
		this.reject = reject;
		this.onHold = onHold;
		this.rejectNote = rejectNote;
		this.amount = amount;
		this.quantity = quantity;
	}

	public void updateWoQuotationNegotiationMapper(String exclusionDetail, String inclusionDetail, String paymentTerm,
			String remark) {
		this.exclusionDetail = exclusionDetail;
		this.inclusionDetail = inclusionDetail;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExclusionDetail() {
		return exclusionDetail;
	}

	public void setExclusionDetail(String exclusionDetail) {
		this.exclusionDetail = exclusionDetail;
	}

	public String getInclusionDetail() {
		return inclusionDetail;
	}

	public void setInclusionDetail(String inclusionDetail) {
		this.inclusionDetail = inclusionDetail;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Boolean getOnHold() {
		return onHold;
	}

	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
	
	

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public WoQuotationNegotiationDetail getWoQuotationNegotiationDetail() {
		return woQuotationNegotiationDetail;
	}

	public void setWoQuotationNegotiationDetail(WoQuotationNegotiationDetail woQuotationNegotiationDetail) {
		this.woQuotationNegotiationDetail = woQuotationNegotiationDetail;
	}
	
}