package com.param.billing.global.transaction.model;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.CascadeType;
import com.param.global.common.DateConverter;



@Entity
@Table(name="t_doc_share_voucher", schema="billing")
@SequenceGenerator(name = "t_seq_doc_share_voucher", sequenceName = "billing.t_seq_doc_share_voucher", allocationSize = 1)
public class TDocShareVoucher {
	@Id
	@Column(name="doc_share_vouche_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doc_share_voucher")
	private Integer docShareVoucheId;
	
	@Column(name = "voucher_no")
	private String voucherNo;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "doc_id")
	private Integer docId;

	@Column(name = "processed_amt")
	private double processedShareAmt;
	
	@Column(name = "due_share_amt")
	private double dueShareAmt;
	
	@Column(name = "total_doc_share_amout")
	private double totalDocShareAmout;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

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
	
	@Column(name = "referral_id")
	private Integer referralId;
	
	@Column(name = "type")
	private Integer type;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "tDocShareVoucher")
	private List<TDocShareVoucherDtls> listTDocShareVoucherDtls;
	
	@Column(name = "due_date")
	private Date dueDate;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getDocShareVoucheId() {
		return docShareVoucheId;
	}

	public void setDocShareVoucheId(Integer docShareVoucheId) {
		this.docShareVoucheId = docShareVoucheId;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public double getTotalDocShareAmout() {
		return totalDocShareAmout;
	}

	public void setTotalDocShareAmout(double totalDocShareAmout) {
		this.totalDocShareAmout = totalDocShareAmout;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public List<TDocShareVoucherDtls> getListTDocShareVoucherDtls() {
		return listTDocShareVoucherDtls;
	}

	public void setListTDocShareVoucherDtls(List<TDocShareVoucherDtls> listTDocShareVoucherDtls) {
		this.listTDocShareVoucherDtls = listTDocShareVoucherDtls;
	}

	public double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
	}

	public double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	/*public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}
*/
	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/*public Integer getDocSpecId() {
		return docSpecId;
	}

	public void setDocSpecId(Integer docSpecId) {
		this.docSpecId = docSpecId;
	}
*/
	
	
}
