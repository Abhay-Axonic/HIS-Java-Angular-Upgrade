package com.param.billing.global.transaction.model;


import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.model.PackageTypeMaster;
import com.param.global.common.DateConverter;



@Entity
@Table(name="t_doc_share_voucher_dtls", schema="billing")
@SequenceGenerator(name = "t_seq_doc_share_voucher_dtls", sequenceName = "billing.t_seq_doc_share_voucher_dtls", allocationSize = 1)
public class TDocShareVoucherDtls {
	@Id
	@Column(name="voucher_dtls_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doc_share_voucher_dtls")
	private Integer voucherDtlsId;
	
	@Column(name = "doc_share_vouche_id")
	private Integer docShareVoucheId;
	
	@Column(name = "referral_share_id")
	private Integer referralShareId;
	
	@Column(name = "doctor_share_id")
	private Integer doctorShareId;
	
	@Column(name = "processed_amt")
	private double processedAmt;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "doctor_slab_transaction_id")
	private Integer doctorSlabTransactionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_share_vouche_id" , insertable = false , updatable = false , nullable = false)
	private TDocShareVoucher tDocShareVoucher;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_share_id" , insertable = false , updatable = false , nullable = false)
	private TDoctorShare tDoctorShare;
	

	public Integer getDoctorSlabTransactionId() {
		return doctorSlabTransactionId;
	}

	public void setDoctorSlabTransactionId(Integer doctorSlabTransactionId) {
		this.doctorSlabTransactionId = doctorSlabTransactionId;
	}

	public Integer getVoucherDtlsId() {
		return voucherDtlsId;
	}

	public void setVoucherDtlsId(Integer voucherDtlsId) {
		this.voucherDtlsId = voucherDtlsId;
	}

	public Integer getDocShareVoucheId() {
		return docShareVoucheId;
	}

	public void setDocShareVoucheId(Integer docShareVoucheId) {
		this.docShareVoucheId = docShareVoucheId;
	}


	public double getProcessedAmt() {
		return processedAmt;
	}

	public void setProcessedAmt(double processedAmt) {
		this.processedAmt = processedAmt;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public TDocShareVoucher gettDocShareVoucher() {
		return tDocShareVoucher;
	}

	public void settDocShareVoucher(TDocShareVoucher tDocShareVoucher) {
		this.tDocShareVoucher = tDocShareVoucher;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	public TDoctorShare gettDoctorShare() {
		return tDoctorShare;
	}

	public void settDoctorShare(TDoctorShare tDoctorShare) {
		this.tDoctorShare = tDoctorShare;
	}

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}
	
	
}
