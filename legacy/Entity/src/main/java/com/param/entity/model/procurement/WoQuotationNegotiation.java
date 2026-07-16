package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_wo_quotation_negotiation database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_negotiation", schema = "procurement")
public class WoQuotationNegotiation extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_quotation_id")
	private WoQuotationVendor vendorQuotation;

	//bi-directional many-to-one association to TWoQuotationNegotiationDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy="woQuotationNegotiation", cascade = CascadeType.ALL)
	private List<WoQuotationNegotiationDetail> woQuotationNegotiationDetailList;

	private String remark;
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public WoQuotationNegotiation() {
		super();
	}
	
	public WoQuotationNegotiation(Integer id) {
		this.id = id;
	}

	public WoQuotationNegotiation(Status status, WoQuotationVendor vendorQuotation) {
		super();
		this.status = status;
		this.vendorQuotation = vendorQuotation;
	}
	
	/*public void  updateWoQuotationNegotiation(String remark) {
		this.remark = remark;
	}*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WoQuotationVendor getVendorQuotation() {
		return vendorQuotation;
	}

	public void setVendorQuotation(WoQuotationVendor vendorQuotation) {
		this.vendorQuotation = vendorQuotation;
	}

	public List<WoQuotationNegotiationDetail> getWoQuotationNegotiationDetailList() {
		return woQuotationNegotiationDetailList;
	}

	public void setWoQuotationNegotiationDetailList(List<WoQuotationNegotiationDetail> woQuotationNegotiationDetailList) {
		this.woQuotationNegotiationDetailList = woQuotationNegotiationDetailList;
	}

	public WoQuotationNegotiationDetail addWoQuotationNegotiationDetailList(WoQuotationNegotiationDetail woQuotationNegotiationDetail) {
		if (getWoQuotationNegotiationDetailList() == null)
			this.woQuotationNegotiationDetailList = new ArrayList<WoQuotationNegotiationDetail>();

		getWoQuotationNegotiationDetailList().add(woQuotationNegotiationDetail);
		woQuotationNegotiationDetail.setWoQuotationNegotiation(this);

		return woQuotationNegotiationDetail;
	}

	public WoQuotationNegotiationDetail removeWoQuotationNegotiationDetail(WoQuotationNegotiationDetail woQuotationNegotiationDetail) {
		getWoQuotationNegotiationDetailList().remove(woQuotationNegotiationDetail);
		woQuotationNegotiationDetail.setWoQuotationNegotiation(this);

		return woQuotationNegotiationDetail;
	}

	public String getRemark() {
		return remark;
	}

	public void setId(String remark) {
		this.remark = remark;
	}

}