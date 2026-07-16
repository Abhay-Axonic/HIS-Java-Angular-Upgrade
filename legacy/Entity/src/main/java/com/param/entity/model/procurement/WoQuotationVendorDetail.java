package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
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
import com.param.entity.model.master.Status;

/**
 * The persistent class for the t_wo_quotation_send_to_vendor_detail database
 * table.
 * 
 */
@Entity
@Table(name = "t_wo_quotation_send_to_vendor_detail", schema = "procurement")
public class WoQuotationVendorDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quotation_request_detail_id")
	private WorkOrderQuotationRequestDetail workOrderQRDetail;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	// bi-directional many-to-one association to TWoQuotationSendToVendor
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quotation_send_vendor_id")
	private WoQuotationVendor WoQuotationVendor;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="vendorQuotationDetail", cascade = CascadeType.ALL)
	private List<WoQuotationEntryDetail> woQuotationEntryDetailList;


	public WoQuotationVendorDetail() {
		super();
	}
	
	public WoQuotationVendorDetail(Integer id) {
		this.id = id;
	}
	
	public WoQuotationVendorDetail(WorkOrderQuotationRequestDetail workOrderQRDetail, String remark, Status status) {
		super();
		this.workOrderQRDetail = workOrderQRDetail;
		this.remark = remark;
		this.status = status;
	}
	
	public void updateWoQuotationVendorDetail(String remark) {
		this.remark = remark;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WorkOrderQuotationRequestDetail getWorkOrderQRDetail() {
		return workOrderQRDetail;
	}

	public void setWorkOrderQRDetail(WorkOrderQuotationRequestDetail workOrderQRDetail) {
		this.workOrderQRDetail = workOrderQRDetail;
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

	public WoQuotationVendor getWoQuotationVendor() {
		return WoQuotationVendor;
	}

	public void setWoQuotationVendor(WoQuotationVendor woQuotationVendor) {
		WoQuotationVendor = woQuotationVendor;
	}

	public List<WoQuotationEntryDetail> getWoQuotationEntryDetailList() {
		return woQuotationEntryDetailList;
	}

	public void setWoQuotationEntryDetailList(List<WoQuotationEntryDetail> woQuotationEntryDetailList) {
		this.woQuotationEntryDetailList = woQuotationEntryDetailList;
	}
	
	

}