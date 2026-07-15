package com.param.entity.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "OpeningBalanceImplant")
@Table(name = "t_opening_balance_implant", schema = "inventory")
public class OpeningBalanceImplant extends BaseEntity implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "cancel_note", length = 100)
	private String cancelNote;
	
	@Column(name = "remark", length = 100)
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "opb_no", nullable = false, length = 50)
	private String opbNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceImplant", cascade = CascadeType.ALL)
	private List<OpeningBalanceImplantDetail> openingBalanceImplantDetailList;
	
	public OpeningBalanceImplant()
	{
		super();
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

	public String getOpbNo() {
		return opbNo;
	}

	public void setOpbNo(String opbNo) {
		this.opbNo = opbNo;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<OpeningBalanceImplantDetail> getOpeningBalanceImplantDetailList() {
		return openingBalanceImplantDetailList;
	}

	public void setOpeningBalanceImplantDetailList(List<OpeningBalanceImplantDetail> openingBalanceImplantDetailList) {
		this.openingBalanceImplantDetailList = openingBalanceImplantDetailList;
	}

	public OpeningBalanceImplant(String opbNo,String remark, Status status, Store store) {
		super();
		this.opbNo = opbNo;
		this.remark=remark;
		this.status = status;
		this.store = store;
	}
	
	public OpeningBalanceImplantDetail addOpeningBalanceImplantDetail(OpeningBalanceImplantDetail openingBalanceImplantDetail) {
		if (this.openingBalanceImplantDetailList == null)
			this.openingBalanceImplantDetailList = new ArrayList<OpeningBalanceImplantDetail>();

		this.openingBalanceImplantDetailList.add(openingBalanceImplantDetail);
		openingBalanceImplantDetail.setOpeningBalanceImplant(this);

		return openingBalanceImplantDetail;
	}	
	
}
