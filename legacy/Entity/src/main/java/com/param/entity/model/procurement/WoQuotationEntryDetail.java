package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Status;

import java.sql.Timestamp;


/**
 * The persistent class for the t_wo_quotation_entry_detail database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_entry_detail", schema = "procurement")
public class WoQuotationEntryDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="expected_exclusion")
	private String expectedExclusion;

	@Column(name="expected_inclusion")
	private String expectedInclusion;

	@Column(name="payment_term")
	private String paymentTerm;
	
	@Column(name="vendor_cop")
	private Double vendorCop;

	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_quotation_detail_id")
	private WoQuotationVendorDetail vendorQuotationDetail;

	//bi-directional many-to-one association to TWoQuotationEntry
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_entry_id")
	private WoQuotationEntry woQuotationEntry;


	public WoQuotationEntryDetail() {
		super();
	}

	
	public WoQuotationEntryDetail(Double vendorCop, String expectedExclusion, String expectedInclusion, String paymentTerm, String remark,
			Status status, WoQuotationVendorDetail vendorQuotationDetail) {
		super();
		this.vendorCop = vendorCop;
		this.expectedExclusion = expectedExclusion;
		this.expectedInclusion = expectedInclusion;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
		this.status = status;
		this.vendorQuotationDetail = vendorQuotationDetail;
	}
	
	public void updateWoQuotationEntryDetail(Double vendorCop,String expectedExclusion, String expectedInclusion, String paymentTerm, String remark) {
		this.vendorCop = vendorCop;
		this.expectedExclusion = expectedExclusion;
		this.expectedInclusion = expectedInclusion;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExpectedExclusion() {
		return expectedExclusion;
	}

	public void setExpectedExclusion(String expectedExclusion) {
		this.expectedExclusion = expectedExclusion;
	}

	public String getExpectedInclusion() {
		return expectedInclusion;
	}

	public void setExpectedInclusion(String expectedInclusion) {
		this.expectedInclusion = expectedInclusion;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WoQuotationVendorDetail getVendorQuotationDetail() {
		return vendorQuotationDetail;
	}

	public void setVendorQuotationDetail(WoQuotationVendorDetail vendorQuotationDetail) {
		this.vendorQuotationDetail = vendorQuotationDetail;
	}

	public WoQuotationEntry getWoQuotationEntry() {
		return woQuotationEntry;
	}

	public void setWoQuotationEntry(WoQuotationEntry woQuotationEntry) {
		this.woQuotationEntry = woQuotationEntry;
	}

	public Double getVendorCop() {
		// TODO Auto-generated method stub
		return vendorCop;
	}
	
	public void setVendorCop(Double vendorCop) {
		this.vendorCop = vendorCop;
	}


}